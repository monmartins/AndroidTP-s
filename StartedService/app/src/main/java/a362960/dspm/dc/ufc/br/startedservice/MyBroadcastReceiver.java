package a362960.dspm.dc.ufc.br.startedservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by root on 10/05/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
// do something.
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("Check","Screen went OFF");
            Toast.makeText(context, "screen OFF",Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            context.startService(new Intent(context, StartedService.class));
            Log.i("Check","Screen went ON");
            Toast.makeText(context, "screen ON",Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, StartedService.class));
            Log.i("Check","Screen went ON");
            Toast.makeText(context, "screen ON", Toast.LENGTH_LONG).show();
        }
    }
}
