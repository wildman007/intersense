package testiim;

import com.sense3d.api.math.Vector3D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;

/**
 *
 * @author janfabian
 */
public class CubesGrid {
    HashMap<Integer, Vector3D> cubes;
    BoundingBox bb;

    public CubesGrid() {
	cubes = new HashMap<Integer, Vector3D>();
	bb = new BoundingBox(-300, -300, 0, 400, 200, 1000, 100);
    }
    
    public CubesGrid(BoundingBox bb) {
	cubes = new HashMap<Integer, Vector3D>();
	this.bb = bb;
    }
    
    public void processPoints(List<Vector3D> points) {
	for(Vector3D p : points) {
	    int hash = bb.hash(p);
	    if(!cubes.containsKey(hash)) {
		cubes.put(hash, p);
	    } else {
		Vector3D centerOfMass = cubes.get(hash);
		centerOfMass.add(p);
		centerOfMass.divide(2);
		cubes.put(hash, centerOfMass);
	    }
	}
    }
    
    public void draw(PApplet app) {
	int i = 0;
	for (Map.Entry<Integer, Vector3D> cube : cubes.entrySet()) {
	    Vector3D v = cube.getValue();
	    app.point((float) v.getX(), (float) v.getY(), (float) v.getZ());
	    i++;
	}
    }
}
