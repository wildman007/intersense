/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

import com.sense3d.intersense.network.core.ByteArrayLengthFramer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.net.Server;

/**
 * Sends a dataframe through network.
 * 
 * @author wildman
 */
public class Dataframe2Network implements DataframeSender {

    private final int port;
    private final PApplet papplet;
    private Server server;
    private boolean initialized;

    public Dataframe2Network(PApplet p, int port) {
        this.port = port;
        this.papplet = p;
    }

    @Override
    public synchronized void init() {
        server = new Server(papplet, port);
        initialized = true;
    }

    @Override
    public synchronized boolean isInitialized() {
        return initialized;
    }

    @Override
    public synchronized void send(NetworkFrame nf) {
        if (nf != null) {
            try {
                byte[] msg = ByteArrayLengthFramer.frameMsg(nf.toBytes());
                server.write(msg);
            } catch (IOException ex) {
                Logger.getLogger(Dataframe2Network.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void destroy() {
        initialized = false;
        server.stop();
    }

}
