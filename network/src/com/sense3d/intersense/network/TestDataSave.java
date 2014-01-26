/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network;

import com.sense3d.intersense.network.dataframe.DataframeSender;
import com.sense3d.intersense.network.dataframe.Dataframe2File;
import com.sense3d.intersense.network.dataframe.NetworkFrame;
import com.sense3d.intersense.network.datatypes.Centroid;
import com.sense3d.intersense.network.datatypes.SubCentroid;
import com.sense3d.intersense.network.datatypes.Point;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import processing.core.PApplet;

/**
 * Randomly generates dataframes and saves them to a binary file.
 * 
 * @author wildman
 */
public class TestDataSave extends PApplet {

    private final AtomicBoolean wasRunBefore = new AtomicBoolean(false);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> a = new ArrayList<>();
        // a.add(PApplet.ARGS_HIDE_STOP); //hide stop button
        // a.add(PApplet.ARGS_LOCATION + "=0,0"); //do not center the location of sketch
        // a.add(PApplet.ARGS_FULL_SCREEN); //go into fullscreen
        // a.add(PApplet.ARGS_DISPLAY + "=" + 0); //use display number X (0, 1, 2, 3)
        a.add(TestDataSave.class.getCanonicalName());

        PApplet.main(a.toArray(new String[0]));
    }
    private File outputFile;
    private FileOutputStream fos;
    private BufferedOutputStream bfos;
    private DataframeSender sender;
    private int framesSaved=0;

    @Override
    public void setup() {
        intializeOnce();
    }

    private void intializeOnce() {
        //if intializeOnce was already run, simply return
        if (wasRunBefore.getAndSet(true)) {
            return;
        }

        // we will be saving to a file
        sender = new Dataframe2File("data.bin");
        sender.init();
    }

    @Override
    public void draw() {
        background(0);
        // generate points
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < random(50,10000); i++) {
            points.add(i, randomPoint());
        }
        List<Centroid> centroids = new ArrayList<>();
        for (int i = 0; i < random(1,20); i++) {
            centroids.add(i, new Centroid(randomPoint(), i));
        }

        List<SubCentroid> subCentroids = new ArrayList<>();
        for (Centroid centroid : centroids) {
            for (int i = 0; i < random(1,10); i++) {
                subCentroids.add(new SubCentroid(randomPoint(), centroid.getClusterID(),i));
            }
        }

        //make an network frame
        NetworkFrame netFrame = new NetworkFrame(frameCount, points, centroids, subCentroids);

        //transfer it
        if (sender.isInitialized()) {
            sender.send(netFrame);
            framesSaved++;
        }

        //draw logs
        fill(255);
        textSize(30);
        text("FPS: "+((int)frameRate), 500,50);
        text("Frames saved: "+framesSaved, 50,50);
        text("Points: " + points.size(), 50, 80);
        text("Centroids: " + centroids.size(), 50, 110);
        text("Subcentroids: " + subCentroids.size(), 50, 140);

    }

    @Override
    public int sketchWidth() {
        return 640;
    }

    @Override
    public int sketchHeight() {
        return 480;
    }

    @Override
    public String sketchRenderer() {
        return P2D;
    }

    @Override
    public void exit() {
        sender.destroy(); //dont forget!!!
        super.exit(); //To change body of generated methods, choose Tools | Templates.
    }

    private Point randomPoint() {
        return new Point((int) random(-32000, 32000), (int) random(-32000, 32000), (int) random(0, 32000));
    }

}
