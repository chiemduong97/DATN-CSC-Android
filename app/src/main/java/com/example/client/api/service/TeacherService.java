package com.example.client.api.service;

import com.example.client.models.teacher.TeacherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TeacherService {
    @GET("views/teacher/getAll.php")
    Call<List<TeacherModel>> getAll();
    @GET("views/teacher/getById.php")
    Call<TeacherModel> getById(@Query("id") int id);
}
