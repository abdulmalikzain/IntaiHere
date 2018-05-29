package com.intaihere.malikabdul.intaihere.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.model.ModelAnggota;
import com.intaihere.malikabdul.intaihere.utils.Server;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnggotaAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelAnggota> anggotaItems;

    public AnggotaAdapter(Activity activity, List<ModelAnggota> anggotaItems) {
        this.activity = activity;
        this.anggotaItems = anggotaItems;
    }

    @Override
    public int getCount() {
        return anggotaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return anggotaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_list_anggota, null);

        TextView username =  convertView.findViewById(R.id.tv_usernamegrup);
        TextView telephone =  convertView.findViewById(R.id.tv_telephonegrup);
        CircleImageView imageView =  convertView.findViewById(R.id.civ_gambarusergrup);


        final ModelAnggota modelAnggota = anggotaItems.get(position);

            Picasso.with(this.activity)
                    .load(modelAnggota.getGambar())
                    .centerCrop()
                    .resize(100, 100)
                    .error(R.drawable.man)
                    .into(imageView);


        username.setText(modelAnggota.getUsername());
        telephone.setText(modelAnggota.getTelephone());

        return convertView;
    }


}
