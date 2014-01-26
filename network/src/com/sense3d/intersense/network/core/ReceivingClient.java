/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.core;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple TCP client that connects to a server and constantly receives data
 *
 * @author wildman
 */
public class ReceivingClient extends NetworkClient {

    private static final Logger log = Logger.getLogger(ReceivingClient.class.getName());
    private final Receiver receiver;

    public ReceivingClient(String address, int port, Receiver r) {
        super(address, port);
        log.setLevel(Level.SEVERE);
        this.receiver = r;
//        try {
//            FileHandler fh = new FileHandler("rc.log");
//            fh.setFormatter(new SimpleFormatter());
////            log.addHandler(fh);
//
//        } catch (IOException | SecurityException ex) {
//            Logger.getLogger(ReceivingClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void run() {
        log.entering("Receiving client", "run");
        while ((!closeRequest)) {
            if (isConnected()) {
                try {
                    log.info("Waiting for bytes for reading.");
                    byte[] data = framer.nextMsg();
                    if (data == null) { //stream closed
                        disconnect();
                    } else {
                        log.info("Some bytes are available for reading.");
                        if (receiver != null) {
                            log.info("Giving data to the receiver.");
                            receiver.messageReceived(data);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ReceivingClient.class.getName()).log(Level.SEVERE, null, ex);
                    disconnect();
                }
            } else {
                disconnect();
                try {
                    Thread.sleep(RECONNECT_TIMEOUT);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReceivingClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                connect();
            }
        }
    }

}