package com.intaihere.malikabdul.intaihere.menuHome;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intaihere.malikabdul.intaihere.R;

import java.util.ArrayList;

public class DetailMarkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marker);

        String fotoStatus;
//        Bundle bundle = getIntent().getExtras();
//        fotoStatus = bundle.getString("l");
        Intent intent = this.getIntent();
//        String username = intent.getExtras().getString("a");
        String lat =intent.getExtras().getString("TAG_LATITUDE");
        String lng =intent.getExtras().getString("TAG_LONGITUDE");
        Toast.makeText(this, "lllllllll"+lat, Toast.LENGTH_SHORT).show();
    }

//    public void requestDirection() {
//        Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
//        GoogleDirection.withServerKey(serverKey)
//                .from(latLngFrom)
//                .to(latLngTo)
//                .transportMode(TransportMode.DRIVING)
//                .alternativeRoute(true)
//                .execute(new DirectionCallback() {
//                    @Override
//                    public void onDirectionSuccess(Direction direction, String rawBody) {
//                        if (direction.isOK()) {
//
//                            googleMap.clear();
//                            googleMap.addMarker(new MarkerOptions().position(latLngFrom));
//                            googleMap.addMarker(new MarkerOptions().position(latLngTo));
//                            Log.d(TAG, "ooooooooooo: "+latLngTo);
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
//                                btnRequestDirection.setVisibility(View.GONE);
//                                tvhasil.setVisibility(View.VISIBLE);
//                                tvhasil.setText(String.format("distance = %s , duration = %s"
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
//        Log.d(TAG, "requestDirectionnnnn: "+latLngTo);
//    }
//
//    private void setCameraWithCoordinationBounds(Route route) {
//        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
//        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
//        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
//    }
}
