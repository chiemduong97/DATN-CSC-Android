package com.example.client.screens.subject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.models.message.MessageModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.subject.item.SubjectHorizontalItem;
import com.example.client.screens.subject.present.SubjectPresent;

import java.util.List;

public class SubjectByUserActivity extends AppCompatActivity implements View.OnClickListener, ISubjectView {
    private ImageView back;
    private RecyclerView recyclerView;
    private SubjectPresent sPresent;
    private ImageView empty;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_by_user);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        empty = findViewById(R.id.empty);
        refreshLayout = findViewById(R.id.container);
        refreshLayout.setOnRefreshListener(()-> refreshLayout.setRefreshing(false));
        sPresent = new SubjectPresent(this);
        back.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sPresent.onShowSubjectsByUser(getIntent().getIntExtra("user",-1));
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
    public void showSubject(SubjectModel item) {

    }

    @Override
    public void showSubjectsByCategory(List<SubjectModel> items) {

    }

    @Override
    public void showMoreSubjects(List<SubjectModel> items) {

    }

    @Override
    public void showSubjectByUser(List<SubjectModel> items) {
        if(items == null || items.size() == 0){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(SubjectByUserActivity.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            SubjectHorizontalItem item = new SubjectHorizontalItem(items,SubjectByUserActivity.this);
            recyclerView.setAdapter(item);
        }
    }

    @Override
    public void registerSubject(MessageModel message) {

    }

    @Override
    public void checkEnableRegister(MessageModel message) {

    }
}