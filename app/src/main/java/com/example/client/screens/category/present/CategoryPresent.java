package com.example.client.screens.category.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.CategoryService;
import com.example.client.models.category.CategoryModel;
import com.example.client.screens.category.activity.ICategoryView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryPresent implements ICategoryPresent{
    private ICategoryView cView;

    public CategoryPresent(ICategoryView cView){
        this.cView = cView;
    }

    @Override
    public void onShowCategory(int id) {
        CategoryService service = ApiClient.getInstance().create(CategoryService.class);
        service.getById(id).enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                cView.showCategory(response.body());
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onShowCategories() {
        CategoryService service = ApiClient.getInstance().create(CategoryService.class);
        service.getAll().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                cView.showCategories(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                cView.showCategories(new ArrayList<>());
            }
        });
    }
}
