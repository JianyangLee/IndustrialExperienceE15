package com.example.industrialexperiencee15;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Maps_Facilities extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private GoogleMap mMap;
    TextView tvLocInfo;
    private String sportsPlayedByuser;
    private List<SportsActivitesPOJO> facilitiesList;
    String[] JsonListOfFacilites;
    JSONArray jsonArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the sport value from previous values

        SharedPreferences userSharedPreferenceDetails = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        sportsPlayedByuser = userSharedPreferenceDetails.getString("sportsPlayedByUser", "");

        // fetch from the AWS Server related to the users area of iterest
        facilitiesList = new ArrayList<>();
        Maps_Facilities.GetAllFacilitiesLocationAsync AllFacilitiesLocationAsync = new Maps_Facilities.GetAllFacilitiesLocationAsync();
        AllFacilitiesLocationAsync.execute();

        //Instantiate the Maps in to the Content View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__facilities);

        //Plot the Locations Fetched from Server
        //plotUsersAreasOfInterestInMaps();

        // Map ASync Method
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Facilities_Map);
        mapFragment.getMapAsync(this);

        //Get the user Current Location and Plot the same
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Maps_Facilities.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Maps_Facilities.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fetchLastLocation();

   }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(Maps_Facilities.this, currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Facilities_Map);
                    supportMapFragment.getMapAsync(Maps_Facilities.this);

                    // For zooming automatically to the location of the marker
                    LatLng currentLocationLatLang = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocationLatLang).zoom(14).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLang, 14));

                } else {
                    Toast.makeText(Maps_Facilities.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Maps_Facilities.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        99);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        99);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //googleMap.setTrafficEnabled(true);

        //googleMap.setOnMapLongClickListener();

        //googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());


        // LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        // LatLng latLng = new LatLng(37.8770, 145.0449);
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here");
        // googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //Adding the created the marker on the map
        // googleMap.addMarker(markerOptions);

        //googleMap.setMyLocationEnabled(true);
        //Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

       //plotUsersAreasOfInterestInMaps();

    }


    // this Method take care of placing a  marker in the maps screen once the user long click on any area of the maps
    @Override
    public void onMapLongClick(LatLng point) {

        tvLocInfo.setText("New marker added@" + point.toString());

        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .snippet(point.toString()));

        newMarker.setTitle(newMarker.getId());

    }

    // this Method handles the Permission Request Result from the user in order to plot the current user location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(Maps_Facilities.this, "Location permission missing", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //Method to PLot the places in the graph that is of interest to the user
    private void plotUsersAreasOfInterestInMaps() {

        for (SportsActivitesPOJO eachFacilityLocation : facilitiesList) {
            mMap.addMarker(new MarkerOptions().position(eachFacilityLocation.getLatLangOfFacility()).title(eachFacilityLocation.getNameOfFacility()).snippet(eachFacilityLocation.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        }

    }

    // this get all the facilites that the user is intersted in from the Cloud Server
    private class GetAllFacilitiesLocationAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                String returnValue = RestService.getBySportPlayed(sportsPlayedByuser);
                JSONObject jsnobject = new JSONObject(returnValue);
                jsonArr = jsnobject.getJSONArray("data");
                JsonListOfFacilites = new String[jsonArr.length()];

                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        SportsActivitesPOJO EachFaciltyLocation = new SportsActivitesPOJO();
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        EachFaciltyLocation.setNameOfFacility(obj.getString("FACILITY_NAME"));
                        EachFaciltyLocation.setStreet(obj.getString("STREET_NAME"));
                        EachFaciltyLocation.setSuburb(obj.getString("SUBURB"));
                        EachFaciltyLocation.setPostcode(Integer.parseInt(obj.getString("POSTCODE")));
                        Double eachFacilitylatitude = Double.parseDouble(obj.getString("LATTITUDE"));
                        Double eachFacilitylongitude = Double.parseDouble(obj.getString("LONGITUDE"));
                        EachFaciltyLocation.setLatLangOfFacility(new LatLng(eachFacilitylatitude, eachFacilitylongitude));
                        String Description = EachFaciltyLocation.getStreet()+" ,"+EachFaciltyLocation.getSuburb()+" ,"+EachFaciltyLocation.getPostcode();
                        EachFaciltyLocation.setDescription(Description);
                        facilitiesList.add(EachFaciltyLocation);
                    } catch (Exception e) {
                        Log.e("JSONERROR", "parsing of JSON Response");
                    }
                }

            } catch (Exception e) {

            }

            return "";
        }

        @Override
        protected void onPostExecute(String response) {
plotUsersAreasOfInterestInMaps();
        }
    }

}