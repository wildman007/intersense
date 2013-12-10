package iimclient;

import com.sense3d.api.math.Vector3D;

/**
 *
 * @author janfabian
 */
public class BoundingBox {
    private double minX, minY, minZ, maxX, maxY, maxZ;
    private int step;
    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int step) {
	this.minX = minX;
	this.minY = minY;
	this.minZ = minZ;
	this.maxX = maxX;
	this.maxY = maxY;
	this.maxZ = maxZ;
	this.step = step;
    }

    public double getMinX() {
	return minX;
    }

    public void setMinX(double minX) {
	this.minX = minX;
    }

    public double getMinY() {
	return minY;
    }

    public void setMinY(double minY) {
	this.minY = minY;
    }

    public double getMinZ() {
	return minZ;
    }

    public void setMinZ(double minZ) {
	this.minZ = minZ;
    }

    public double getMaxX() {
	return maxX;
    }

    public void setMaxX(double maxX) {
	this.maxX = maxX;
    }

    public double getMaxY() {
	return maxY;
    }

    public void setMaxY(double maxY) {
	this.maxY = maxY;
    }

    public double getMaxZ() {
	return maxZ;
    }

    public void setMaxZ(double maxZ) {
	this.maxZ = maxZ;
    }
    
    public int hash(Vector3D v) {
	int x = (int) Math.round((v.getX() - minX)/step);
	int y = (int) Math.round((v.getY() - minY)/step);
	int z = (int) Math.round((v.getZ() - minZ)/step);
	return hash(x, y, z);
    }
    
    public int hash(int x, int y, int z) {
	x -= minX;
	y -= minY;
	z -= minZ;
	int result = (int) (((x*(maxX - minX) + y) * (maxY - minY)) + z);
	return result;
    }
    
}
