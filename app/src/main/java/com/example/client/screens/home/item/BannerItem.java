package com.example.client.screens.home.item;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.models.banner.BannerModel;

import java.util.List;

public class BannerItem extends FragmentStateAdapter {
    private List<BannerModel> items;
    public static class MyViewHolder extends Fragment{
        private BannerModel item;
        public MyViewHolder(BannerModel item){
            this.item = item;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.item_banner,null);
            ImageView banner = view.findViewById(R.id.banner);
            Glide.with(getContext()).asBitmap().placeholder(R.drawable.subject_default)
                    .load(item.getLink()).into(banner);
            return view;
        }
    }

    public BannerItem(@NonNull FragmentActivity fragmentActivity, List<BannerModel> items) {
        super(fragmentActivity);
        this.items = items;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        BannerModel item = items.get(position);
        MyViewHolder fragment = new MyViewHolder(item);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
