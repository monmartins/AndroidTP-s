package a362960.dspm.dc.ufc.br.androidlocation1;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private TextView addressLabel;
    private TextView locationLabel;
    private GoogleApiClient googleApiClient;
    private  Maps_frag mp = new Maps_frag();
    private android.location.LocationListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.your_placeholder, mp);
        ft.commitAllowingStateLoss();


    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();

    }
    public void getLocation(View view) {
        if(mp.getP()==4){
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            GetAddressTask task = new GetAddressTask(this);
            mp.addMap(location);
            if(mp.verificar(location)){
                Toast.makeText(this,"Está dentro!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Está fora!!", Toast.LENGTH_SHORT).show();
            }
            task.execute(location);
        }
        else{
            Toast.makeText(this,"Escolha quatro pontos", Toast.LENGTH_SHORT).show();
        }

    }
    public void clear(View view) {
        mp.clearMap();

    }

    public void doSubscribe(View view) {
        if (googleApiClient.isConnected()) {
            LocationRequest request = new LocationRequest();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setInterval(5000);
            request.setSmallestDisplacement(2);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int value) {
        Toast.makeText(this, "Disconnected!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "Connection failed...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

//        String text = "Updated Location = <" + location.getLatitude() + "," + location.getLongitude() + ">";

//        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        private Context context;

        public GetAddressTask(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPostExecute(String address){
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            Location location = params[0];
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException ioe) {
                Log.e("GetAddressTask", "IO Exception in getFromLocation()");
                ioe.printStackTrace();
                return "IO Exception trying to get address";
            } catch (IllegalArgumentException iae) {
                String errorString = "Illegal arguments " + Double.toString(location.getLatitude()) + " , " + Double.toString(location.getLongitude()) + " passed to address service";
                Log.e("GetAddressTask", errorString);
                iae.printStackTrace();
                return errorString;
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0) + ", " + address.getAdminArea() + ", " + address.getCountryCode();
                return addressText;
            } else {
                return "No address found!";
            }
        }
    }
}
