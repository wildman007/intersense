/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract TCP client with implemented reconnection logic.
 * @author wildman
 */
public abstract class NetworkClient implements Runnable {

    public static final int SOCKET_BUFFER_SIZE = 0; //should be RRT*bandwidth/8; RRT is from ping in seconds
    protected long RECONNECT_TIMEOUT = 2000;
    protected final String address;
    protected volatile boolean closeRequest = false;
    protected LengthFramer framer;
    protected BufferedInputStream in;
    protected boolean isConnected = false;
    protected BufferedOutputStream out = null;
    protected final int port;
    protected Socket socket;
    private static final Logger log = Logger.getLogger(NetworkClient.class.getName());
    protected final ExecutorService exec = Executors.newSingleThreadExecutor();

    public NetworkClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public boolean connect() {
        log.info("Connecting to " + address + ":" + port);
        try {
            socket = new Socket();
            if (SOCKET_BUFFER_SIZE > 0) {
                socket.setSendBufferSize(SOCKET_BUFFER_SIZE);
                socket.setReceiveBufferSize(SOCKET_BUFFER_SIZE);
            }
            log.info("Socket SendBuff: " + socket.getSendBufferSize());
            log.info("Socket RecieveBuff: " + socket.getReceiveBufferSize());
            //connection attempt will timeout after 100ms
            socket.setSoTimeout(3000);
            socket.connect(new InetSocketAddress(address, port), 100);
//                        socket = new Socket(address, port);
            out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            framer = new LengthFramer(in);
            isConnected = true;
        } catch (SocketTimeoutException ex) {
            log.info("Connection timeout");
            return false;
        } catch (IOException ex) {
            //            log.log(Level.SEVERE, null, ex);
            log.info("Connection broken");
            return false;
        }
        return true;
    }

    public void disconnect() {
        log.info("Disconnecting");
        isConnected = false;
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            //            log.log(Level.SEVERE, null, ex);
            //            log.info("Not connected");
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            //            log.log(Level.SEVERE, null, ex);
            //            log.info("Not connected");
        }
        try {
            if ((socket != null) && (!socket.isClosed())) {
                socket.close();
            }
        } catch (IOException ex) {
            //            log.log(Level.SEVERE, null, ex);
            //            log.info("Not connected");
        }
    }

    public boolean isConnected() {
        return isConnected && (socket.isConnected() && (!socket.isClosed()) && (!socket.isInputShutdown()) && (!socket.isOutputShutdown()));
    }

    public void requestClose() {
        closeRequest = true;
        exec.shutdownNow();
    }

    public void init() {
        exec.submit(this);
    }

    public void destroy() {
        requestClose();
        try {
            exec.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(NetworkClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
