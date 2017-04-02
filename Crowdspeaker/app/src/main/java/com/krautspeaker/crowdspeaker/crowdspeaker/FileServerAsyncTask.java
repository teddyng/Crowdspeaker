package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.concurrent.Executor;


public class FileServerAsyncTask extends AsyncTask {

    Boolean server;
    Boolean run;
    Context myContext;
    MulticastSocket s;
    InetAddress group1;

    public FileServerAsyncTask(Boolean server, Context context, Executor executor){
        this.server = server;
        this.run = true;
        this.myContext = context;

        executeOnExecutor(executor);






    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Register the Multicast Socket
        try {
            group1 = InetAddress.getByName("FF02::1");
            s = new MulticastSocket(9876);
            s.joinGroup(group1);
            s.setReuseAddress(true);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object[] params) {
        try{

            while(run){

                if(server) {
                    String msg = "Hello at " +  System.currentTimeMillis() ;
                    DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                            group1, 9876);
                    s.send(hi);
                    // get their responses!
                    Log.i("Message send", msg);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{

                    Log.i("IF", "IF");
                    byte[] buf = new byte[1000];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    s.receive(recv);

                    Log.i("Message recieved", new String(buf, 0, recv.getLength()));
                }

            }
            // OK, I'm done talking - leave the group...

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Cool";
    }

    public void stopFileServer(){
        run = false;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            s.leaveGroup(group1);
            Log.i("IF", "OFF");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Start activity that can handle the JPEG image
     *
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            statusText.setText("File copied - " + result);
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + result), "image/*");
            context.startActivity(intent);
        }
    }*/

}
