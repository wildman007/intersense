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
//import net.sf.javaml.clustering.Clusterer;
//import net.sf.javaml.clustering.KMeans;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.core.DefaultDataset;
//import net.sf.javaml.core.DenseInstance;
//import net.sf.javaml.core.Instance;
//import net.sf.javaml.tools.data.FileHandler;
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
    private ArrayList<Vector3D> pointsOut=new ArrayList<Vector3D>();
    List<Vector3D> points = new ArrayList<Vector3D>();
    
    int minX = -300, minY = -300, minZ = 0, maxX = 400, maxY = 200, maxZ = 1000, step = 50;
    
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
       camera((float) eye.getX(), (float) eye.getY(), (float) eye.getZ(),
              (float) sceneCenter.getX(), (float) sceneCenter.getY(), (float) sceneCenter.getZ(),
              0, -1, 0);

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

	stroke(255);
        strokeWeight(2);
        drawPoints(depthMap);
	
	stroke(0, 250, 0);
	strokeWeight(5);
	CubesGrid cg = new CubesGrid(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ, step));
	cg.processPoints(points);
	cg.draw(this);
	
	//poslani bodu
	writeData(points);
	points.clear();
    }

    void writeData(List<Vector3D> points) {
        for (Vector3D p : points) {
            s.write((int) p.getX() + ";" + (int) p.getY() + ";" + (int) p.getZ() + "\n");
        }
        s.write("#");
    }

    private void drawPoints(int[] depthMap) {
        Vector3D vv = new Vector3D();
        int c = 0;
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
			    points.add(v1);
                            c++;
                        }
                    }
                }
            }
        }
        vv.divide(c);
        stroke(255, 0, 0);
        

    }


    public static void main(String[] args) {
        PApplet.main(WalkerExample.class.getCanonicalName());
    }
}
