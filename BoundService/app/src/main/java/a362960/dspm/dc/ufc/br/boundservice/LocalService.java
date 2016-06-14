package a362960.dspm.dc.ufc.br.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

/**
 * Created by root on 14/06/16.
 */
public class LocalService extends Service {
    private final IBinder binder = new LocalBinder();
    private final Random generator = new Random();

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getRandomNumber() {
        return generator.nextInt(100);
    }

    public float somar(float a, float b) {
        return a+b;
    }
    public float subtrair(float a, float b) {
        return a-b;
    }
    public float dividir(float a, float b) {
        return a/b;
    }
    public float multiplicar(float a, float b) {
        return a*b;
    }

}