/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.core;

/**
 *
 * @author wildman
 */
public interface Receiver {
    public void messageReceived(byte[] data);
    public void messageReceived(Object data);
    public void requestClose();
}
