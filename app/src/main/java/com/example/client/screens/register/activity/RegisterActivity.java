package com.example.client.screens.register.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.client.R;
import com.example.client.app.Constants;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.dialog.VerificationDialog;
import com.example.client.screens.login.activity.LoginEmailActivity;
import com.example.client.screens.main.activity.MainActivity;
import com.example.client.screens.register.present.RegisterPresent;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, IRegisterView {
    private ScrollView scrollView;
    private EditText fullname, phone, email, password, confirm;
    private ToggleButton eyePassword, eyeConfirm;
    private ImageView back;
    private TextView register;
    private TextView fullname_error, error_phone, error_email, error_password, error_confirm;
    private RegisterPresent rPresent;
    private ProgressBar progressBar;
    private PrimaryDialog dialog;
    private VerificationDialog verificationDialog;
    private RelativeLayout rllLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        eyePassword = findViewById(R.id.eyePassword);
        eyeConfirm = findViewById(R.id.eyeConfirm);
        back = findViewById(R.id.back);
        register = findViewById(R.id.register);
        fullname_error = findViewById(R.id.fullname_error);
        error_phone = findViewById(R.id.error_phone);
        error_email = findViewById(R.id.error_email);
        error_password = findViewById(R.id.error_password);
        error_confirm = findViewById(R.id.error_confirm);
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progress_bar);
        rllLoading = findViewById(R.id.rll_loading);

        rPresent = new RegisterPresent(this);
        dialog = new PrimaryDialog();
        dialog.getInstance(this);
        verificationDialog = new VerificationDialog();
        verificationDialog.getInstance(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        eyePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                password.setSelection(password.getText().length());
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.getText().length());
            }
        });

        eyeConfirm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                confirm.setInputType(InputType.TYPE_CLASS_TEXT);
                confirm.setSelection(confirm.getText().length());
            } else {
                confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirm.setSelection(confirm.getText().length());
            }
        });

        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    fullname_error.setText("*Email < 6 ký tự");
                    fullname_error.setVisibility(View.VISIBLE);
                } else {
                    fullname_error.setText("");
                    fullname_error.setVisibility(View.GONE);
                }
                checkButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String regex = "^(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b";
                if (phone.getText().toString().matches(regex)) {
                    error_phone.setText("");
                    error_phone.setVisibility(View.GONE);
                } else {
                    error_phone.setText("*Số điện thoại chưa đúng");
                    error_phone.setVisibility(View.VISIBLE);
                }
                checkButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    error_email.setText("*Email < 6 ký tự");
                    error_email.setVisibility(View.VISIBLE);
                } else {
                    String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                    if (email.getText().toString().matches(regex)) {
                        error_email.setText("");
                        error_email.setVisibility(View.GONE);
                    } else {
                        error_email.setText("*Email chưa đúng định dạng");
                        error_email.setVisibility(View.VISIBLE);
                    }
                }
                checkButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    error_password.setText("*Password < 6 ký tự");
                    error_password.setVisibility(View.VISIBLE);
                } else {
                    error_password.setText("");
                    error_password.setVisibility(View.GONE);
                }
                checkButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!password.getText().toString().equals(confirm.getText().toString())) {
                    error_confirm.setText(R.string.password_invalid);
                    error_confirm.setVisibility(View.VISIBLE);
                    checkButton();
                    scrollView.post(() -> scrollView.scrollTo(0, scrollView.getHeight()));
                } else {
                    error_confirm.setText("");
                    error_confirm.setVisibility(View.GONE);
                    checkButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        register.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    private void checkButton() {
        if (fullname_error.getText().toString().length() == 0 &&
                error_phone.getText().toString().length() == 0 &&
                error_email.getText().toString().length() == 0 &&
                error_password.getText().toString().length() == 0 &&
                error_confirm.getText().toString().length() == 0) {
            setButton(true, R.drawable.bg_btn);
        } else {
            setButton(false, R.drawable.bg_btn_disable);
        }
    }

    private void setButton(boolean enable, int background) {

        register.setEnabled(enable);
        register.setBackgroundResource(background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.register:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                rPresent.sendEmail(email.getText().toString());
                break;
        }
    }

    @Override
    public void register() {
        dialog.setDescription("Đăng ký thành công");
        dialog.hideBtnCancel();
        dialog.show();
        dialog.setOKListener(() -> {
            finish();
            LoginEmailActivity.loginEmailActivity.finish();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    public void showVerificationDialog(boolean showDescription) {
        if (!showDescription) {
            verificationDialog.hideDescription();
        } else {
            verificationDialog.setDescription(getString(R.string.err_code_1010));
            verificationDialog.showDescription();
        }
        verificationDialog.setOKListener(() ->
                rPresent.verification(email.getText().toString(),
                        VerificationDialog.verificationCode,
                        fullname.getText().toString(),
                        phone.getText().toString(),
                        password.getText().toString()));
        verificationDialog.setCancelListener(() -> {
        });
        verificationDialog.show();
    }

    @Override
    public void showLoading() {
        rllLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rllLoading.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(int errMessage) {
        dialog.setDescription(getString(errMessage));
        dialog.setOKListener(() -> {
        });
        dialog.hideBtnCancel();
        dialog.show();
    }
}