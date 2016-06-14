package a362960.dspm.dc.ufc.br.sensorapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SensorManager sMgr;
    Sensor light, pressure, temperature, humidity;
    TextView textReadingT,textReadingP, textReadingU, textReadingL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        light = sMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
//        pressure = sMgr.getDefaultSensor(Sensor.TYPE_PRESSURE);
//        temperature = sMgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//        humidity = sMgr.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        //Não tenho nenhum desses três sensores, substituir por sensores existentes no meu celular
        pressure = sMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        temperature = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        humidity = sMgr.getDefaultSensor(Sensor.TYPE_GRAVITY);
        textReadingT = (TextView)findViewById(R.id.temperatura);
        textReadingP = (TextView)findViewById(R.id.pressao);
        textReadingU  = (TextView)findViewById(R.id.umidade);
        textReadingL = (TextView)findViewById(R.id.iluminosidade);


    }
    SensorEventListener lightSensorEventListener = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_LIGHT) {
                final float currentReading = event.values[0];
                textReadingL.setText("Leitura(Luz): " + String.valueOf(currentReading));
            }
        }
    };
    SensorEventListener pressureSensorEventListener = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
//        public void onSensorChanged(SensorEvent event) {
//            if(event.sensor.getType()==Sensor.TYPE_PRESSURE){
//                final float currentReading = event.values[0];
//                textReadingP.setText("Leitura(Pressão): " + String.valueOf(currentReading));
//
//
//            }
//
//        }

        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
                final float currentReading = event.values[0];
                textReadingP.setText("Leitura(Giroscópio): " + String.valueOf(currentReading));


            }

        }
    };
    SensorEventListener temperatureSensorEventListener = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
//        public void onSensorChanged(SensorEvent event) {
//            if(event.sensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE){
//                final float currentReading = event.values[0];
//                textReadingT.setText("Leitura(Temperatura): " + String.valueOf(currentReading));
//
//
//            }
//        }

        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                final float currentReading = event.values[0];
                textReadingT.setText("Leitura(Acelerômetro): " + String.valueOf(currentReading));


            }
        }
    };

    SensorEventListener humidityEventListener = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
//        public void onSensorChanged(SensorEvent event) {
//            if(event.sensor.getType()==Sensor.TYPE_RELATIVE_HUMIDITY){
//                final float currentReading = event.values[0];
//                textReadingU.setText("Leitura(Umidade): " + String.valueOf(currentReading));
//
//
//            }
//        }

        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_GRAVITY){
                final float currentReading = event.values[0];
                textReadingU.setText("Leitura(Gravidade): " + String.valueOf(currentReading));

            }
        }
    };
    public void tutorial(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }
    public void start(View view){
        sMgr.registerListener( lightSensorEventListener, light, SensorManager.SENSOR_DELAY_NORMAL);
        sMgr.registerListener( pressureSensorEventListener, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        sMgr.registerListener( temperatureSensorEventListener, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        sMgr.registerListener( humidityEventListener, humidity, SensorManager.SENSOR_DELAY_NORMAL);

    }
    public void finish(View view){
        sMgr.unregisterListener(lightSensorEventListener);
        sMgr.unregisterListener(pressureSensorEventListener);
        sMgr.unregisterListener(temperatureSensorEventListener);
        sMgr.unregisterListener(humidityEventListener);

    }

}
