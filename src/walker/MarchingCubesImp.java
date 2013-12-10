package walker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import rui.marchingCubes.MarchingCubes;
import processing.core.PApplet;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import toxi.geom.Vec3D;
/**
 *
 * @author janfabian
 */
public class MarchingCubesImp extends MarchingCubes{
    static int width = 640, height = 480;
    
    public static void main(String[] args) {
	try {
	    readImage("/Users/janfabian/Dropbox/diplomka/screen.txt");
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(MarchingCubesImp.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(MarchingCubesImp.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    private static int[] readImage(String path) throws FileNotFoundException, IOException {
	int[] res = new int[width*height];
	
	FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
	
        String line;
	int i = 0;
        while ((line = bufferedReader.readLine()) != null) {
	    StringTokenizer tk = new StringTokenizer(line);
            System.out.println(line);
            while (tk.hasMoreTokens()) {
                res[i++] = Integer.parseInt(tk.nextToken());
            }
        }
        bufferedReader.close();
	return res;
    }
    
    public MarchingCubesImp(PApplet _p5, Vec3D _aabbMin, Vec3D _aabbMax, Vec3D _numPoints, float _isoLevel) {
	super(_p5, _aabbMin, _aabbMax, _numPoints, _isoLevel);
    }
    
}
