/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testiim;

import com.sense3d.api.math.Vector3D;
import com.sense3d.api.sensor.SensorException;
import com.sense3d.api.sensor.TrackingSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.net.*;

/**
 *
 * @author Honza
 */
public class KmeanImp extends PApplet {

    TrackingSensor sensor;
    Vector3D sceneCenter = new Vector3D(0, 0, 2000);
    Vector3D eye = new Vector3D(0, 0, 0);
    int[] depthMap;
    int sizex = 1024;
    int sizey = 768;
    int resx = 640;
    int resy = 480;
    protected int nthpoint = 50;
    int MAX_Z = 3000;
    int MIN_Y = -1000;
    Server s;
//    private Dataset data;
    private ArrayList<Vector3D> data;
    private List<Vector3D> pointsOut=new ArrayList<Vector3D>();

    
    private Downsampler ds = new Downsampler();
    private Kmeansampler ks = new Kmeansampler();

    @Override
    public void setup() {
        try {
            size(sizex, sizey, P3D);

            sensor = TrackingSensor.getBuilder().enableDepth().build();
            sensor.startTracking();


        } catch (SensorException ex) {
            ex.printStackTrace();
        }

        s = new Server(this, 12345); // Start a simple server on a port
//        s.run();
//       data = new DefaultDataset();
        data = new ArrayList<Vector3D>();

    }

    @Override
    public void draw() {
        background(0);
        try {
            sensor.updateWait();
            depthMap = sensor.getDepthMap();
        } catch (SensorException ex) {
            Logger.getLogger(WalkerExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawPoints(depthMap);
        camera((float) eye.getX(), (float) eye.getY(), (float) eye.getZ(),
                (float) sceneCenter.getX(), (float) sceneCenter.getY(), (float) sceneCenter.getZ(),
                0, -1, 0);
    
        //poslani bodu  
        writeData(pointsOut);
        pointsOut.clear();
    }

    void writeData(List<Vector3D> points) {
        for (Vector3D p : points) {
            if(p.getX() != 0 && p.getY() != 0 && p.getZ() != 0) {
                s.write((int) p.getX() + ";" + (int) p.getY() + ";" + (int) p.getZ() + "\n");
            }
        }
        if(points.size() > 0) {
            s.write("#");
        }       
    }

    private void drawPoints(int[] depthMap) {
        Vector3D vv = new Vector3D();
        data.clear();
        int c = 0;
        stroke(255);
        strokeWeight(5);
        for (int x = 0; x < resx; x++) {
            for (int y = 0; y < resy; y++) {
                int i = x + y * resx;
                if (i % nthpoint == 0) {
                    int z = depthMap[i];
                    if (z > 50) {
                        Vector3D v = new Vector3D(x, y, z);
                        Vector3D v1 = sensor.convertProjectiveToRealWorld(v);
                        if (v1.getZ() < MAX_Z && v1.getY() > MIN_Y) {
                            point((float) v1.getX(), (float) v1.getY(), (float) v1.getZ());
                            vv.add(v1);
//                            data.add(new DenseInstance(new double[]{v1.getX(), v1.getY(), v1.getZ()}));
                            data.add(v1);
                            c++;
                        }
                    }
                }
            }
        }
        vv.divide(c);
        stroke(255, 0, 0);
//        point((float) vv.getX(),(float) vv.getY(),(float) vv.getZ());
        
        pointsOut = ks.kmeansSample(data, 20);
//        pointsOut = ds.downsampleRand(data, 300);
//        pointsOut = ds.downsampleGrid(pointsOut);
        
        for(Vector3D v : pointsOut) {
            point((float)v.getX(), (float)v.getY(), (float)v.getZ());
        }  

    }
   

    public static void main(String[] args) {
        PApplet.main(KmeanImp.class.getCanonicalName());
    }
}
