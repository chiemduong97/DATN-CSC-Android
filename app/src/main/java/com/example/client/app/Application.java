package com.example.client.app;



public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.createInstance(this);
    }

}
