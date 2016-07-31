package com.example.aditya.shareit;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by aditya on 25/6/16.
 */
public class Client extends AsyncTask<String,Integer,Void> {
    Socket socket = null;
    DataInputStream dataInputStream = null;
    TextView textIn;
    File newfile ;
    String name = null;
    FileOutputStream fos = null;
    public int c=1;
    public int filec=1;
    Client(TextView text){
        textIn=text;
    }
    @Override
    protected Void doInBackground(String... params) {
        try {
            String dest_dir= Environment.getExternalStorageDirectory()+"/FileShare";
            File folder=new File(dest_dir);
            if(!folder.exists())
            {
                folder.mkdirs();

            }
            socket = new Socket(params[0], 3128);
            if(socket!=null)
                publishProgress(0);
            dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] buffer = new byte[4096];
            int read ;

            try {
                //name = dataInputStream.readUTF();
                //Log.i("tagg",name);
                //publishProgress(6);
                String temp = MainActivity.newfile.getName();
                newfile = new File(dest_dir+"/"+temp);
                filec++;
                fos = new FileOutputStream(newfile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                while ((read = dataInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, read);
                        publishProgress(1);
                    }
                publishProgress(2);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataInputStream != null){
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    protected void onProgressUpdate(Integer... k){
        if(k[0]==0)
            textIn.setText("Connected to Server (Waiting for file to be send)");
        else if(k[0]==1){
            textIn.setText("Recieving File");
            for(int i=0;i<c%5;i++){
                textIn.setText(textIn.getText().toString()+".");
            }
            c++;
        }
        else if(k[0]==6){
            MainActivity.senderIP.setText(name);
        }
        else if(k[0]==2){
            textIn.setText("File Recieved ");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
