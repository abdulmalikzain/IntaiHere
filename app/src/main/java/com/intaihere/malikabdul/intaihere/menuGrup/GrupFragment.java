package com.intaihere.malikabdul.intaihere.menuGrup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.adapter.AnggotaAdapter;
import com.intaihere.malikabdul.intaihere.model.ModelAnggota;
import com.intaihere.malikabdul.intaihere.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GrupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView list;
    private SwipeRefreshLayout swipe;
    List<ModelAnggota> anggotaList = new ArrayList<ModelAnggota>();

    private static String url_list   = Server.URL + "getusers.php?offset=";

    private static final String TAG = "";

    private int offSet = 0;

    private int no;

    private AnggotaAdapter anggotaAdapter;

    public static final String TAG_NO       = "no";
    public static final String TAG_ID       = "id";
    public static final String TAG_USER    = "username";
    public static final String TAG_EMAIL      = "email";
    public static final String TAG_TELEPHONE      = "telephone";
    public static final String TAG_GAMBAR   = "image";
    public static final String TAG_ALAMAT   = "alamat";

    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grup, container, false);

        swipe = view.findViewById(R.id.swipe_refresh_anggota);
        list = view.findViewById(R.id.list_view_anggota);
        anggotaList.clear();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getContext(), DetailAnggotaActivity.class);
                intent.putExtra(TAG_ID, anggotaList.get(position).getId());
                startActivity(intent);
            }
        });

        anggotaAdapter= new AnggotaAdapter(getActivity(), anggotaList);
        list.setAdapter(anggotaAdapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           anggotaList.clear();
                           anggotaAdapter.notifyDataSetChanged();
                           callAnggota(0);
                       }
                   }
        );

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            callAnggota(offSet);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });

        return view;

    }

    @Override
    public void onRefresh() {
        anggotaList.clear();
        anggotaAdapter.notifyDataSetChanged();
        callAnggota(0);
    }

    private void callAnggota(int page){

        swipe.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(url_list + page,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    final ModelAnggota data = new ModelAnggota();

                                    no = obj.getInt(TAG_NO);

                                    data.setUsername(obj.getString(TAG_USER));
                                    data.setEmail(obj.getString(TAG_EMAIL));
                                    data.setId(obj.getString(TAG_ID));
                                    data.setAlamat(obj.getString(TAG_ALAMAT));
                                    data.setGambar(obj.getString("image"));
                                    data.setTelephone(obj.getString(TAG_TELEPHONE));

                                    // adding news to news array
                                    anggotaList.add(data);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.d(TAG, "offSet " + offSet);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                anggotaAdapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(arrReq);
    }

}
