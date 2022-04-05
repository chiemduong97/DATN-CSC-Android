package com.example.client.screens.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.screens.login.present.LoginPresent;
import com.example.client.screens.register.activity.RegisterActivity;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener, ILoginView{
    private TextView next,tv_error,register;
    private EditText email;
    private LoginPresent lPresent;
    private LinearLayout view_error;
    private ProgressBar progressBar;
    public static Activity loginEmailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        next = findViewById(R.id.next);
        tv_error = findViewById(R.id.tv_error);
        view_error = findViewById(R.id.view_error);
        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progress_bar);

        loginEmailActivity = this;

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    setButton(false,R.drawable.bg_btn_disable);
                }
                else {
                    String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                    if(email.getText().toString().matches(regex)){
                        setButton(true,R.drawable.bg_btn);
                    }
                    else {
                        setButton(false,R.drawable.bg_btn_disable);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lPresent = new LoginPresent(this);

        next.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setButton(boolean enable, int background){
        next.setEnabled(enable);
        next.setBackgroundResource(background);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus()!=null){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    email.clearFocus();
                }
                lPresent.checkEmail(email.getText().toString());
                break;
            case R.id.register:
                startActivity(new Intent(LoginEmailActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void next() {
        view_error.setVisibility(View.GONE);
        Intent intent = new Intent(LoginEmailActivity.this,LoginPasswordActivity.class);
        intent.putExtra("email",email.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void login() {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        next.setBackgroundResource(R.drawable.bg_btn_disable);
        next.setText("");
        next.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        next.setBackgroundResource(R.drawable.bg_btn);
        next.setText("Tiếp");
        next.setEnabled(true);
    }

    @Override
    public void showErrorMessage(int errMessage) {
        tv_error.setText(getString(errMessage));
        view_error.setVisibility(View.VISIBLE);
    }

}