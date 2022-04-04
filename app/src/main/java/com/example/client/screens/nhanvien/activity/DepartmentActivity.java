package com.example.client.screens.nhanvien.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.BannerService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentActivity extends AppCompatActivity {
    private ListView listView;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        listView = findViewById(R.id.listview);
        btn = findViewById(R.id.btn);

        BannerService service = ApiClient.getInstance().create(BannerService.class);
        service.getDepartment().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DepartmentActivity.this, android.R.layout.simple_list_item_1,response.body());
                Log.d("Duong", "onResponse: " + new Gson().toJson(response.body()));
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(DepartmentActivity.this, NhanVienActivity.class);
                        intent.putExtra("department", response.body().get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        btn.setOnClickListener(v -> startActivity(new Intent(DepartmentActivity.this, NhanVienActivity.class)));
    }
}