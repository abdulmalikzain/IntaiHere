package com.intaihere.malikabdul.intaihere.menuHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intaihere.malikabdul.intaihere.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DetailMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView tvPosisiTujuan, tvHasilDir;
    private LatLng latLngTujuan, latLngPosisi;
    private Toolbar mActionToolbar;
    private FloatingActionButton fabDirCar, fabDirWalk, fabDirBike, fabSms, fabPhone;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyB4t-S7drZDkkmiYDTFy2bc0w1BZ2SZel4";
    private String[] colors = {"#FF3F51B5", "#FF2E2F33", "#FF7B7B7C"};
    private String telephone, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marker);

        tvPosisiTujuan = findViewById(R.id.tv_posisi_tujuan);
        fabDirCar       = findViewById(R.id.fab_dir_car);
        fabDirBike      = findViewById(R.id.fab_dir_bike);
        fabDirWalk      = findViewById(R.id.fab_dir_walk);
        fabSms          = findViewById(R.id.fab_dm_sms);
        fabPhone        = findViewById(R.id.fab_dm_phone);
        tvHasilDir      = findViewById(R.id.tv_hasil_dir);

        Intent intent = this.getIntent();
        String lat =intent.getExtras().getString("TAG_LATITUDE");
        String lng =intent.getExtras().getString("TAG_LONGITUDE");
        username = intent.getExtras().getString("username");
        telephone = intent.getExtras().getString("telephone");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_detail_marker);
        mapFragment.getMapAsync(this);


        latLngTujuan = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

        tvPosisiTujuan.setText(username);

        mActionToolbar = findViewById(R.id.toolbar_detail_marker);
        setSupportActionBar(mActionToolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        requestLocationUpdates();

        fabDirBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDirection();
            }
        });

        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephone();
            }
        });

        fabSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimSms();
            }
        });
    }

    //button back toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
//        googleMap.addMarker(new MarkerOptions().position(latLngTujuan).title(username));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngTujuan, 15f));
    }


    ///////////////titik koordinat LatLng
    private void requestLocationUpdates() {
        @SuppressLint("RestrictedApi")
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        latLngPosisi = new LatLng(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "posisiiiii: "+latLngPosisi);
                    }
                }
            }, null);
        }else {
            Log.d(TAG, "location update not grand");
        }
    }

    /////////////////////
    public void requestDirection() {
        Snackbar.make(fabDirCar, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
//        final LatLng a = new LatLng(-6.9907074,110.4088166);
//        final LatLng b = new LatLng(-6.9920513,110.4196016);

        GoogleDirection.withServerKey(serverKey)
                .from(latLngPosisi)
                .to(latLngTujuan)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {

                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(latLngPosisi));
                            googleMap.addMarker(new MarkerOptions().position(latLngTujuan));
                            Log.d(TAG, "ooooooooooo: "+latLngPosisi);
                            Log.d(TAG, "kikikikikiki: "+latLngTujuan);

                            for (int i = 0; i < direction.getRouteList().size(); i++) {
                                Route route = direction.getRouteList().get(i);
                                Leg leg = route.getLegList().get(0);
                                String color = colors[i % colors.length];
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                googleMap.addPolyline(DirectionConverter.createPolyline(DetailMarkerActivity.this,
                                        directionPositionList, 5, Color.parseColor(color)));

                                setCameraWithCoordinationBounds(route);

                                tvHasilDir.setVisibility(View.VISIBLE);
                                tvHasilDir.setText(String.format("distance = %s , duration = %s"
                                        ,leg.getDistance().getText() , leg.getDuration().getText()));
                            }


                        } else {
//                            Snackbar.make(btnRequestDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    //////////////////telephone
    private void telephone(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +telephone));

        if (ActivityCompat.checkSelfPermission(DetailMarkerActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        }
        startActivity(callIntent);
    }

    ////////////////////SMS
    private void kirimSms(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", telephone,
                null)));
    }

}
