/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testiim;

import com.google.code.ekmeans.EKmeans;
import com.sense3d.api.math.Vector3D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author kinect
 */
public class Kmeansampler {
    private double[][] points;
    private boolean init = true;
    private int iter;
    private double[][] centroids = null, prevCentroids;
    
    
    public List<Vector3D> kmeansSample(List<Vector3D> data, int k){
        List<Vector3D> result = new ArrayList<Vector3D>();
        points = new double[data.size()][3];
        for (int i = 0; i < data.size(); i++) {
            Vector3D instance = data.get(i);
            points[i][0] = instance.getX(); 
            points[i][1] = instance.getY();
            points[i][2] = instance.getZ();
        }
        
        if(init) {
            init = false;
            iter = 124;
            centroids = new double[k][3];
            for (int i = 0; i < k; i++) {
                int rand = (int) Math.floor(points.length * Math.random());
                centroids[i] = points[rand];
            }
        } else {
            iter = 1;
            
        }
        prevCentroids = centroids.clone();
        
        EKmeans eKmeans = new EKmeans(centroids, points);
        eKmeans.setEqual(true);
        eKmeans.setIteration(iter);
        eKmeans.run();
        double[][] dists = calcDistances(prevCentroids, centroids, k);
            int[] pairs = findClosestPairs(dists);
            for (int i = 0; i < pairs.length; i++) {
                centroids[pairs[i]][0] += prevCentroids[i][0];
                centroids[pairs[i]][0] /= 2;
                centroids[pairs[i]][1] += prevCentroids[i][1];
                centroids[pairs[i]][1] /= 2;
                centroids[pairs[i]][2] += prevCentroids[i][2];
                centroids[pairs[i]][2] /= 2;
            }
        for (int i = 0; i < k; i++) {
            result.add(new Vector3D(centroids[i][0], centroids[i][1], centroids[i][2]));
        }
        return result;
    }
    
    private int[] findClosestPairs(double[][] dists) {
        int k = dists.length;
        HashSet<Integer> hs = new HashSet<Integer>();
        HashSet<Integer> hs2 = new HashSet<Integer>();
        int[] pairs = new int[k];
        for (int h = 0; h < k; h++) {
            int mini = 0, minj = 0;
            double minval = Double.MAX_VALUE;
            for (int i = 0; i < k; i++) {
                if(hs.contains(i)) {
                    continue;
                }
                for (int j = 0; j < k; j++) {
                    if(hs2.contains(j)) {
                        continue;
                    }
                    if(dists[i][j] < minval) {
                        mini = i; minj = j;
                        minval = dists[i][j];
                    }
                }
            }
            pairs[mini] = minj;
            hs2.add(minj);
            hs.add(mini);
            
        }
        return pairs;
    }

    public double[][] calcDistances(double[][] prev, double[][] next, int k) {
        double[][] dist = new double[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                Vector3D v1 = new Vector3D(prev[i][0],prev[i][1],prev[i][2]);
                Vector3D v2 = new Vector3D(next[j][0],next[j][1],next[j][2]);
                dist[i][j] = Vector3D.sub(v1, v2).size();
            }
        }
        return dist;
    }
}
