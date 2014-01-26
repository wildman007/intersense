package com.sense3d.intersense.network.core;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteArrayLengthFramer {

    public static byte[] frameMsg(byte[] message) throws IOException{
        if (message.length > Integer.MAX_VALUE) {
            throw new IOException("message too long");
        }
        ByteBuffer bb=ByteBuffer.allocate(message.length+Integer.SIZE/8);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(message.length);
        bb.put(message, 0, message.length);
        return bb.array();
    }

    public static byte[] decodeMsg(byte[] data) throws BufferUnderflowException{
        ByteBuffer bb=ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        int length=bb.getInt();
        byte[] msg = new byte[length];
        bb.get(msg, 0, length);
        return msg;
    }
}