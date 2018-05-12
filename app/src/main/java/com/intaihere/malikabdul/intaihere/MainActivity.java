package com.intaihere.malikabdul.intaihere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intaihere.malikabdul.intaihere.adapter.CustomInfoWindowGoogleMap;
import com.intaihere.malikabdul.intaihere.menuGrup.GrupFragment;
import com.intaihere.malikabdul.intaihere.menuHome.DetailMarkerActivity;
import com.intaihere.malikabdul.intaihere.menuHome.HomeFragment;
import com.intaihere.malikabdul.intaihere.menuSetting.SettingFragment;
import com.intaihere.malikabdul.intaihere.menuStatus.DetailStatusActivity;
import com.intaihere.malikabdul.intaihere.menuStatus.StatusFragment;
import com.intaihere.malikabdul.intaihere.model.InfoWindowData;
import com.intaihere.malikabdul.intaihere.utils.BottomNavigationViewHelper;
import com.intaihere.malikabdul.intaihere.utils.Server;
import com.intaihere.malikabdul.intaihere.utils.ServiceUpdateLokasi;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private FrameLayout frameLayout;
    private RelativeLayout relativeLayout;
    private static final String TAG = "MainActivity";
    boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LocationRequest request;
    private boolean mRequestingLocationUpdates;
    private View mapView;
    private static final int PERMISSIONS_REQUEST = 1;
    private SupportMapFragment mapFragment;
    private SharedPreferences sharedpreferences;
    private LatLng latLng;
    private String image;
    private CircleImageView civBtnMarker;
    private Marker marker;

    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionMenu menuRed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsiki);
        mapFragment.getMapAsync(this);

        relativeLayout = findViewById(R.id.rel_home);
        frameLayout = findViewById(R.id.fragment_container);
        civBtnMarker = findViewById(R.id.civ_btn_marker);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);

        menuRed = findViewById(R.id.menu_red);
        menuRed.setClosedOnTouchOutside(true);
        fab3.setEnabled(false);

        setupBottomNavigationView();

        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission

        startTrackerService();

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMarkers();
                menuRed.setClosedOnTouchOutside(true);
                fab3.setEnabled(true);
                Toast.makeText(MainActivity.this, "Menampilkan Marker", Toast.LENGTH_SHORT).show();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                fab3.setEnabled(false);
                Toast.makeText(MainActivity.this, "Marker dihapus", Toast.LENGTH_SHORT).show();
            }
        });

//        civBtnMarker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMarkers();
//            }
//        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener botnav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.fragment_container, new HomeFragment()).commit();
                    relativeLayout.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    return true;

                case R.id.navigation_status:
                    transaction.replace(R.id.fragment_container, new StatusFragment()).commit();
                    relativeLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_grup:
                    transaction.replace(R.id.fragment_container, new GrupFragment()).commit();
                    relativeLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_setting:
                    transaction.replace(R.id.fragment_container, new SettingFragment()).commit();
                    relativeLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.navigasi);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);

        BottomNavigationView BNV = findViewById(R.id.navigasi);
        BNV.setOnNavigationItemSelectedListener(botnav);

    }

    //////////////BECK PRESED
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan kembali lagi untuk keluar.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    ///////////////////service update lokasi
    private void startTrackerService() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, ServiceUpdateLokasi.class));
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
        //finish();
    }

    ///////////////////////////////
    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                showSettingDialog();
                setupLocationManager();
            }
        } else {
            showSettingDialog();
            setupLocationManager();
        }
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    @SuppressLint("RestrictedApi")
    private void showSettingDialog() {
        request = new LocationRequest();
        request.setSmallestDisplacement(10);
        request.setFastestInterval(50000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(3);
        request.setInterval(30 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        setInitialLocation();
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
//                        updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
//                        updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        setInitialLocation();
                        mRequestingLocationUpdates = true;
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
//                        updateGPSStatus("GPS is Disabled in your device");
                        mRequestingLocationUpdates = false;
                        break;

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
//                    updateGPSStatus("GPS is Enabled in your device");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
//                    updateGPSStatus("GPS is Disabled in your device");
                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };


    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (googleApiClient == null) {
                        initGoogleAPIClient();
//                        showSettingDialog();
                        setupLocationManager();
                    }
                } else {
//                    updateGPSStatus("Location Permission denied.");
                    Toast.makeText(MainActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //This line will show your current location on Map with GPS dot
        mMap.setMyLocationEnabled(true);

        //layout button my postioon
        mapView = mapFragment.getView();
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void setupLocationManager() {
        //buildGoogleApiClient();
        if (googleApiClient == null) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            //mGoogleApiClient = new GoogleApiClient.Builder(this);
        }
        googleApiClient.connect();
//        showSettingDialog();
    }

    private void setInitialLocation() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //getMarkers();

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mLastLocation = location;

                Log.d(TAG, "setInitialLocation:moving camera to: lat: " + location.getLatitude() + ", lng: " + location.getLongitude());
                try {
                    LatLng positionUpdate = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(positionUpdate, 15);
                    mMap.animateCamera(update);

//                    sendLatitudeLongitude();
                } catch (Exception ex) {

                    ex.printStackTrace();
                    Log.e("MapException", ex.getMessage());

                }
            }

        });
    }

    // Fungsi get JSON marker
    public void getMarkers() {
        sharedpreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        final String id = (sharedpreferences.getString("id", ""));
        StringRequest strReq = new StringRequest(Request.Method.GET, Server.URS_GET_MARKERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("wisata");
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        image = jsonObject.getString("image");
                        String username = jsonObject.getString("username");
                        String telephone = jsonObject.getString("telephone");
                        String id_user = jsonObject.getString("id");
                        latLng = new LatLng(Double.parseDouble(jsonObject.getString("latitude")),
                                Double.parseDouble(jsonObject.getString("longitude")));

                        getAddress(latLng.latitude, latLng.longitude);
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void getAddress(double LATITUDE, double LONGITUDE) {
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                InfoWindowData info = new InfoWindowData();
                info.setAlamat(address);
                info.setLatitude(LATITUDE);
                info.setLongitude(LONGITUDE);
                LatLng latLng = new LatLng(LATITUDE, LONGITUDE);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(address);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MainActivity.this)));

                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                mMap.setInfoWindowAdapter(customInfoWindow);

                marker = mMap.addMarker(markerOptions);
                marker.setTag(info);
                marker.showInfoWindow();
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MainActivity.this, DetailMarkerActivity.class);
                        Double lat = marker.getPosition().latitude;
                        Double lng = marker.getPosition().longitude;
                        intent.putExtra("TAG_LATITUDE", Double.toString(lat));
                        intent.putExtra("TAG_LONGITUDE", Double.toString(lng));

                        startActivity(intent);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    private void addMarker() {


//

//        InfoWindowData info = new InfoWindowData();
//        String alamat = info.getAlamat();
//        double lat = info.getLatitude();
//        Log.d(TAG, "addMarker: "+alamat+"ss"+lat);
//        latLng1 = new LatLng(info.getLatitude(), info.getLongitude());
//        info.setAlamat(address);

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng1);
//        markerOptions.title(title);
//        markerOptions.snippet(jalan);
//        Log.d(TAG, "aaaaallll: "+address +"aaa"+latLng1);

//        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
//        mMap.setInfoWindowAdapter(customInfoWindow);
//
//        marker = mMap.addMarker(markerOptions);
//        m.setTag(info);
//        m.showInfoWindow();


//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {

//                Intent intent = new Intent(MainActivity.this, HomeFragment.class);
//                intent.putExtra("TAG_TELEPHONE", marker.getPosition());
//                intent.putExtra("TAG_NAME", marker.getTitle());

//                TextView txtclose, poptelephone, popname;
//                CircleImageView ictelephone;
//                dialog.setContentView(R.layout.layout_popup_infowindow);

//                String username = intent.getExtras().getString("TAG_NAME");
//                popname = (TextView) dialog.findViewById(R.id.popname);
//                popname.setText(username);

//                final String telephone = intent.getExtras().getString("TAG_TELEPHONE");
//                poptelephone = (TextView) dialog.findViewById(R.id.popuptelephone);
//                poptelephone.setText(telephone);

//                txtclose = (TextView) dialog.findViewById(R.id.cancelpopup);
//                txtclose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

//                ictelephone = (CircleImageView) dialog.findViewById(R.id.ictelephone);
//                ictelephone.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View arg0) {
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        callIntent.setData(Uri.parse("tel:" +telephone));
//
//                        if (ActivityCompat.checkSelfPermission(HomeActivity.this,
//                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        startActivity(callIntent);
//                    }
//                });

//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
    }

    public Bitmap createCustomMarker(Context context) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_icon_marker, null);

        CircleImageView circleImageViewmarker = marker.findViewById(R.id.user_dp);

        try {
            URL url = new URL(image);
//            Toast.makeText(mContext, "url : " + url, Toast.LENGTH_SHORT).show();
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            Picasso.with(context)
                    .load(String.valueOf(url))
                    .error(R.drawable.man)
                    .into(circleImageViewmarker);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

}
