/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sense3d.intersense.network.datatypes;

import static com.sense3d.intersense.network.dataframe.AbstractFrame.*;
import java.nio.ByteBuffer;
/**
 * Point class - max value is limited to almost short's max value.
 * @author wildman
 */
public class Point {
    private final int x,y,z;
    public static final int MAX_VALUE=32000;

    public Point(int x, int y, int z) {
        this.x = limit(x);
        this.y = limit(y);
        this.z = limit(z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public static int getByteSize(){
        return SHORT_BYTE_SIZE*3;
    }

    public void toBytes(ByteBuffer bb) {
        bb.putShort((short)x);
        bb.putShort((short)y);
        bb.putShort((short)z);
    }

    public static Point fromBytes(ByteBuffer bb) {
        int newX = bb.getShort();
        int newY = bb.getShort();
        int newZ = bb.getShort();
        return new Point(newX, newY, newZ);
    }

    public static int limit(int v) {
        return Math.min(v, MAX_VALUE);
    }
    
    
}
