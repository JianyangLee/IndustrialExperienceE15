package com.example.industrialexperiencee15;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


    public class Facilities extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
        private Location currentLocation;
        private FusedLocationProviderClient fusedLocationProviderClient;
        private static final int LOCATION_REQUEST_CODE = 101;
        private GoogleMap googleMap;
        TextView tvLocInfo;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_facilities);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(Facilities.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Facilities.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                return;
            }
            fetchLastLocation();

        }

        private void fetchLastLocation() {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        Toast.makeText(Facilities.this,currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                        SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        supportMapFragment.getMapAsync(Facilities.this);
                    }else{
                        Toast.makeText(Facilities.this,"No Location recorded",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        @Override
        public void onMapReady(GoogleMap googleMap) {



            LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            //Adding the created the marker on the map
            googleMap.addMarker(markerOptions);

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            //googleMap.setMyLocationEnabled(true);
            //Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            //googleMap.setTrafficEnabled(true);

            //googleMap.setOnMapLongClickListener();

            //googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

            //mMap.setMyLocationEnabled(true);
        }

        @Override
        public void onMapLongClick(LatLng point) {

            tvLocInfo.setText("New marker added@" + point.toString());

            Marker newMarker = googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .snippet(point.toString()));

            newMarker.setTitle(newMarker.getId());

        }
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
            switch (requestCode) {
                case LOCATION_REQUEST_CODE:
                    if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                        fetchLastLocation();
                    } else {
                        Toast.makeText(Facilities.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
