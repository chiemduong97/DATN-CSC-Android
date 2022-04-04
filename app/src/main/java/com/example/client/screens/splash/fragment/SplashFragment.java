package com.example.client.screens.splash.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.client.R;
import com.example.client.models.splash.Splash;
import com.example.client.utils.ICallBack;

public class SplashFragment extends Fragment {
    private Context context;
    private ImageView imageView;
    private TextView title,description,btnNext,btnPrev;
    private Splash splash;
    private int position;
    private ICallBack nextListener;
    private ICallBack prevListener;

    public SplashFragment(){

    }

    public SplashFragment(Context context,Splash splash,int position){
        this.context = context;
        this.splash = splash;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_splash,null);
        imageView = view.findViewById(R.id.iv);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrev = view.findViewById(R.id.btnPrev);
        imageView.setImageResource(splash.getImg());
        title.setText(splash.getTitle());
        description.setText(splash.getDescription());
        if(position==0){
            btnPrev.setBackgroundResource(R.drawable.bg_btn_disable);
            btnPrev.setEnabled(false);
        }
        if(position==2){
            btnNext.setText("Bắt đầu");
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextListener.onCallback();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevListener.onCallback();
            }
        });
        return view;
    }

    public SplashFragment setNextListener(ICallBack nextListener) {
        this.nextListener = nextListener;
        return this;
    }

    public SplashFragment setPrevListener(ICallBack prevListener){
        this.prevListener = prevListener;
        return this;
    }
}
