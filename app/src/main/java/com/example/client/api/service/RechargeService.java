package com.example.client.api.service;

import com.example.client.models.message.MessageModel;
import com.example.client.models.transaction.TransactionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RechargeService {
    @FormUrlEncoded
    @POST("views/recharge/insert.php")
    Call<MessageModel> insert(@Field("user") int user,
                              @Field("amount") Double amount);

    @GET("views/recharge/confirm.php")
    Call<MessageModel> confirmRecharge(@Query("ordercode") String ordercode);

    @GET("views/recharge/delete.php")
    Call<MessageModel> delete(@Query("ordercode") String ordercode);

    @GET("views/recharge/getByUser.php")
    Call<List<TransactionModel>> getByUser(@Query("user") int user);

}
