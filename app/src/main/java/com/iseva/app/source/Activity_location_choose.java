package com.iseva.app.source;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class Activity_location_choose extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    GoogleMap googleMap;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    Location location; // location
    double latitude = 0; // latitude
    double longitude = 0; // longitude
    TextView search_textview;

    int REQUEST_SELECT_PLACE = 1;

    int LocationEnable = 1;

    private Button location_filal_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choose);
        init();
        set_on_click_listner();


    }

    public void init() {
        search_textview = (TextView) findViewById(R.id.search_txtview);
        location_filal_btn = (Button)findViewById(R.id.location_filal_btn);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checklocationenable();
    }

    public void set_on_click_listner()
    {
        search_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(Activity_location_choose.this);
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                Activity_Category_Choose.latitute = latLng.latitude;
                Activity_Category_Choose.longitude = latLng.longitude;
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title("Selected Point");

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
            }
        });

        location_filal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_location_choose.this.finish();
            }
        });

    }

    public void checklocationenable() {
        LocationManager lm = (LocationManager) Activity_location_choose.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            LocationEnable = 0;

            AlertDialog.Builder dialog = new AlertDialog.Builder(Activity_location_choose.this);
            dialog.setTitle("Enable Location");
            dialog.setMessage("Please Enable Location to show current location on map.");
            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(myIntent, 2);

                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    set_location_on_map();
                }
            });
            dialog.show();
        } else if (network_enabled) {
            if (lm != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                }
                location = lm
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    set_location_on_map();
                }

            }
        }
        else if(gps_enabled)
        {
            if (lm != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    location = lm
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        set_location_on_map();
                    }
                }

            }
        }
    }


    public void set_location_on_map()
    {

        Activity_Category_Choose.latitute = latitude;
        Activity_Category_Choose.longitude = longitude;

        LatLng coordinate = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(coordinate);

        // Setting the title for the marker.
        // This will be displayed on taping the marker


        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        // googleMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
        if(latitude !=0 && longitude != 0)
        {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));
        }


        // Placing a marker on the touched position
        googleMap.addMarker(markerOptions);
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);

                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                set_location_on_map();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if(requestCode == 2)
        {
            checklocationenable();
        }
    }
}
