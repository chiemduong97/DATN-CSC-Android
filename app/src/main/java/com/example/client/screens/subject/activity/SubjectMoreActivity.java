package com.example.client.screens.subject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.app.Constants;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.message.MessageModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.search.activity.SearchActivity;
import com.example.client.screens.subject.item.SubjectHorizontalItem;
import com.example.client.screens.subject.present.SubjectPresent;

import java.util.List;

public class SubjectMoreActivity extends AppCompatActivity implements View.OnClickListener, ISubjectView {
    private TextView title;
    private ImageView back;
    private RecyclerView recyclerView;
    private String method;
    private SubjectPresent sPresent;
    private ImageView empty;
    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;
    private CategoryModel categoryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_more);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        empty = findViewById(R.id.empty);
        searchView = findViewById(R.id.searchView);
        refreshLayout = findViewById(R.id.container);
        refreshLayout.setOnRefreshListener(() -> refreshLayout.setRefreshing(false));
        sPresent = new SubjectPresent(this);

        categoryModel = (CategoryModel) getIntent().getExtras().getSerializable(Constants.CATEGORY_MODEL);

        title.setText(categoryModel != null ? categoryModel.getName() : getIntent().getStringExtra("name"));
        method = getIntent().getStringExtra("method");
        back.setOnClickListener(this);
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                startActivity(new Intent(this, SearchActivity.class));
                searchView.clearFocus();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sPresent.onShowMoreSubjects(getIntent().getIntExtra("id", -1), method);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                onBackPressed();
                break;

        }
    }

    @Override
    public void showSubject(SubjectModel item) {

    }

    @Override
    public void showSubjectsByCategory(List<SubjectModel> items) {

    }

    @Override
    public void showMoreSubjects(List<SubjectModel> items) {
        if (items == null || items.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(SubjectMoreActivity.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            SubjectHorizontalItem item = new SubjectHorizontalItem(items, SubjectMoreActivity.this);
            recyclerView.setAdapter(item);
        }

    }

    @Override
    public void showSubjectByUser(List<SubjectModel> items) {

    }

    @Override
    public void registerSubject(MessageModel message) {

    }

    @Override
    public void checkEnableRegister(MessageModel message) {

    }
}