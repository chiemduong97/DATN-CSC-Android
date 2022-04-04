package com.example.client.screens.category.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.client.R;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.message.MessageModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.category.item.CategoryItem;
import com.example.client.screens.category.present.CategoryPresent;
import com.example.client.screens.search.activity.SearchActivity;
import com.example.client.screens.subject.activity.ISubjectView;
import com.example.client.screens.subject.present.SubjectPresent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener,ICategoryView, ISubjectView {
    private ImageView back;
    private RecyclerView recyclerView;
    private CategoryPresent cPresent;
    private SubjectPresent sPresent;
    private ImageView empty;
    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;
    private List<SubjectModel> subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        empty = findViewById(R.id.empty);
        searchView = findViewById(R.id.searchView);
        refreshLayout = findViewById(R.id.container);
        refreshLayout.setOnRefreshListener(()-> refreshLayout.setRefreshing(false));


        cPresent = new CategoryPresent(this);
        sPresent = new SubjectPresent(this);

        back.setOnClickListener(this);
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                startActivity(new Intent(this, SearchActivity.class));
                searchView.clearFocus();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cPresent.onShowCategories();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                onBackPressed();
                break;

        }
    }

    @Override
    public void showCategory(CategoryModel item) {

    }

    @Override
    public void showCategories(List<CategoryModel> items) {
        if(items == null || items.size() == 0){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(CategoryActivity.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            CategoryItem item = new CategoryItem(items,CategoryActivity.this);
            recyclerView.setAdapter(item);
        }

    }

    @Override
    public void showSubject(SubjectModel item) {

    }

    @Override
    public void showSubjectsByCategory(List<SubjectModel> items) {
        this.subjects = items;
    }

    @Override
    public void showMoreSubjects(List<SubjectModel> items) {

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