package com.example.client.screens.profile.manager_info.update;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.app.Preferences;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.profile.manager_info.IManagerInfoView;
import com.example.client.screens.profile.manager_info.present.ManagerInfoPresent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateInfoActivity extends AppCompatActivity implements View.OnClickListener, IManagerInfoView {
    private EditText fullname,birthday,phone;
    private TextView update;
    private ImageView back;
    private ManagerInfoPresent mPresent;
    private PrimaryDialog dialog;
    private ProfileModel user;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        fullname = findViewById(R.id.fullname);
        birthday = findViewById(R.id.birthday);
        phone = findViewById(R.id.phone);
        update = findViewById(R.id.update);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progress_bar);

        dialog = new PrimaryDialog();
        dialog.getInstance(this);
        mPresent = new ManagerInfoPresent(this);

        birthday.setOnClickListener(this);
        update.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent.getUserFromRes();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus()!=null){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                if(fullname.getText().toString().trim().equals("")){
                    dialog.setDescription("Không được để trống họ và tên");
                    dialog.setOKListener(()->{});
                    dialog.hideBtnCancel();
                    dialog.show();
                }
                else {
                    user.setFullname(fullname.getText().toString().trim());
                    user.setBirthday(birthday.getText().toString().trim());
                    user.setPhone(phone.getText().toString().trim());
                    mPresent.updateProfile(user);
                }
                break;
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.birthday:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        birthday.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void showUserInfo(ProfileModel user) {
        this.user = new ProfileModel();
        this.user = user;
        fullname.setText(user.getFullname());
        birthday.setText(user.getBirthday());
        phone.setText(user.getPhone());
    }

    @Override
    public void updateInfo() {
        Preferences.getInstance().setProfile(user);
        dialog.setDescription(getString(R.string.update_profile_success));
        dialog.hideBtnCancel();
        dialog.show();
        dialog.setOKListener(()->{
            finish();
            onBackPressed();
        });
    }

    @Override
    public void updatePass() {

    }

    @Override
    public void updateAvatar() {

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

    @Override
    public void showErrorMessage(int errMessage) {
        dialog.setDescription(getString(errMessage));
        dialog.setOKListener(()->{});
        dialog.hideBtnCancel();
        dialog.show();
    }
}