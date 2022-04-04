package com.example.client.screens.subject.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.SubjectService;
import com.example.client.api.service.TransactionService;
import com.example.client.app.Constrants;
import com.example.client.models.message.MessageModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.subject.activity.ISubjectView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectPresent implements ISubjectPresent{
    private ISubjectView sView;
    public SubjectPresent(ISubjectView sView){
        this.sView = sView;
    }

    @Override
    public void onShowSubject(int id) {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.getById(id).enqueue(new Callback<SubjectModel>() {
            @Override
            public void onResponse(Call<SubjectModel> call, Response<SubjectModel> response) {
                sView.showSubject(response.body());
            }

            @Override
            public void onFailure(Call<SubjectModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onShowSubjectsByCategory(int category) {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.getByCategory(category,10).enqueue(new Callback<List<SubjectModel>>() {
            @Override
            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                sView.showSubjectsByCategory(response.body());
            }

            @Override
            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                sView.showSubjectsByCategory(new ArrayList<>());
            }
        });
    }

    @Override
    public void onShowMoreSubjects(int id, String method) {

        if(method.equals(Constrants.MORE.CATEGORY)){
            SubjectService service = ApiClient.getInstance().create(SubjectService.class);
            service.getByCategory(id,1000).enqueue(new Callback<List<SubjectModel>>() {
                @Override
                public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                    sView.showMoreSubjects(response.body());
                }

                @Override
                public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                    sView.showMoreSubjects(new ArrayList<>());
                }
            });
        }
        if(method.equals(Constrants.MORE.HIGHLIGHT)){
            SubjectService service = ApiClient.getInstance().create(SubjectService.class);
            service.getHighLight(1000).enqueue(new Callback<List<SubjectModel>>() {
                @Override
                public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                    sView.showMoreSubjects(response.body());
                }

                @Override
                public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                    sView.showMoreSubjects(new ArrayList<>());

                }
            });
        }
        if(method.equals(Constrants.MORE.NEW)){
            SubjectService service = ApiClient.getInstance().create(SubjectService.class);
            service.getNew(1000).enqueue(new Callback<List<SubjectModel>>() {
                @Override
                public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                    sView.showMoreSubjects(response.body());
                }

                @Override
                public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                    sView.showMoreSubjects(new ArrayList<>());

                }
            });
        }
    }

    @Override
    public void onRegisterSubject(int user, int subject, Double amount) {
        TransactionService service = ApiClient.getInstance().create(TransactionService.class);
        service.insert(user,subject,amount).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                sView.registerSubject(response.body());
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                sView.registerSubject(new MessageModel(false,1001,null));
            }
        });
    }

    @Override
    public void onShowSubjectsByUser(int user) {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.getByUser(user).enqueue(new Callback<List<SubjectModel>>() {
            @Override
            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                sView.showSubjectByUser(response.body());
            }

            @Override
            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                sView.showSubjectByUser(new ArrayList<>());
            }
        });
    }

    @Override
    public void enableRegister(int user, int subject) {
        TransactionService service = ApiClient.getInstance().create(TransactionService.class);
        service.checkInsert(user,subject).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                sView.checkEnableRegister(response.body());
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                sView.checkEnableRegister(new MessageModel(false,1001,null));
            }
        });
    }
}
