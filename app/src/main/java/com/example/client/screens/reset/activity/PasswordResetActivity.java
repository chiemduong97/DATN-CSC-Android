package com.example.client.screens.reset.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.client.R;
import com.example.client.app.Constrants;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.message.MessageModel;
import com.example.client.screens.login.activity.LoginEmailActivity;
import com.example.client.screens.login.activity.LoginPasswordActivity;
import com.example.client.screens.reset.present.PasswordResetPresent;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener, IPasswordResetView {
    private OtpTextView code;
    private EditText password;
    private ToggleButton eye;
    private ImageView back;
    private TextView reset,sendEmail,tv_error;
    private LinearLayout view_code,view_password;
    private ProgressBar progressBar1,progressBar2,progressBar3;
    private PasswordResetPresent pPresent;
    private PrimaryDialog dialog;
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        code = findViewById(R.id.code);
        password = findViewById(R.id.password);
        eye = findViewById(R.id.eye);
        back = findViewById(R.id.back);
        view_password = findViewById(R.id.view_password);
        view_code = findViewById(R.id.view_code);
        tv_error = findViewById(R.id.tv_error);
        reset = findViewById(R.id.reset);
        sendEmail = findViewById(R.id.sendEmail);
        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);
        progressBar3 = findViewById(R.id.progress_bar3);
        pPresent = new PasswordResetPresent(this);
        dialog = new PrimaryDialog();
        dialog.getInstance(this);

        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        eye.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                password.setSelection(password.getText().length());
            }
            else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.getText().length());
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 6){
                    setButton(false,R.drawable.bg_btn_disable);
                }
                else {
                    setButton(true,R.drawable.bg_btn);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        back.setOnClickListener(this);
        reset.setOnClickListener(this);
        RxView.clicks(sendEmail)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> pPresent.onSendEmail(getIntent().getStringExtra("email")), Throwable::printStackTrace);
        code.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                pPresent.onVertification(getIntent().getStringExtra("email"),otp);
            }
        });

    }
    public void setButton(boolean enable, int background){
        reset.setEnabled(enable);
        reset.setBackgroundResource(background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.reset:
                pPresent.onResetPassword(getIntent().getStringExtra("email"),password.getText().toString());
                break;
        }
    }

    @Override
    public void verification(MessageModel message) {

        if(message.isStatus()){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(getCurrentFocus()!=null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            code.showSuccess();
            code.clearFocus();
            view_password.setVisibility(View.VISIBLE);
        }
        else {
            code.showError();
            switch (message.getCode()){
                case Constrants.ErrorCode.ERROR_1001:
                    Toast.makeText(this,getString(R.string.err_code_1001),Toast.LENGTH_SHORT).show();
                    break;
                case Constrants.ErrorCode.ERROR_1007:
                    Toast.makeText(this,getString(R.string.err_code_1007),Toast.LENGTH_SHORT).show();
                    break;
                case Constrants.ErrorCode.ERROR_1008:
                    Toast.makeText(this,getString(R.string.err_code_1008),Toast.LENGTH_SHORT).show();
                    break;
                case Constrants.ErrorCode.ERROR_1009:
                    Toast.makeText(this,getString(R.string.err_code_1009),Toast.LENGTH_SHORT).show();
                    break;
            }
            view_password.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void resetPassword(MessageModel message) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        if(message.isStatus()){
            dialog.setDescription("Reset mật khẩu thành công");
            dialog.hideBtnCancel();
            dialog.show();
            dialog.setOKListener(()->{
                finish();
                LoginEmailActivity.loginEmailActivity.finish();
                LoginPasswordActivity.loginPasswordActivity.finish();
                startActivity(new Intent(this,LoginEmailActivity.class));
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
    public void sendEmail(MessageModel message) {
        if(message.isStatus()){
            tv_error.setVisibility(View.GONE);
            view_code.setVisibility(View.VISIBLE);
            code.setOTP("");
            pPresent.onCountDownTimer(sendEmail,300);
        }
        else {
            switch (message.getCode()){
                case Constrants.ErrorCode.ERROR_1001:
                    tv_error.setText(R.string.err_code_1001);
                    view_code.setVisibility(View.INVISIBLE);
                    break;
                case Constrants.ErrorCode.ERROR_1010:
                    tv_error.setText(R.string.err_code_1010);
                    view_code.setVisibility(View.VISIBLE);
                    code.setOTP("");
                    break;
            }
            tv_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showSendEmailLoading() {
        progressBar1.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.GONE);
        view_code.setVisibility(View.INVISIBLE);
        view_password.setVisibility(View.INVISIBLE);
        password.setText("");
    }

    @Override
    public void hideSendEmailLoading() {
        progressBar1.setVisibility(View.GONE);
    }

    @Override
    public void showVertifiLoading() {
        progressBar3.setVisibility(View.VISIBLE);
        view_password.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideVertifiLoading() {
        progressBar3.setVisibility(View.GONE);
    }

    @Override
    public void showResetLoading() {
        progressBar2.setVisibility(View.VISIBLE);
        reset.setBackgroundResource(R.drawable.bg_btn_disable);
        reset.setText("");
        reset.setEnabled(false);
    }

    @Override
    public void hideResetLoading() {
        progressBar2.setVisibility(View.GONE);
        reset.setBackgroundResource(R.drawable.bg_btn);
        reset.setText("Reset mật khẩu");
        reset.setEnabled(true);
    }

}