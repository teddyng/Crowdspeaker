package com.krautspeaker.crowdspeaker.crowdspeaker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.sql.Time;


public class FileServerAsyncTask extends AsyncTask {

    Boolean server;
    Boolean run;

    public FileServerAsyncTask(Boolean server) {
        this.server = server;
        this.run = true;
    }


    @Override
    protected String doInBackground(Object[] params) {
        try{

            //Register the Multicast Socket
            InetAddress group = InetAddress.getByName("228.5.6.7");
            MulticastSocket s = new MulticastSocket(6789);
            s.joinGroup(group);

            while(run) {
                Thread.sleep(1000);

                if(server) {

                    String msg = "Hello at " +  System.currentTimeMillis() ;
                    DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                            group, 6789);
                    s.send(hi);
                    // get their responses!
                    Log.i("Messege sending", msg);
                }else{
                    byte[] buf = new byte[1000];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                s.receive(recv);


                Log.i("Message recieved", buf.toString());
                }
            }
            // OK, I'm done talking - leave the group...
            s.leaveGroup(group);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Cool";
    }

    public void stop(){
        run = false;
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
