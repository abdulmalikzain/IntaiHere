package com.intaihere.malikabdul.intaihere.menuGrup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.utils.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailAnggotaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView thumb_image;
    private TextView email, telephone, alamat;
    private String titleNama, id_news, stringTelephone;
    private CollapsingToolbarLayout collapsingToolbar;
    private static final String TAG = DetailAnggotaActivity.class.getSimpleName();
    private FloatingActionMenu famHubungi;
    private FloatingActionButton fabSms, fabTelepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anggota);

        toolbar     = findViewById(R.id.toolbar_detailanggota);
        thumb_image = findViewById(R.id.backdrop);
        email       = findViewById(R.id.tv_emailDG);
        telephone   = findViewById(R.id.tv_telephoneDG);
        alamat      = findViewById(R.id.tv_alamatDG);
        famHubungi  = findViewById(R.id.fam_hubungi);
        fabSms      = findViewById(R.id.fab_anggota_sms);
        fabTelepon  = findViewById(R.id.fab_anggota_telepon);

        famHubungi.setClosedOnTouchOutside(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        id_news = getIntent().getStringExtra("id");
        Log.d(TAG, "ssssss: "+titleNama);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        callDetailAnggota(id_news);

        fabTelepon.setOnClickListener(new View.OnClickListener() {
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

    private void callDetailAnggota(final String id){

        StringRequest strReq = new StringRequest(Request.Method.POST, Server.URL_DETAIL_GRUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());

                try {
                    JSONObject obj = new JSONObject(response);

                    titleNama           = obj.getString("username");
                    String Gambar      = obj.getString("image");
                    String Email       = obj.getString("email");
                    stringTelephone   = obj.getString("telephone");
                    String Alamat      = obj.getString("alamat");

                    collapsingToolbar.setTitle(titleNama);
                    email.setText(Email);
                    alamat.setText(Alamat);
                    telephone.setText(stringTelephone);
                    Picasso.with(getApplication()).load(Gambar)
                            .error(R.drawable.man).into(thumb_image);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(DetailAnggotaActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //////////////////telephone
    private void telephone(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +stringTelephone));

        if (ActivityCompat.checkSelfPermission(DetailAnggotaActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        }
        startActivity(callIntent);
    }

    ////////////////////SMS
    private void kirimSms(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", stringTelephone,
                null)));
    }

}
