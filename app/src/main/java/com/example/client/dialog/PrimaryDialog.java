package com.example.client.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.utils.ICallBack;

public class PrimaryDialog implements View.OnClickListener{
    private TextView description;
    private TextView btnOK, btnCancel;
    private AlertDialog dialog;
    private ICallBack oKListener;
    private ICallBack cancelListener;

    public PrimaryDialog(){

    }

    public void getInstance(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_primary,null);
        builder.setView(dialogView);
        description = dialogView.findViewById(R.id.description);
        btnOK = dialogView.findViewById(R.id.btnOK);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        dialog = builder.create();
        builder.setCancelable(false);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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

    public void setDescription(String des){
        description.setText(des);
    }

    public void show(){
        dialog.show();
    }

    public void hideBtnCancel(){
        btnCancel.setVisibility(View.GONE);
    }
}
