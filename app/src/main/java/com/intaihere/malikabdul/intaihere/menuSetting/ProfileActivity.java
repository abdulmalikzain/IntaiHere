package com.intaihere.malikabdul.intaihere.menuSetting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvTelephone, tvAlamat;
    private CircleImageView civFotoProfile;
    private ImageView ivBtnUbahFoto;
    private Button btnUbahProfil;
    private Toolbar mActionToolbar;

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";

    Intent intent;
    Uri fileUri;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;

    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mActionToolbar = findViewById(R.id.toolbarprofile);
        setSupportActionBar(mActionToolbar);
        getSupportActionBar().setTitle("Profile");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tvUsername      = findViewById(R.id.tv_usernameProfil);
        tvEmail         = findViewById(R.id.tv_emailProfil);
        tvTelephone     = findViewById(R.id.tv_telephoneProfil);
        tvAlamat        = findViewById(R.id.tv_alamatProfil);
        civFotoProfile  = findViewById(R.id.civ_fotoProfil);
        ivBtnUbahFoto   = findViewById(R.id.iv_btnFoto);
        btnUbahProfil   = findViewById(R.id.btn_ubahProfil);

        fotoProfile();

        ivBtnUbahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadFotoActivity.class);
                startActivity(intent);
            }
        });

        btnUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UbahProfileActivity.class);
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        String username = (sharedpreferences.getString("username", ""));
        String email    = (sharedpreferences.getString("email", ""));
        String telephone = (sharedpreferences.getString("telephone",""));
        String alamat   = (sharedpreferences.getString("alamat", ""));
//        String image    = (sharedpreferences.getString("image", ""));

        tvUsername.setText(username);
        tvAlamat.setText(alamat);
        tvEmail.setText(email);
        tvTelephone.setText(telephone);

    }


    private void fotoProfile(){
        sharedpreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        final String id = (sharedpreferences.getString("id", ""));
        String url_fotoProfile = Server.URL_DATA_BY + id;

        StringRequest request = new StringRequest(Request.Method.GET, url_fotoProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String FotoProfile = object.getString("image");
                        Picasso.with(getApplication())
                                .load(FotoProfile)
                                .centerCrop()
                                .resize(100,100)
                                .placeholder(R.drawable.man)
                                .into(civFotoProfile);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



}
