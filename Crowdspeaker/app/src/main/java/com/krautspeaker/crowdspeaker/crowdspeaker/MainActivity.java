package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    WifiP2pManager myManager;
    WifiP2pManager.Channel myChannel;
    BroadcastReceiver myReceiver;
    IntentFilter myIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setup the Broadcast Reciever, to get System Status
            //PreSetup
                myManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                myChannel = myManager.initialize(this, getMainLooper(), null);
                myIntentFilter = new IntentFilter();
            //Create new Reciever and add Intents
                myReceiver = new WifiP2PBroadcastReciever(myManager, myChannel, this);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);



       /*myManager.discoverPeers(myChannel, new WifiP2pManager.ActionListener(){
            @Override
            public void onSuccess() {
                Log.i("Start discovery", "Discovery started");

            }

            @Override
            public void onFailure(int reason) {
                Log.e("Start discovery", "Discovery error");

            }
        });*/



        try {
        String msg = "Hello";
        InetAddress group = null;
            group = InetAddress.getByName("228.5.6.7");
        MulticastSocket s = new MulticastSocket(6789);
        s.joinGroup(group);
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                group, 6789);
        s.send(hi);

            Log.i("Message send", msg);
        // get their responses!
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        s.receive(recv);
            Log.i("Message recieve", buf.toString());
        // OK, I'm done talking - leave the group...
        s.leaveGroup(group);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Register the Broadcast reciever
            registerReceiver(myReceiver, myIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Only recieve broadcast when the activity is open
            unregisterReceiver(myReceiver);
    }
}
