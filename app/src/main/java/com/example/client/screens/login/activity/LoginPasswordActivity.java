package com.example.client.screens.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.client.R;
import com.example.client.app.Constrants;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.login.present.LoginPresent;
import com.example.client.screens.main.activity.MainActivity;
import com.example.client.screens.reset.activity.PasswordResetActivity;
import com.google.gson.Gson;

public class LoginPasswordActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {
    private EditText password;
    private ToggleButton eye;
    private ImageView back;
    private TextView login,tv_error,reset;
    private LinearLayout view_error;
    private LoginPresent lPresent;
    private ProgressBar progressBar;
    public static Activity loginPasswordActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        password = findViewById(R.id.password);
        eye = findViewById(R.id.eye);
        back = findViewById(R.id.back);
        login = findViewById(R.id.login);
        tv_error = findViewById(R.id.tv_error);
        view_error = findViewById(R.id.view_error);
        reset = findViewById(R.id.reset);
        progressBar = findViewById(R.id.progress_bar);
        loginPasswordActivity = this;
        lPresent = new LoginPresent(this);

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

        login.setOnClickListener(this);
        back.setOnClickListener(this);
        reset.setOnClickListener(this);

    }
    public void setButton(boolean enable, int background){
        login.setEnabled(enable);
        login.setBackgroundResource(background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                lPresent.onLogin(getIntent().getStringExtra("email"), password.getText().toString().trim());
                break;
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.reset:
                Intent intent = new Intent(this, PasswordResetActivity.class);
                intent.putExtra("email",getIntent().getStringExtra("email"));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void next(MessageModel message) {

    }

    @Override
    public void login(MessageModel message) {
        if(message.isStatus()){
            view_error.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(getCurrentFocus()!=null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                password.clearFocus();
            }
            finish();
            LoginEmailActivity.loginEmailActivity.finish();
            startActivity(new Intent(LoginPasswordActivity.this, MainActivity.class));
        }
        else {
            switch (message.getCode()){
                case Constrants.ErrorCode.ERROR_1001:
                    tv_error.setText(R.string.err_code_1001);
                    break;
                case Constrants.ErrorCode.ERROR_1004:
                    tv_error.setText(R.string.err_code_1004);
                    break;
                case Constrants.ErrorCode.ERROR_1005:
                    tv_error.setText(R.string.err_code_1005);
                    break;
            }
            view_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        login.setBackgroundResource(R.drawable.bg_btn_disable);
        login.setText("");
        login.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        login.setBackgroundResource(R.drawable.bg_btn);
        login.setText("Đăng nhập");
        login.setEnabled(true);
    }

}