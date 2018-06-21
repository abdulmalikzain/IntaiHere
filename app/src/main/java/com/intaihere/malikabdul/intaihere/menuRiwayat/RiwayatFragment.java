package com.intaihere.malikabdul.intaihere.menuRiwayat;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.adapter.RiwayatAdapter;
import com.intaihere.malikabdul.intaihere.model.ModelRiwayat;
import com.intaihere.malikabdul.intaihere.utils.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private RecyclerView recyclerView;
    private List<ModelRiwayat> modelRiwayats;

    public RiwayatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_riwayat, container, false);

        recyclerView = view.findViewById(R.id.rv_riwayat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        modelRiwayats = new ArrayList<>();
        tampilRiwayat();

        return view;
    }

    private void tampilRiwayat(){
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        final String id = (sharedpreferences.getString("id", ""));
        String url_fotoProfile = Server.URL_GET_REKAM + id;

        StringRequest request = new StringRequest(Request.Method.GET, url_fotoProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String username = object.getString("username");
                    String waktu = object.getString("waktu");
                    String alamat = object.getString("alamat");

                    final ModelRiwayat modelTask = new ModelRiwayat();
                    modelTask.setWaktu(waktu);
                    modelTask.setUsername(username);
                    modelTask.setAlamat(alamat);
                    modelRiwayats.add(modelTask);

                    //creating adapter object and setting it to recyclerview
                    RiwayatAdapter adapter = new RiwayatAdapter(getContext(), modelRiwayats);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
