package iimclient;

import com.sense3d.api.math.Vector3D;

/**
 *
 * @author janfabian
 */
public class Test {
    public static void main(String[] args) {
	BoundingBox bb = new BoundingBox(-300, -300, 400, 400, 200, 800, 100);
	System.out.println(bb.hash(new Vector3D(395, 194, 792)));
	System.out.println(bb.hash(new Vector3D(389, 180, 795)));
    }
}
