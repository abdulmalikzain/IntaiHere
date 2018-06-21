package com.intaihere.malikabdul.intaihere.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.model.ModelRiwayat;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.TaskViewHolder>  {

    private List<ModelRiwayat> statusList;
    private Context context;

    public RiwayatAdapter (Context context, List<ModelRiwayat> list) {
        this.context = context;
        this.statusList = list;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        ModelRiwayat modelTask = statusList.get(position);
        holder.tvAUsername.setText(modelTask.getUsername());
        holder.tvAWaktu.setText(modelTask.getWaktu());
        holder.tvAlamat.setText(modelTask.getAlamat());

    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvAUsername, tvAWaktu, tvAlamat;

        public TaskViewHolder(View itemView) {
            super(itemView);
            tvAUsername = itemView.findViewById(R.id.tv_riwayat_username);
            tvAWaktu = itemView.findViewById(R.id.tv_riwayat_waktu);
            tvAlamat = itemView.findViewById(R.id.tv_riwayat_alamat);
        }
    }

    @Override
    public RiwayatAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_riwayat, parent, false);
        return new RiwayatAdapter.TaskViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }
}
