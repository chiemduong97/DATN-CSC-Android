package com.example.client.api.service;

import com.example.client.app.Constants;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("api/user/checkEmail.php")
    Call<MessageModel> checkEmail(@Query("email") String email);

    @FormUrlEncoded
    @POST("api/user/register.php")
    Call<MessageModel> register(@Field("fullname") String fullname,
                                @Field("phone") String phone,
                                @Field("email") String email,
                                @Field("password") String password);
    @FormUrlEncoded
    @POST("api/user/login.php")
    Call<MessageModel> login(@Field("email") String email,
                             @Field("password") String password);

    @GET("api/user/getUserByEmail.php")
    Call<ProfileModel> getUserByEmail(@Query("email") String email);

    @POST("api/user/updateInfo.php")
    Call<MessageModel> updateInfo(@Body ProfileModel user);

    @FormUrlEncoded
    @POST("api/user/updatePass.php")
    Call<MessageModel> udpatePass(@Field("email") String email,
                                  @Field("oldpassword") String oldpassword,
                                  @Field("newpassword") String newpassword);

    @FormUrlEncoded
    @POST("api/user/updateAvatar.php")
    Call<MessageModel> updateAvatar(@Field("email") String email,
                                    @Field("avatar") String avatar);

    @FormUrlEncoded
    @POST("api/user/updateDeviceToken.php")
    Call<MessageModel> updateDeviceToken(@Field("email") String email,
                                         @Field("deviceToken") String deviceToken);

    @GET("api/user/sendEmail.php")
    Call<MessageModel> sendEmail(@Query("email") String email, @Query("requestType") Constants.RequestType requestType);

    @FormUrlEncoded
    @POST("api/user/verification.php")
    Call<MessageModel> verification(@Field("email") String email,
                                    @Field("code") String code);
    @FormUrlEncoded
    @POST("api/user/resetPassword.php")
    Call<MessageModel> resetPassword(@Field("email") String email,
                                     @Field("password") String password);
}
