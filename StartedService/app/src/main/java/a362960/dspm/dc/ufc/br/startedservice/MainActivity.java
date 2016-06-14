package a362960.dspm.dc.ufc.br.startedservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));


        //startService(new Intent(this,StartedService.class));


    }
    //Create broadcast object
    BroadcastReceiver mybroadcast = new BroadcastReceiver() {
        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("[BroadcastReceiver]", "MyReceiver");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                context.startService(new Intent(context,StartedService.class));
                Log.i("[BroadcastReceiver]", "Screen ON");
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("[BroadcastReceiver]", "Screen OFF");
            }

        }
    };
}

