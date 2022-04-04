package com.example.client.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.example.client.models.profile.ProfileModel;
import com.google.gson.Gson;

public class Preferences {
    private static String SHARE_PREFERENCES = "SHARE_PREFERENCES";
    private static String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static String DEVICE_TOKEN = "DEVICE_TOKEN";
    private static String PROFILE_MODEL = "PROFILE_MODEL";
    private static Preferences preferences;
    private SharedPreferences sharePreferences;
    private Preferences(Context context){
        sharePreferences = context.getSharedPreferences(SHARE_PREFERENCES,Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(){
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
        editor.commit();
    }

    public void deleteAccessToken(){
        Editor editor = sharePreferences.edit();
        editor.remove(ACCESS_TOKEN);
        editor.commit();
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
        } catch (Exception e)
        {
        }
        editor.putString(PROFILE_MODEL, json);
        editor.commit();
    }

    public void deleteProfile(){
        Editor editor = sharePreferences.edit();
        editor.remove(PROFILE_MODEL);
        editor.commit();
    }

    public String getDeviceToken(){
        return sharePreferences.getString(DEVICE_TOKEN,"");
    }

    public void setDeviceToken(String deviceToken){
        Editor editor = sharePreferences.edit();
        editor.putString(DEVICE_TOKEN,deviceToken);
        editor.commit();
    }

    public void deleteDeviceToken(){
        Editor editor = sharePreferences.edit();
        editor.remove(DEVICE_TOKEN);
        editor.commit();
    }
}
