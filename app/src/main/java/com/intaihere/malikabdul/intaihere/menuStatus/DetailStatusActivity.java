package com.intaihere.malikabdul.intaihere.menuStatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.intaihere.malikabdul.intaihere.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailStatusActivity extends AppCompatActivity {
    private TextView tvDetUsername, tvDetWaktu, tvDetStatus, tvDetTujuan, tvFotoStatus;
    private ImageView ivFotoStatus;
    private String fotoStatus, fotoImage;
    private String TAG = "";
    Toolbar mActionToolbar;
    private CircleImageView civFotoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_status);

        tvDetUsername   = findViewById(R.id.tv_status_detUsername);
        tvDetWaktu      = findViewById(R.id.tv_status_detWaktu);
        tvDetStatus     = findViewById(R.id.tv_status_detStatus);
        tvDetTujuan     = findViewById(R.id.tv_status_detTujuan);
        ivFotoStatus    = findViewById(R.id.iv_status_detailgambar);
        civFotoUser     = findViewById(R.id.civ_status_detailfotouser);

        Bundle bundle = getIntent().getExtras();
        tvDetUsername.setText(bundle.getString("username"));
        tvDetWaktu.setText(bundle.getString("waktu"));
        tvDetStatus.setText(bundle.getString("status"));
        tvDetTujuan.setText(bundle.getString("tujuan"));
        fotoStatus = bundle.getString("foto_status");
        Picasso.with(getApplication()).load(fotoStatus).error(R.drawable.man)
                .into(ivFotoStatus);

        mActionToolbar = findViewById(R.id.tabsdetailstatus);
        setSupportActionBar(mActionToolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    //button back toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
