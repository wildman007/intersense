/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

import com.sense3d.intersense.network.core.ReceivingClient;
import com.sense3d.intersense.network.core.Receiver;
import com.sense3d.intersense.network.dataframe.NetworkFrame;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Receives a dataframe from network. Works asynchronously.
 * @author wildman
 */
public class DataframeFromNetwork implements DataframeReceived, Receiver {

    private boolean initialized;
    private ReceivingClient client;
    private final int port;
    private final String address;
    private ExecutorService exec;
    private NetworkFrame nf=new NetworkFrame(0, null, null, null);

    public DataframeFromNetwork(String address, int port) {
        this.address=address;
        this.port=port;
    }

    @Override
    public synchronized void init() {
        client = new ReceivingClient(address, port, this);
        exec=Executors.newSingleThreadExecutor();
        exec.submit(client);
        initialized=true;
    }

    @Override
    public synchronized boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns the most recent NetworkFrame received from network.<br><br>
     * Reading from network is in separate thread, therefore this methods returns almost instantly.
     * @return 
     */
    @Override
    public synchronized NetworkFrame receive() {
        return nf;
    }

    @Override
    public synchronized void destroy() {
        client.requestClose();
        exec.shutdownNow();
    }

    @Override
    public synchronized void messageReceived(byte[] data) {
        NetworkFrame newNF=NetworkFrame.fromBytes(data);
        nf=newNF;
    }

    @Override
    public void messageReceived(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void requestClose() {
        
    }

}
