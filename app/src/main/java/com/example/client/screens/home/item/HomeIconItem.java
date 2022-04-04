package com.example.client.screens.home.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.models.home.HomeIconModel;
import com.example.client.screens.category.activity.CategoryActivity;

import java.util.List;

public class HomeIconItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeIconModel> list;

    public HomeIconItem(Context context, List<HomeIconModel> list){
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_icon,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeIconModel item = list.get(position);
        ((MyViewHolder)holder).icon.setImageResource(item.getIcon());
        ((MyViewHolder)holder).title.setText(item.getTitle());
        ((MyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        context.startActivity(new Intent(context, CategoryActivity.class));
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
