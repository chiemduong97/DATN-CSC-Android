package com.example.client.screens.wallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.transaction.TransactionModel;
import com.example.client.screens.wallet.fragment.IWalletView;
import com.example.client.screens.wallet.present.WalletPresent;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionDetailActivity extends AppCompatActivity implements View.OnClickListener, IWalletView {
    private ImageView back;
    private TextView title,orderCode,createAt,amount,status,subject_name,description;
    private LinearLayout subjectView;
    private WalletPresent wPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        orderCode = findViewById(R.id.orderCode);
        createAt = findViewById(R.id.createAt);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);
        subject_name = findViewById(R.id.subject_name);
        subjectView = findViewById(R.id.subject);
        description = findViewById(R.id.description);

        wPresent = new WalletPresent(this);

        title.setText("Chi tiết giao dịch #"+getIntent().getStringExtra("ordercode"));

        back.setOnClickListener(this);
        subjectView.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        wPresent.onShowTransactionByOrderCode(getIntent().getStringExtra("ordercode"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.subject:
                break;
        }
    }

    @Override
    public void showInfoUser(ProfileModel user) {

    }

    @Override
    public void showTransaction(List<TransactionModel> items, String method) {

    }

    @Override
    public void showTransactionByOrderCode(TransactionModel item) {
        if(item != null){
            orderCode.setText("#"+item.getOrdercode());
            createAt.setText(item.getCreateAt());
            Locale localeVN = new Locale("vi","VN");
            NumberFormat format = NumberFormat.getCurrencyInstance(localeVN);
            amount.setText(format.format(item.getAmount()));
            amount.setTextColor(Color.RED);
            status.setText("Đã xong");
            status.setTextColor(Color.GREEN);
            wPresent.onGetSubject(item.getSubject());
        }
    }


    @Override
    public void deleteRecharge(MessageModel message) {

    }

    @Override
    public void requestRecharge(MessageModel message) {

    }

    @Override
    public void refreshUserActive(MessageModel message) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}