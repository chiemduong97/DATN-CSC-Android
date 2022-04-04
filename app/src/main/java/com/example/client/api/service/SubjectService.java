package com.example.client.api.service;

import com.example.client.models.subject.SubjectModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SubjectService {
    @GET("views/subject/getAll.php")
    Call<List<SubjectModel>> getAll();

    @GET("views/subject/getById.php")
    Call<SubjectModel> getById(@Query("id") int id);

    @GET("views/subject/getByCategory.php")
    Call<List<SubjectModel>> getByCategory(@Query("id") int id,@Query("limit") int limit);

    @GET("views/subject/getHighLight.php")
    Call<List<SubjectModel>> getHighLight(@Query("limit") int limit);

    @GET("views/subject/getNew.php")
    Call<List<SubjectModel>> getNew(@Query("limit") int limit);

    @GET("views/subject/getByUser.php")
    Call<List<SubjectModel>> getByUser(@Query("user") int user);

    @GET("views/subject/search.php")
    Call<List<SubjectModel>> search(@Query("query") String query);
}
