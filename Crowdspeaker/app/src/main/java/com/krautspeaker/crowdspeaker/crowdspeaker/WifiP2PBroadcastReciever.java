package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

/**
 * Created by yooui on 01.04.17.
 */

public class WifiP2PBroadcastReciever extends BroadcastReceiver {

    private WifiP2pManager peerManager;
    private Channel peerChannel;
    private MainActivity mainActivity;
    private WifiP2pDeviceList myDevices;
    private Integer type;

    public WifiP2PBroadcastReciever(WifiP2pManager manager, Channel channel, MainActivity activity, Integer type) {
        super();

        this.peerManager = manager;
        this.peerChannel = channel;
        this.mainActivity = activity;
        this.type = type;
        this.myDevices = new WifiP2pDeviceList();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                //Everything fine
                Log.i("P2P State Test", "P2P available");
            }else{
                //Inform user and close app

                Log.e("P2P State Test", "P2P not available");            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if(type == 1) {
                // Respond to new connection or disconnections
                if (peerManager != null) {
                    peerManager.requestPeers(peerChannel, new WifiP2pManager.PeerListListener() {

                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            Log.i("OnPeersAvailable", String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));
                            for (final WifiP2pDevice singlePeer : peers.getDeviceList()) {
                                Log.i("Peer discoverd", singlePeer.deviceName);


                                WifiP2pConfig config = new WifiP2pConfig();
                                config.deviceAddress = singlePeer.deviceAddress;
                                config.wps.setup = WpsInfo.PBC;

                                peerManager.connect(peerChannel, config, new WifiP2pManager.ActionListener() {

                                    @Override
                                    public void onSuccess() {
                                        // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
                                        Log.i("Peer Connection", "Connected to " + singlePeer.deviceName);
                                    }

                                    @Override
                                    public void onFailure(int reason) {
                                        Log.e("Peer Connection", "Connection failed: " + singlePeer.deviceName);
                                    }
                                });
                            }
                            FileServerAsyncTask myTask = new FileServerAsyncTask(true, mainActivity.getApplicationContext());
                            myTask.execute();

                        }
                    });
                }
            }else if(type == 2){
                FileServerAsyncTask myTask = new FileServerAsyncTask(false, mainActivity.getApplicationContext());
                myTask.execute();
            }


        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }


}
