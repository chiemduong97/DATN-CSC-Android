package com.example.client.screens.profile.manager_info.update;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.client.R;
import com.example.client.app.Constrants;
import com.example.client.app.Preferences;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.profile.manager_info.IManagerInfoView;
import com.example.client.screens.profile.manager_info.present.ManagerInfoPresent;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener, IManagerInfoView {
    private EditText old_password,new_password,confirm;
    private ToggleButton eye_old_password,eye_new_password,eye_confirm;
    private TextView error_old_password,error_new_password,error_confirm;
    private TextView update;
    private ProgressBar progressBar;
    private PrimaryDialog dialog;
    private ImageView back;
    private ManagerInfoPresent mPresent;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        confirm = findViewById(R.id.confirm);
        eye_old_password = findViewById(R.id.eye_old_password);
        eye_new_password = findViewById(R.id.eye_new_password);
        eye_confirm = findViewById(R.id.eye_confirm);
        error_old_password = findViewById(R.id.error_old_password);
        error_new_password = findViewById(R.id.error_new_password);
        error_confirm = findViewById(R.id.error_confirm);
        update = findViewById(R.id.update);
        progressBar = findViewById(R.id.progress_bar);
        back = findViewById(R.id.back);

        dialog = new PrimaryDialog();
        dialog.getInstance(this);
        mPresent = new ManagerInfoPresent(this);

        old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        eye_old_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                old_password.setInputType(InputType.TYPE_CLASS_TEXT);
                old_password.setSelection(old_password.getText().length());
            }
            else {
                old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                old_password.setSelection(old_password.getText().length());
            }
        });

        eye_new_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                new_password.setInputType(InputType.TYPE_CLASS_TEXT);
                new_password.setSelection(new_password.getText().length());
            }
            else {
                new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new_password.setSelection(new_password.getText().length());
            }
        });

        eye_confirm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                confirm.setInputType(InputType.TYPE_CLASS_TEXT);
                confirm.setSelection(confirm.getText().length());
            }
            else {
                confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirm.setSelection(confirm.getText().length());
            }
        });

        old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 6){
                    error_old_password.setText("*Password < 6 ký tự");
                    error_old_password.setVisibility(View.VISIBLE);
                    checkButton();
                }
                else {
                    error_old_password.setText("");
                    error_old_password.setVisibility(View.GONE);
                    checkButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 6){
                    error_new_password.setText("*Password < 6 ký tự");
                    error_new_password.setVisibility(View.VISIBLE);
                    checkButton();
                }
                else {
                    error_new_password.setText("");
                    error_new_password.setVisibility(View.GONE);
                    checkButton();
                }
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
                if(!new_password.getText().toString().equals(confirm.getText().toString())){
                    error_confirm.setText("*Xác nhận mật khẩu chưa chính xác");
                    error_confirm.setVisibility(View.VISIBLE);
                    checkButton();
                }
                else {
                    error_confirm.setText("");
                    error_confirm.setVisibility(View.GONE);
                    checkButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent.onShowInfoUser();
    }

    public void checkButton(){
        if(error_old_password.getText().toString().length()==0&&
                error_new_password.getText().toString().length()==0&&
                error_confirm.getText().toString().length()==0)
        {
            setButton(true,R.drawable.bg_btn);
        }
        else {
            setButton(false,R.drawable.bg_btn_disable);
        }
    }

    public void setButton(boolean enable, int background){
        update.setEnabled(enable);
        update.setBackgroundResource(background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                mPresent.onUpdatePass(email,old_password.getText().toString().trim(),
                        new_password.getText().toString().trim());
                break;
            case R.id.back:
                finish();
                onBackPressed();
                break;
        }

    }

    @Override
    public void showInfoUser(ProfileModel user) {
        this.email = user.getEmail();
    }

    @Override
    public void updateInfo(MessageModel message) {

    }

    @Override
    public void updatePass(MessageModel message) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        if(message.isStatus()){
            dialog.setDescription("Cập nhật thành công");
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
                case Constrants.ErrorCode.ERROR_1006:
                    dialog.setDescription(getString(R.string.err_code_1006));
                    break;
            }
            dialog.setOKListener(()->{});
            dialog.hideBtnCancel();
            dialog.show();
        }
    }

    @Override
    public void updateAvatar(MessageModel message) {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        update.setBackgroundResource(R.drawable.bg_btn_disable);
        update.setText("");
        update.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        update.setBackgroundResource(R.drawable.bg_btn);
        update.setText("Lưu thay đổi");
        update.setEnabled(true);
    }
}