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


public class FileServerAsyncTask extends AsyncTask {


    public FileServerAsyncTask() {
    }


    @Override
    protected String doInBackground(Object[] params) {
        try{
            String msg = "Hello";
            InetAddress group = InetAddress.getByName("228.5.6.7");
            MulticastSocket s = new MulticastSocket(6789);
            s.joinGroup(group);
            DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                group, 6789);
            s.send(hi);
            // get their responses!
            Log.i("Messege sending", "Send");
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            s.receive(recv);


            Log.i("Messege sending", "Recieved");
            // OK, I'm done talking - leave the group...
            s.leaveGroup(group);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Cool";
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
