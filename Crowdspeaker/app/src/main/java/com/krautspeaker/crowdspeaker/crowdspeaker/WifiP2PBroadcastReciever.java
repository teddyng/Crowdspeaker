package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yooui on 01.04.17.
 */

public class WifiP2PBroadcastReciever extends BroadcastReceiver {

    private WifiP2pManager peerManager;
    private WifiP2pManager.Channel peerChannel;
    private MainActivity mainActivity;
    private WifiP2pDeviceList myDevices;

    public WifiP2PBroadcastReciever(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity) {
        super();

        this.peerManager = manager;
        this.peerChannel = channel;
        this.mainActivity = activity;

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

                Log.i("P2P State Test", "P2P available");            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            if (peerManager != null) {
                peerManager.requestPeers(peerChannel, new WifiP2pManager.PeerListListener() {

                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {
                    Log.i("OnPeersAvailable",String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));

                    // DO WHATEVER YOU WANT HERE
                    // YOU CAN GET ACCESS TO ALL THE DEVICES YOU FOUND FROM peers OBJECT

                }
            });
            }



        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }


}
