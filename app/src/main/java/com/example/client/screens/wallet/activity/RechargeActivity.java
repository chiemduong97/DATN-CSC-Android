package com.example.client.screens.wallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.app.Constrants;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.models.transaction.TransactionModel;
import com.example.client.screens.wallet.fragment.IWalletView;
import com.example.client.screens.wallet.present.WalletPresent;

import java.util.List;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener, IWalletView {
    private ImageView back;
    private TextView section_1,section_2,section_5;
    private TextView request;
    private ProgressBar progressBar;
    private WalletPresent wPresent;
    private PrimaryDialog dialog;
    private Double amount;
    private ProfileModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        back = findViewById(R.id.back);
        section_1 = findViewById(R.id.section_1);
        section_2 = findViewById(R.id.section_2);
        section_5 = findViewById(R.id.section_5);
        request = findViewById(R.id.request);
        progressBar = findViewById(R.id.progress_bar);

        dialog = new PrimaryDialog();
        dialog.getInstance(this);
        wPresent = new WalletPresent(this);

        back.setOnClickListener(this);
        request.setOnClickListener(this);
        section_1.setOnClickListener(this);
        section_2.setOnClickListener(this);
        section_5.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wPresent.onShowInfoUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.request:
                wPresent.onRequestRecharge(user.getId(),amount);
                break;
            case R.id.section_1:
                amount = Constrants.SECTION_1;
                section_1.setBackgroundResource(R.drawable.bg_btn);
                section_1.setTextColor(Color.WHITE);
                section_2.setBackgroundResource(R.drawable.border_item);
                section_2.setTextColor(Color.BLACK);
                section_5.setBackgroundResource(R.drawable.border_item);
                section_5.setTextColor(Color.BLACK);
                request.setBackgroundResource(R.drawable.bg_btn);
                request.setEnabled(true);
                break;
            case R.id.section_2:
                amount = Constrants.SECTION_2;
                section_2.setBackgroundResource(R.drawable.bg_btn);
                section_2.setTextColor(Color.WHITE);
                section_1.setBackgroundResource(R.drawable.border_item);
                section_1.setTextColor(Color.BLACK);
                section_5.setBackgroundResource(R.drawable.border_item);
                section_5.setTextColor(Color.BLACK);
                request.setBackgroundResource(R.drawable.bg_btn);
                request.setEnabled(true);
                break;
            case R.id.section_5:
                amount = Constrants.SECTION_5;
                section_5.setBackgroundResource(R.drawable.bg_btn);
                section_5.setTextColor(Color.WHITE);
                section_2.setBackgroundResource(R.drawable.border_item);
                section_2.setTextColor(Color.BLACK);
                section_1.setBackgroundResource(R.drawable.border_item);
                section_1.setTextColor(Color.BLACK);
                request.setBackgroundResource(R.drawable.bg_btn);
                request.setEnabled(true);
                break;
        }
    }


    @Override
    public void showInfoUser(ProfileModel user) {
        this.user = user;
    }

    @Override
    public void showTransaction(List<TransactionModel> items, String method) {

    }

    @Override
    public void showTransactionByOrderCode(TransactionModel item) {

    }

    @Override
    public void getSubject(SubjectModel item) {

    }

    @Override
    public void deleteRecharge(MessageModel message) {

    }

    @Override
    public void requestRecharge(MessageModel message) {
        if(this!=null){

        }
        if(message.isStatus()){
            dialog.setDescription("Yêu cầu nạp tiền đã được gửi, vui lòng lên văn phòng để nạp tiền");
            dialog.hideBtnCancel();
            dialog.show();
            dialog.setOKListener(()->{
                finish();
                onBackPressed();
            });
        }
        else {
            switch (message.getCode()){
                case Constrants.ErrorCode.ERROR_1001:
                    dialog.setDescription(getString(R.string.err_code_1001));
                    break;
            }
            dialog.setOKListener(()->{});
            dialog.hideBtnCancel();
            dialog.show();
        }
    }

    @Override
    public void refreshUserActive(MessageModel message) {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        request.setBackgroundResource(R.drawable.bg_btn_disable);
        request.setText("");
        request.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        request.setBackgroundResource(R.drawable.bg_btn);
        request.setText("Lưu thay đổi");
        request.setEnabled(true);
    }
}