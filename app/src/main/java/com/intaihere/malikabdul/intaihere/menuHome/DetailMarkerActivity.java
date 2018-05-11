package com.intaihere.malikabdul.intaihere.menuHome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.intaihere.malikabdul.intaihere.R;

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
}
