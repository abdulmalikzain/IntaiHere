package com.intaihere.malikabdul.intaihere.menuHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.utils.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;

public class DetailMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView tvPosisiTujuan, tvHasilDir;
    private LatLng latLngTujuan;
    private LatLng latLngPosisi;
    private LatLng latLngTujuanMobil;
    private LatLng latLngTujuanBike;
    private ProgressDialog progressDialog;
    private Toolbar mActionToolbar;
    private FloatingActionButton fabDirCar, fabDirWalk, fabDirBike, fabSms, fabPhone, fabDmRiwayat;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyBpmvzj6M8tfy9-VrD80oq70qmRbC4lb2Q";
    private String[] colors = {"#FF3F51B5", "#FF2E2F33", "#FF7B7B7C"};
    private String telephone, username, alamat, id;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marker);

        tvPosisiTujuan = findViewById(R.id.tv_posisi_tujuan);
        fabDirCar       = findViewById(R.id.fab_dir_car);
//        fabDirBike      = findViewById(R.id.fab_dir_bike);
        fabDirWalk      = findViewById(R.id.fab_dir_walk);
        fabSms          = findViewById(R.id.fab_dm_sms);
        fabPhone        = findViewById(R.id.fab_dm_phone);
        tvHasilDir      = findViewById(R.id.tv_hasil_dir);
        fabDmRiwayat    = findViewById(R.id.fab_dm_riwayat);

        Intent intent = this.getIntent();
        String lat =intent.getExtras().getString("TAG_LATITUDE");
        String lng =intent.getExtras().getString("TAG_LONGITUDE");
        username = intent.getExtras().getString("username");
        telephone = intent.getExtras().getString("telephone");
        alamat = intent.getExtras().getString("alamat");
        sharedpreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        id = (sharedpreferences.getString("id", ""));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_detail_marker);
        mapFragment.getMapAsync(this);

        latLngTujuan        = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        latLngTujuanBike    = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        latLngTujuanMobil   = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        tvPosisiTujuan.setText(username);

        mActionToolbar = findViewById(R.id.toolbar_detail_marker);
        setSupportActionBar(mActionToolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        requestLocationUpdates();

        fabDirWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDirectionJalan();
            }
        });
//        fabDirBike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestDirectionSepeda();
//            }
//        });
        fabDirCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDirectionMobil();
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

        fabDmRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rekamPosisi();
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //This line will show your current location on Map with GPS dot
        googleMap.setMyLocationEnabled(true);

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
                        latLngPosisi = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            }, null);
        }else {
            Log.d(TAG, "location update not grand");
        }
    }


            /////////////////////request direction
    public void requestDirectionMobil() {
        Snackbar.make(fabDirCar, "Sedang mengarahkan...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(latLngPosisi)
                .to(latLngTujuanMobil)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {

                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(latLngTujuanMobil).title(username));

                            for (int i = 0; i < direction.getRouteList().size(); i++) {
                                Route route = direction.getRouteList().get(i);
                                Leg leg = route.getLegList().get(0);
                                String color = colors[i % colors.length];
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                googleMap.addPolyline(DirectionConverter.createPolyline(DetailMarkerActivity.this,
                                        directionPositionList, 5, Color.parseColor(color)));

                                setCameraWithCoordinationBounds(route);

                                tvHasilDir.setVisibility(View.VISIBLE);
                                tvHasilDir.setText(String.format("jarak tempuh = %s , waktu tempuh= %s"
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

//    public void requestDirectionSepeda() {
//        Snackbar.make(fabDirBike, "Sedang mengarahkan...", Snackbar.LENGTH_SHORT).show();
//        GoogleDirection.withServerKey(serverKey)
//                .from(latLngPosisi)
//                .to(latLngTujuanBike)
//                .transportMode(TransportMode.BICYCLING)
//                .alternativeRoute(true)
//                .execute(new DirectionCallback() {
//                    @Override
//                    public void onDirectionSuccess(Direction direction, String rawBody) {
//                        if (direction.isOK()) {
//
//                            googleMap.clear();
//                            googleMap.addMarker(new MarkerOptions().position(latLngTujuanBike).title(username));
//
//                            for (int i = 0; i < direction.getRouteList().size(); i++) {
//                                Route route = direction.getRouteList().get(i);
//                                Leg leg = route.getLegList().get(0);
//                                String color = colors[i % colors.length];
//                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
//                                googleMap.addPolyline(DirectionConverter.createPolyline(DetailMarkerActivity.this,
//                                        directionPositionList, 5, Color.parseColor(color)));
//
//                                setCameraWithCoordinationBounds(route);
//
//                                tvHasilDir.setVisibility(View.VISIBLE);
//                                tvHasilDir.setText(String.format("jarak tempuh = %s , waktu tempuh = %s"
//                                        ,leg.getDistance().getText() , leg.getDuration().getText()));
//                            }
//
//
//                        } else {
////                            Snackbar.make(btnRequestDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onDirectionFailure(Throwable t) {
//
//                    }
//                });
//    }

    public void requestDirectionJalan() {
        Snackbar.make(fabDirWalk, "Sedang mengarahkan...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(latLngPosisi)
                .to(latLngTujuan)
                .transportMode(TransportMode.WALKING)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {

                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(latLngTujuan).title(username));

                            for (int i = 0; i < direction.getRouteList().size(); i++) {
                                Route route = direction.getRouteList().get(i);
                                Leg leg = route.getLegList().get(0);
                                String color = colors[i % colors.length];
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                googleMap.addPolyline(DirectionConverter.createPolyline(DetailMarkerActivity.this,
                                        directionPositionList, 5, Color.parseColor(color)));

                                setCameraWithCoordinationBounds(route);

                                tvHasilDir.setVisibility(View.VISIBLE);
                                tvHasilDir.setText(String.format("jarak tempuh = %s , waktu tempuh= %s"
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


    private void rekamPosisi(){

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dateNow = dateFormat.format(date);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan posisi...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        StringRequest sr = new StringRequest(Request.Method.POST,Server.URL_INSERT_REKAM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("message");
                    progressDialog.dismiss();
                    Toast.makeText(DetailMarkerActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_user", id);
                params.put("username", username);
                params.put("waktu", dateNow);
                params.put("alamat", alamat);

                return params;
            }

        };
        queue.add(sr);
    }

}
