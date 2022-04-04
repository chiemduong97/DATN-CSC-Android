package com.example.client.screens.splash.item;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.client.models.splash.Splash;
import com.example.client.screens.splash.fragment.SplashFragment;
import com.example.client.utils.ICallBack;

import java.util.List;

public class SplashItem extends FragmentStateAdapter {
    private List<Splash> splashes;
    private Context context;
    private ICallBack nextListener;
    private ICallBack prevListener;
    public SplashItem(@NonNull FragmentActivity fragmentActivity, List<Splash> splashes, Context context, ICallBack nextListener, ICallBack prevListener) {
        super(fragmentActivity);
        this.splashes = splashes;
        this.context = context;
        this.nextListener = nextListener;
        this.prevListener = prevListener;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Splash splash = splashes.get(position);
        SplashFragment fragment = new SplashFragment(context,splash,position);
        fragment.setNextListener(nextListener);
        fragment.setPrevListener(prevListener);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return splashes.size();
    }
}
