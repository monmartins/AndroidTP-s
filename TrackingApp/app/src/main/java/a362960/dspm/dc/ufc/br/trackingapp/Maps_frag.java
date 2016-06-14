package a362960.dspm.dc.ufc.br.trackingapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by root on 09/06/16.
 */
public class Maps_frag extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment supportMapFragment;
    private View rootview;
    private GoogleMap googleMap;
    private boolean trava = false;


    public boolean getT(){
        return trava;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        trava = true;
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supportMapFragment = SupportMapFragment.newInstance();
        rootview = inflater.inflate(R.layout.activity_maps, null);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.map, supportMapFragment).commitAllowingStateLoss();
        }
        supportMapFragment.getMapAsync(this);
        return rootview;
    }
    public void refreshMap(Location lc){
        this.googleMap.clear();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng location = new LatLng(lc.getLatitude(), lc.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(location).title("Você está aqui");
        this.googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    public void addMap(Location lc){
        LatLng location = new LatLng(lc.getLatitude(), lc.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(location).title("Você está aqui");
        this.googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    public void clearMap(Location lc){
        this.googleMap.clear();
    }
    public void path(PolylineOptions pl){
        this.googleMap.addPolyline(pl);
    }


}
