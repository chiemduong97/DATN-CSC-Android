package com.example.client.screens.wallet.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.RechargeService;
import com.example.client.api.service.SubjectService;
import com.example.client.api.service.TransactionService;
import com.example.client.api.service.UserService;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.models.transaction.TransactionModel;
import com.example.client.screens.wallet.fragment.IWalletView;
import com.example.client.app.Constrants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletPresent implements IWalletPresent{
    private IWalletView wView;
    private ProfileModel user;
    public WalletPresent(IWalletView wView){
        this.wView = wView;
    }
    @Override
    public void onShowInfoUser() {
        user = Preferences.getInstance().getProfile();
        wView.showInfoUser(user);
    }

    @Override
    public void onShowTransaction(String label, int user) {
        if(label.equals(Constrants.TRANSACTION.INPUT)){
            RechargeService service = ApiClient.getInstance().create(RechargeService.class);
            service.getByUser(user).enqueue(new Callback<List<TransactionModel>>() {
                @Override
                public void onResponse(Call<List<TransactionModel>> call, Response<List<TransactionModel>> response) {
                    wView.showTransaction(response.body(), Constrants.TRANSACTION.INPUT);

                }

                @Override
                public void onFailure(Call<List<TransactionModel>> call, Throwable t) {
                    wView.showTransaction(new ArrayList<>(), Constrants.TRANSACTION.INPUT);

                }
            });
        }
        if(label.equals(Constrants.TRANSACTION.OUPUT)){
            TransactionService service = ApiClient.getInstance().create(TransactionService.class);
            service.getByUser(user).enqueue(new Callback<List<TransactionModel>>() {
                @Override
                public void onResponse(Call<List<TransactionModel>> call, Response<List<TransactionModel>> response) {
                    wView.showTransaction(response.body(), Constrants.TRANSACTION.OUPUT);

                }

                @Override
                public void onFailure(Call<List<TransactionModel>> call, Throwable t) {
                    wView.showTransaction(new ArrayList<>(), Constrants.TRANSACTION.OUPUT);

                }
            });
        }
    }

    @Override
    public void onShowTransactionByOrderCode(String orderCode) {
        TransactionService service = ApiClient.getInstance().create(TransactionService.class);
        service.getByOrderCode(orderCode).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                wView.showTransactionByOrderCode(response.body());
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onGetSubject(int subject) {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.getById(subject).enqueue(new Callback<SubjectModel>() {
            @Override
            public void onResponse(Call<SubjectModel> call, Response<SubjectModel> response) {
                wView.getSubject(response.body());
            }

            @Override
            public void onFailure(Call<SubjectModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDeleteRecharge(String ordercode) {
        RechargeService service = ApiClient.getInstance().create(RechargeService.class);
        service.delete(ordercode).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                wView.deleteRecharge(response.body());
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                wView.deleteRecharge(new MessageModel(false,1001,null));

            }
        });

    }

    @Override
    public void onRequestRecharge(int user, Double amount) {
        wView.showLoading();
        RechargeService service = ApiClient.getInstance().create(RechargeService.class);
        service.insert(user, amount).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                wView.requestRecharge(response.body());
                wView.hideLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                wView.requestRecharge(new MessageModel(false,1001,null));
                wView.hideLoading();
            }
        });
    }

    @Override
    public void onRefeshUserActive(String email) {
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(email).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Preferences.getInstance().setProfile(response.body());
                wView.refreshUserActive(new MessageModel(true,-1,null));
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                wView.refreshUserActive(new MessageModel(false,1001,null));

            }
        });
    }
}
