package com.intaihere.malikabdul.intaihere.menuSetting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.adapter.SettingAdapter;
import com.intaihere.malikabdul.intaihere.logReg.LoginActivity;
import com.intaihere.malikabdul.intaihere.utils.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.intaihere.malikabdul.intaihere.logReg.LoginActivity.my_shared_preferences;


public class SettingFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private ListView list;
    private LinearLayout llProfil;
    TextView tvUsernameSetting;
    private CircleImageView civCloseTentang, civCloseInfo, civFotoSetting;
    private Dialog dialog;
    private String[] keterangan = {
            "Tentang Intai Here",
            "Info Aplikasi",
            "Hapus akun",
            "Keluar Akun"

    } ;
    private Integer[] imageId = {
            R.drawable.ic_setting,
            R.drawable.ic_info,
            R.drawable.ic_delete,
            R.drawable.ic_logout
//            R.drawable.download
    };

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_setting, container, false);

         civFotoSetting = view.findViewById(R.id.civ_fotosetting);
         tvUsernameSetting  = view.findViewById(R.id.tv_usernamesetting);

         llProfil   = view.findViewById(R.id.ll_profil);

        sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        String image = (sharedPreferences.getString("image", ""));
        Picasso.with(getContext()).load(image).error(R.drawable.man).into(civFotoSetting);

        SettingAdapter listAdapter = new
                SettingAdapter(getActivity(), keterangan, imageId);
        list= view.findViewById(R.id.list_view_setting);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                    startActivityForResult(myIntent, 0);
                    showPopupTentang();
                }

                if (position == 1) {
                    showInfoAplikasi();
                }

                if (position == 2) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
                    final String username1 = (sharedPreferences.getString("username", ""));
                    builder.setTitle("Hapus Akun " )
                            .setMessage(username1)
                            .setIcon(R.drawable.ic_dialog_close_light)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    removeAkun();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
//                        .setIcon(android.R.drawable.alert)
                            .show();
                }

                if (position == 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
                    final String username2 = (sharedPreferences.getString("username", ""));
                    builder.setTitle("Keluar dari Akun ini?")
                            .setMessage(username2)
                            .setIcon(android.R.drawable.ic_lock_power_off)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    keluarAkun();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

            }
        });


        llProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

         return view;
    }

    private void showPopupTentang (){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view1 = factory.inflate(R.layout.layout_popup_tentang, null);
        alertDialog.setView(view1);
        alertDialog.setNegativeButton("kembali", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showInfoAplikasi(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view1 = factory.inflate(R.layout.layout_popup_infoaplikasi, null);
        alertDialog.setView(view1);
        alertDialog.setNegativeButton("kembali", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void keluarAkun(){
        sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LoginActivity.session_status, false);
        editor.putString("id", null);
        editor.putString("username", null);
        editor.putString("email", null);
        editor.putString("telephone", null);
        editor.putString("alamat", null);
        editor.putString("image", null);
        editor.clear();
        editor.commit();
        getActivity().finish();

        Intent intent1 = new Intent(getContext(), LoginActivity.class);
        startActivity(intent1);
    }

    public void removeAkun() {
        sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        final String id = (sharedPreferences.getString("id", ""));
        final String url_removeAkun = "http://trackingeye.000webhostapp.com/trackingeye/delete_akun.php?id="+id;
        StringRequest strReq = new StringRequest(Request.Method.GET, url_removeAkun, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(LoginActivity.session_status, false);
                    editor.putString("id", null);
                    editor.clear();
                    editor.commit();
                    getActivity().finish();
                    Toast.makeText(getContext() ,"Akun Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }

}
