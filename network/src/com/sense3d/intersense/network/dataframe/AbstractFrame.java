/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author wildman
 */
public abstract class AbstractFrame {

    public static final int INT_BYTE_SIZE = Integer.SIZE / 8;
    public static final int DOUBLE_BYTE_SIZE = Double.SIZE / 8;
    public static final int LONG_BYTE_SIZE = Long.SIZE / 8;
    public static final int BYTE_SIZE = 1;
    public static final int BYTE_MASK = 0xFF;
    public static int SHORT_BYTE_SIZE=Short.SIZE/8;

    public static AbstractFrame fromBytes(byte[] data) {
        throw new UnsupportedOperationException("Not supported - this is a generic Message type, you need to use subclass implementation.");
    }
    private final int frameId;

    public AbstractFrame(int frameId) {
        this.frameId=frameId;
    }

    public abstract byte[] toBytes();

    protected static ByteBuffer createByteBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocate(size);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb;
    }

    protected static ByteBuffer createByteBuffer(byte[] array) {
        ByteBuffer bb = ByteBuffer.wrap(array);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb;
    }

    public int getFrameId() {
        return frameId;
    }
    
    
    
    
}
