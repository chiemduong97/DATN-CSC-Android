package com.example.client.screens.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.R;

import com.example.client.app.Constants;
import com.example.client.models.event.Event;
import com.example.client.models.noti.Notification;
import com.example.client.models.order.OrderModel;
import com.example.client.screens.cart.activity.CartActivity;
import com.example.client.screens.home.fragment.HomeFragment;
import com.example.client.screens.main.present.MainPresent;
import com.example.client.screens.map.activity.MapsActivity;
import com.example.client.screens.noti.activity.NotificationActivity;
import com.example.client.screens.order.detail.OrderDetailActivity;
import com.example.client.screens.order.history.activity.OrderHistoryActivity;
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
    private ImageView imvNoti;
    private CardView cvCartPlace;
    private TextView tvQuantity;
    private RelativeLayout rllCountOrder, rllOrder, rllLoading;
    private TextView tvCountOrder, tvSeeMore, tvOrderStatus, tvOrderAddress, tvSeeOrder;
    private OrderModel order;
    private String tag = HomeFragment.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imvNoti = findViewById(R.id.imv_noti);
        navigation = findViewById(R.id.navigation);
        cvCartPlace = findViewById(R.id.cv_cart_place);
        tvQuantity = findViewById(R.id.tv_quantity);
        rllCountOrder = findViewById(R.id.rll_count_order);
        rllOrder = findViewById(R.id.rll_order);
        rllLoading = findViewById(R.id.rll_loading);
        tvCountOrder = findViewById(R.id.tv_count_order);
        tvSeeMore = findViewById(R.id.tv_see_more);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvOrderAddress = findViewById(R.id.tv_order_address);
        tvSeeOrder = findViewById(R.id.tv_see_order);

        mPresent = new MainPresent(this);


        mPresent.replaceFragment(R.id.menu_home);
        mPresent.getCartFromRes();

        mPresent.getListOrderFromService();

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

        imvNoti.setOnClickListener(this);
        cvCartPlace.setOnClickListener(this);
        tvSeeMore.setOnClickListener(this);
        tvSeeOrder.setOnClickListener(this);
    }


    @Override
    public void showHomeScreen() {
        tag = HomeFragment.class.getName();

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        if (!fragmentManager.popBackStackImmediate (tag, 0)) {
            fragmentTransaction.add(R.id.frameLayout,new HomeFragment()).addToBackStack(tag).commit();
        } else {
            fragmentManager.popBackStack(tag,0);
        }
    }

    @Override
    public void showNotiScreen() {
        tag = WalletFragment.class.getName();

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        if (!fragmentManager.popBackStackImmediate (tag, 0)) {
            fragmentTransaction.add(R.id.frameLayout,new WalletFragment()).addToBackStack(tag).commit();
        } else {
            fragmentManager.popBackStack(tag,0);
        }
    }

    @Override
    public void showProfileScreen() {
        tag = ProfileFragment.class.getName();
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        if (!fragmentManager.popBackStackImmediate (tag, 0)) {
            fragmentTransaction.add(R.id.frameLayout,new ProfileFragment()).addToBackStack(tag).commit();
        } else {
            fragmentManager.popBackStack(tag,0);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
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
    public void showLoading() {
        rllLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rllLoading.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(int errMessage) {
        Toast.makeText(this, getString(errMessage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOrderCount(int count) {
        rllCountOrder.setVisibility(View.VISIBLE);
        tvCountOrder.setText(getString(R.string.text_count_order).replace("%s", count + ""));
    }

    @Override
    public void hideOrderCount() {
        rllCountOrder.setVisibility(View.GONE);

    }

    @Override
    public void showOrder(OrderModel order) {
        this.order = order;
        rllOrder.setVisibility(View.VISIBLE);
        tvOrderStatus.setText(order.getStatusString());
        tvOrderAddress.setText(order.getAddress());
    }

    @Override
    public void hideOrder() {
        rllOrder.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_noti:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.cv_cart_place:
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                break;
            case R.id.tv_see_more:
                startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
                break;
            case R.id.tv_see_order:
                startActivity(OrderDetailActivity.Companion.newInstance(MainActivity.this, order.getOrdercode()));
                break;
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
            case Constants.EventKey.UPDATE_STATUS_ORDER:
                mPresent.getListOrderFromService();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}