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
package com.jme.input;

/**
 * <code>MouseInput</code> defines an interface to communicate with the mouse
 * input device. 
 * @author Mark Powell
 * @version $Id: MouseInput.java,v 1.1.1.1 2003-10-29 10:56:21 Anakan Exp $
 */
public interface MouseInput {
    
    /**
     * 
     * <code>destroy</code> cleans up the native mouse interface.
     *
     */
    public void destroy();
    
    /**
     * 
     * <code>getButtonIndex</code> gets the button code for a given button
     * name.
     * @param buttonName the name to get the code for.
     * @return the code for the given button name.
     */
    public int getButtonIndex(String buttonName);
    
    /**
     * 
     * <code>isButtonDown</code> returns true if a given button is pressed,
     * false if it is not pressed.
     * @param buttonCode the button code to check.
     * @return true if the button is pressed, false otherwise.
     */
    public boolean isButtonDown(int buttonCode);
    
    /**
     * 
     * <code>getButtonName</code> gets the button name for a given button
     * code.
     * @param buttonIndex the code to get the name for.
     * @return the name for the given button code.
     */
    public String getButtonName(int buttonIndex);
    
    /**
     * 
     * <code>isCreated</code> returns true if the mouse input is created, 
     * false otherwise.
     * @return true if the mouse input is created, false otherwise.
     */
    public boolean isCreated();
    
    /**
     * 
     * <code>poll</code> updates the mouse.
     *
     */
    public void poll();
    
    /**
     * 
     * <code>getWheelDelta</code> gets the change in the mouse wheel.
     * @return the change in the mouse wheel.
     */
    public int getWheelDelta();
    
    /**
     * 
     * <code>getXDelta</code> gets the change along the x axis.
     * @return the change along the x axis.
     */
    public int getXDelta();
    
    /**
     * 
     * <code>getYDelta</code> gets the change along the y axis.
     * @return the change along the y axis.
     */
    public int getYDelta();

}