package com.example.client.screens.wallet.item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.app.Constrants;
import com.example.client.models.transaction.TransactionModel;
import com.example.client.screens.wallet.activity.TransactionDetailActivity;
import com.example.client.utils.OnDeleteItemClick;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TransactionModel> items;
    private String method;
    private OnDeleteItemClick listener;

    public TransactionItem(Context context, List<TransactionModel> items, String method, OnDeleteItemClick listener){
        this.context = context;
        this.items = items;
        this.method = method;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView label;
        private TextView orderCode;
        private TextView createAt;
        private TextView amount;
        private TextView status;
        private TextView destroy;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            orderCode = itemView.findViewById(R.id.orderCode);
            createAt = itemView.findViewById(R.id.createAt);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
            destroy = itemView.findViewById(R.id.destroy);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TransactionModel item = items.get(position);
        ((MyViewHolder)holder).createAt.setText(item.getCreateAt());
        ((MyViewHolder)holder).orderCode.setText("#"+item.getOrdercode());
        if(method.equals(Constrants.TRANSACTION.INPUT)){
            ((MyViewHolder)holder).label.setText("Nạp tiền");
            Locale localeVN = new Locale("vi","VN");
            NumberFormat format = NumberFormat.getCurrencyInstance(localeVN);
            ((MyViewHolder)holder).amount.setText("+" + format.format(item.getAmount()));
            ((MyViewHolder)holder).amount.setTextColor(Color.GREEN);
        }
        if(method.equals(Constrants.TRANSACTION.OUPUT)){
            ((MyViewHolder)holder).label.setText("Đăng ký khóa học");
            Locale localeVN = new Locale("vi","VN");
            NumberFormat format = NumberFormat.getCurrencyInstance(localeVN);
            ((MyViewHolder)holder).amount.setText("-" + format.format(item.getAmount()));
            ((MyViewHolder)holder).amount.setTextColor(Color.RED);
            ((MyViewHolder)holder).itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, TransactionDetailActivity.class);
                intent.putExtra("ordercode",item.getOrdercode());
                context.startActivity(intent);
            });
        }
        if(item.getStatus()==0) {
            ((MyViewHolder)holder).status.setText("Đang duyệt");
            ((MyViewHolder)holder).status.setTextColor(Color.BLUE);
            ((MyViewHolder)holder).destroy.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).destroy.setOnClickListener(v -> listener.onDeleteRecharge(item.getOrdercode()));

        }
        else if(item.getStatus()==1){
            ((MyViewHolder)holder).status.setText("Đã xong");
            ((MyViewHolder)holder).status.setTextColor(Color.GREEN);
        }
        else if(item.getStatus()==2) {
            ((MyViewHolder) holder).status.setText("Đã hủy");
            ((MyViewHolder) holder).status.setTextColor(Color.GRAY);
            ((MyViewHolder)holder).amount.setTextColor(Color.GRAY);
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
