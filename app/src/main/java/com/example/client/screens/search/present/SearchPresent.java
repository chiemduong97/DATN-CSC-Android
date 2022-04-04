package com.example.client.screens.search.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.SubjectService;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.search.activity.ISearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresent implements ISearchPresent {
    private ISearchView sView;
    public SearchPresent(ISearchView sView){
        this.sView = sView;
    }
    @Override
    public void searchSubjects(String query) {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.search(query).enqueue(new Callback<List<SubjectModel>>() {
            @Override
            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                sView.onSearchSubjects(response.body());
            }

            @Override
            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                sView.onSearchSubjects(new ArrayList<>());
            }
        });
    }
}
