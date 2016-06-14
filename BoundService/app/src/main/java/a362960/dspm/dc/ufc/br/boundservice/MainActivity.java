package a362960.dspm.dc.ufc.br.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LocalService myservice;
    boolean isBound = false;
    TextView operacao, resultado;
    EditText primeiroNumero, segundoNumero;
    float num;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            myservice = binder.getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operacao = (TextView)findViewById(R.id.operação);
        resultado = (TextView)findViewById(R.id.resultado);
        primeiroNumero = (EditText)findViewById(R.id.primeiroNumero);
        segundoNumero = (EditText)findViewById(R.id.segundoNumero);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
    public void somar(View view) {
        if (isBound) {
            operacao.setText("+");
            num = myservice.somar(Float.parseFloat(primeiroNumero.getText().toString()) ,Float.parseFloat(segundoNumero.getText().toString()));
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }
    public void subtrair(View view) {
        if (isBound) {
            operacao.setText("-");
            num = myservice.subtrair(Float.parseFloat(primeiroNumero.getText().toString()) ,Float.parseFloat(segundoNumero.getText().toString()));
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }
    public void dividir(View view) {
        if (isBound) {
            operacao.setText("/");
            num = myservice.dividir(Float.parseFloat(primeiroNumero.getText().toString()) ,Float.parseFloat(segundoNumero.getText().toString()));
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }
    public void multiplicar(View view) {
        if (isBound) {
            operacao.setText("*");
            num = myservice.multiplicar(Float.parseFloat(primeiroNumero.getText().toString()) ,Float.parseFloat(segundoNumero.getText().toString()));
            //Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }
    public void resultado(View view) {
        if (isBound) {
            resultado.setText(num+"");
        }
    }


}
