package com.example.client.screens.noti.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.client.R;
import com.example.client.models.noti.Notification;
import com.example.client.screens.noti.item.NotificationItem;
import com.example.client.screens.noti.present.NotificationPresent;

import java.util.List;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, INotificationView {
    private ImageView back;
    private RecyclerView recyclerView;
    private ImageView empty;
    private NotificationPresent nPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        nPresent = new NotificationPresent(this);
        empty = findViewById(R.id.empty);
        back.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        nPresent.onShowNotifications();
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
    public void showNotifications(List<Notification> items) {
        if(items == null || items.size() == 0){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(NotificationActivity.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            NotificationItem item = new NotificationItem(NotificationActivity.this, items);
            recyclerView.setAdapter(item);
        }
    }
}