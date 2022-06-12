package com.example.client.screens.category.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.models.category.CategoryModel;
import com.example.client.utils.OnClickCategoryItemListener;

import java.util.List;

public class HomeCategoryItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CategoryModel> list;
    private OnClickCategoryItemListener listener;

    public HomeCategoryItem(Context context, List<CategoryModel> list, OnClickCategoryItemListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_category, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryModel item = list.get(position);
        Glide.with(context).asBitmap().placeholder(R.drawable.icon_default).load(item.getAvatar()).into(((MyViewHolder) holder).avatar);
        ((MyViewHolder) holder).name.setText(item.getName());
        ((MyViewHolder) holder).itemView.setOnClickListener(v -> listener.onClickItem(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
