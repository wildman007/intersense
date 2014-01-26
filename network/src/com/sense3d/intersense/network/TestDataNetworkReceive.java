/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network;

import com.sense3d.intersense.network.dataframe.DataframeFromNetwork;
import com.sense3d.intersense.network.dataframe.DataframeReceived;
import com.sense3d.intersense.network.dataframe.NetworkFrame;
import static com.sense3d.intersense.network.TestDataLoadAndNetworkSend.SERVER_PORT;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import processing.core.PApplet;

/**
 * Receives dataframes from network and display what the dataframe contains.
 * 
 * @author wildman
 */
public class TestDataNetworkReceive extends PApplet {

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
        a.add(TestDataNetworkReceive.class.getCanonicalName());

        PApplet.main(a.toArray(new String[0]));
    }
    private DataframeReceived receiver;
    public static String ADDRESS="127.0.0.1";

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
        receiver = new DataframeFromNetwork(ADDRESS, SERVER_PORT);
        receiver.init();
    }

    @Override
    public void draw() {
        background(0);
        NetworkFrame nf = null;
        if (receiver.isInitialized()) {
            nf = receiver.receive();
        }

        //draw logs
        fill(255);
        textSize(30);
        text("FPS: " + ((int) frameRate), 500, 50);

        if (nf != null) {
            text("Frame ID: " + nf.getFrameId(), 50, 50);
            text("Points: " + nf.getPoints().size(), 50, 80);
            text("Centroids: " + nf.getCentroids().size(), 50, 110);
            text("Subcentroids: " + nf.getSubCentroids().size(), 50, 140);
        }
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
        receiver.destroy(); //dont forget!!!
        super.exit(); //To change body of generated methods, choose Tools | Templates.
    }

}
