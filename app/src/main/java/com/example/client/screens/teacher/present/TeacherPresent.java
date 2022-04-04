package com.example.client.screens.teacher.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.TeacherService;
import com.example.client.models.teacher.TeacherModel;
import com.example.client.screens.teacher.ITeacherView;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherPresent implements ITeacherPresent {
    private ITeacherView tView;

    public TeacherPresent(ITeacherView tView) {
        this.tView = tView;
    }

    @Override
    public void onGetTeacher(int id) {
        TeacherService teacherService = ApiClient.getInstance().create(TeacherService.class);
        teacherService.getById(id).enqueue(new Callback<TeacherModel>() {
            @Override
            public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
                tView.getTeacher(response.body());
            }

            @Override
            public void onFailure(Call<TeacherModel> call, Throwable t) {

            }
        });
    }
}
