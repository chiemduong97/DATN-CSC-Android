package com.example.client.screens.subject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.app.Constrants;
import com.example.client.app.Preferences;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.models.teacher.TeacherModel;
import com.example.client.screens.category.activity.ICategoryView;
import com.example.client.screens.category.present.CategoryPresent;
import com.example.client.screens.subject.item.SubjectVerticalItem;
import com.example.client.screens.subject.present.SubjectPresent;
import com.example.client.screens.teacher.ITeacherView;
import com.example.client.screens.teacher.present.TeacherPresent;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SubjectDetailActivity extends AppCompatActivity implements View.OnClickListener, ISubjectView, ICategoryView, ITeacherView {
    private TextView title,category,name,price,rate,teacher,time,start,end,quantity,description;
    private ImageView back,share,avatar;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing_toolbar;
    private Toolbar toolbar;
    private boolean scrollTop = true;
    private RecyclerView recyclerView;
    private TextView more;
    private CategoryModel categoryModel;
    private SubjectPresent sPresent;
    private CategoryPresent cPresent;
    private TeacherPresent tPresent;
    private TextView register;
    private PrimaryDialog dialog;
    private ProfileModel user;
    private SubjectModel subject;
    private ImageView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        avatar = findViewById(R.id.avatar);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        rate = findViewById(R.id.rate);
        teacher = findViewById(R.id.teacher);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        quantity = findViewById(R.id.quantity);
        description = findViewById(R.id.description);
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);
        share = findViewById(R.id.share);
        appbar = findViewById(R.id.appbar);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        more = findViewById(R.id.more);
        empty = findViewById(R.id.empty);

        user = Preferences.getInstance().getProfile();
        dialog = new PrimaryDialog();
        dialog.getInstance(this);

        sPresent = new SubjectPresent(this);
        cPresent = new CategoryPresent(this);
        tPresent = new TeacherPresent(this);

        appbar.addOnOffsetChangedListener(new   AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == - collapsing_toolbar.getHeight() + toolbar.getHeight()) {
                    title.setVisibility(View.VISIBLE);
                    back.setColorFilter(Color.BLACK);
                    share.setColorFilter(Color.BLACK);
                    scrollTop = false;
                }
                else if(!scrollTop){
                    title.setVisibility(View.INVISIBLE);
                    back.setColorFilter(Color.WHITE);
                    share.setColorFilter(Color.WHITE);
                    scrollTop = true;
                }
            }
        });


        register.setOnClickListener(this);
        back.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sPresent.onShowSubject(getIntent().getIntExtra("id",-1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                onBackPressed();
                break;
            case R.id.more:
                Intent intentNew = new Intent(this, SubjectMoreActivity.class);
                intentNew.putExtra("name", categoryModel.getName());
                intentNew.putExtra("id", categoryModel.getId());
                intentNew.putExtra("method", Constrants.MORE.CATEGORY);
                startActivity(intentNew);
                break;
            case R.id.register:
                if(user.getWallet()<subject.getPrice()){
                    dialog.setDescription("Tài khoản không đủ, vui lòng nạp thêm! " + price.getText().toString() + " ?");
                    dialog.setOKListener(()->{ });
                    dialog.hideBtnCancel();
                    dialog.show();
                }
                else {
                    dialog.setDescription("Xác định đăng ký khóa học với giá " + price.getText().toString() + " ?");
                    dialog.setOKListener(()->{
                        sPresent.onRegisterSubject(user.getId(),subject.getId(),subject.getPrice());
                    });
                    dialog.setCancelListener(()->{});
                    dialog.show();
                }

                break;

        }
    }

    @Override
    public void showSubject(SubjectModel item) {
        this.subject = item;
        Glide.with(SubjectDetailActivity.this).asBitmap().placeholder(R.drawable.subject_default)
                .load(item.getAvatar())
                .into(avatar);
        title.setText(item.getName());
        name.setText(item.getName());
        Locale localeVN = new Locale("vi","VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeVN);
        price.setText(format.format(item.getPrice()));
        rate.setText(item.getRate()+"/5");
        time.setText("Ca học: " + item.getTime());
        start.setText("Bắt đầu: " + item.getStart());
        end.setText("Kết thúc: " + item.getEnd());
        quantity.setText("Lượt đăng ký: " + item.getQuantity());
        description.setText(item.getDescription());
        cPresent.onShowCategory(item.getCategory());
        tPresent.onGetTeacher(item.getTeacher());
        sPresent.enableRegister(user.getId(),subject.getId());
    }

    @Override
    public void showSubjectsByCategory(List<SubjectModel> items) {
        if(items == null || items.size() == 0){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(SubjectDetailActivity.this,LinearLayoutManager.HORIZONTAL,false);
            SubjectVerticalItem subjectVerticalItem = new SubjectVerticalItem(items,SubjectDetailActivity.this);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(subjectVerticalItem);
        }

    }

    @Override
    public void showMoreSubjects(List<SubjectModel> items) {

    }

    @Override
    public void showSubjectByUser(List<SubjectModel> items) {

    }

    @Override
    public void registerSubject(MessageModel message) {
        if(message.isStatus()){
            user.setWallet(user.getWallet() - subject.getPrice());
            quantity.setText("Lượt đăng ký: " + (subject.getQuantity()+1));
            Preferences.getInstance().setProfile(user);
            register.setBackgroundResource(R.drawable.bg_btn_disable);
            register.setEnabled(false);
            dialog.setDescription("Đăng ký thành công");
            dialog.hideBtnCancel();
            dialog.show();
            dialog.setOKListener(()->{});
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
    public void checkEnableRegister(MessageModel message) {
        if(message.isStatus()){
            register.setBackgroundResource(R.drawable.bg_btn_disable);
            register.setEnabled(false);
        }
        else {
            register.setBackgroundResource(R.drawable.bg_btn);
            register.setEnabled(true);
        }
    }

    @Override
    public void showCategory(CategoryModel item) {
        if(item != null){
            categoryModel = new CategoryModel();
            categoryModel.setId(item.getId());
            categoryModel.setName(item.getName());
            category.setText("Thể loại: "+item.getName());
            sPresent.onShowSubjectsByCategory(item.getId());
        }
    }

    @Override
    public void showCategories(List<CategoryModel> items) {

    }

    @Override
    public void getTeacher(TeacherModel item) {
        if(item != null){
            teacher.setText("Giảng viên: "+ item.getName());
        }
    }
}