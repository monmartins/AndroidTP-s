package a362960.dspm.dc.ufc.br.androidlocation1;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 09/06/16.
 */
public class Maps_frag extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment supportMapFragment;
    private View rootview;
    private GoogleMap googleMap;
    private static int numeroPonto =0;
    private boolean trava = false;
    private float max;
    private boolean estaDentro;
    private List<Double> raioP = new ArrayList<Double>();

    private List<Location> locs = new ArrayList<Location>();


    public int getP(){
        return numeroPonto;
    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        trava = true;
        this.googleMap = googleMaps;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if(numeroPonto<4){
            this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    googleMap.addMarker(new MarkerOptions().position(point).title("Ponto:"+ numeroPonto).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    Location loc = new Location("dummyprovider");
                    loc.setLatitude(point.latitude);
                    loc.setLongitude(point.longitude);
                    locs.add(loc);
                    numeroPonto++;
                }
            });
        }


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
    public void clearMap(){
        this.googleMap.clear();
        numeroPonto = 0;
        locs.clear();
        raioP.clear();
    }
    public void path(PolylineOptions pl){
        this.googleMap.addPolyline(pl);
    }

    public boolean verificar(Location lc){
        for(int i=0;i<4;i++){
            max=-9999;
            for(int j=0;j<4;j++){
                if(max<locs.get(i).distanceTo(locs.get(j))){
                    max = locs.get(i).distanceTo(locs.get(j));
                }
            }
            raioP.add((double)max);
        }
        estaDentro = true;
        for(int i=0;i<4;i++){
            if(locs.get(i).distanceTo(lc)>raioP.get(i)){
                estaDentro = false;
            }
        }
        return estaDentro;

    }



}
