/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testiim;

import com.google.code.ekmeans.EKmeans;
import com.sense3d.api.math.Vector3D;
import com.sense3d.api.sensor.SensorException;
import com.sense3d.api.sensor.TrackingSensor;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import net.sf.javaml.clustering.Clusterer;
//import net.sf.javaml.clustering.KMeans;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.core.DefaultDataset;
//import net.sf.javaml.core.DenseInstance;
//import net.sf.javaml.core.Instance;
//import net.sf.javaml.tools.data.FileHandler;
import org.OpenNI.Point3D;
import processing.core.PApplet;
import processing.net.*;

/**
 *
 * @author Honza
 */
public class WalkerExample extends PApplet {

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
    private ArrayList<Vector3D> pointsOut=new ArrayList<>();
    
    
    private double[][] points;
    private double[][] centroids = null;
    private int k = 10;

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
            s.write((int) p.getX() + ";" + (int) p.getY() + ";" + (int) p.getZ() + "\n");
        }
        s.write("#");
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
                            data.add(new DenseInstance(new double[]{v1.getX(), v1.getY(), v1.getZ()}));
                            c++;
                        }
                    }
                }
            }
        }
        vv.divide(c);
        stroke(255, 0, 0);
//        point((float) vv.getX(),(float) vv.getY(),(float) vv.getZ());
//        points = new double[data.size()][3];
//        for (int i = 0; i < data.size(); i++) {
//            Instance instance = data.get(i);
//            points[i][0] = instance.get(0); 
//            points[i][1] = instance.get(1);
//            points[i][2] = instance.get(2);
//        }
        
//        if(centroids == null) {
//            centroids = new double[k][3];
//            for (int i = 0; i < k; i++) {
//                int rand = (int) Math.floor(points.length * Math.random());
//                centroids[i] = points[rand];
//            }
//        }
//        
//        EKmeans eKmeans = new EKmeans(centroids, points);
//        eKmeans.setIteration(64);
//        eKmeans.run();
//        int[] assignments = eKmeans.getAssignments();
//        
//        for (int i = 0; i < k; i++) {
//            pointsOut.add(new Vector3D(centroids[i][0], centroids[i][1], centroids[i][2]));
//            System.out.println(new Vector3D(centroids[i][0], centroids[i][1], centroids[i][2]));
//        }
        
        Clusterer kmeans = new KMeans(20, 10);
        Dataset[] clusters = kmeans.cluster(data);
        System.out.println(data.size());
        for (int i = 0; i < clusters.length; i++) {
            Dataset dataset = clusters[i];
            Vector3D vc = clusterCenter(dataset);
            pointsOut.add(vc);
            point((float) vc.getX(), (float) vc.getY(), (float) vc.getZ());
        }

    }

    private Vector3D clusterCenter(Dataset data) {
        Iterator<Instance> it = data.iterator();
        Vector3D center = new Vector3D();
        while (it.hasNext()) {
            Instance object = it.next();
            center.add(new Vector3D(object.get(0), object.get(1), object.get(2)));
        }
        center.divide(data.size());
        return center;
    }

    //    @Override
//    public void keyPressed(KeyEvent e) {
//        //ovladej walkera
//            switch(e.getKeyCode()){
//                case KeyEvent.VK_LEFT:
//                    walker.left();
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    walker.right();
//                    break;
//                case KeyEvent.VK_UP:
//                    walker.up();
//                    break;
//                case KeyEvent.VK_DOWN:
//                    walker.down();
//                    break;
//                case KeyEvent.VK_K:
//                    walker.forward();
//                    break;
//                case KeyEvent.VK_M:
//                    walker.backward();
//                    break;
//                case KeyEvent.VK_P:
//                    walker.rotateRight();
//                    break;
//		case KeyEvent.VK_O:
//                    walker.rotateLeft();
//                    break;
//		case KeyEvent.VK_E:
//		    walker.rotateUp();
//		    break;
//		case KeyEvent.VK_D:
//		    walker.rotateDown();
//		    break;
//		case KeyEvent.VK_G:
//		    this.screenshot();
//		    break;
//            }
//    }
    public static void main(String[] args) {
        PApplet.main(WalkerExample.class.getCanonicalName());
    }
}
