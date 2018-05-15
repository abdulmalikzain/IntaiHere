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

//        TextView hotel_tv = view.findViewById(R.id.hotels);
//        TextView food_tv = view.findViewById(R.id.food);
//        TextView transport_tv = view.findViewById(R.id.transport);

//        name_tv.setText(marker.getTitle());
//        details_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

//        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
//                "drawable", context.getPackageName());
//        img.setImageResource(imageId);

        username.setText(marker.getTitle());
        alamat.setText(infoWindowData.getAlamat());

//        LatLng latLng         = new LatLng(infoWindowData.getLatitude(), infoWindowData.getLongitude());
//        Log.d(TAG, "bismillahhhhh: "+latLng);
//        marker.setPosition(latLng);
//        marker.setIcon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));


        return view;
    }
}
