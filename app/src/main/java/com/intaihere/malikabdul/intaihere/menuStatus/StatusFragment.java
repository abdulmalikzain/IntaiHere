package com.intaihere.malikabdul.intaihere.menuStatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.adapter.StatusAdapter;
import com.intaihere.malikabdul.intaihere.model.ModelStatus;
import com.intaihere.malikabdul.intaihere.utils.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;

public class StatusFragment extends Fragment {

    private static final String TAG = "" ;
    private RecyclerView recyclerView;
    private ImageView ivFotoProfile;
    private LinearLayout linearLayout;

    private List<ModelStatus> modelTasks;

    private SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        ivFotoProfile = view.findViewById(R.id.civ_profilestatus);
        linearLayout = view.findViewById(R.id.ll_buatstatus);

        recyclerView = view.findViewById(R.id.rv_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        modelTasks = new ArrayList<>();

        tampilStatus();

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InputStatusActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        final String image = (sharedPreferences.getString("image", ""));
        if (!image.equals("")){
            Picasso.with(getContext()).load(image).error(R.drawable.man).into(ivFotoProfile);
        }else {
            Picasso.with(getContext()).load(Server.URS_GET_IMAGEDEFAULT)
                    .centerCrop()
                    .resize(50,50)
                    .error(R.drawable.man).into(ivFotoProfile);
        }


        return view;
    }

    private void tampilStatus(){
        String url_getTask = Server.URL_GET_TASK;
        StringRequest requestTampil = new StringRequest(Request.Method.GET, url_getTask, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("task");
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        String dataDate = data.getString("waktu");
                        String Nama = data.getString("username");
//                        String image = data.getString("image");
                        String status = data.getString("status");
                        String tujuan = data.getString("tujuan");
                        String foto_status = data.getString("foto_status");
                        covertTimeToText(dataDate, Nama,  status, tujuan, foto_status);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(requestTampil);
    }

    public String covertTimeToText(String dataDate, String Nama, String status, String tujuan, String foto_status) {

        String convTime = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long detik = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long menit = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long jam   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long hari  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (detik < 60) {
                convTime = detik+ "detik lalu";
            } else if (menit < 60) {
                convTime = menit+ "menit lalu";
            } else if (jam < 24) {
                convTime = jam+ "jam lalu";
            } else if (hari >= 7) {
                if (hari > 30) {
                    convTime = (hari / 30)+ "bulan lalu";
                } else if (hari > 360) {
                    convTime = (hari / 360)+ "tahun lalu";
                } else {
                    convTime = (hari / 7) + "minggu lalu";
                }
            } else if (hari < 7) {
                convTime = hari+ "hari lalu";
            }

            final ModelStatus modelTask = new ModelStatus();
            modelTask.setWaktu(convTime);
            modelTask.setUsername(Nama);
//            modelTask.setGambar(image);
            modelTask.setStatus(status);
            modelTask.setTujuan(tujuan);
            modelTask.setFoto_status(foto_status);
            modelTasks.add(modelTask);

            //creating adapter object and setting it to recyclerview
            StatusAdapter adapter = new StatusAdapter(getContext(), modelTasks);
            recyclerView.setAdapter(adapter);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;

    }

}