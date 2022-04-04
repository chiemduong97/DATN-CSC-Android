package com.example.client.screens.category.activity;

import com.example.client.models.category.CategoryModel;

import java.util.List;

public interface ICategoryView {
    void showCategory(CategoryModel item);
    void showCategories(List<CategoryModel> items);
}
