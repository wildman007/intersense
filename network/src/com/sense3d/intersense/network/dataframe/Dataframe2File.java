/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

import com.sense3d.intersense.network.TestDataSave;
import com.sense3d.intersense.network.core.LengthFramer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Saves a dataframe to a binary file.
 * 
 * @author wildman
 */
public class Dataframe2File implements DataframeSender {

    private final File outputFile;
    private FileOutputStream fos;
    private BufferedOutputStream bfos;
    private boolean initialized = false;

    public Dataframe2File(String filename) {
        outputFile = new File(filename);
    }

    @Override
    public synchronized void init() {
        //prepare a file to write to
        try {
            fos = new FileOutputStream(outputFile, false);
            bfos = new BufferedOutputStream(fos);
            initialized = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestDataSave.class.getName()).log(Level.SEVERE, null, ex);
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
    public synchronized void send(NetworkFrame nf) {
        try {
            //save it to the file
            if (isInitialized()) {
                LengthFramer.frameMsg(bfos, nf.toBytes());
            }
        } catch (IOException ex) {
            Logger.getLogger(TestDataSave.class.getName()).log(Level.SEVERE, null, ex);
            initialized = false;
        }
    }

    @Override
    public synchronized void destroy() {
        initialized = false;
        try {
            if (bfos != null) {
                bfos.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Dataframe2File.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
