package com.example.client.screens.subject.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.subject.activity.SubjectDetailActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SubjectVerticalItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SubjectModel> items;
    private Context context;

    public SubjectVerticalItem(List<SubjectModel> items, Context context){
        this.items = items;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView avatar;
        private TextView name,price,rate,quantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            rate = itemView.findViewById(R.id.rate);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_vertical, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SubjectModel item = items.get(position);
        Glide.with(context).asBitmap().placeholder(R.drawable.subject_default).load(item.getAvatar()).into(((MyViewHolder)holder).avatar);
        ((MyViewHolder)holder).name.setText(item.getName());
        Locale localeVN = new Locale("vi","VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeVN);
        ((MyViewHolder)holder).price.setText(format.format(item.getPrice()));
        ((MyViewHolder)holder).rate.setText(item.getRate()+"");
        ((MyViewHolder)holder).quantity.setText(item.getQuantity()+" lượt đăng ký");
        ((MyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubjectDetailActivity.class);
                intent.putExtra("title", item.getName());
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
