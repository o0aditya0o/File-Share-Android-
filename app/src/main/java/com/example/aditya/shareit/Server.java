package com.example.aditya.shareit;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aditya on 25/6/16.
 */
public class Server extends AsyncTask<String,Integer,Void> {

    TextView deviceIP,progress;
    Context context;
    String ipaddress;
    ServerSocket server = null;
    Socket sock = null;
    File newfile ;
    BufferedInputStream bis = null;
    DataOutputStream dos = null;
    String path =null;
    static InputStream in;


    Server(Context context,TextView a,TextView b){
        deviceIP = a;
        progress = b;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        ipaddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));

        publishProgress(1);

        try {
            server = new ServerSocket(3128);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sock = server.accept();
            publishProgress(2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        publishProgress(4);


        bis = new BufferedInputStream(in);

        byte[] data = new byte[4076];

        try {
            dos = new DataOutputStream(sock.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int read;
       /*try {
           dos.writeUTF(newfile.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            while((read = bis.read(data))>0){
                dos.write(data);
                publishProgress(3);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        publishProgress(4);


        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... val) {
        int k=0;
        if(val[0]==1) {
            deviceIP.setText(ipaddress);
        }
        else if(val[0]==2)
            progress.setText("Connection Established");
        else if(val[0]==3){
            if(k%5==0)
                progress.setText("Sending File");
            else{
                String temp=progress.getText().toString();
                progress.setText(temp+".");
            }
        }
        else
            progress.setText("File Send");
    }



}
