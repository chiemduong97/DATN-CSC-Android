package com.example.client.screens.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.R;

import com.example.client.app.Constants;
import com.example.client.models.event.Event;
import com.example.client.screens.cart.activity.CartActivity;
import com.example.client.screens.home.fragment.HomeFragment;
import com.example.client.screens.main.present.MainPresent;
import com.example.client.screens.noti.activity.NotificationActivity;
import com.example.client.screens.profile.fragment.ProfileFragment;
import com.example.client.screens.wallet.fragment.WalletFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;


public class MainActivity extends AppCompatActivity implements IMainView, View.OnClickListener{
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MainPresent mPresent;
    private ImageView notification;
    private CardView cvCartPlace;
    private TextView tvQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notification = findViewById(R.id.notification);
        navigation = findViewById(R.id.navigation);
        cvCartPlace = findViewById(R.id.cv_cart_place);
        tvQuantity = findViewById(R.id.tv_quantity);

        mPresent = new MainPresent(this);


        mPresent.replaceFragment(R.id.menu_home);
        mPresent.getCartFromRes();

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
        cvCartPlace.setOnClickListener(this);
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
    public void showCart(int quantity) {
        cvCartPlace.setVisibility(View.VISIBLE);
        tvQuantity.setText(MessageFormat.format("{0}", quantity));
    }

    @Override
    public void hideCart() {
        cvCartPlace.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.cv_cart_place:{
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.EventKey.UPDATE_CART:
                mPresent.getCartFromRes();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}