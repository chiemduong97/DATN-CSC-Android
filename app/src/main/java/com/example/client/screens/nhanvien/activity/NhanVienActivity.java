package com.example.client.screens.nhanvien.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.BannerService;
import com.example.client.models.nhanvien.NhanVienModel;
import com.example.client.screens.nhanvien.items.NhanVienItem;
import com.example.client.screens.subject.activity.SubjectDetailActivity;
import com.example.client.screens.subject.item.SubjectVerticalItem;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhanVienActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);
        recyclerView = findViewById(R.id.recyclerView);

        String dep = getIntent().getStringExtra("department");
        if(dep == null) {
            BannerService service = ApiClient.getInstance().create(BannerService.class);
            service.getBySalary().enqueue(new Callback<List<NhanVienModel>>() {
                @Override
                public void onResponse(Call<List<NhanVienModel>> call, Response<List<NhanVienModel>> response) {
                    Log.d("Duong", "onResponse: " + new Gson().toJson(response.body()));
                    LinearLayoutManager manager = new LinearLayoutManager(NhanVienActivity.this,LinearLayoutManager.VERTICAL,false);
                    NhanVienItem item = new NhanVienItem(response.body(),NhanVienActivity.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(item);
                    return;
                }

                @Override
                public void onFailure(Call<List<NhanVienModel>> call, Throwable t) {

                }
            });
        }

        BannerService service = ApiClient.getInstance().create(BannerService.class);
        service.getByDepartment(dep).enqueue(new Callback<List<NhanVienModel>>() {
            @Override
            public void onResponse(Call<List<NhanVienModel>> call, Response<List<NhanVienModel>> response) {
                Log.d("Duong", "onResponse: " + new Gson().toJson(response.body()));
                LinearLayoutManager manager = new LinearLayoutManager(NhanVienActivity.this,LinearLayoutManager.VERTICAL,false);
                NhanVienItem item = new NhanVienItem(response.body(),NhanVienActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(item);
            }

            @Override
            public void onFailure(Call<List<NhanVienModel>> call, Throwable t) {

            }
        });

    }
}