package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    Integer type = -1;
    Boolean canRegister = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context activityContext = this.getApplicationContext();
        final MainActivity thisActivity = this;

        /**
         * Set the Buttons for selection
         */
        Button clientButton = (Button) findViewById(R.id.client);
        Button serverButton = (Button) findViewById(R.id.server);

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(canRegister)onPause();


                //Setup the Broadcast Reciever, to get System Status
                //PreSetup
                myManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                myChannel = myManager.initialize(activityContext, getMainLooper(), null);
                myIntentFilter = new IntentFilter();
                //Create new Reciever and add Intents
                myReceiver = new WifiP2PBroadcastReciever(myManager, myChannel, thisActivity, 2);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

                canRegister = true;

                onResume();






            }
        });

        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(canRegister)onPause();

                //Setup the Broadcast Reciever, to get System Status
                //PreSetup
                myManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                myChannel = myManager.initialize(activityContext, getMainLooper(), null);
                myIntentFilter = new IntentFilter();
                //Create new Reciever and add Intents
                myReceiver = new WifiP2PBroadcastReciever(myManager, myChannel, thisActivity, 1);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

                canRegister = true;

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



                onResume();






            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

            if(canRegister) {
                //Register the Broadcast reciever
                registerReceiver(myReceiver, myIntentFilter);
                Log.i("Reciever Registerer", "Registered");

            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(canRegister) {
            //Register the Broadcast reciever
            unregisterReceiver(myReceiver);
            Log.i("Reciever Registerer", "Unregistered");

        }

    }
}
