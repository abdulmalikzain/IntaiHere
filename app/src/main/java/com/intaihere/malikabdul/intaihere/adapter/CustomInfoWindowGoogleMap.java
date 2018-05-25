package com.intaihere.malikabdul.intaihere.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.menuHome.HomeFragment;
import com.intaihere.malikabdul.intaihere.menuStatus.DetailStatusActivity;
import com.intaihere.malikabdul.intaihere.model.InfoWindowData;

import static com.android.volley.VolleyLog.TAG;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.layout_custom_infowindow, null);

        TextView username = view.findViewById(R.id.tv_iw_username);
        TextView alamat = view.findViewById(R.id.tv_iw_alamat);

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();


        username.setText(marker.getTitle());
        alamat.setText(infoWindowData.getAlamat());

        return view;
    }
}
