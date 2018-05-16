package com.intaihere.malikabdul.intaihere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intaihere.malikabdul.intaihere.adapter.CustomInfoWindowAdapter;
import com.intaihere.malikabdul.intaihere.adapter.CustomInfoWindowGoogleMap;
import com.intaihere.malikabdul.intaihere.adapter.PlaceAutocompleteAdapter;
import com.intaihere.malikabdul.intaihere.menuGrup.GrupFragment;
import com.intaihere.malikabdul.intaihere.menuHome.DetailMarkerActivity;
import com.intaihere.malikabdul.intaihere.menuHome.HomeFragment;
import com.intaihere.malikabdul.intaihere.menuSetting.SettingFragment;
import com.intaihere.malikabdul.intaihere.menuStatus.StatusFragment;
import com.intaihere.malikabdul.intaihere.model.InfoWindowData;
import com.intaihere.malikabdul.intaihere.model.PlaceInfo;
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
    private Marker marker;

    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionMenu menuRed;
    private AutoCompleteTextView mSearchText;
    private static final float DEFAULT_ZOOM = 15f;
    private PlaceInfo mPlace;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsiki);
        mapFragment.getMapAsync(this);

        relativeLayout = findViewById(R.id.rel_home);
        frameLayout = findViewById(R.id.fragment_container);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        mSearchText = findViewById(R.id.input_search);

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



        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
//                    geoLocate();
                }

                return false;
            }
        });

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

    ///////////////////////////////dialog gps
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

    ////////////////////////// Fungsi get marker
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

                        getAddress(latLng.latitude, latLng.longitude, username, telephone);
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

    public void getAddress(double LATITUDE, double LONGITUDE, String useraname, String telephone) {
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
                markerOptions.title(useraname);
                markerOptions.snippet(telephone);
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
                        intent.putExtra("username", marker.getTitle());
                        intent.putExtra("telephone", marker.getSnippet());

                        startActivity(intent);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
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


    /////////////////////////////////////////mitch tabian


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                marker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();
    }

}
