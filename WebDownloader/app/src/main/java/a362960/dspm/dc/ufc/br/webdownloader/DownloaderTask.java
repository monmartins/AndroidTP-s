package a362960.dspm.dc.ufc.br.webdownloader;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by root on 24/05/16.
 */
public class DownloaderTask extends AsyncTask<String, String, String> {
    private TextView textView;
    public DownloaderTask(TextView textView) {
        this.textView = textView;
    }

    protected void onProgressUpdate(String result) {}

    @Override
    protected String doInBackground(String... strings) {
        String content = null;
        String link = (String) strings[0];
        try {
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String data = null;
                content = "";
                while ((data = reader.readLine()) != null) {
                    content += data + "\n";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    protected void onPostExecute(String result) {
        this.textView.setText(result);
    }
}