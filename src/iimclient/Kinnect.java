package iimclient;

import com.sense3d.api.math.Vector3D;
import com.sense3d.api.sensor.SensorException;
import com.sense3d.api.sensor.TrackingSensor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import walker.Walker;
import walker.WalkerExample;

/**
 *
 * @author janfabian
 */
public class Kinnect extends PApplet{
    
TrackingSensor sensor;
    Vector3D sceneCenter = new Vector3D(0,0,2000);
    Vector3D eyeCoor = new Vector3D(0,0,0);
    Walker walker;
    int[] depthMap;
    
    int sizex = 1024;
    int sizey = 768;
    int resx = 640;
    int resy = 480;
    
    int minX = -300, minY = -300, minZ = 0, maxX = 400, maxY = 200, maxZ = 1000, step = 50;
    

    protected int nthpoint = 15;
    List<Vector3D> points = new ArrayList<Vector3D>();
    
    @Override
    public void setup() {
        size(sizex, sizey, P3D);
        try {
            sensor = TrackingSensor.getBuilder().enableDepth().build();
            sensor.startTracking();
        } catch (SensorException ex) {
            Logger.getLogger(Walker.class.getName()).log(Level.SEVERE, null, ex);
        }
	walker = new Walker(sceneCenter, eyeCoor);
//	camera((float)eyeCoor.getX(),(float)eyeCoor.getY(),(float)eyeCoor.getZ(),
//               (float)sceneCenter.getX(),(float)sceneCenter.getY(),(float)sceneCenter.getZ(),
//               0,-1,0);
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
        strokeWeight(5);
        drawPoints(depthMap);
	
	stroke(0, 250, 0);
	CubesGrid cg = new CubesGrid(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ, step));
	cg.processPoints(points);
	cg.draw(this);
	
	points.clear();
	walker.setCamera(this);
    }
    
    private void drawPoints(int[] depthMap){
        for (int x = 0; x < resx; x++) {
            for (int y = 0; y < resy; y++) {
                int i = x + y * resx;
                if (i % nthpoint == 0) {
                    int z = depthMap[i];
                    if (z > minZ && z < maxZ) {
                        Vector3D v = new Vector3D(x, y, z);
                        Vector3D v1 = sensor.convertProjectiveToRealWorld(v);
			points.add(v1);
//                        point((float) v1.getX(), (float) v1.getY(), (float) v1.getZ());
                    }
                }
            }
        }
    }
    
        @Override
    public void keyPressed(KeyEvent e) {
        //ovladej walkera
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    walker.left();
                    break;
                case KeyEvent.VK_RIGHT:
                    walker.right();
                    break;
                case KeyEvent.VK_UP:
                    walker.up();
                    break;
                case KeyEvent.VK_DOWN:
                    walker.down();
                    break;
                case KeyEvent.VK_K:
                    walker.forward();
                    break;
                case KeyEvent.VK_M:
                    walker.backward();
                    break;
                case KeyEvent.VK_P:
                    walker.rotateRight();
                    break;
		case KeyEvent.VK_O:
                    walker.rotateLeft();
                    break;
		case KeyEvent.VK_E:
		    walker.rotateUp();
		    break;
		case KeyEvent.VK_D:
		    walker.rotateDown();
		    break;
            }
    }
    
    private void calcBoundingBox(List<Vector3D> points) {
	double min_x, min_y, min_z, max_x, max_y, max_z; 
	min_x = min_y = min_z = Double.MAX_VALUE;
	max_x = max_y = max_z = Double.MIN_VALUE;
	for(Vector3D p : points) {
	    min_x = p.getX() < min_x ? p.getX() : min_x;
	    min_y = p.getY() < min_y ? p.getY() : min_y;
	    min_z = p.getZ() < min_z ? p.getZ() : min_z;
	    max_x = p.getX() > max_x ? p.getX() : max_x;
	    max_y = p.getY() > max_y ? p.getY() : max_y;
	    max_z = p.getZ() > max_z ? p.getZ() : max_z;
	}
	System.out.println(min_x + " " + min_y + " " + min_z);
	System.out.println(max_x + " " + max_y + " " + max_z);
    }
    
    public static void main(String[] args){
        PApplet.main(Kinnect.class.getCanonicalName());
    }
}
