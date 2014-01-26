/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.datatypes;

import com.sense3d.intersense.network.dataframe.AbstractFrame;
import java.nio.ByteBuffer;

/**
 *
 * @author wildman
 */
public class Centroid {

    private final int clusterID;
    private final Point point;

    public Centroid(int x, int y, int z, int clusterID) {
        this(new Point(x, y, z),clusterID);
    }

    public Centroid(Point p, int clusterId) {
        this.point=p;
        this.clusterID = clusterId;
    }

    public Point getPoint() {
        return point;
    }

    public int getClusterID() {
        return clusterID;
    }

    public static int getByteSize() {
        return Point.getByteSize()+AbstractFrame.INT_BYTE_SIZE;
    }
    
    public void toBytes(ByteBuffer bb) {
        point.toBytes(bb);
        bb.putInt(clusterID);
    }

    public static Centroid fromBytes(ByteBuffer bb) {
        Point p=Point.fromBytes(bb);
        return new Centroid(p, bb.getInt());
    }

}
