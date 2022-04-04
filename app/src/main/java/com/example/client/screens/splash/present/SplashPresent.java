package com.example.client.screens.splash.present;

import com.example.client.R;
import com.example.client.models.splash.Splash;
import com.example.client.screens.splash.activity.ISplashView;

import java.util.ArrayList;
import java.util.List;

public class SplashPresent implements ISplashPresent{
    private ISplashView sView;

    public SplashPresent(ISplashView sView){
        this.sView = sView;
    }

    @Override
    public void onShowPager() {
        List<Splash> splashes = new ArrayList<>();
        splashes.add(new Splash(R.drawable.splash1,"Study","The devotion of time and attention to acquiring knowledge on an academic subject, especially by means of books"));
        splashes.add(new Splash(R.drawable.splash2,"Books","A written or printed work consisting of pages glued or sewn together along one side and bound in covers"));
        splashes.add(new Splash(R.drawable.splash3,"Initiative","The power or opportunity to act or take charge before others do"));
        sView.showPager(splashes);
    }
}
