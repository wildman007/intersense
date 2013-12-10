/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package walker;

import com.sense3d.api.math.Geometry;
import com.sense3d.api.math.Vector3D;
import processing.core.PApplet;

/**
 *
 * @author Honza
 */
public class Walker {
    Vector3D sceneCenter;
    Vector3D eyeCoor;
    double rotx, roty, rotz;

//    public final double EYE_SCENE_DISTANCE = 415.69;
    public final double EYE_SCENE_DISTANCE = 1415.69;
    public final double STEP_SIZE = 20;
    public final double ROT_STEP_SIZE = 0.02;
    
    public Walker(Vector3D sceneCenter, Vector3D eyeCoor) {
        this.eyeCoor = eyeCoor;
        Vector3D addV =Vector3D.sub(sceneCenter, eyeCoor).norm();
        addV.multiply(EYE_SCENE_DISTANCE);
        this.sceneCenter = Vector3D.add(eyeCoor, addV);
    }

    public Walker() {
        this.eyeCoor = new Vector3D(640/2.0, 480/2.0, EYE_SCENE_DISTANCE);
        this.sceneCenter = new Vector3D(640/2.0, 480/2.0, 0);
    }

    public void rotateX(double rx){
	Vector3D dir = Vector3D.sub(sceneCenter, eyeCoor), 
		right= dir.cross(new Vector3D(0, 1, 0)),
		axis = right.cross(dir);
	sceneCenter.setVector(Geometry.rotatePointAroundAxis(sceneCenter, eyeCoor, axis, rx));
    }
    
    public void rotateZ(double rz){
	Vector3D dir = Vector3D.sub(sceneCenter, eyeCoor), 
		axis = dir.cross(new Vector3D(0, 1, 0));
	sceneCenter.setVector(Geometry.rotatePointAroundAxis(sceneCenter, eyeCoor, axis, rz));
    }
    
    public void rotateLeft(){
        rotx -= ROT_STEP_SIZE;
        rotateX(-ROT_STEP_SIZE);
    }
    
    public void rotateRight(){
        rotx += ROT_STEP_SIZE;
        rotateX(ROT_STEP_SIZE);
    }
    
    public void rotateUp(){
        rotz += ROT_STEP_SIZE;
        rotateZ(ROT_STEP_SIZE);
    }
    
    public void rotateDown(){
        rotz -= ROT_STEP_SIZE;
        rotateZ(-ROT_STEP_SIZE);
    }
    
    

    public void forward(){
        Vector3D direction = Vector3D.sub(sceneCenter, eyeCoor).norm();
        direction.multiply(STEP_SIZE);
        sceneCenter.add(direction);
        eyeCoor.add(direction);
    }
    
    public void backward(){
        Vector3D direction = Vector3D.sub(sceneCenter, eyeCoor).norm();
        direction.multiply(STEP_SIZE);
        sceneCenter.subtract(direction);
        eyeCoor.subtract(direction);
    }
    
    public void right(){
        Vector3D direction = Vector3D.sub(sceneCenter, eyeCoor).cross(new Vector3D(0, 1, 0)).norm();
        direction.multiply(STEP_SIZE);
        sceneCenter.subtract(direction);
        eyeCoor.subtract(direction);
    }
    
    public void left(){
        Vector3D direction = Vector3D.sub(sceneCenter, eyeCoor).cross(new Vector3D(0, 1, 0)).norm();
        direction.multiply(STEP_SIZE);
        sceneCenter.add(direction);
        eyeCoor.add(direction);
    }
    
    public void up(){
        Vector3D direction = Vector3D.sub(sceneCenter, eyeCoor).norm();
        Vector3D right = direction.cross(new Vector3D(0,1,0));
        Vector3D up = right.cross(direction);
        up = up.norm();
        up.multiply(STEP_SIZE);
        sceneCenter.add(up);
        eyeCoor.add(up);
    }
    
    public void down(){
        Vector3D direction = Vector3D.sub(sceneCenter, eyeCoor).norm();
        Vector3D right = direction.cross(new Vector3D(0,1,0));
        Vector3D up = right.cross(direction);
        up = up.norm();
        up.multiply(STEP_SIZE);
        sceneCenter.subtract(up);
        eyeCoor.subtract(up);
    }
    
    public Vector3D getSceneCenter() {
        return sceneCenter;
    }

    public Vector3D getEyeCoor() {
        return eyeCoor;
    }

    public void setSceneCenter(Vector3D sceneCenter) {
        this.sceneCenter = sceneCenter;
    }

    public void setEyeCoor(Vector3D eyeCoor) {
        this.eyeCoor = eyeCoor;
    }
    
    public void setCamera(PApplet applet){
        applet.camera((float)eyeCoor.getX(),(float)eyeCoor.getY(),(float)eyeCoor.getZ(),
               (float)sceneCenter.getX(),(float)sceneCenter.getY(),(float)sceneCenter.getZ(),
               0,-1,0);
    }
}
