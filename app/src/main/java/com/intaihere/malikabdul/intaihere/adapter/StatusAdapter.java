package com.intaihere.malikabdul.intaihere.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intaihere.malikabdul.intaihere.R;
import com.intaihere.malikabdul.intaihere.menuStatus.DetailStatusActivity;
import com.intaihere.malikabdul.intaihere.model.ModelStatus;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.TaskViewHolder>  {

    private List<ModelStatus> statusList;
    private Context context;

    public StatusAdapter (Context context, List<ModelStatus> list) {
        this.context = context;
        this.statusList = list;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        ModelStatus modelTask = statusList.get(position);
        holder.tvAUsername.setText(modelTask.getUsername());
        holder.tvAWaktu.setText(modelTask.getWaktu());
        holder.tvStatus.setText(modelTask.getStatus());
        holder.tvTujuan.setText(modelTask.getTujuan());
        holder.tvFoto_status.setText(modelTask.getFoto_status());

        Picasso.with(context).load(modelTask.getFoto_status()).centerCrop().resize(80, 80).error(R.drawable.man).into(holder.civFoto);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvAUsername, tvAWaktu, tvStatus, tvTujuan, tvFoto_status;
        CircleImageView civFoto;

        int post;
        public TaskViewHolder(View itemView) {
            super(itemView);
            tvAUsername = itemView.findViewById(R.id.tv_rv_usernamestatus);
            tvAWaktu = itemView.findViewById(R.id.tv_rv_statuswaktu);
            civFoto = itemView.findViewById(R.id.civ_rv_statusfoto);
            tvStatus    = itemView.findViewById(R.id.tv_rv_StatusStatus);
            tvTujuan    = itemView.findViewById(R.id.tv_rv_statustujuan);
            tvFoto_status   = itemView.findViewById(R.id.tv_rv_fotostatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post = getAdapterPosition();

                    Intent intent = new Intent(context, DetailStatusActivity.class);
                    intent.putExtra("username", tvAUsername.getText().toString().trim());
                    intent.putExtra("waktu", tvAWaktu.getText().toString().trim());
                    intent.putExtra("status", tvStatus.getText().toString().trim());
                    intent.putExtra("tujuan", tvTujuan.getText().toString().trim());
                    intent.putExtra("foto_status", tvFoto_status.getText().toString().trim());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public StatusAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_status, parent, false);
        return new StatusAdapter.TaskViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

}
