package a362960.dspm.dc.ufc.br.trackingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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
    private  List<Location> locs = new ArrayList<Location>();
    private android.location.LocationListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addressLabel = (TextView) findViewById(R.id.addressTextView);
        this.locationLabel = (TextView) findViewById(R.id.locationTextView);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.your_placeholder,mp);
// ou ft.add(R.id.your_placeholder, new Fragment());
        ft.commitAllowingStateLoss();


        //Verificar -------------
//
//        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(this.LOCATION_SERVICE);
//
//
//        long tempo = 20 ; //20 segundos
//        float distancia = 100; // 100 metros
//
//        this.listener = new android.location.LocationListener() {
//
//            @Override
//            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//                Toast.makeText(getApplicationContext(), "Status alterado", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onProviderEnabled(String arg0) {
//                Toast.makeText(getApplicationContext(), "Provider Habilitado", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onProviderDisabled(String arg0) {
//                Toast.makeText(getApplicationContext(), "Provider Desabilitado", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onLocationChanged(Location location) {
//
//
//            }
//        };
//
//        locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER , tempo , distancia, this.listener , null );


        //

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        locationLabel.setText("Got connected...");

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        locationLabel.setText("Got disconnected...");
        super.onStop();

    }
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//
//        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        if(mp.getT()){
//            mp.refreshMap(location);
//        }
//    }


    public void doConnect(View view) {
        googleApiClient.connect();
        locationLabel.setText("Got connected...");
    }

    public void doDisconnect(View view) {
        googleApiClient.disconnect();
        locationLabel.setText("Got disconnected...");
    }

    public void getLocation(View view) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        String text = "Location = <" + location.getLatitude() + "," + location.getLongitude() + ">";
        this.locationLabel.setText(text);
        mp.refreshMap(location);
        locs.add(location);
        GetAddressTask task = new GetAddressTask(this);
        task.execute(location);
    }

    public void doSubscribe(View view) {
        if (googleApiClient.isConnected()) {
            LocationRequest request = new LocationRequest();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setInterval(5000);
            request.setSmallestDisplacement(2);
            Toast.makeText(this, "O rastreio está funcionando.", Toast.LENGTH_LONG).show();
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
        }
    }

    public void doUnsubscribe(View view) {
        if (googleApiClient.isConnected()) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions polyLineOptions = new PolylineOptions();
            for(int i=0; i<locs.size();i++){
                double lat = (locs.get(i).getLatitude());
                double lng = (locs.get(i).getLongitude());
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            polyLineOptions.addAll(points);
            polyLineOptions.width(10);
            polyLineOptions.color(Color.BLUE);
            mp.path(polyLineOptions);
            Toast.makeText(this, "Mostrando resultado.", Toast.LENGTH_LONG).show();
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            locs.clear();
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
        if(locs.size()==0){
            locs.add(location);
            mp.addMap(location);
            Toast.makeText(this,"passou aqui",Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this,"Tamanho: "+locs.size(),Toast.LENGTH_SHORT).show();
        if(locs.get(locs.size()-1).distanceTo(location)>=100.0){
            Toast.makeText(this,""+locs.get(locs.size()-1).distanceTo(location),Toast.LENGTH_SHORT).show();
            locs.add(location);
            mp.addMap(location);
        }
//        String text = "Updated Location = <" + location.getLatitude() + "," + location.getLongitude() + ">";

//        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        private Context context;

        public GetAddressTask(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPostExecute(String address) {
            addressLabel.setText(address);
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
