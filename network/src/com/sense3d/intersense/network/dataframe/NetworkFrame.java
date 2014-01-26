/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

import com.sense3d.intersense.network.datatypes.Centroid;
import com.sense3d.intersense.network.datatypes.SubCentroid;
import com.sense3d.intersense.network.datatypes.Point;
import static com.sense3d.intersense.network.dataframe.AbstractFrame.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wildman
 */
public class NetworkFrame extends AbstractFrame {

    private final List<Point> points;
    private final List<Centroid> centroids;
    private final List<SubCentroid> subCentroids;

    /**
     * Creates a new network frame to be transfered/saved.<br>
     * In order to omit some of the lists, just pass null or an empty list.
     *
     * @param points
     * @param centroids
     * @param subCentroids
     * @param frameId
     */
    public NetworkFrame(int frameId, List<Point> points, List<Centroid> centroids, List<SubCentroid> subCentroids) {
        super(frameId);
        if (points == null) {
            points = new ArrayList<>();
        }
        if (centroids == null) {
            centroids = new ArrayList<>();
        }
        if (subCentroids == null) {
            subCentroids = new ArrayList<>();
        }
        this.points = points;
        this.centroids = centroids;
        this.subCentroids = subCentroids;
    }

    public static NetworkFrame fromBytes(byte[] data) {
        ByteBuffer bb = createByteBuffer(data);
        int id = bb.getInt();
        int pSize = bb.getInt();
        List<Point> newPoints = new ArrayList<>(pSize);
        for (int i = 0; i < pSize; i++) {
            newPoints.add(i, Point.fromBytes(bb));
        }
        int cSize = bb.getInt();
        List<Centroid> newCentroids = new ArrayList<>(cSize);
        for (int i = 0; i < cSize; i++) {
            newCentroids.add(i, Centroid.fromBytes(bb));
        }
        int scSize = bb.getInt();
        List<SubCentroid> newSubCentroids = new ArrayList<>(scSize);
        for (int i = 0; i < scSize; i++) {
            newSubCentroids.add(i, SubCentroid.fromBytes(bb));
        }
        return new NetworkFrame(id, newPoints, newCentroids, newSubCentroids);
    }

    /**
     * Frame: frameId, noPoints, points, noCentroids, centroids, noSubcentroids,
     * subCentroids
     *
     * @return
     */
    @Override
    public byte[] toBytes() {
        int bufferSize = INT_BYTE_SIZE + //frameId
                INT_BYTE_SIZE + //noPoints
                Point.getByteSize() * points.size() + //points
                INT_BYTE_SIZE + //noCentroids
                Centroid.getByteSize() * centroids.size() + //centroids
                INT_BYTE_SIZE + //noSubCentroids
                SubCentroid.getByteSize() * subCentroids.size(); //subCentroids

        ByteBuffer bb = createByteBuffer(bufferSize);
        bb.putInt(getFrameId());
        bb.putInt(points.size());
        for (Point point : points) {
            point.toBytes(bb);
        }
        bb.putInt(centroids.size());
        for (Centroid c : centroids) {
            c.toBytes(bb);
        }
        bb.putInt(subCentroids.size());
        for (SubCentroid sc : subCentroids) {
            sc.toBytes(bb);
        }
        return bb.array();
    }

    public List<Centroid> getCentroids() {
        return centroids;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<SubCentroid> getSubCentroids() {
        return subCentroids;
    }

}
