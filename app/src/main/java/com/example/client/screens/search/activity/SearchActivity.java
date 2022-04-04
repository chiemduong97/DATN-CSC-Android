package com.example.client.screens.search.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.client.R;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.search.present.SearchPresent;
import com.example.client.screens.subject.item.SubjectHorizontalItem;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, ISearchView{
    private ImageView back;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageView empty;
    private SearchPresent sPresent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        back = findViewById(R.id.back);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        empty = findViewById(R.id.empty);

        sPresent = new SearchPresent(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sPresent.searchSubjects(query);
                return false;
            }

            @SuppressLint("CheckResult")
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText!= null && newText.trim().length() >=2){
                    RxSearchView.queryTextChanges(searchView)
                            .debounce(500, TimeUnit.MILLISECONDS)
                            .filter(str -> !TextUtils.isEmpty(str.toString().trim()) && str.toString().trim().length() >= 1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(res-> sPresent.searchSubjects(res.toString()), Throwable::printStackTrace);
                }
                return true;
            }
        });

        back.setOnClickListener(this);
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
    public void onSearchSubjects(List<SubjectModel> items) {
        if(items == null || items.size() == 0){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            SubjectHorizontalItem item = new SubjectHorizontalItem(items,SearchActivity.this);
            recyclerView.setAdapter(item);
        }
    }
}