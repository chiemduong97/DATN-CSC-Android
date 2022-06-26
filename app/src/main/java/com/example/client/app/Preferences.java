package com.example.client.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.example.client.models.branch.BranchModel;
import com.example.client.models.cart.CartModel;
import com.example.client.models.profile.ProfileModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Preferences {
    private static final String SHARE_PREFERENCES = "SHARE_PREFERENCES";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    private static final String PROFILE_MODEL = "PROFILE_MODEL";
    private static final String BRANCH_MODEL = "BRANCH_MODEL";
    private static final String CART_MODEL = "CART_MODEL";

    private static Preferences preferences;
    private final SharedPreferences sharePreferences;
    private Preferences(Context context){
        sharePreferences = context.getSharedPreferences(SHARE_PREFERENCES,Context.MODE_PRIVATE);
    }

    public static Preferences newInstance(){
        if(preferences == null){
            throw new NullPointerException("Preferences is null!");
        }
        return preferences;
    }

    public static synchronized void createInstance(Context context){
        if(preferences == null){
            preferences = new Preferences(context);
        }
    }

    public String getAccessToken(){
        return sharePreferences.getString(ACCESS_TOKEN,"");
    }

    public void setAccessToken(String accessToken){
        Editor editor = sharePreferences.edit();
        editor.putString(ACCESS_TOKEN,accessToken);
        editor.apply();
    }

    public void deleteAccessToken(){
        Editor editor = sharePreferences.edit();
        editor.remove(ACCESS_TOKEN);
        editor.apply();
    }

    public ProfileModel getProfile()
    {
        String json = sharePreferences.getString(PROFILE_MODEL, "");
        if (TextUtils.isEmpty(json))
            return null;

        Gson gson = new Gson();
        try
        {
            return gson.fromJson(json, ProfileModel.class);
        } catch (Exception e)
        {
            return null;
        }
    }

    public void setProfile(ProfileModel profile)
    {
        Editor editor = sharePreferences.edit();

        Gson gson = new Gson();
        String json = "";
        try
        {
            json = gson.toJson(profile);
        } catch (Exception ignored)
        {
        }
        editor.putString(PROFILE_MODEL, json);
        editor.apply();
    }

    public void deleteProfile(){
        Editor editor = sharePreferences.edit();
        editor.remove(PROFILE_MODEL);
        editor.apply();
    }

    public String getDeviceToken(){
        return sharePreferences.getString(DEVICE_TOKEN,"");
    }

    public void setDeviceToken(String deviceToken){
        Editor editor = sharePreferences.edit();
        editor.putString(DEVICE_TOKEN,deviceToken);
        editor.apply();
    }

    public void deleteDeviceToken(){
        Editor editor = sharePreferences.edit();
        editor.remove(DEVICE_TOKEN);
        editor.apply();
    }

    public BranchModel getBranch()
    {
        String json = sharePreferences.getString(BRANCH_MODEL, "");
        if (TextUtils.isEmpty(json))
            return null;

        Gson gson = new Gson();
        try
        {
            return gson.fromJson(json, BranchModel.class);
        } catch (Exception e)
        {
            return null;
        }
    }

    public void setBranch(BranchModel branch) {
        Editor editor = sharePreferences.edit();

        Gson gson = new Gson();
        String json = "";
        try
        {
            json = gson.toJson(branch);
        } catch (Exception ignored)
        {
        }
        editor.putString(BRANCH_MODEL, json);
        editor.apply();
    }

    public CartModel getCart()
    {
        String json = sharePreferences.getString(CART_MODEL, "");
        if (TextUtils.isEmpty(json))
            return new CartModel(new ArrayList<>());

        Gson gson = new Gson();
        try
        {
            return gson.fromJson(json, CartModel.class);
        } catch (Exception e)
        {
            return new CartModel(new ArrayList<>());
        }
    }

    public void setCart(CartModel cart) {
        Editor editor = sharePreferences.edit();

        Gson gson = new Gson();
        String json = "";
        try
        {
            json = gson.toJson(cart);
        } catch (Exception ignored)
        {
        }
        editor.putString(CART_MODEL, json);
        editor.apply();
    }

    public void deleteCart() {
        Editor editor = sharePreferences.edit();
        editor.remove(CART_MODEL);
        editor.apply();
    }

    public void deleteAll() {
        deleteDeviceToken();
        deleteProfile();
        deleteAccessToken();
        deleteCart();
    }
}
