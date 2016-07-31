package com.example.aditya.shareit;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends Activity {

    public static EditText senderIP;
    public static TextView deviceIP,progress;
    Button client,server,choose;
    public static Uri uri;
    public static final int READ_REQUEST_CODE = 42;
    static File newfile ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senderIP = (EditText)findViewById(R.id.senderip);
        client = (Button)findViewById(R.id.recieve);
        deviceIP = (TextView)findViewById(R.id.deviceip);
        client.setOnClickListener(clientOnClickListener);
        server = (Button)findViewById(R.id.send);
        server.setOnClickListener(serverOnClickListener);
        progress = (TextView)findViewById(R.id.progress);
        choose = (Button)findViewById(R.id.filech);

    }


    Button.OnClickListener clientOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Client client = new Client(progress);
            client.execute(senderIP.getText().toString());
        }};

    Button.OnClickListener serverOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Server server = new Server(MainActivity.this,deviceIP,progress);
            server.execute(senderIP.getText().toString());

        }};

    public void ChooseFile(View view) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    public void onActivityResult(int requestCode,int resultCode,Intent resultData)
    {
        if(requestCode==READ_REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            if(resultData!=null)
            {
                uri=resultData.getData();
                newfile=new File(uri.getPath());
                try {
                    Server.in = getContentResolver().openInputStream(MainActivity.uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                progress.setText(newfile.getName());
            }
        }
    }



}