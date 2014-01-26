/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network;

import com.sense3d.intersense.network.dataframe.DataframeFromFile;
import com.sense3d.intersense.network.dataframe.DataframeSender;
import com.sense3d.intersense.network.dataframe.DataframeReceived;
import com.sense3d.intersense.network.dataframe.Dataframe2Network;
import com.sense3d.intersense.network.dataframe.NetworkFrame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import processing.core.PApplet;

/**
 * Loads dataframes from file and sends them through network (posing as an server).
 * 
 * @author wildman
 */
public class TestDataLoadAndNetworkSend extends PApplet {
    private static String fileName;
    private static int FPS;

    private final AtomicBoolean wasRunBefore = new AtomicBoolean(false);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length==2){
            FPS=Integer.parseInt(args[0]);
            fileName=args[1];
            
        }else{
            System.out.println("ERROR: FPS and filename of a file with dataframes has to be provided as an argument!");
            System.exit(1);
        }
        if(!(new File(fileName)).exists()){
            System.out.println("ERROR: File provided does not exist!");
            System.exit(1);
        }
        List<String> a = new ArrayList<>();
        // a.add(PApplet.ARGS_HIDE_STOP); //hide stop button
        // a.add(PApplet.ARGS_LOCATION + "=0,0"); //do not center the location of sketch
        // a.add(PApplet.ARGS_FULL_SCREEN); //go into fullscreen
        // a.add(PApplet.ARGS_DISPLAY + "=" + 0); //use display number X (0, 1, 2, 3)
        a.add(TestDataLoadAndNetworkSend.class.getCanonicalName());

        PApplet.main(a.toArray(new String[0]));
    }
    private DataframeReceived receiver;
    private DataframeSender sender;
    public static final int SERVER_PORT=10001;

    @Override
    public void setup() {
        intializeOnce();
    }

    private void intializeOnce() {
        //if intializeOnce was already run, simply return
        if (wasRunBefore.getAndSet(true)) {
            return;
        }
        
        frameRate(FPS);
        
        // we will be saving to a file
        receiver = new DataframeFromFile(fileName);
        receiver.init();
        sender=new Dataframe2Network(this,SERVER_PORT);
        sender.init();
    }

    @Override
    public void draw() {
        background(0);
        NetworkFrame nf = null;
        if (receiver.isInitialized()) {
            nf = receiver.receive();
            if(sender.isInitialized()){
                sender.send(nf);
            }
        }else{
            receiver.init();
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
            text("Sender initialized: " + sender.isInitialized(), 50, 250);
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
        sender.destroy();
        super.exit(); //To change body of generated methods, choose Tools | Templates.
    }

}
