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
package com.jme.input.action;

import com.jme.math.Matrix3f;
import com.jme.scene.Spatial;

/**
 * <code>KeyNodeBackwardAction</code> 
 * @author Mark Powell
 * @version $Id: KeyNodeLookUpAction.java,v 1.1 2003-12-11 23:21:07 mojomonkey Exp $
 */
public class KeyNodeLookUpAction implements InputAction {
    private Matrix3f incr;
    private Spatial node;
    private float speed;
    private String key;
    
    /**
     
     * @param camera the camera that will be affected by this action.
     * @param speed the speed at which the camera can move.
     */
    public KeyNodeLookUpAction(Spatial node, float speed) {
        incr = new Matrix3f();
        this.node = node;
        this.speed = speed;
    }

    /**
     * 
     * <code>setSpeed</code> sets the speed in units/second that the 
     * camera node can move.
     * @param movementSpeed the units/second of the camera.
     */
    public void setSpeed(float movementSpeed) {
        this.speed = movementSpeed;
    }
    
    /**
     * <code>performAction</code> moves the camera node along it's negative
     * direction vector at a speed of movement speed * time. Where time is
     * the time between frames and 1 corresponds to 1 second.
     * @see com.jme.input.action.InputAction#performAction(float)
     */
    public void performAction(float time) {
        incr.loadIdentity();
        incr.fromAxisAngle(node.getLocalRotation().getColumn(0), -speed * time);
        node.setLocalRotation(incr.mult(node.getLocalRotation()));
        node.updateWorldData(time);

    }

    /**
     * <code>getKey</code> retrieves the key associated with this action.
     * @see com.jme.input.action.InputAction#getKey()
     */
    public String getKey() {
        return key;
    }

    /**
     * <code>setKey</code> sets the key associated with this action.
     * @see com.jme.input.action.InputAction#setKey(java.lang.String)
     */
    public void setKey(String key) {
        this.key = key;

    }

}
