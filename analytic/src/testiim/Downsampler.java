/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testiim;

import com.sense3d.api.math.Vector3D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Honza
 */
public class Downsampler {

    int minx = -1000;
    int maxx = 1000;
    int miny = -1000;
    int maxy = 1000;
    int minz = 50;
    int maxz = 4000;
    int N = 10;
    int numSamples = 1000;
//    boolean[][][] full;
//    Vector3D[][][] points;
    HashMap<Vector3D,Boolean> map;
    public Downsampler() {
    }

    public List<Vector3D> downsampleRand(List<Vector3D> datain, int numSamples){
        ArrayList<Vector3D> list = new ArrayList<Vector3D>();
        Random rand = new Random();
        for (int i = 0; i<numSamples; i++){
            int r = rand.nextInt(datain.size());
            Vector3D v = datain.get(r);
            list.add(v);
        }
        return list;
    }
    
    public List<Vector3D> downsampleGrid(List<Vector3D> datain){
        int stepx = (maxx-minx)/N;
        int stepy = (maxy-miny)/N;
        int stepz = (maxz-minz)/N;
//        map = new HashMap<>();
//        int i = 0; int j = 0; int k = 0;
//        for (int x = minx; x < maxx; x = x+stepx){
//            i++;
//            for (int y = miny; y<maxy; y = y + stepy){
//                j++;
//                for (int z = minz; z<maxz; z = z + stepz){
//                    k++;
//                    //points[i][j][k] = new Vector3D(x,y,z);
//                }
//            }
//        }
        ArrayList<Vector3D> list = new ArrayList<Vector3D>();
//        Random rand = new Random();
//        for (int i = 0; i<numSamples; i++){
//            int r = rand.nextInt(datain.size());
//            Vector3D v = datain.get(r);
        for (Vector3D v : datain){
            int xx = (int)v.getX();
            int rest = xx % stepx;
            xx = xx-rest;
            int yy = (int)v.getY();
            rest = yy % stepy;
            yy = yy-rest;
            int zz = (int)v.getZ();
            rest = zz % stepz;
            zz = zz-rest;
            Vector3D out = new Vector3D(xx,yy,zz);
            if (!list.contains(out)){
                list.add(out);
            }
        }
        return list;
    }
}
