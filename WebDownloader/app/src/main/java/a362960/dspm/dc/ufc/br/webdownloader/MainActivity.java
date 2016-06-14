package a362960.dspm.dc.ufc.br.webdownloader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager connManager;
    EditText url;
    TextView conteudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection(this.getCurrentFocus());
        url = (EditText) findViewById(R.id.url);
        conteudo = (TextView) findViewById(R.id.corpo);
        conteudo.setMovementMethod(new ScrollingMovementMethod());

    }

    public void checkConnection( View view) {
        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = info.isConnected();
        Toast.makeText(this, "Is Wi-Fi connected?" + isWifiConn, Toast.LENGTH_SHORT).show();
    info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    boolean isMobileConn = info.isConnected();
    Toast.makeText(this, "Is Mobile connected?"+ isMobileConn, Toast.LENGTH_SHORT).show();
    }

    public void download(View view){
        DownloaderTask result = new DownloaderTask(conteudo);
        result.execute(url.getText().toString());

    }
}
