package com.example.client.api.service;

import com.example.client.models.Test.Request;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.models.teacher.TeacherModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TestService {
    @GET("views/lab1/")
    Call<Request> getString();

    @GET("views/category/getAll.php")
    Call<List<CategoryModel>> getAllCategory();

    @GET("views/subject/getAll.php")
    Call<List<SubjectModel>> getAllSubject();

    @GET("views/subject/getById.php")
    Call<SubjectModel> getSubject(@Query("id") int id);

    @GET("views/subject/getByCategory.php")
    Call<List<SubjectModel>> getSubjectByCategory(@Query("id") int id);

    @GET("views/teacher/getById.php")
    Call<TeacherModel> getTeacher(@Query("id") int id);

    @GET("views/category/getById.php")
    Call<CategoryModel> getCategory(@Query("id") int id);
}
