package com.example.client.screens.splash.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.client.R;
import com.example.client.app.Preferences;
import com.example.client.models.splash.Splash;
import com.example.client.screens.login.activity.LoginEmailActivity;
import com.example.client.screens.main.activity.MainActivity;
import com.example.client.screens.splash.item.SplashItem;
import com.example.client.screens.splash.present.SplashPresent;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements ISplashView{
    private ViewPager2 pagerSplash;
    private SplashPresent sPresent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pagerSplash = findViewById(R.id.pagerSplash);
        sPresent = new SplashPresent(this);
        sPresent.onShowPager();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void auto(){
        Handler handler = new Handler();

        Runnable update = () -> {
            if (pagerSplash.getCurrentItem()<2)
                pagerSplash.setCurrentItem(pagerSplash.getCurrentItem()+1);
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 10000, 10000);
    }

    @Override
    public void showPager(List<Splash> splashes) {
        SplashItem item = new SplashItem(this,splashes,this,
                ()->{
                    if(pagerSplash.getCurrentItem()==2){
                        finish();
                        String token = Preferences.getInstance().getAccessToken();
                        if(token.equals("")){
                            startActivity(new Intent(SplashActivity.this, LoginEmailActivity.class));
                        }
                        else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    }
                    else
                        pagerSplash.setCurrentItem(pagerSplash.getCurrentItem()+1);
                },
                ()-> pagerSplash.setCurrentItem(pagerSplash.getCurrentItem()-1));
        pagerSplash.setAdapter(item);

        TabLayout tabLayout = findViewById(R.id.tabDots);
        new TabLayoutMediator(tabLayout,pagerSplash,(tab, position) -> {

        }).attach();
        auto();
    }
}