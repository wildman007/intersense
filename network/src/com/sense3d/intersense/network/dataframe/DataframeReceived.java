/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sense3d.intersense.network.dataframe;

/**
 * Interface that receives dataframes.
 *
 * @author wildman
 */
public interface DataframeReceived {

    public void init();

    public boolean isInitialized();

    public NetworkFrame receive();

    public void destroy();

}
