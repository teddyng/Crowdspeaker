package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    WifiP2pManager myManager;
    WifiP2pManager.Channel myChannel;
    BroadcastReceiver myReceiver;
    IntentFilter myIntentFilter;
    Integer type = -1;
    Boolean canRegister = false;
    FileServerAsyncTask myTask;
    clientDiscoverer myDiscoverer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context activityContext = this.getApplicationContext();
        final MainActivity thisActivity = this;


        //have a bigger threadpool
        final Executor myExecutor = Executors.newFixedThreadPool(4);

        //Setup the Broadcast Reciever, to get System Status
        //PreSetup
        myManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        myChannel = myManager.initialize(activityContext, getMainLooper(), null);
        myDiscoverer = new clientDiscoverer(myManager, myChannel, myExecutor);

        myIntentFilter = new IntentFilter();
        //Create new Reciever and add Intents
        myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        myIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);



        /**
         * Set the Buttons for selection
         */
        Button clientButton = (Button) findViewById(R.id.client);
        Button serverButton = (Button) findViewById(R.id.server);
        Button stopButton = (Button) findViewById(R.id.Stop);

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    onPause();


                myReceiver = new WifiP2PBroadcastReciever(myManager, myChannel, thisActivity, 2);

                canRegister = true;
                myTask = new FileServerAsyncTask(false, activityContext, myExecutor);

                onResume();

            }
        });

        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    onPause();




                myReceiver = new WifiP2PBroadcastReciever(myManager, myChannel, thisActivity, 1);
                myTask = new FileServerAsyncTask(true, activityContext, myExecutor);

                canRegister = true;



                onResume();






            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onPause();
                canRegister = false;


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

            if(canRegister) {
                //Register the Broadcast reciever
                if(myTask.getStatus().toString() != "RUNNING"){
                    Log.i("Resume", "Tasker RESUMED");
                    myTask.execute();
                }
                if(myDiscoverer.getStatus().toString() != "RUNNING"){
                    Log.i("Resume", "Discovere RESUMED");
                    myDiscoverer.execute();
                }

                registerReceiver(myReceiver, myIntentFilter);

                Log.i("Reciever Registerer", "Registered");

            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(canRegister) {
            //Register the Broadcast reciever

            myDiscoverer.cancel(true);
                Log.i("Pause", "Discovere PAUSED");

                myTask.stopFileServer();
                myTask.cancel(true);
                Log.i("Pause", "myTask PAUSED");


            unregisterReceiver(myReceiver);

        }

    }
}
