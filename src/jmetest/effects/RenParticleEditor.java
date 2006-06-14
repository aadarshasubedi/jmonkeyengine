/*
 * Copyright (c) 2003-2006 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jmetest.effects;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.RenderThreadActionQueue;
import com.jme.util.RenderThreadExecutable;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.awt.JMECanvas;
import com.jmex.awt.SimpleCanvasImpl;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.SimpleParticleForceFactory;

/**
 * <code>RenParticleControlFrame</code>
 *
 * @author Joshua Slack
 * @author Andrzej Kapolka - additions for multiple layers, save/load from jme format
 * @version $Id: RenParticleEditor.java,v 1.28 2006-06-14 03:42:20 renanse Exp $
 *
 */

public class RenParticleEditor extends JFrame {

    int width = 640, height = 480;
    public static Node particleNode;
    public static ParticleMesh particleMesh;
    public static File newTexture = null;

    MyImplementor impl;
    private Canvas glCanvas;
    private static final long serialVersionUID = 1L;
    private Node root;
    JTabbedPane mainTabbedPane1 = new JTabbedPane();
    JPanel layerPanel = new JPanel();
    JPanel appPanel = new JPanel();
    JPanel emitPanel = new JPanel();
    JPanel worldPanel = new JPanel();
    JLabel layerLabel = new JLabel();
    JTable layerTable = null;
    JScrollPane layerSP = new JScrollPane();
    LayerTableModel layerModel = new LayerTableModel();
    JButton newLayerButton = new JButton();
    JButton deleteLayerButton = new JButton();
    JPanel colorPanel = new JPanel();
    JLabel startColorLabel = new JLabel();
    JLabel colorLabel = new JLabel();
    JLabel endColorLabel = new JLabel();
    JPanel startColorPanel = new JPanel();
    JPanel endColorPanel = new JPanel();
    JLabel startColorHex = new JLabel();
    JLabel endColorHex = new JLabel();
    TitledBorder colorBorder;
    JSpinner startAlphaSpinner = new JSpinner();
    JLabel startAlphaLabel = new JLabel();
    JLabel endAlphaLabel = new JLabel();
    JSpinner endAlphaSpinner = new JSpinner();
    JCheckBox additiveBlendingBox = new JCheckBox();
    JPanel sizePanel = new JPanel();
    JLabel startSizeLabel = new JLabel();
    JSlider startSizeSlider = new JSlider();
    TitledBorder sizeBorder;
    JLabel endSizeLabel = new JLabel();
    JSlider endSizeSlider = new JSlider();
    TitledBorder ageBorder;
    JPanel speedPanel = new JPanel();
    TitledBorder speedBorder;
    JLabel speedLabel = new JLabel();
    JSlider speedSlider = new JSlider();
    JPanel texturePanel = new JPanel();
    TitledBorder textureBorder;
    JLabel textureLabel = new JLabel();
    JButton changeTextureButton = new JButton();
    JLabel imageLabel = new JLabel();
    JPanel agePanel = new JPanel();
    JLabel minAgeLabel = new JLabel();
    JSlider minAgeSlider = new JSlider();
    JLabel maxAgeLabel = new JLabel();
    JSlider maxAgeSlider = new JSlider();
    JLabel emitYLabel = new JLabel();
    JLabel emitZLabel = new JLabel();
    JSlider emitYSlider = new JSlider();
    JLabel emitXLabel = new JLabel();
    JSlider emitXSlider = new JSlider();
    JSlider emitZSlider = new JSlider();
    JPanel directionPanel = new JPanel();
    TitledBorder emitBorder;
    JPanel anglePanel = new JPanel();
    TitledBorder angleBorder;
    JLabel angleLabel = new JLabel();
    JSlider angleSlider = new JSlider();
    JLabel minAngleLabel = new JLabel();
    JSlider minAngleSlider = new JSlider();
    JPanel randomPanel = new JPanel();
    TitledBorder randomBorder;
    JLabel randomLabel = new JLabel();
    JSlider randomSlider = new JSlider();
    JPanel examplesPanel = new JPanel();
    JScrollPane exampleSP = new JScrollPane();
    JList exampleList = null;
    JLabel examplesLabel = new JLabel();
    JButton exampleButton = new JButton();
    JPanel codePanel = new JPanel();
    JLabel codeLabel = new JLabel();
    JScrollPane codeSP = new JScrollPane();
    JTextArea codeTextArea = new JTextArea();
    DefaultListModel exampleModel = new DefaultListModel();
    JPanel countPanel = new JPanel();
    TitledBorder countBorder;
    JLabel countLabel = new JLabel();
    JButton countButton = new JButton();
    File lastDir = null;
    JPanel flowPanel = new JPanel();
    JPanel ratePanel = new JPanel();
    TitledBorder rateBorder;
    JLabel rateLabel = new JLabel();
    JPanel spawnPanel = new JPanel();
    TitledBorder spawnBorder;
    JCheckBox spawnBox = new JCheckBox();
    JButton spawnButton = new JButton();
    JLabel rateVarLabel = new JLabel();
    JSlider rateVarSlider = new JSlider();
    JSlider rateSlider = new JSlider();
    JCheckBox rateBox = new JCheckBox();
    JPanel velocityPanel = new JPanel();
    TitledBorder velocityBorder;
    JLabel velocityLabel = new JLabel();
    JSlider velocitySlider = new JSlider();
    JPanel spinPanel = new JPanel();
    TitledBorder spinBorder;
    JLabel spinLabel = new JLabel();
    JSlider spinSlider = new JSlider();

    JFrame colorChooserFrame = new JFrame("Choose a color.");
    JColorChooser colorChooser = new JColorChooser();
    boolean colorstart = false;
  
    JFileChooser fileChooser = new JFileChooser();
    
    /**
     * Main Entry point...
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RenParticleEditor();
    }

  
    public RenParticleEditor() {
        try {
            jbInit();
            // center the frame
            setLocationRelativeTo(null);
            // show frame
            setVisible(true);

            while (glCanvas == null || impl.startTime == 0) ;

            // MAKE SURE YOU REPAINT SOMEHOW OR YOU WON'T SEE THE UPDATES...
            new Thread() {
                { setDaemon(true); }
                public void run() {
                    while (true) {
                        glCanvas.repaint();
                        try {
                            sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setTitle("Particle System Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar mbar = new JMenuBar();
        setJMenuBar(mbar);
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        mbar.add(file);
        Action newaction = new AbstractAction("New") {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                createNewSystem();
            }
        };
        newaction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        file.add(newaction);
        Action open = new AbstractAction("Open...") {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                showOpenDialog();
            }
        };
        open.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        file.add(open);
        Action save = new AbstractAction("Save...") {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                showSaveDialog();
            }
        };
        save.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        file.add(save);
        file.addSeparator();
        Action quit = new AbstractAction("Quit") {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        quit.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
        file.add(quit);
        
        initColorChooser();
        initFileChooser();
        getContentPane().setLayout(new GridBagLayout());
        colorBorder = new TitledBorder(" PARTICLE COLOR ");
        sizeBorder = new TitledBorder(" PARTICLE SIZE ");
        ageBorder = new TitledBorder(" PARTICLE AGE ");
        speedBorder = new TitledBorder(" PARTICLE SPEED ");
        textureBorder = new TitledBorder(" PARTICLE TEXTURE ");
        emitBorder = new TitledBorder(" EMISSION DIRECTION ");
        angleBorder = new TitledBorder(" ANGLE ");
        randomBorder = new TitledBorder(" SYSTEM RANDOMNESS ");
        countBorder = new TitledBorder(" PARTICLE COUNT ");
        rateBorder = new TitledBorder(" RATE ");
        spawnBorder = new TitledBorder(" SPAWN s");
        velocityBorder = new TitledBorder(" VELOCITY ");
        spinBorder = new TitledBorder(" PARTICLE SPIN ");
        appPanel.setLayout(new GridBagLayout());
        emitPanel.setLayout(new GridBagLayout());
        worldPanel.setLayout(new GridBagLayout());
        
        layerPanel.setLayout(new GridBagLayout());
        layerLabel.setFont(new java.awt.Font("Arial", 1, 13));
        layerLabel.setText("Particle Layers:");
        newLayerButton.setFont(new java.awt.Font("Arial", 0, 12));
        newLayerButton.setMargin(new Insets(2, 14, 2, 14));
        newLayerButton.setText("New");
        newLayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = particleNode.getQuantity();
                createNewLayer();
                layerModel.fireTableRowsInserted(idx, idx);
                layerTable.setRowSelectionInterval(idx, idx);
                deleteLayerButton.setEnabled(true);
            }
        });
        deleteLayerButton.setFont(new java.awt.Font("Arial", 0, 12));
        deleteLayerButton.setMargin(new Insets(2, 14, 2, 14));
        deleteLayerButton.setText("Delete");
        deleteLayerButton.setEnabled(false);
        deleteLayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteLayer();
            }
        });
        layerTable = new JTable(layerModel);
        layerTable.setFont(new java.awt.Font("Arial", 0, 13));
        layerTable.setColumnSelectionAllowed(false);
        layerTable.setRowSelectionAllowed(true);
        layerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int vwidth = layerTable.getTableHeader().getDefaultRenderer().
            getTableCellRendererComponent(layerTable, "Visible", false, false,
                -1, 1).getMinimumSize().width;
        TableColumn vcol = layerTable.getColumnModel().getColumn(1);
        vcol.setMinWidth(vwidth);
        vcol.setPreferredWidth(vwidth);
        vcol.setMaxWidth(vwidth);
        layerTable.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (layerTable.getSelectedRow() != -1) {
                    particleMesh = (ParticleMesh)particleNode.getChild(
                        layerTable.getSelectedRow());
                    updateFromManager();
                }
            }
        });
        
        colorPanel.setLayout(new GridBagLayout());
        startColorLabel.setFont(new java.awt.Font("Arial", 1, 13));
        startColorLabel.setRequestFocusEnabled(true);
        startColorLabel.setText("Starting Color:");
        colorLabel.setFont(new java.awt.Font("Arial", 1, 14));
        colorLabel.setText(">>");
        endColorLabel.setFont(new java.awt.Font("Arial", 1, 13));
        endColorLabel.setText("End Color:");
        startColorHex.setFont(new java.awt.Font("Arial", 0, 10));
        startColorHex.setText("#FFFFFF");
        endColorHex.setFont(new java.awt.Font("Arial", 0, 10));
        endColorHex.setText("#FFFFFF");
        startColorPanel.setBackground(Color.white);
        startColorPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        startColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                startColorPanel_mouseClicked(e);
            }
        });
        endColorPanel.setBackground(Color.white);
        endColorPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        endColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                endColorPanel_mouseClicked(e);
            }
        });
        colorPanel.setBorder(colorBorder);
        colorPanel.setOpaque(false);
        colorBorder.setTitleColor(Color.black);
        colorBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        colorBorder.setBorder(BorderFactory.createEtchedBorder());
        startAlphaLabel.setFont(new java.awt.Font("Arial", 0, 11));
        startAlphaLabel.setText("alpha:");
        endAlphaLabel.setFont(new java.awt.Font("Arial", 0, 11));
        endAlphaLabel.setText("alpha:");
        additiveBlendingBox.setFont(new java.awt.Font("Arial", 1, 13));
        additiveBlendingBox.setText("Additive Blending");
        additiveBlendingBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAlphaState(additiveBlendingBox.isSelected());
            }
        });
        startSizeLabel.setFont(new java.awt.Font("Arial", 1, 13));
        startSizeLabel.setText("Start Size:  0.0");
        sizePanel.setLayout(new GridBagLayout());
        sizePanel.setBorder(sizeBorder);
        sizePanel.setOpaque(false);
        sizeBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        endSizeLabel.setFont(new java.awt.Font("Arial", 1, 13));
        endSizeLabel.setText("End Size: 0.0");
        endSizeSlider.setMaximum(400);
        endSizeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = endSizeSlider.getValue();
                particleMesh.setEndSize(val / 10f);
                updateSizeLabels();
                regenCode();
            }
        });
        startSizeSlider.setMaximum(400);
        startSizeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = startSizeSlider.getValue();
                particleMesh.setStartSize(val / 10f);
                updateSizeLabels();
                regenCode();
            }
        });
        ageBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        ageBorder.setBorder(BorderFactory.createEtchedBorder());
        speedPanel.setLayout(new GridBagLayout());
        speedPanel.setBorder(speedBorder);
        speedPanel.setOpaque(false);
        speedLabel.setFont(new java.awt.Font("Arial", 1, 13));
        speedLabel.setText("Speed Mod.: 0%");
        speedBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        speedSlider.setMaximum(100);
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = speedSlider.getValue();
                particleMesh.getParticleController().setSpeed((float) val * .1f);
                updateSpeedLabels();
                regenCode();
            }
        });
        texturePanel.setBorder(textureBorder);
        texturePanel.setLayout(new GridBagLayout());
        textureBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        textureLabel.setFont(new java.awt.Font("Arial", 1, 13));
        textureLabel.setText("Texture Image:");
        changeTextureButton.setFont(new java.awt.Font("Arial", 1, 12));
        changeTextureButton.setMargin(new Insets(2, 2, 2, 2));
        changeTextureButton.setText("Browse...");
        changeTextureButton
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        changeTexture();
                    }
                });
        imageLabel.setBackground(Color.lightGray);
        imageLabel.setMaximumSize(new Dimension(128, 128));
        imageLabel.setMinimumSize(new Dimension(0, 0));
        imageLabel.setOpaque(false);
        imageLabel.setText("");

        agePanel.setLayout(new GridBagLayout());
        agePanel.setBorder(ageBorder);
        minAgeLabel.setFont(new java.awt.Font("Arial", 1, 13));
        minAgeLabel.setText("Minimum Age: 1000ms");
        minAgeSlider.setMajorTickSpacing(1000);
        minAgeSlider.setMaximum(10000);
        minAgeSlider.setMinimum(0);
        minAgeSlider.setMinorTickSpacing(100);
        minAgeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = minAgeSlider.getValue();
                particleMesh.setMinimumLifeTime((float)val);
                updateAgeLabels();
                regenCode();
            }
        });
        maxAgeLabel.setFont(new java.awt.Font("Arial", 1, 13));
        maxAgeLabel.setText("Maximum Age: 1000ms");
        maxAgeSlider.setMajorTickSpacing(1000);
        maxAgeSlider.setMaximum(10000);
        maxAgeSlider.setMinimum(0);
        maxAgeSlider.setMinorTickSpacing(100);
        maxAgeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = maxAgeSlider.getValue();
                particleMesh.setMaximumLifeTime((float)val);
                updateAgeLabels();
                regenCode();
            }
        });

        directionPanel.setBorder(emitBorder);
        directionPanel.setLayout(new GridBagLayout());
        emitBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        emitBorder.setTitle(" DIRECTION ");
        emitZSlider.setOrientation(JSlider.VERTICAL);
        emitZSlider.setMajorTickSpacing(5);
        emitZSlider.setMinimum(-10);
        emitZSlider.setMaximum(10);
        emitZSlider.setMinorTickSpacing(1);
        emitZSlider.setPaintLabels(true);
        emitZSlider.setPaintTicks(true);
        emitZSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = emitZSlider.getValue();
                if (particleMesh != null) {
                    particleMesh.getEmissionDirection().z = (float) val * .1f;
                    particleMesh.updateRotationMatrix();
                }
                regenCode();
            }
        });
        emitYSlider.setOrientation(JSlider.VERTICAL);
        emitYSlider.setMajorTickSpacing(5);
        emitYSlider.setMinimum(-10);
        emitYSlider.setMaximum(10);
        emitYSlider.setMinorTickSpacing(1);
        emitYSlider.setPaintLabels(true);
        emitYSlider.setPaintTicks(true);
        emitYSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = emitYSlider.getValue();
                if (particleMesh != null) {
                    particleMesh.getEmissionDirection().y = (float) val * .1f;
                    particleMesh.updateRotationMatrix();
                }
                regenCode();
            }
        });
        emitXSlider.setOrientation(JSlider.VERTICAL);
        emitXSlider.setMajorTickSpacing(5);
        emitXSlider.setMinimum(-10);
        emitXSlider.setMaximum(10);
        emitXSlider.setMinorTickSpacing(1);
        emitXSlider.setPaintLabels(true);
        emitXSlider.setPaintTicks(true);
        emitXSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = emitXSlider.getValue();
                if (particleMesh != null) {
                    particleMesh.getEmissionDirection().x = (float) val * .1f;
                    particleMesh.updateRotationMatrix();
                }
                regenCode();
            }
        });
        emitXLabel.setFont(new java.awt.Font("Arial", 1, 13));
        emitXLabel.setText("X");
        emitYLabel.setFont(new java.awt.Font("Arial", 1, 13));
        emitYLabel.setText("Y");
        emitZLabel.setFont(new java.awt.Font("Arial", 1, 13));
        emitZLabel.setText("Z");
        anglePanel.setBorder(angleBorder);
        anglePanel.setLayout(new GridBagLayout());
        angleBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        angleLabel.setText("Degrees Off Direction:  0");
        angleLabel.setFont(new java.awt.Font("Arial", 1, 13));
        angleSlider.setValue(0);
        angleSlider.setMinimum(0);
        angleSlider.setMaximum(180);
        angleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = angleSlider.getValue();
                particleMesh.setMaximumAngle((float) val * FastMath.DEG_TO_RAD);
                updateAngleLabels();
                regenCode();
            }
        });
        
        minAngleLabel.setText("Min Degrees Off Direction:  0");
        minAngleLabel.setFont(new java.awt.Font("Arial", 1, 13));
        minAngleSlider.setValue(0);
        minAngleSlider.setMinimum(0);
        minAngleSlider.setMaximum(179);
        minAngleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = minAngleSlider.getValue();
                particleMesh.setMinimumAngle((float) val
                        * FastMath.DEG_TO_RAD);
                updateAngleLabels();
                regenCode();
            }
        });

        randomPanel.setBorder(randomBorder);
        randomPanel.setLayout(new GridBagLayout());
        randomBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        randomLabel.setFont(new java.awt.Font("Arial", 1, 13));
        randomLabel.setText("Random Factor: 0.0");
        randomSlider.setValue(0);
        randomSlider.setMaximum(100);
        randomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = randomSlider.getValue();
                particleMesh.setRandomMod((float) val * .1f);
                updateRandomLabels();
                regenCode();
            }
        });
        examplesPanel.setLayout(new GridBagLayout());
        examplesLabel.setFont(new java.awt.Font("Arial", 1, 13));
        examplesLabel.setText("Prebuilt Examples:");
        exampleButton.setFont(new java.awt.Font("Arial", 0, 12));
        exampleButton.setMargin(new Insets(2, 14, 2, 14));
        exampleButton.setText("Apply");
        exampleButton.setEnabled(false);
        exampleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyExample();
            }
        });
        updateExampleModel();
        exampleList = new JList(exampleModel);
        exampleList.setFont(new java.awt.Font("Arial", 0, 13));
        exampleList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (exampleList.getSelectedValue() instanceof String)
                    exampleButton.setEnabled(true);
                else
                    exampleButton.setEnabled(false);
            }
        });
        codePanel.setFont(new java.awt.Font("Arial", 0, 13));
        codePanel.setLayout(new GridBagLayout());
        layerPanel.setFont(new java.awt.Font("Arial", 0, 13));
        appPanel.setFont(new java.awt.Font("Arial", 0, 13));
        emitPanel.setFont(new java.awt.Font("Arial", 0, 13));
        worldPanel.setFont(new java.awt.Font("Arial", 0, 13));
        examplesPanel.setFont(new java.awt.Font("Arial", 0, 13));
        mainTabbedPane1.setFont(new java.awt.Font("Arial", 0, 13));
        codeLabel.setFont(new java.awt.Font("Arial", 1, 13));
        codeLabel.setText("Particle Code:");
        codeTextArea.setFont(new java.awt.Font("Monospaced", 0, 10));
        codeTextArea.setText("");
        codeTextArea.setEditable(false);
        codeTextArea.setAutoscrolls(true);
        countPanel.setFont(new java.awt.Font("Arial", 0, 13));
        countPanel.setBorder(countBorder);
        countPanel.setLayout(new GridBagLayout());
        countBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        countLabel.setFont(new java.awt.Font("Arial", 1, 13));
        countLabel.setText("Particles: 300");
        countButton.setFont(new java.awt.Font("Arial", 1, 12));
        countButton.setMargin(new Insets(2, 2, 2, 2));
        countButton.setText("Change...");
        countButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countButton_actionPerformed(e);
            }
        });
        flowPanel.setFont(new java.awt.Font("Arial", 0, 13));
        flowPanel.setLayout(new GridBagLayout());
        ratePanel.setFont(new java.awt.Font("Arial", 0, 13));
        ratePanel.setBorder(rateBorder);
        ratePanel.setLayout(new GridBagLayout());
        rateBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        rateLabel.setEnabled(false);
        rateLabel.setFont(new java.awt.Font("Arial", 1, 13));
        rateLabel.setText("Particles per second: 1000");
        spawnPanel.setLayout(new GridBagLayout());
        spawnPanel.setBorder(spawnBorder);
        spawnBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        spawnBox.setSelected(true);
        spawnBox.setText("Respawn \'dead\' particles.");
        spawnBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (spawnBox.isSelected())
                    particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
                else
                    particleMesh.getParticleController()
                            .setRepeatType(Controller.RT_CLAMP);
            }
        });
        spawnButton.setFont(new java.awt.Font("Arial", 0, 12));
        spawnButton.setText("Force Spawn");
        spawnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Spatial child : particleNode.getChildren()) {
                    if (child instanceof ParticleMesh) {
                        ((ParticleMesh)child).forceRespawn();
                    }
                }
            }
        });
        rateVarLabel.setEnabled(false);
        rateVarLabel.setFont(new java.awt.Font("Arial", 1, 13));
        rateVarLabel.setText("Variance: 0%");
        rateSlider.setMaximum(600);
        rateSlider.setEnabled(false);
        rateSlider.setValue(300);
        rateSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = rateSlider.getValue();
                particleMesh.setReleaseRate(val);
                updateRateLabels();
                regenCode();
            }
        });
        rateVarSlider.setMaximum(100);
        rateVarSlider.setEnabled(false);
        rateVarSlider.setValue(0);
        rateVarSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = rateVarSlider.getValue();
                particleMesh
                        .setReleaseVariance((float) val * .01f);
                updateRateLabels();
                regenCode();
            }
        });
        rateBox.setFont(new java.awt.Font("Arial", 1, 13));
        rateBox.setText("Regulate Flow");
        rateBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                particleMesh.getParticleController().setControlFlow(rateBox.isSelected());
                updateRateLabels();
            }
        });
        velocityPanel.setLayout(new GridBagLayout());
        velocityPanel.setBorder(velocityBorder);
        velocityBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        velocityBorder.setBorder(BorderFactory.createEtchedBorder(new Color(
                240, 217, 205), new Color(117, 106, 100)));
        velocityLabel.setFont(new java.awt.Font("Arial", 1, 13));
        velocityLabel.setText("Initial Velocity: .0001");
        velocitySlider.setMaximum(1000);
        velocitySlider.setValue(10);
        velocitySlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = velocitySlider.getValue();
                particleMesh
                        .setInitialVelocity((float) val * .01f);
                updateVelocityLabels();
                regenCode();
            }
        });
        spinPanel.setLayout(new GridBagLayout());
        spinPanel.setBorder(spinBorder);
        spinBorder.setTitleFont(new java.awt.Font("Arial", 0, 10));
        spinBorder.setBorder(BorderFactory.createEtchedBorder(new Color(240,
                217, 205), new Color(117, 106, 100)));
        spinLabel.setFont(new java.awt.Font("Arial", 1, 13));
        spinLabel.setText("Spin Speed: .0001");
        spinSlider.setMaximum(50);
        spinSlider.setMinimum(-50);
        spinSlider.setValue(0);
        spinSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = spinSlider.getValue();
                particleMesh
                        .setParticleSpinSpeed((float) val * .01f);
                updateSpinLabels();
                regenCode();
            }
        });
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.getContentPane().add(mainTabbedPane1, gridBagConstraints);
        emitPanel.add(directionPanel, new GridBagConstraints(0, 0, 1, 1, 0.5,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 5, 5), 0, 0));
        emitPanel.add(velocityPanel, new GridBagConstraints(0, 2, 1, 1, 0.5,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 10, 10), 0, 0));
        emitPanel.add(spinPanel, new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 5, 10, 10), 0, 0));
        directionPanel.add(emitXSlider, new GridBagConstraints(0, 0, 1, 1, 0.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 5, 0), 0, 0));
        directionPanel.add(emitYSlider, new GridBagConstraints(1, 0, 1, 1, 0.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 0, 5, 0), 0, 0));
        directionPanel.add(emitZSlider, new GridBagConstraints(2, 0, 1, 1, 0.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 0, 5, 10), 0, 0));
        directionPanel.add(emitXLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        directionPanel.add(emitYLabel, new GridBagConstraints(1, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        directionPanel.add(emitZLabel, new GridBagConstraints(2, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5), 0, 0));

        worldPanel.add(speedPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 10, 5, 5), 0, 0));

        startAlphaSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                particleMesh.getStartColor().a = (Integer
                        .parseInt(startAlphaSpinner.getValue().toString()) / 255f);
                regenCode();
            }
        });

        endAlphaSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                particleMesh.getEndColor().a = (Integer
                        .parseInt(endAlphaSpinner.getValue().toString()) / 255f);
                regenCode();
            }
        });

        layerPanel.add(layerLabel, new GridBagConstraints(0, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 5, 10), 0, 0));
        layerPanel.add(layerSP, new GridBagConstraints(0, 1, 2, 1, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 10), 0, 0));
        layerPanel.add(newLayerButton, new GridBagConstraints(0, 2, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 10, 10, 10), 0, 0));
        layerPanel.add(deleteLayerButton, new GridBagConstraints(1, 2, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 10, 10, 10), 0, 0));
        layerSP.setViewportView(layerTable);
        appPanel.add(colorPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 10, 5, 5), 0, 0));
        colorPanel.add(startColorLabel, new GridBagConstraints(0, 0, 2, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 10, 0, 10), 0, 0));
        colorPanel.add(colorLabel, new GridBagConstraints(2, 0, 1, 3, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        0, 5, 0, 5), 0, 0));
        colorPanel.add(endColorLabel, new GridBagConstraints(3, 0, 2, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 10, 0, 10), 0, 0));
        colorPanel.add(startColorPanel, new GridBagConstraints(0, 1, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 25, 25));
        colorPanel.add(endColorPanel, new GridBagConstraints(3, 1, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 25, 25));
        colorPanel.add(startColorHex, new GridBagConstraints(0, 2, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 4, 0), 0, 0));
        colorPanel.add(endColorHex, new GridBagConstraints(3, 2, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 4, 0), 0, 0));
        colorPanel.add(startAlphaSpinner, new GridBagConstraints(1, 3, 1, 1,
                0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 20, 0));
        colorPanel.add(startAlphaLabel, new GridBagConstraints(0, 3, 1, 1,
                0.25, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        colorPanel.add(endAlphaLabel, new GridBagConstraints(3, 3, 1, 1, 0.25,
                0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        colorPanel.add(endAlphaSpinner, new GridBagConstraints(4, 3, 1, 1,
                0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 20, 0));
        colorPanel.add(additiveBlendingBox, new GridBagConstraints(0, 4, 5, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        appPanel.add(sizePanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 5, 5, 10), 0, 0));
        sizePanel.add(startSizeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 4, 0, 0), 0, 0));
        sizePanel.add(startSizeSlider, new GridBagConstraints(0, 1, 1, 1, 1.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 4, 0, 0), 100, 0));
        sizePanel.add(endSizeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(4, 4, 0, 0), 0, 0));
        sizePanel.add(endSizeSlider, new GridBagConstraints(0, 3, 1, 1, 1.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 4, 0, 0), 100, 0));
        appPanel.add(texturePanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 10, 5, 5), 0, 0));
        texturePanel.add(textureLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 10, 5, 5), 0, 0));
        texturePanel.add(changeTextureButton, new GridBagConstraints(0, 1, 1,
                1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 10, 10, 5), 0, 0));
        texturePanel.add(imageLabel, new GridBagConstraints(1, 0, 1, 2, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
        appPanel.add(countPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 5, 5, 10), 0, 0));
        speedPanel.add(speedLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        10, 10, 0, 10), 0, 0));
        speedPanel.add(speedSlider, new GridBagConstraints(0, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        worldPanel.add(agePanel, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 10, 5, 5), 0, 0));
        agePanel.add(minAgeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
                        5, 5, 0), 0, 0));
        agePanel.add(minAgeSlider, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        agePanel.add(maxAgeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
                        5, 5, 0), 0, 0));
        agePanel.add(maxAgeSlider, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        worldPanel.add(randomPanel, new GridBagConstraints(0, 2, 1, 1, 0.5,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 10, 5), 0, 0));
        emitPanel.add(anglePanel, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 10, 10, 5), 0, 0));
        anglePanel.add(angleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 10, 5, 10), 0, 0));
        anglePanel.add(angleSlider, new GridBagConstraints(0, 1, 1, 1, 1.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
        anglePanel.add(minAngleLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 10, 5, 10), 0, 0));
        anglePanel.add(minAngleSlider, new GridBagConstraints(0, 3, 1, 1, 1.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
        randomPanel.add(randomLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 10, 5, 10), 0, 0));
        randomPanel.add(randomSlider, new GridBagConstraints(0, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        examplesPanel.add(examplesLabel, new GridBagConstraints(0, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 5, 10), 0, 0));
        examplesPanel.add(exampleSP, new GridBagConstraints(0, 1, 1, 1, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 10), 0, 0));
        examplesPanel.add(exampleButton, new GridBagConstraints(0, 2, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 10, 10, 10), 0, 0));
        exampleSP.setViewportView(exampleList);
        codePanel.add(codeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        10, 10, 5, 10), 0, 0));
        final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
        gridBagConstraints_2.gridy = 0;
        gridBagConstraints_2.gridx = 1;
        codePanel.add(codeSP, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(5, 10, 10, 10), 0, 0));
        countPanel.add(countLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        5, 10, 5, 10), 0, 0));
        countPanel.add(countButton, new GridBagConstraints(1, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 10, 5, 10), 0, 0));
        codeSP.setViewportView(codeTextArea);

        mainTabbedPane1.add(layerPanel, "Layers");
        mainTabbedPane1.add(appPanel, "Appearance");
        mainTabbedPane1.add(emitPanel, "Emission");
        mainTabbedPane1.add(flowPanel, "Flow");
        mainTabbedPane1.add(worldPanel, "World");
        mainTabbedPane1.add(examplesPanel, "Examples");
        mainTabbedPane1.add(codePanel, "Code");
        flowPanel.add(ratePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 10, 5, 10), 0, 0));
        flowPanel.add(spawnPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        10, 5, 10, 10), 0, 0));
        spawnPanel.add(spawnBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        10, 10, 5, 10), 0, 0));
        spawnPanel.add(spawnButton, new GridBagConstraints(0, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 10, 10, 10), 0, 0));
        ratePanel.add(rateLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
                        10, 0, 10), 0, 0));
        ratePanel.add(rateSlider, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 10, 10), 0, 0));
        ratePanel.add(rateVarLabel, new GridBagConstraints(0, 3, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 10, 0, 10), 0, 0));
        ratePanel.add(rateVarSlider, new GridBagConstraints(0, 4, 1, 1, 1.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 10, 10), 0, 0));
        ratePanel.add(rateBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        10, 10, 5, 10), 0, 0));
        velocityPanel.add(velocityLabel, new GridBagConstraints(0, 0, 1, 1,
                1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 5, 10), 0, 0));
        velocityPanel.add(velocitySlider, new GridBagConstraints(0, 1, 1, 1,
                1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 10, 10), 0, 0));
        spinPanel.add(spinLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        10, 10, 5, 10), 0, 0));
        spinPanel.add(spinSlider, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 10, 10, 10), 0, 0));

        setSize(new Dimension(800, 600));
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.weightx = .9;
        gridBagConstraints_1.weighty = 1.0;
        gridBagConstraints_1.fill = GridBagConstraints.BOTH;
        gridBagConstraints_1.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints_1.gridx = 1;
        gridBagConstraints_1.gridy = 0;
        getContentPane().add(getGlCanvas(), gridBagConstraints_1);
    }

    private void createNewSystem() {
        layerTable.clearSelection();
        particleNode.detachAllChildren();
        createNewLayer();
        layerModel.fireTableDataChanged();
        layerTable.setRowSelectionInterval(0, 0);
        deleteLayerButton.setEnabled(false);
    }
    
    private void showOpenDialog() {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        try {
            Object obj = BinaryImporter.getInstance().load(file);
            if (obj instanceof Node) {
                Node node = (Node)obj;
                for (int ii = node.getQuantity() - 1; ii >= 0; ii--) {
                    if (!(node.getChild(ii) instanceof ParticleMesh)) {
                        node.detachChildAt(ii);
                    }
                }
                if (node.getQuantity() == 0) {
                    throw new Exception("Node contains no particle meshes");
                }
                layerTable.clearSelection();
                root.detachChild(particleNode);
                particleNode = node;
                root.attachChild(particleNode);
                deleteLayerButton.setEnabled(true);
                
            } else { // obj instanceof ParticleMesh
                particleMesh = (ParticleMesh)obj;
                layerTable.clearSelection();
                particleNode.detachAllChildren();
                particleNode.attachChild(particleMesh);   
                deleteLayerButton.setEnabled(false);
            }
            particleNode.updateRenderState();
            layerModel.fireTableDataChanged();
            layerTable.setRowSelectionInterval(0, 0);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Couldn't open '" + file +
                "': " + e, "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void showSaveDialog() {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        try {
            BinaryExporter.getInstance().save(particleNode.getQuantity() > 1 ?
                particleNode : particleMesh, file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Couldn't save '" + file +
                "': " + e, "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createNewLayer() {
        particleMesh = ParticleFactory.buildParticles(createLayerName(), 300);
        particleMesh.addForce(SimpleParticleForceFactory.createBasicGravity(new Vector3f(0,-3f,0)));
        particleMesh.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
        particleMesh.setMaximumAngle(0.2268928f);
        particleMesh.getParticleController().setSpeed(1.0f);
        particleMesh.setMinimumLifeTime(2000.0f);
        particleMesh.setStartSize(10.0f);
        particleMesh.setEndSize(10.0f);
        particleMesh.setStartColor(new ColorRGBA(0.0f, 0.0625f, 1.0f, 1.0f));
        particleMesh.setEndColor(new ColorRGBA(0.0f, 0.0625f, 1.0f, 0.0f));
        particleMesh.setRandomMod(1.0f);
        particleMesh.warmUp(120);

        updateAlphaState(true);
        
        TextureState ts = impl.getRenderer().createTextureState();
        ts.setTexture(TextureManager.loadTexture(
            RenParticleEditor.class.getClassLoader().getResource(
                "jmetest/data/texture/flaresmall.jpg"),
            Texture.FM_LINEAR, Texture.FM_LINEAR));
        particleMesh.setRenderState(ts);
        
        particleNode.attachChild(particleMesh);
        particleMesh.updateRenderState();
    }
    
    private String createLayerName () {
        int max = -1;
        for (int ii = 0, nn = particleNode.getQuantity(); ii < nn; ii++) {
            String name = particleNode.getChild(ii).getName();
            if (name.startsWith("Layer #")) {
                try {
                    max = Math.max(max, Integer.parseInt(name.substring(7)));
                } catch (NumberFormatException e) {}
            }
        }
        return "Layer #" + (max + 1);
    }
    
    private void updateAlphaState(boolean additive) {
        AlphaState as = (AlphaState)particleMesh.getRenderState(
            RenderState.RS_ALPHA);
        if (as == null) {
            as = impl.getRenderer().createAlphaState();
            as.setBlendEnabled(true);
            as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
            as.setTestEnabled(true);
            as.setTestFunction(AlphaState.TF_GREATER);
            particleMesh.setRenderState(as);
            particleMesh.updateRenderState();
        }
        as.setDstFunction(additive ?
            AlphaState.DB_ONE : AlphaState.DB_ONE_MINUS_SRC_ALPHA);
    }
    
    private void deleteLayer() {
        int idx = layerTable.getSelectedRow(),
            sidx = (idx == particleNode.getQuantity() - 1) ? idx - 1 : idx;
        layerTable.clearSelection();
        particleNode.detachChildAt(idx);
        layerModel.fireTableRowsDeleted(idx, idx);
        layerTable.setRowSelectionInterval(sidx, sidx);
        
        if (particleNode.getQuantity() == 1) {
            deleteLayerButton.setEnabled(false);
        }
    }
    
  /**
     * applyExample
     */
    private void applyExample() {
        if (exampleList == null || exampleList.getSelectedValue() == null)
            return;
        String examType = exampleList.getSelectedValue().toString();
        particleMesh.clearForces();
        if ("FIRE".equalsIgnoreCase(examType)) {
            particleMesh.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
            particleMesh.setMaximumAngle(0.20943952f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(1.0f);
            particleMesh.setMinimumLifeTime(1000.0f);
            particleMesh.setMaximumLifeTime(1500.0f);
            particleMesh.setStartSize(40.0f);
            particleMesh.setEndSize(40.0f);
            particleMesh.setStartColor(new ColorRGBA(1.0f, 0.312f, 0.121f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(1.0f, 0.312f, 0.121f, 0.0f));
            particleMesh.setRandomMod(6.0f);
            particleMesh.getParticleController().setControlFlow(true);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(0.3f);
            particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
        } else if ("FOUNTAIN".equalsIgnoreCase(examType)) {
            particleMesh.addForce(SimpleParticleForceFactory.createBasicGravity(new Vector3f(0,-3f,0)));
            particleMesh.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
            particleMesh.setMaximumAngle(0.2268928f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(1.0f);
            particleMesh.setMinimumLifeTime(1300.0f);
            particleMesh.setMaximumLifeTime(1950.0f);
            particleMesh.setStartSize(10.0f);
            particleMesh.setEndSize(10.0f);
            particleMesh.setStartColor(new ColorRGBA(0.0f, 0.0625f, 1.0f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(0.0f, 0.0625f, 1.0f, 0.0f));
            particleMesh.setRandomMod(1.0f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(1.1f);
            particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
        } else if ("LAVA".equalsIgnoreCase(examType)) {
            particleMesh.addForce(SimpleParticleForceFactory.createBasicGravity(new Vector3f(0,-3f,0)));
            particleMesh.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
            particleMesh.setMaximumAngle(0.418f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(1.0f);
            particleMesh.setMinimumLifeTime(1057.0f);
            particleMesh.setMaximumLifeTime(1500.0f);
            particleMesh.setStartSize(40.0f);
            particleMesh.setEndSize(40.0f);
            particleMesh.setStartColor(new ColorRGBA(1.0f, 0.18f, 0.125f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(1.0f, 0.18f, 0.125f, 0.0f));
            particleMesh.setRandomMod(2.0f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(1.1f);
            particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
        } else if ("SMOKE".equalsIgnoreCase(examType)) {
            particleMesh.setEmissionDirection(new Vector3f(0.0f, 0.6f, 0.0f));
            particleMesh.setMaximumAngle(0.36651915f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(0.2f);
            particleMesh.setMinimumLifeTime(1000.0f);
            particleMesh.setMaximumLifeTime(1500.0f);
            particleMesh.setStartSize(32.5f);
            particleMesh.setEndSize(40.0f);
            particleMesh.setStartColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 0.0f));
            particleMesh.setRandomMod(0.1f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(0.58f);
            particleMesh.setParticleSpinSpeed(0.08f);
        } else if ("RAIN".equalsIgnoreCase(examType)) {
            particleMesh.addForce(SimpleParticleForceFactory.createBasicGravity(new Vector3f(0,-3f,0)));
            particleMesh.setEmissionDirection(new Vector3f(0.0f, -1.0f, 0.0f));
            particleMesh.setMaximumAngle(3.1415927f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(0.5f);
            particleMesh.setMinimumLifeTime(1626.0f);
            particleMesh.setMaximumLifeTime(2400.0f);
            particleMesh.setStartSize(9.1f);
            particleMesh.setEndSize(13.6f);
            particleMesh.setStartColor(new ColorRGBA(0.16078432f, 0.16078432f, 1.0f,
                    1.0f));
            particleMesh.setEndColor(new ColorRGBA(0.16078432f, 0.16078432f, 1.0f,
                    0.15686275f));
            particleMesh.setRandomMod(0.0f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(0.58f);
            particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
        } else if ("SNOW".equalsIgnoreCase(examType)) {
            particleMesh.addForce(SimpleParticleForceFactory.createBasicGravity(new Vector3f(0,-3f,0)));
            particleMesh.setEmissionDirection(new Vector3f(0.0f, -1.0f, 0.0f));
            particleMesh.setMaximumAngle(1.5707964f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(0.2f);
            particleMesh.setMinimumLifeTime(1057.0f);
            particleMesh.setMaximumLifeTime(1500.0f);
            particleMesh.setStartSize(30.0f);
            particleMesh.setEndSize(30.0f);
            particleMesh.setStartColor(new ColorRGBA(0.3764706f, 0.3764706f,
                    0.3764706f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(0.3764706f, 0.3764706f,
                    0.3764706f, 0.1882353f));
            particleMesh.setRandomMod(1.0f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(0.59999996f);
            particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
        } else if ("JET".equalsIgnoreCase(examType)) {
            particleMesh.setEmissionDirection(new Vector3f(-1.0f, 0.0f, 0.0f));
            particleMesh.setMaximumAngle(0.034906585f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(1.0f);
            particleMesh.setMinimumLifeTime(100.0f);
            particleMesh.setMaximumLifeTime(150.0f);
            particleMesh.setStartSize(6.6f);
            particleMesh.setEndSize(30.0f);
            particleMesh.setStartColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(0.6f, 0.2f, 0.0f, 0.0f));
            particleMesh.setRandomMod(10.0f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(1.4599999f);
            particleMesh.getParticleController().setRepeatType(Controller.RT_WRAP);
        } else if ("EXPLOSION".equalsIgnoreCase(examType)) {
            particleMesh.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
            particleMesh.setMaximumAngle(3.1415927f);
            particleMesh.setMinimumAngle(0);
            particleMesh.getParticleController().setSpeed(1.4f);
            particleMesh.setMinimumLifeTime(1000.0f);
            particleMesh.setMaximumLifeTime(1500.0f);
            particleMesh.setStartSize(40.0f);
            particleMesh.setEndSize(40.0f);
            particleMesh.setStartColor(new ColorRGBA(1.0f, 0.312f, 0.121f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(1.0f, 0.24313726f, 0.03137255f,
                    0.0f));
            particleMesh.setRandomMod(0.0f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.getParticleController().setRepeatType(Controller.RT_CLAMP);
        } else if ("GROUND FOG".equalsIgnoreCase(examType)) {
            particleMesh.setEmissionDirection(new Vector3f(0.0f, 0.3f, 0.0f));
            particleMesh.setMaximumAngle(1.5707964f);
            particleMesh.setMinimumAngle(1.5707964f);
            particleMesh.getParticleController().setSpeed(0.5f);
            particleMesh.setMinimumLifeTime(1774.0f);
            particleMesh.setMaximumLifeTime(2800.0f);
            particleMesh.setStartSize(35.4f);
            particleMesh.setEndSize(40.0f);
            particleMesh.setStartColor(new ColorRGBA(0.87058824f, 0.87058824f, 0.87058824f, 1.0f));
            particleMesh.setEndColor(new ColorRGBA(0.0f, 0.8f, 0.8f, 0.0f));
            particleMesh.setRandomMod(0.3f);
            particleMesh.getParticleController().setControlFlow(false);
            particleMesh.setReleaseRate(300);
            particleMesh.setReleaseVariance(0.0f);
            particleMesh.setInitialVelocity(1.0f);
            particleMesh.setParticleSpinSpeed(0.0f);
        }

        particleMesh.warmUp(120);
        updateFromManager();
    }

    /**
     * updateExampleModel
     */
    private void updateExampleModel() {
        exampleModel.addElement("Fire");
        exampleModel.addElement("Fountain");
        exampleModel.addElement("Lava");
        exampleModel.addElement("Smoke");
        exampleModel.addElement("Jet");
        exampleModel.addElement("Snow");
        exampleModel.addElement("Rain");
        exampleModel.addElement("Explosion");
        exampleModel.addElement("Ground Fog");
    }

    /**
     * updateFromManager
     */
    public void updateFromManager() {
        startColorPanel.setBackground(makeColor(particleMesh
                .getStartColor(), false));
        endColorPanel.setBackground(makeColor(particleMesh
                .getEndColor(), false));
        startAlphaSpinner.setValue(new Integer(makeColor(
                particleMesh.getStartColor(), true).getAlpha()));
        endAlphaSpinner.setValue(new Integer(makeColor(
                particleMesh.getEndColor(), true).getAlpha()));
        updateColorLabels();
        AlphaState as = (AlphaState)particleMesh.getRenderState(
            RenderState.RS_ALPHA);
        additiveBlendingBox.setSelected(as == null ||
            as.getDstFunction() == AlphaState.DB_ONE);
        startSizeSlider.setValue((int) (particleMesh
                .getStartSize() * 10));
        endSizeSlider
                .setValue((int) (particleMesh.getEndSize() * 10));
        updateSizeLabels();
        
        Texture tex = ((TextureState)particleMesh.getRenderState(
            RenderState.RS_TEXTURE)).getTexture();
        try {
            imageLabel.setIcon(
                new ImageIcon(new URL(tex.getImageLocation())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        minAgeSlider.setValue((int) (particleMesh
                .getMinimumLifeTime()));
        maxAgeSlider.setValue((int) (particleMesh
                .getMaximumLifeTime()));
        updateAgeLabels();
        speedSlider.setValue((int) (particleMesh.getParticleController().getSpeed() * 10));
        updateSpeedLabels();
        emitXSlider.setValue((int) (particleMesh
                .getEmissionDirection().x * 10));
        emitYSlider.setValue((int) (particleMesh
                .getEmissionDirection().y * 10));
        emitZSlider.setValue((int) (particleMesh
                .getEmissionDirection().z * 10));
        angleSlider.setValue((int) (particleMesh
                .getMaximumAngle() * FastMath.RAD_TO_DEG));
        minAngleSlider.setValue((int) (particleMesh
                .getMinimumAngle() * FastMath.RAD_TO_DEG));
        updateAngleLabels();
        randomSlider
                .setValue((int) (particleMesh.getRandomMod() * 10));
        updateRandomLabels();
        rateBox.setSelected(particleMesh.getParticleController().getControlFlow());
        rateSlider.setValue(particleMesh.getReleaseRate());
        rateSlider
                .setMaximum(particleMesh.getNumParticles() * 5);
        rateVarSlider.setValue((int) (particleMesh
                .getReleaseVariance() * 100));
        updateRateLabels();
        spawnBox
                .setSelected(particleMesh.getParticleController().getRepeatType() == Controller.RT_WRAP);
        velocitySlider.setValue((int) (particleMesh
                .getInitialVelocity() * 100));
        updateVelocityLabels();
        spinSlider.setValue((int) (particleMesh
                .getParticleSpinSpeed() * 100));
        updateSpinLabels();
        regenCode();
        validate();
    }

    /**
     * updateManager
     * 
     * @param particles
     *            number of particles to reset manager with.
     */
    public void resetManager(int particles) {
        ParticleMesh omesh = particleMesh;
        particleNode.detachChild(particleMesh);
        particleMesh = ParticleFactory.buildParticles(omesh.getName(), particles);

        ColorRGBA rgba = makeColorRGBA(startColorPanel.getBackground());
        rgba.a = (Integer.parseInt(startAlphaSpinner.getValue().toString()) / 255f);
        particleMesh.setStartColor(rgba);

        rgba = makeColorRGBA(endColorPanel.getBackground());
        rgba.a = (Integer.parseInt(endAlphaSpinner.getValue().toString()) / 255f);
        particleMesh.setEndColor(rgba);

        int val = startSizeSlider.getValue();
        particleMesh.setStartSize(val / 10f);

        val = endSizeSlider.getValue();
        particleMesh.setEndSize(val / 10f);

        val = minAgeSlider.getValue();
        particleMesh.setMinimumLifeTime((float) val);

        val = maxAgeSlider.getValue();
        particleMesh.setMaximumLifeTime((float) val);

        val = speedSlider.getValue();
        particleMesh.getParticleController().setSpeed((float) val * .1f);

        val = emitXSlider.getValue();
        particleMesh.getEmissionDirection().x = (float) val * .1f;
        val = emitYSlider.getValue();
        particleMesh.getEmissionDirection().y = (float) val * .1f;
        val = emitZSlider.getValue();
        particleMesh.getEmissionDirection().z = (float) val * .1f;
        particleMesh.updateRotationMatrix();

        val = angleSlider.getValue();
        particleMesh.setMaximumAngle((float) val
                * FastMath.DEG_TO_RAD);

        val = minAngleSlider.getValue();
        particleMesh.setMinimumAngle((float) val
                * FastMath.DEG_TO_RAD);

        val = randomSlider.getValue();
        particleMesh.setRandomMod((float) val * .1f);

        val = rateSlider.getValue();
        particleMesh.setReleaseRate(val);
        val = rateVarSlider.getValue();
        particleMesh.setReleaseVariance((float) val * .01f);

        particleMesh.getParticleController()
                .setRepeatType(spawnBox.isSelected() ? Controller.RT_WRAP
                        : Controller.RT_CLAMP);

        particleMesh.getParticleController().setControlFlow(rateBox.isSelected());
        rateSlider.setMaximum(particles * 5);
        rateSlider.setValue(particles);

        val = velocitySlider.getValue();
        particleMesh.setInitialVelocity((float) val * .01f);
        updateVelocityLabels();

        val = spinSlider.getValue();
        particleMesh.setParticleSpinSpeed((float) val * .01f);
        updateSpinLabels();

        for (int ii = 0; ii < RenderState.RS_MAX_STATE; ii++) {
            RenderState rs = omesh.getRenderState(ii);
            if (rs != null) {
                particleMesh.setRenderState(rs);
            }
        }
        
        particleNode.attachChild(particleMesh);
        particleNode.updateRenderState();
        regenCode();
        validate();
    }

    /**
     * updateVelocityLabels
     */
    private void updateVelocityLabels() {
        int val = velocitySlider.getValue();
        velocityLabel.setText("Initial Velocity: " + (val / 100f));
    }

    /**
     * updateVelocityLabels
     */
    private void updateSpinLabels() {
        int val = spinSlider.getValue();
        spinLabel.setText("Spin Speed: " + (val / 100f));
    }

    /**
     * updateRateLabels
     */
    private void updateRateLabels() {
        rateLabel.setEnabled(rateBox.isSelected());
        rateSlider.setEnabled(rateBox.isSelected());
        rateVarLabel.setEnabled(rateBox.isSelected());
        rateVarSlider.setEnabled(rateBox.isSelected());
        int val = rateSlider.getValue();
        rateLabel.setText("Particles per second: " + val);
        val = rateVarSlider.getValue();
        rateVarLabel.setText("Variance: " + (val / 100f) + "%");
    }

    /**
     * updateRandomLabels
     */
    private void updateRandomLabels() {
        int val = randomSlider.getValue();
        randomLabel.setText("Random Factor: " + val / 10f);
    }

    /**
     * updateAngleLabels
     */
    private void updateAngleLabels() {
        int val = angleSlider.getValue();
        angleLabel.setText("Degrees Off Direction: " + val);
        val = minAngleSlider.getValue();
        minAngleLabel.setText("Min Degrees Off Direction: " + val);
    }

    /**
     * updateSpeedLabels
     */
    private void updateSpeedLabels() {
        int val = speedSlider.getValue();
        speedLabel.setText("Speed Mod: " + val * 10 + "%");
    }

    /**
     * updateCountLabels
     */
    private void updateCountLabels() {
        int val = particleMesh.getNumParticles();
        countLabel.setText("Particles: " + val);
    }

    /**
     * updateAgeLabels
     */
    private void updateAgeLabels() {
        int val = minAgeSlider.getValue();
        minAgeLabel.setText("Minimum Age: " + val + "ms");
        val = maxAgeSlider.getValue();
        maxAgeLabel.setText("Maximum Age: " + val + "ms");
    }

    /**
     * updateSizeLabels
     */
    private void updateSizeLabels() {
        int val = endSizeSlider.getValue();
        endSizeLabel.setText("End Size: " + val / 10f);
        val = startSizeSlider.getValue();
        startSizeLabel.setText("Start Size: " + val / 10f);
    }

    private String convColorToHex(Color c) {
        if (c == null)
            return null;
        String sRed = Integer.toHexString(c.getRed());
        if (sRed.length() == 1)
            sRed = "0" + sRed;
        String sGreen = Integer.toHexString(c.getGreen());
        if (sGreen.length() == 1)
            sGreen = "0" + sGreen;
        String sBlue = Integer.toHexString(c.getBlue());
        if (sBlue.length() == 1)
            sBlue = "0" + sBlue;
        return "#" + sRed + sGreen + sBlue;
    }

    /**
     * updateColorLabels
     */
    private void updateColorLabels() {
        startColorHex.setText(convColorToHex(startColorPanel.getBackground()));
        endColorHex.setText(convColorToHex(endColorPanel.getBackground()));
    }

    private Color makeColor(ColorRGBA rgba, boolean useAlpha) {
        return new Color(rgba.r, rgba.g, rgba.b, (useAlpha ? rgba.a : 1f));
    }

    private ColorRGBA makeColorRGBA(Color color) {
        return new ColorRGBA(color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, color.getAlpha() / 255f);
    }
    
    private void startColorPanel_mouseClicked(MouseEvent e) {
        if (!colorChooserFrame.isVisible()) {
            colorstart = true;
            colorChooserFrame.setVisible(true);
        }
    }

    private void endColorPanel_mouseClicked(MouseEvent e) {
        if (!colorChooserFrame.isVisible()) {
            colorstart = false;
            colorChooserFrame.setVisible(true);
        }
    }
        
    private void initColorChooser() {
        colorChooser.setColor(endColorPanel.getBackground());
        colorChooserFrame.setLayout(new BorderLayout());
        colorChooserFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        colorChooserFrame.add(colorChooser, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        JButton okButton = new JButton("Ok");
        okButton.setOpaque(true);
        okButton.setMnemonic('O');
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = colorChooser.getColor();
                if (color == null) {
                    return;
                }
                ColorRGBA rgba = makeColorRGBA(color);
                if (colorstart) {
                    rgba.a = (Integer.parseInt(startAlphaSpinner.getValue()
                            .toString()) / 255f);
                    particleMesh.setStartColor(rgba);
                    startColorPanel.setBackground(color);
                } else {
                    rgba.a = (Integer.parseInt(endAlphaSpinner.getValue()
                            .toString()) / 255f);
                    particleMesh.setEndColor(rgba);
                    endColorPanel.setBackground(color);
                }
                regenCode();
                updateColorLabels();
                colorChooserFrame.setVisible(false);
            }
         });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setOpaque(true);
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               colorChooserFrame.setVisible(false);
           }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        colorChooserFrame.add(buttonPanel, BorderLayout.SOUTH);
        colorChooserFrame.setSize(colorChooserFrame.getPreferredSize());
        colorChooserFrame.setLocationRelativeTo(RenParticleEditor.this);
    }
    
    private void initFileChooser() {
        fileChooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() ||
                    f.toString().toLowerCase().endsWith(".jme");
            }
            public String getDescription() {
                return "JME Files (*.jme)";
            }
        });
    }
    
    private void regenCode() {
        StringBuffer code = new StringBuffer();
        if (particleMesh == null) {
            codeTextArea.setText("");
            return;
        }
        int quantity = particleNode.getQuantity();
        if (quantity > 1) {
            code.append("Node particleNode = new Node(\"myParticles\");\n\n");
        }
        for (int ii = 0; ii < quantity; ii++) {
            ParticleMesh pmesh = (ParticleMesh)particleNode.getChild(ii);
            if (ii == 0) {
                code.append("ParticleMesh ");
            }
            code.append("particles = ParticleFactory.buildParticles(\""
                    + (quantity > 1 ? pmesh.getName() : "myParticles") + "\", "
                    + pmesh.getNumParticles()
                    + ");\n");
            code.append("particles.setEmissionDirection("
                    + codeString(pmesh.getEmissionDirection())
                    + ");\n");
            code.append("particles.setMaximumAngle("
                    + pmesh.getMaximumAngle()
                    + "f);\n");
            code.append("particles.setMinimumAngle("
                    + pmesh.getMinimumAngle()
                    + "f);\n");
            code.append("particles.setSpeed(" + pmesh.getParticleController().getSpeed()
                    + "f);\n");
            code.append("particles.setMinimumLifeTime("
                    + pmesh.getMinimumLifeTime()
                    + "f);\n");
            code.append("particles.setMaximumLifeTime("
                    + pmesh.getMaximumLifeTime()
                    + "f);\n");
            code.append("particles.setStartSize("
                    + pmesh.getStartSize() + "f);\n");
            code.append("particles.setEndSize("
                    + pmesh.getEndSize() + "f);\n");
            code.append("particles.setStartColor("
                    + codeString(pmesh.getStartColor())
                    + ");\n");
            code.append("particles.setEndColor("
                    + codeString(pmesh.getEndColor()) + ");\n");
            code.append("particles.setRandomMod("
                    + pmesh.getRandomMod() + "f);\n");
            code.append("particles.setControlFlow("
                    + pmesh.getParticleController().getControlFlow() + ");\n");
            code.append("particles.setReleaseRate("
                    + pmesh.getReleaseRate() + ");\n");
            code.append("particles.setReleaseVariance("
                    + pmesh.getReleaseVariance() + "f);\n");
            code.append("particles.setInitialVelocity("
                    + pmesh.getInitialVelocity() + "f);\n");
            code.append("particles.setParticleSpinSpeed("
                    + pmesh.getParticleSpinSpeed() + "f);\n");
            code.append("\n");
            code.append("particles.warmUp(1000);\n");
            code.append("\n");
            code.append("particles.setRenderState(YOUR TEXTURE STATE);\n");
            if (quantity > 1) {
                code.append("\nparticleNode.attachChild(particles);\n\n");
            }
        }
        String name = (quantity > 1) ? "particleNode" : "particles";
        code.append(name + ".setRenderState(YOUR ALPHA STATE);\n");
        code.append("ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();\n");
        code.append("zstate.setEnabled(false);\n");
        code.append(name + ".setRenderState(zstate);\n");

        codeTextArea.setText(code.toString());
        codeTextArea.setCaretPosition(0);
    }

    private String codeString(ColorRGBA rgba) {
        StringBuffer code = new StringBuffer("new ColorRGBA(");
        code.append(rgba.r + "f, ");
        code.append(rgba.g + "f, ");
        code.append(rgba.b + "f, ");
        code.append(rgba.a + "f");
        code.append(")");
        return code.toString();
    }

    private String codeString(Vector3f vect) {
        StringBuffer code = new StringBuffer("new Vector3f(");
        code.append(vect.x + "f, ");
        code.append(vect.y + "f, ");
        code.append(vect.z + "f");
        code.append(")");
        return code.toString();
    }

    private void countButton_actionPerformed(ActionEvent e) {
        String response = JOptionPane.showInputDialog(this,
                "Please enter a new particle count for this system:",
                "How many particles?", JOptionPane.PLAIN_MESSAGE);
        if (response == null)
            return;
        int particles = 100;
        try {
            particles = Integer.parseInt(response);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number entered.  Using 100 instead.", "Invalid",
                    JOptionPane.WARNING_MESSAGE);
            particles = 100;
        }
        resetManager(particles);
        updateCountLabels();
    }

    private void changeTexture() {
        try {
            JFileChooser chooser = new JFileChooser(lastDir);
            chooser.setMultiSelectionEnabled(false);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.CANCEL_OPTION) {
                return;
            }
            File textFile = chooser.getSelectedFile();
            lastDir = textFile.getParentFile();

            newTexture = textFile;

            ImageIcon icon = new ImageIcon(textFile.getAbsolutePath());
            imageLabel.setIcon(icon);
            validate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected Canvas getGlCanvas() {
        if (glCanvas == null) {

            // -------------GL STUFF------------------

            // make the canvas:
            glCanvas = DisplaySystem.getDisplaySystem("LWJGL").createCanvas(width, height);

            // add a listener... if window is resized, we can do something about it.
            glCanvas.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent ce) {
                    doResize();
                }
            });
            
            MyMouseListener l = new MyMouseListener();
            
            glCanvas.addMouseWheelListener(l);
            glCanvas.addMouseListener(l);
            glCanvas.addMouseMotionListener(l);

            // Important!  Here is where we add the guts to the canvas:
            impl = new MyImplementor(width, height);

            ((JMECanvas) glCanvas).setImplementor(impl);
            
            // -----------END OF GL STUFF-------------
        }
        return glCanvas;
    }

    class MyMouseListener extends MouseAdapter implements MouseMotionListener, MouseWheelListener {
        Point last = new Point(0,0);
        Matrix3f incr = new Matrix3f();

        public void mouseDragged(final MouseEvent arg0) {
            RenderThreadExecutable exe = new RenderThreadExecutable() {
                public void doAction() {
                    int difX = last.x - arg0.getX();
                    int difY = last.y - arg0.getY();
                    last.x = arg0.getX();
                    last.y = arg0.getY();
                    
                    if (arg0.isShiftDown()) {
                        difX *=5;
                        difY *=5;
                    }
                    
                    Camera camera = impl.getRenderer().getCamera();
                    
                    if (difY != 0) {
                        incr.fromAxisAngle(camera.getLeft(), -difY*.001f);
                        incr.mult(camera.getLeft(), camera.getLeft());
                        incr.mult(camera.getDirection(), camera.getDirection());
                        incr.mult(camera.getUp(), camera.getUp());
                        camera.normalize();
                        camera.update();
                    }
                    if (difX != 0) {
                        incr.fromAxisAngle(Vector3f.UNIT_Y, difX*.001f);
                        incr.mult(camera.getUp(), camera.getUp());
                        incr.mult(camera.getLeft(), camera.getLeft());
                        incr.mult(camera.getDirection(), camera.getDirection());
                        camera.normalize();
                        camera.update();
                    }
                }
            };
            RenderThreadActionQueue.addToQueue(exe);
        }
        public void mouseMoved(MouseEvent arg0) {}

        public void mousePressed(MouseEvent arg0) {
            last.x = arg0.getX();
            last.y = arg0.getY();
        }

        public void mouseWheelMoved(final MouseWheelEvent arg0) {
            RenderThreadExecutable exe = new RenderThreadExecutable() {
                public void doAction() {
                    int amnt = arg0.getWheelRotation();

                    if (arg0.isShiftDown()) {
                        amnt *= 5;
                    }

                    Camera cam = impl.getRenderer().getCamera();
                    cam.getLocation().addLocal(
                            cam.getDirection().mult(amnt * -20));
                    cam.update();
                }
            };
            RenderThreadActionQueue.addToQueue(exe);
        }
        
    }
    
    protected void doResize() {
        impl.resizeCanvas(glCanvas.getWidth(), glCanvas.getHeight());
    }

    class LayerTableModel extends AbstractTableModel {
        
        private static final long serialVersionUID = 1L;

        public int getRowCount() {
            return particleNode == null ? 0 : particleNode.getQuantity();
        }
        
        public int getColumnCount() {
            return 2;
        }
        
        public String getColumnName(int columnIndex) {
            return columnIndex == 0 ? "Name" : "Visible";
        }
        
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? String.class : Boolean.class;
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            ParticleMesh pmesh = (ParticleMesh)particleNode.getChild(rowIndex);
            return (columnIndex == 0) ? pmesh.getName() : Boolean.valueOf(
                pmesh.getCullMode() != ParticleMesh.CULL_ALWAYS);
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ParticleMesh pmesh = (ParticleMesh)particleNode.getChild(rowIndex);
            if (columnIndex == 0) {
                pmesh.setName((String)aValue);
            } else {
                pmesh.setCullMode(((Boolean)aValue).booleanValue() ?
                    ParticleMesh.CULL_DYNAMIC : ParticleMesh.CULL_ALWAYS);
            }
        }
    }
    
    // IMPLEMENTING THE SCENE:

    class MyImplementor extends SimpleCanvasImpl {

        public long startTime = 0;

        long fps = 0;

        public MyImplementor(int width, int height) {
            super(width, height);
        }

        public void simpleSetup() {
            cam.setFrustum(1f, 1000F, -0.55f, 0.55f, 0.4125f, -0.4125f);

            Vector3f loc = new Vector3f(0, 0, -850);
            Vector3f left = new Vector3f(1, 0, 0);
            Vector3f up = new Vector3f(0, 1, 0f);
            Vector3f dir = new Vector3f(0, 0, 1);
            cam.setFrame(loc, left, up, dir);
            
            root = rootNode;
            
            particleNode = new Node("particles");
            root.attachChild(particleNode);

            ZBufferState zbuf = renderer.createZBufferState();
            zbuf.setWritable( false );
            zbuf.setEnabled( true );
            zbuf.setFunction( ZBufferState.CF_LEQUAL );

            particleNode.setRenderState(zbuf);
            particleNode.updateRenderState();
            
            createNewSystem();
            
            startTime = System.currentTimeMillis() + 5000;
        };

        public void simpleUpdate() {
            
            if (newTexture != null) {
                loadApplyTexture();
            }

            if (startTime > System.currentTimeMillis()) {
                fps++;
            } else {
                long timeUsed = 5000 + (startTime - System.currentTimeMillis());
                startTime = System.currentTimeMillis() + 5000;
                System.out.println(fps + " frames in "
                        + (float) (timeUsed / 1000f) + " seconds = "
                        + (fps / (timeUsed / 1000f)) + " FPS (average)");
                fps = 0;
            }
        }
        

        private void loadApplyTexture() {
            TextureState ts = (TextureState)particleMesh.getRenderState(RenderState.RS_TEXTURE);
            ts.setTexture(
                    TextureManager.loadTexture(
                            newTexture.getAbsolutePath(),
                            Texture.MM_LINEAR,
                            Texture.FM_LINEAR));
            ts.setEnabled(true);
            particleMesh.setRenderState(ts);
            particleMesh.updateRenderState();
            newTexture = null;
        }

    }
}
