package com.example.client.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.client.R;
import com.example.client.utils.ICallBack;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class VertificationDialog implements View.OnClickListener {
    private TextView btnOK, btnCancel;
    private AlertDialog dialog;
    private ICallBack oKListener;
    private ICallBack cancelListener;
    private OtpTextView code;
    private TextView description;
    private LinearLayout viewCode;
    public static String vertificationCode;

    public VertificationDialog() {
    }

    public void getInstance(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_vertification, null);
        builder.setView(dialogView);
        code = dialogView.findViewById(R.id.code);
        btnOK = dialogView.findViewById(R.id.btnOK);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        description = dialogView.findViewById(R.id.description);
        viewCode = dialogView.findViewById(R.id.view_code);
        dialog = builder.create();
        builder.setCancelable(false);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        disableBtnOk();

        code.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                enableBtnOK();
                vertificationCode = otp;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                oKListener.onCallback();
                dialog.dismiss();
                code.setOTP("");
                break;
            case R.id.btnCancel:
                cancelListener.onCallback();
                dialog.dismiss();
                code.setOTP("");
                break;

        }
    }

    public void setOKListener(ICallBack oKListener) {
        this.oKListener = oKListener;
    }

    public void setCancelListener(ICallBack cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void show() {
        dialog.show();
    }

    public void hideBtnCancel() {
        btnCancel.setVisibility(View.GONE);
    }

    public void disableBtnOk() {
        btnOK.setEnabled(false);
        btnOK.setBackgroundResource(R.drawable.bg_btn_disable);
    }

    public void enableBtnOK(){
        btnOK.setEnabled(true);
        btnOK.setBackgroundResource(R.drawable.bg_btn);
    }

    public void showBtnCancel() {
        btnCancel.setVisibility(View.VISIBLE);
    }

    public void setDescription(String des) {
        description.setText(des);
    }


    public void hideDescription() {
        description.setVisibility(View.GONE);
    }

    public void showViewCode() {
        viewCode.setVisibility(View.VISIBLE);
    }

    public void hideViewCode() {
        viewCode.setVisibility(View.GONE);
    }

}
