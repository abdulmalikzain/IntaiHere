package com.intaihere.malikabdul.intaihere.menuGrup;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    private String title, id_news;
    private static final String TAG = DetailAnggotaActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anggota);

        toolbar     = findViewById(R.id.toolbar);
        thumb_image = findViewById(R.id.backdrop);
        email       = findViewById(R.id.tv_emailDG);
        telephone   = findViewById(R.id.tv_telephoneDG);
        alamat      = findViewById(R.id.tv_alamatDG);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        id_news = getIntent().getStringExtra("id");

        callDetailAnggota(id_news);
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

                    title              = obj.getString("username");
                    String Gambar      = obj.getString("image");
                    String Email       = obj.getString("email");
                    String Telephone   = obj.getString("telephone");
                    String Alamat      = obj.getString("alamat");

                    email.setText(Email);
                    alamat.setText(Alamat);
                    telephone.setText(Telephone);
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

}
