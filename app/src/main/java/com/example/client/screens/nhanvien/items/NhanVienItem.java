package com.example.client.screens.nhanvien.items;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.models.nhanvien.NhanVienModel;

import java.util.List;

public class NhanVienItem  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NhanVienModel> list;
    private Context context;

    public NhanVienItem(List<NhanVienModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,salary,department,address,position;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            salary = itemView.findViewById(R.id.salary);
            address = itemView.findViewById(R.id.address);
            position = itemView.findViewById(R.id.position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NhanVienModel item = list.get(position);
        ((MyViewHolder) holder).name.setText(item.getName());
        ((MyViewHolder) holder).salary.setText(item.getSalary()+"");
        ((MyViewHolder) holder).address.setText(item.getAddress());
        ((MyViewHolder) holder).position.setText(item.getPosition());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
