/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

import com.sense3d.intersense.network.TestDataSave;
import com.sense3d.intersense.network.core.LengthFramer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads a dataframe from a binary file.
 * @author wildman
 */
public class DataframeFromFile implements DataframeReceived {

    private final File inputFile;
    private FileInputStream fis;
    private BufferedInputStream bfis;
    private boolean initialized = false;
    private LengthFramer framer;

    public DataframeFromFile(String filename) {
        inputFile = new File(filename);
    }

    @Override
    public synchronized void init() {
        //prepare a file to write to
        try {
            fis = new FileInputStream(inputFile);
            bfis = new BufferedInputStream(fis);
            framer=new LengthFramer(bfis);
            initialized = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestDataSave.class.getName()).log(Level.SEVERE, null, ex);
            initialized = false;
        } catch (IOException ex) {
            Logger.getLogger(DataframeFromFile.class.getName()).log(Level.SEVERE, null, ex);
            initialized = false;
        }

    }

    /**
     * Checks that everything is OK.
     *
     * @return
     */
    @Override
    public synchronized boolean isInitialized() {
        return initialized;
    }

    @Override
    public synchronized void destroy() {
        initialized = false;
        try {
            if (bfis != null) {
                bfis.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(DataframeFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized NetworkFrame receive() {
        try {
            byte[] msg=framer.nextMsg();
            if(msg!=null){
                return NetworkFrame.fromBytes(msg);
            }
        } catch (IOException ex) {
            Logger.getLogger(DataframeFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        destroy();
        return null;
    }

}
