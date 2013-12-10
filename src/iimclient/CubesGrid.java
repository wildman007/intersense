package iimclient;

import com.sense3d.api.math.Vector3D;
import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;
import delaunay_triangulation.Triangle_dt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	Point_dt[] pointsForTriang = new Point_dt[cubes.size()];
	int i = 0;
	for (Map.Entry<Integer, Vector3D> cube : cubes.entrySet()) {
	    Vector3D v = cube.getValue();
	    pointsForTriang[i] = new Point_dt(v.getX(), v.getY(), v.getZ());
	    app.point((float) v.getX(), (float) v.getY(), (float) v.getZ());
	    i++;
	}
	
	app.strokeWeight(2);
	delaunay_triangulation.Delaunay_Triangulation dt = new Delaunay_Triangulation(pointsForTriang);
	Iterator<Triangle_dt> it = dt.trianglesIterator();
	while(it.hasNext()) {
	    Triangle_dt tr = it.next();
	    app.line((float) tr.p1().x(), (float) tr.p1().y(), (float) tr.p1().z(), (float) tr.p2().x(), (float) tr.p2().y(), (float) tr.p2().z());
	    if(tr.p3() != null) {
		app.line((float) tr.p2().x(), (float) tr.p2().y(), (float) tr.p2().z(), (float) tr.p3().x(), (float) tr.p3().y(), (float) tr.p3().z());
		app.line((float) tr.p3().x(), (float) tr.p3().y(), (float) tr.p3().z(), (float) tr.p1().x(), (float) tr.p1().y(), (float) tr.p1().z());
	    }
	}
    }
}
