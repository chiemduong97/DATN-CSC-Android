package com.example.client.screens.message.items;

import android.content.Context;
import android.graphics.Color;
import android.os.Messenger;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.models.message.MessengerModel;

import java.util.List;

public class MessengerItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MessengerModel> list;

    public MessengerItem(Context context, List<MessengerModel> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout view;
        private CardView box;
        private TextView data;
        private TextView createAt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            box = itemView.findViewById(R.id.box);
            data = itemView.findViewById(R.id.data);
            createAt = itemView.findViewById(R.id.createAt);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_messenger,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessengerModel item = list.get(position);
        ((MyViewHolder)holder).data.setText(item.getData());
        ((MyViewHolder)holder).createAt.setText(item.getCreateAt());
        if (item.isFromMe()) {
            ((MyViewHolder)holder).box.setCardBackgroundColor(Color.parseColor("#0033FF"));
            ((MyViewHolder)holder).data.setTextColor(Color.WHITE);
            ((MyViewHolder)holder).view.setGravity(Gravity.RIGHT);
        } else {
            ((MyViewHolder)holder).box.setCardBackgroundColor(Color.parseColor("#f0fff0"));
            ((MyViewHolder)holder).data.setTextColor(Color.BLACK);
            ((MyViewHolder)holder).view.setGravity(Gravity.LEFT);
        }
        ((MyViewHolder)holder).itemView.setOnClickListener(v -> {
            if (((MyViewHolder)holder).createAt.getVisibility() == View.VISIBLE) {
                ((MyViewHolder)holder).createAt.setVisibility(View.GONE);
            }
            else {
                ((MyViewHolder)holder).createAt.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
