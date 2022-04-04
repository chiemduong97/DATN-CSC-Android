package com.example.client.api.service;

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
    @GET("views/user/checkEmail.php")
    Call<MessageModel> checkEmail(@Query("email") String email);

    @FormUrlEncoded
    @POST("views/user/register.php")
    Call<MessageModel> register(@Field("fullname") String fullname,
                                @Field("email") String email,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("views/user/login.php")
    Call<MessageModel> login(@Field("email") String email,
                             @Field("password") String password);

    @GET("views/user/getUserByEmail.php")
    Call<ProfileModel> getUserByEmail(@Query("email") String email);

    @POST("views/user/updateInfo.php")
    Call<MessageModel> updateInfo(@Body ProfileModel user);

    @FormUrlEncoded
    @POST("views/user/updatePass.php")
    Call<MessageModel> udpatePass(@Field("email") String email,
                                  @Field("oldpassword") String oldpassword,
                                  @Field("newpassword") String newpassword);

    @FormUrlEncoded
    @POST("views/user/updateAvatar.php")
    Call<MessageModel> updateAvatar(@Field("email") String email,
                                    @Field("avatar") String avatar);

    @FormUrlEncoded
    @POST("views/user/updateDeviceToken.php")
    Call<MessageModel> updateDeviceToken(@Field("email") String email,
                                         @Field("deviceToken") String deviceToken);

    @GET("views/user/sendEmail.php")
    Call<MessageModel> sendEmail(@Query("email") String email);

    @FormUrlEncoded
    @POST("views/user/vertification.php")
    Call<MessageModel> vertification(@Field("email") String email,
                                     @Field("code") String code);
    @FormUrlEncoded
    @POST("views/user/resetPassword.php")
    Call<MessageModel> resetPassword(@Field("email") String email,
                                     @Field("password") String password);
}
