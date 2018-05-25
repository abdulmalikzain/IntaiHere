package com.intaihere.malikabdul.intaihere.menuSetting;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;
import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.session_status;
import static com.intaihere.malikabdul.intaihere.utils.Server.URL_UBAH_DATA;

public class UbahProfileActivity extends AppCompatActivity {

    private Toolbar mActionToolbar;
    private EditText etId, etUsername, etEmail, etAlamat, etTelephone;
    private Button btnUbahData;

    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profile);

        mActionToolbar = findViewById(R.id.toolbar_ubahprofil);
        setSupportActionBar(mActionToolbar);
        getSupportActionBar().setTitle("Ubah Data Pengguna");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etUsername  = findViewById(R.id.et_username_ubah);
        etEmail     = findViewById(R.id.et_email_ubah);
        etAlamat    = findViewById(R.id.et_alamat_ubah);
        etTelephone = findViewById(R.id.et_Telephone_ubah);
        btnUbahData = findViewById(R.id.btn_simpan_ubahdata);
        etId        = findViewById(R.id.et_id_ubahdata);

        sharedpreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        String username = (sharedpreferences.getString("username", ""));
        String id = (sharedpreferences.getString("id", ""));
        String email    =(sharedpreferences.getString("email",""));
        String telephone = (sharedpreferences.getString("telephone",""));
        String alamat   = (sharedpreferences.getString("alamat",""));

        etId.setText(id);
        etUsername.setText(username);
        etAlamat.setText(alamat);
        etTelephone.setText(telephone);
        etEmail.setText(email);

        btnUbahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahData();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //method ubah data ke database
    private void ubahData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.show();
        final String id = etId.getText().toString();
        final String username = etUsername.getText().toString();
        final String email = etEmail.getText().toString();
        final String telephone = etTelephone.getText().toString();
        final String alamat = etAlamat.getText().toString();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UBAH_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d( "Respon: ", response.toString());
                            JSONObject obj = new JSONObject(response);

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString("id", id);
                            editor.putString("username", username);
                            editor.putString("email", email);
                            editor.putString("telephone", telephone);
                            editor.putString("alamat", alamat);
                            editor.commit();

                            Toast.makeText(UbahProfileActivity.this, obj.getString("message"),
                                    Toast.LENGTH_LONG).show();
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                            Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("username", username);
                params.put("email", email);
                params.put("telephone", telephone);
                params.put("alamat", alamat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
