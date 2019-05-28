package com.e.googlemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import model.LatitudeLongitude;

public class GoogleSearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_search);

        //  Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etCity.getText().toString())){
                    etCity.setError("Please enter a place name");
                    return;
                }

                // Get the current location of the place
                int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(GoogleSearchActivity.this, "Location not found by name: "
                            + etCity.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This function will fill arraylist with static data and set autocompleteview with the marker
    private void fillArrayListAndSetAdapter() {
        latitudeLongitudeList = new ArrayList<>();
        latitudeLongitudeList.add(new LatitudeLongitude(27.7134481, 85.32419222, "Naagpokhari"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7181749, 85.3173212, "Narayanhiti Palace Museum"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7127827, 85.3265391, "Hotel Brihaspati"));

        String[] data = new String[latitudeLongitudeList.size()];
        for (int i =0; i <data.length; i++){
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                GoogleSearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);


    }

    // This function will check whether the location is in list or not
    private int SearchArrayList(String name) {
        for(int i = 0; i < latitudeLongitudeList.size(); i++){
            if (latitudeLongitudeList.get(i).getMarker().contains(name)){
                return i;
            }

        }
        return -1;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Load Kathmandu city when application launches

        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.7172453, 85.3239605));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }


    private void loadMap(int position) {
        // Remove old marker from map
        if(markerName != null)
        {
            markerName.remove();
        }
        double latitude = latitudeLongitudeList.get(position).getLat();
        double longitude = latitudeLongitudeList.get(position).getLon();
        String marker = latitudeLongitudeList.get(position).getMarker();

        center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
                longitude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
}
