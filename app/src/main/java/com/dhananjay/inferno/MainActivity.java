package com.dhananjay.inferno;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    String TAG="My";
    MyBroadcastReceiver receiver;
    String message="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String token=FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
        Log.d("token", "onCreate: "+token);
        tv= (TextView) findViewById(R.id.text_view);
        tv.setText(message);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                message=message+"\n"+" from intent "+"Key: " + key + " Value: " + value;
                tv.setText(message);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String update = extras.getString("extra");
            message=message+"\n"+update;
            Log.d(TAG, "onReceive: "+message);
            tv.setText(message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.dhananjay.inferno.onMessageReceived");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
