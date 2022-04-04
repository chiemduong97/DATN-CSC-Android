package com.example.client.screens.noti.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.models.noti.Notification;

import java.util.List;

public class NotificationItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Notification> list;

    public NotificationItem(Context context, List<Notification> list){
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView action;
        private TextView description;
        private TextView createAt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            action = itemView.findViewById(R.id.action);
            description = itemView.findViewById(R.id.description);
            createAt = itemView.findViewById(R.id.createAt);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_noti, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Notification item = list.get(position);
        ((MyViewHolder)holder).action.setText(item.getAction());
        ((MyViewHolder)holder).description.setText(item.getDescription());
        ((MyViewHolder)holder).createAt.setText(item.getCreateAt());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
