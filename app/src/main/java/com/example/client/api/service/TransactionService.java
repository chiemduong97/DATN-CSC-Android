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

public interface TransactionService {
    @FormUrlEncoded
    @POST("views/transaction/insert.php")
    Call<MessageModel> insert(@Field("user") int user,
                              @Field("subject") int subject,
                              @Field("amount") Double amount);

    @GET("views/transaction/checkInsert.php")
    Call<MessageModel> checkInsert(@Query("user") int user,@Query("subject") int subject);

    @GET("views/transaction/getByUser.php")
    Call<List<TransactionModel>> getByUser(@Query("user") int user);

    @GET("views/transaction/getByOrderCode.php")
    Call<TransactionModel> getByOrderCode(@Query("ordercode") String ordercode);

}
