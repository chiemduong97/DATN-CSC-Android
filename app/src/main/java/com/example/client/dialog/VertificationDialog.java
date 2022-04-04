package com.example.client.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.utils.ICallBack;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class VertificationDialog implements View.OnClickListener{
    private TextView btnOK, btnCancel;
    private AlertDialog dialog;
    private ICallBack oKListener;
    private ICallBack cancelListener;
    private OtpTextView code;
    public static String vertificationCode;

    public VertificationDialog(){
    }

    public void getInstance(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_vertification,null);
        builder.setView(dialogView);
        code = dialogView.findViewById(R.id.code);
        btnOK = dialogView.findViewById(R.id.btnOK);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        dialog = builder.create();
        builder.setCancelable(false);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        btnOK.setEnabled(false);
        btnOK.setBackgroundResource(R.drawable.bg_btn_disable);

        code.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                btnOK.setEnabled(true);
                btnOK.setBackgroundResource(R.drawable.bg_btn);
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
                break;
            case R.id.btnCancel:
                cancelListener.onCallback();
                dialog.dismiss();
                break;

        }
    }

    public void setOKListener(ICallBack oKListener){
        this.oKListener = oKListener;
    }

    public void setCancelListener(ICallBack cancelListener){
        this.cancelListener = cancelListener;
    }

    public void show(){
        dialog.show();
    }

    public void hideBtnCancel(){
        btnCancel.setVisibility(View.GONE);
    }

}
