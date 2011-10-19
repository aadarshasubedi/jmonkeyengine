/*
 * Copyright (c) 2003, jMonkeyEngine - Mojo Monkey Coding
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution. 
 * 
 * Neither the name of the Mojo Monkey Coding, jME, jMonkey Engine, nor the 
 * names of its contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.jme.scene.state;

import com.jme.image.Texture;

/**
 * <code>TextureState</code> maintains a texture state for a given node and
 * it's children. The state maintains a single texture object at a time. It is 
 * not within the scope of this class to generate the texture, and is
 * recommended that <code>TextureManager</code> be used to create the 
 * Texture objects.
 * @see com.jme.util.TextureManager
 * @author Mark Powell
 * @version $Id: TextureState.java,v 1.2 2004-02-29 23:49:07 mojomonkey Exp $
 */
public abstract class TextureState extends RenderState {
    //the texture 
    protected Texture[] texture;
    
    /**
     * Constructor instantiates a new <code>TextureState</code> object. 
     *
     */
    public TextureState() {
        texture = new Texture[0];
    }

    /**
     * <code>getType</code> returns this type of render state.
     * (RS_TEXTURE).
     * @see com.jme.scene.state.RenderState#getType()
     */
    public int getType() {
        return RS_TEXTURE;
    }
    
    /**
     * 
     * <code>setTexture</code> sets a single texture to the first
     * texture unit.
     * @param texture the texture to set.
     */
    public void setTexture(Texture texture) {
        this.texture[0] = texture;
    }
    
    /**
     * 
     * <code>getTexture</code> gets the texture that is assigned to the
     * first texture unit.
     * @return the texture in the first texture unit.
     */
    public Texture getTexture() {
        return texture[0];
    }
    
    
    /**
     * 
     * <code>setTexture</code> sets the texture object to be used by the
     * state. The texture unit that this texture uses is set, if the
     * unit is not valid, i.e. less than zero or greater than the 
     * number of texture units supported by the graphics card, it is
     * ignored.
     * @param texture the texture to be used by the state.
     * @param textureUnit the texture unit this texture will fill.
     */
    public void setTexture(Texture texture, int textureUnit) {
        if(textureUnit >= 0 && textureUnit < this.texture.length) {
            this.texture[textureUnit] = texture;
        }
    }
    
    /**
     * 
     * <code>getTexture</code> retrieves the texture being used by the 
     * state in a particular texture unit.
     * @param textureUnit the texture unit to retrieve the texture from.
     * @return the texture being used by the state. If the texture unit
     *      is invalid, null is returned.
     */
    public Texture getTexture(int textureUnit) {
        if(textureUnit >= 0 && textureUnit < this.texture.length) {
            return texture[textureUnit];
        } else {
            return null;
        }
    }
    
    /**
     * 
     * <code>getNumberOfUnits</code> returns the number of texture units
     * the computer's graphics card supports.
     * @return the number of texture units supported by the graphics card.
     */
    public int getNumberOfUnits() {
        return texture.length;
    }
}