package com.sense3d.intersense.network.core;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.UnsupportedDataTypeException;

public class LengthFramer{

    public static final int BYTEMASK = 0xff;
//  public static final int SHORTMASK = 0xffffffff;
//  public static final int BYTESHIFT = 8;
    private DataInputStream in; // wrapper for data I/O
    public LengthFramer(InputStream in) throws IOException {
        this.in = new DataInputStream(in);
    }

    public static void frameMsg(OutputStream out, byte[] message) throws IOException {
        if (message.length > Integer.MAX_VALUE) {
            throw new IOException("message too long");
        }
        // write length prefix
        if (Integer.SIZE == 32) {
            out.write((message.length >> 24) & BYTEMASK);
            out.write((message.length >> 16) & BYTEMASK);
            out.write((message.length >> 8) & BYTEMASK);
            out.write(message.length & BYTEMASK);
            // write message
            out.write(message);
            out.flush();
        } else {
            throw new UnsupportedDataTypeException("Integer has " + Integer.SIZE + " bits instead of 32");
        }
    }

    public byte[] nextMsg() throws IOException {
        int length;
        try {
//      length = in.readUnsignedShort(); // read 2 bytes
            length = in.readInt(); //read 4 bytes
//        System.out.println("MSG length: "+length);
        } catch (EOFException e) { // no (or 1 byte) message
            return null;
        }
        // 0 <= length <= 2^31-1
        byte[] msg = new byte[length];
        in.readFully(msg); // if exception, it's a framing error.
        return msg;
    }
}