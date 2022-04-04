package com.example.client.screens.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.client.R;

import com.example.client.screens.home.fragment.HomeFragment;
import com.example.client.screens.main.present.MainPresent;
import com.example.client.screens.noti.activity.NotificationActivity;
import com.example.client.screens.profile.fragment.ProfileFragment;
import com.example.client.screens.wallet.fragment.WalletFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements IMainView, View.OnClickListener{
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MainPresent mPresent;
    private ImageView notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notification = findViewById(R.id.notification);
        navigation = findViewById(R.id.navigtion);
        mPresent = new MainPresent(this);


        mPresent.replaceFragment(R.id.menu_home);

//        navigation.getOrCreateBadge(R.id.menu_noti).setVisible(true);
//        navigation.getOrCreateBadge(R.id.menu).setNumber(99);

        navigation.setOnItemSelectedListener(item -> {
            if(!item.isChecked()){
                switch (item.getItemId()){
                    case R.id.menu_home:
                        mPresent.replaceFragment(R.id.menu_home);
                        break;
                    case R.id.menu_wallet:
                        mPresent.replaceFragment(R.id.menu_wallet);
                        break;
                    case R.id.menu_profile:
                        mPresent.replaceFragment(R.id.menu_profile);
                        break;
                }
            }
            return true;
        });

        notification.setOnClickListener(this);
    }


    @Override
    public void showHomeScreen() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new HomeFragment()).commit();
    }

    @Override
    public void showNotiScreen() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new WalletFragment()).commit();
    }

    @Override
    public void showProfileScreen() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new ProfileFragment()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
        }
    }
}