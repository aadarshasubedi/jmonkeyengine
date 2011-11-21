package com.g3d.asset.pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class J3PCreator {

    private static final Logger logger = Logger.getLogger(J3PCreator.class.getName());

    public static final int VERSION = 1;
    public static final int MAX_NAME_LENGTH = Short.MAX_VALUE;

    private HashSet<Integer> usedHashes = new HashSet(100);
    private J3PHeader header;
    private List<J3PEntry> entries = new ArrayList<J3PEntry>();
    private List<InputStream> streams = new ArrayList<InputStream>();
    private ProgressListener progListener;
    private int maxProgress = 0;

    private FileChannel channel;
    private Compressor compressor = new Compressor();
    private StringBuffer infoLine = new StringBuffer();
    private long totalCompressed = 0,
                 totalUncompressed = 0;
    private boolean cancel = false;


    /**
     * Size of the entry table, including the # of entries integer.
     */
    private int entryTableSize = 4;

    private J3PEntry createEntry(String name, long size){
        if (name == null){
            System.err.println("name is null! Ignored.");
            return null;
        }
        if (size > Integer.MAX_VALUE){
            System.err.println(name+" is larger than 2 GB! Ignored.");
            return null;
        }
        if (name.length() > MAX_NAME_LENGTH){
            System.err.println(name+" name is too large! Ignored.");
            return null;
        }
        if (name.contains(".svn")){
            System.err.println(name+" is SVN metadata! Ignored.");
            return null;
        }

        // fixes issues when trying to load mesh.xml file, since
        // extension is technically XML
        int idx = name.indexOf("mesh.xml");
        if (idx > 0)
            name = name.substring(0, idx) + "meshxml";

        idx = name.indexOf("skeleton.xml");
        if (idx > 0)
            name = name.substring(0, idx) + "skeletonxml";

        J3PEntry out = new J3PEntry(name);
        out.length = (int) size;
        entryTableSize += 4 + 1 + 4 + 4;
        if (usedHashes.contains(out.hash)){
            out.flags |= J3PEntry.KEY_INCLUDED;
            entryTableSize += 2 + out.name.length();
        }else{
            usedHashes.add(out.hash);
        }

        // use uncompressed size of all entries as
        // maximum progress value
        maxProgress += size;

        return out;
    }

    private void writeEntry(J3PEntry entry, ByteBuffer store){
        if (entry == null)
            return;

        store.putInt(entry.hash);
        store.put((byte)entry.flags);
        if ((entry.flags & J3PEntry.KEY_INCLUDED) != 0){
            byte[] utf8 = null;
            try{
                utf8 = entry.name.getBytes("UTF-8");
            }catch (UnsupportedEncodingException ex){
                ex.printStackTrace();
            }
            store.putShort((short) entry.name.length());
            store.put(utf8);
        }
        store.putInt((int) entry.offset);
        store.putInt(entry.length);
    }


    private long writeEntryData(J3PEntry entry, InputStream in, FileChannel out) throws IOException{
        long t = System.nanoTime();
        long initialPos = out.position();
        long uncompSize = entry.length;

        if (progListener != null){
            progListener.onText(infoLine.toString());
            in = new PackerInputStream(in, progListener);
        }
        
        compressor.compress(in, out, entry, entry.length);
        long compSize = out.position() - initialPos;

        entry.offset = initialPos - header.dataOffset;
        entry.length = (int) compSize;

        totalUncompressed += uncompSize;
        totalCompressed += compSize;

        double seconds = ((double) (System.nanoTime() - t)) / 1000000000.0;
        double mbs = (compSize / 1024.0) / 1024.0;
        String speed = String.format("%9.3f", mbs / seconds);
        String compRatio = String.format("%1.0f%%", 
                (double)totalCompressed / totalUncompressed * 100.0);

        infoLine.setLength(0);
//        infoLine.append("MB/s: ").append(speed).append('\n');
        infoLine.append("Ratio: ").append(compRatio).append('\n');

        if (progListener != null)
            progListener.onText(infoLine.toString());

        return uncompSize;
    }

    private ByteBuffer createTable(){
        ByteBuffer tableBuf = ByteBuffer.allocate(entryTableSize);
        tableBuf.order(ByteOrder.LITTLE_ENDIAN);
        tableBuf.putInt(entries.size());
        for (J3PEntry entry : entries){
            writeEntry(entry, tableBuf);
        }
        tableBuf.flip();

        return tableBuf;
    }

    private ByteBuffer createHeader(){
        header = new J3PHeader();
        header.version = VERSION;

        // sig + version + flags + data offset
        int headerSize = 4 + 4 + 4 + 4;
        // header size + table size
        header.dataOffset = headerSize + entryTableSize;

        ByteBuffer headerBuf = ByteBuffer.allocate(entryTableSize);
        headerBuf.order(ByteOrder.LITTLE_ENDIAN);
        headerBuf.put(header.signature);
        headerBuf.putInt(header.version);
        headerBuf.putInt(header.flags);
        headerBuf.putInt(header.dataOffset);
        headerBuf.flip();

        return headerBuf;
    }

    public void reset(){
        usedHashes.clear();
        entries.clear();
        maxProgress = 0;
        totalCompressed = 0;
        totalUncompressed = 0;
    }

    public void setProgressListener(ProgressListener listener){
        this.progListener = listener;
        progListener.onMaxProgress(maxProgress);
    }

    public void addEntry(String name, long size, InputStream stream){
        J3PEntry entry = createEntry(name, size);
        if (entry == null)
            return;

        entries.add(entry);
        streams.add(stream);
    }

    private void innerCancel(){

    }

    public void cancel(){
        synchronized (this){
            cancel = true;
        }
    }

    public void finish(File outFile) {
        if (progListener != null)
            progListener.onMaxProgress(maxProgress);

        // create header, also updates header.dataOffset variable
        ByteBuffer headerBuf = createHeader();

        // go to dataoffset
        try {
            channel = new RandomAccessFile(outFile, "rw").getChannel();
            channel.truncate(header.dataOffset);
            channel.position(header.dataOffset);
        } catch (IOException ex){
            progListener.onError("Error occured while saving to archive", ex);
        }

        // write data of entries
        for (int i = 0; i < entries.size(); i++){
            J3PEntry entry = entries.get(i);
            InputStream in = streams.get(i);
            System.out.println("Writing: "+entry.name);
            try {
                writeEntryData(entry, in, channel);
            } catch (IOException ex){
                progListener.onError("Error occured while reading input files", ex);
                cancel = true;
            } finally {
                try{
                    in.close();
                }catch (IOException ex){
                }
            }
            synchronized (this){
                if (cancel){
                    cancel = false;
                    try {
                        for (int j = i + 1; j < entries.size(); i++){
                            streams.get(j).close();
                        }
                        channel.close();
                        outFile.delete();
                        return;
                    } catch (IOException ex) {}
                }
            }
        }

        try {
            // position after writing all entries
            long fileSize = channel.position();

            // generate table after writing entries,
            // since compression may alter entry data size
            ByteBuffer tableBuf = createTable();

            channel.position(0);
            channel.write(new ByteBuffer[]{ headerBuf, tableBuf });

            if (channel.size() != fileSize)
                throw new IOException("Channel size and end offset do not match!");

            channel.close();
        } catch (IOException ex){
            progListener.onError("Error occured while finalizing archive", ex);
        }
        reset();
    }

}