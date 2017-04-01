package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.Executor;

/**
 * Created by yooui on 01.04.17.
 */

public class clientDiscoverer extends AsyncTask{

    WifiP2pManager myManager;
    Channel myChannel;


     public clientDiscoverer(WifiP2pManager manager, Channel channel, Executor executor){
         myManager = manager;
         myChannel = channel;

         executeOnExecutor(executor);

     }


    @Override
    protected Object doInBackground(Object[] params) {

        if(myManager != null && myChannel != null){

            while(true) {
                myManager.discoverPeers(myChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("Start discovery", "Discovery started");

                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e("Start discovery", "Discovery error");

                    }
                });

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }else{
            Log.e("clientDiscoverer", "MyManager or MyChannel is null");
        }
        return -1;
    }
}
