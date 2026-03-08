package com.example.smart_city;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker selectedMarker;
    private Button btnSelectLocation;
    private LatLng selectedLatLng;
    private String selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSelectLocation.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLatLng.latitude);
                resultIntent.putExtra("longitude", selectedLatLng.longitude);
                resultIntent.putExtra("address", selectedAddress);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(MapsActivity.this, "Please select a location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLocation = new LatLng(18.5089, 73.9259); // Updated default location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Add a marker for the default location
        selectedMarker = mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
        selectedLatLng = defaultLocation;

        // Get Address for Default Location
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(defaultLocation.latitude, defaultLocation.longitude, 1);
            if (!addresses.isEmpty()) {
                selectedAddress = addresses.get(0).getAddressLine(0);
                Toast.makeText(this, "Default Location: " + selectedAddress, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnMapClickListener(latLng -> {
            if (selectedMarker != null) {
                selectedMarker.remove();
            }
            selectedLatLng = latLng;
            selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            // Get Address
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (!addresses.isEmpty()) {
                    selectedAddress = addresses.get(0).getAddressLine(0);
                    Toast.makeText(this, "Location: " + selectedAddress, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}