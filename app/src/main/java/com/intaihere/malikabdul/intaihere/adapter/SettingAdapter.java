package com.intaihere.malikabdul.intaihere.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.intaihere.malikabdul.intaihere.R;

public class SettingAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] keterangan;
    private final Integer[] imageId;

    public SettingAdapter(Activity context,
                      String[] keterangan, Integer[] imageId) {
        super(context, R.layout.layout_list_setting, keterangan);
        this.context = context;
        this.keterangan = keterangan;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.layout_list_setting, null, true);
        TextView txtTitle = rowView.findViewById(R.id.tv_LS_text);
        ImageView imageView = rowView.findViewById(R.id.iv_LS_icon);
        txtTitle.setText(keterangan[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
