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
public class SubCentroid {

    private final Point point;
    private final int clusterId;
    private final int subCentroidId;

    public SubCentroid(int x, int y, int z, int clusterID, int subCentroidId) {
        this(new Point(x, y, z), clusterID, subCentroidId);
    }

    public SubCentroid(Point point, int clusterId, int subCentroidId) {
        this.point = point;
        this.clusterId = clusterId;
        this.subCentroidId = subCentroidId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public Point getPoint() {
        return point;
    }

    public int getSubCentroidId() {
        return subCentroidId;
    }

    public static int getByteSize() {
        return Point.getByteSize() + 2 * AbstractFrame.INT_BYTE_SIZE;
    }

    public void toBytes(ByteBuffer bb) {
        point.toBytes(bb);
        bb.putInt(clusterId);
        bb.putInt(subCentroidId);
    }

    public static SubCentroid fromBytes(ByteBuffer bb) {
        Point p = Point.fromBytes(bb);
        return new SubCentroid(p, bb.getInt(), bb.getInt());
    }

}
