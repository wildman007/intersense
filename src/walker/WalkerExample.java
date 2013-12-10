/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package walker;

import com.sense3d.api.math.Vector3D;
import com.sense3d.api.sensor.SensorException;
import com.sense3d.api.sensor.TrackingSensor;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;

/**
 *
 * @author Honza
 */
public class WalkerExample extends PApplet{
    TrackingSensor sensor;
    Walker walker;
    Vector3D sceneCenter = new Vector3D(0,0,2000);
    Vector3D eye = new Vector3D(0,0,0);
    int[] depthMap;
    
    int sizex = 1024;
    int sizey = 768;
    int resx = 640;
    int resy = 480;
    
    protected int nthpoint = 15;
    
    @Override
    public void setup() {
        size(sizex, sizey, P3D);
        try {
            sensor = TrackingSensor.getBuilder().enableDepth().build();
            sensor.startTracking();
        } catch (SensorException ex) {
            Logger.getLogger(Walker.class.getName()).log(Level.SEVERE, null, ex);
        }
        walker = new Walker(sceneCenter, eye);
//        walker = new Walker();
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
        walker.setCamera(this);
    }
    
    private void drawPoints(int[] depthMap){
        stroke(255);
        strokeWeight(5);
        for (int x = 0; x < resx; x++) {
            for (int y = 0; y < resy; y++) {
                int i = x + y * resx;
                if (i % nthpoint == 0) {
                    int z = depthMap[i];
                    if (z > 0 && z < 800) {
                        Vector3D v = new Vector3D(x, y, z);
                        Vector3D v1 = sensor.convertProjectiveToRealWorld(v);
                        point((float) v1.getX(), (float) v1.getY(), (float) v1.getZ());
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
		case KeyEvent.VK_G:
		    this.screenshot();
		    break;
            }
    }
   
    private void screenshot(){
	
	try {
	    PrintWriter pr = new PrintWriter("/Users/janfabian/Dropbox/diplomka/screen.txt");
	    for (int x = 0; x < resx; x++) {
		for (int y = 0; y < resy; y++) {
		    int i = x + y * resx;
		    int z = depthMap[i];
		    if (z > 0) {
			pr.print(z + " ");  
		    } else {
			pr.print(-1 + " ");
		    }
		}
		pr.println();
	    }
	    pr.close();
	} catch (IOException ex) {
	    Logger.getLogger(WalkerExample.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    public static void main(String[] args){
        PApplet.main(WalkerExample.class.getCanonicalName());
    }
}
