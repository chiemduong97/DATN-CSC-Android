package com.example.client.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull
            Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment)
                .addToBackStack(null);

        transaction.commit();
    }

    public static void addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, String TAG) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment).addToBackStack(TAG);
        transaction.commit();
    }

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager, @NonNull
            Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);

        transaction.commit();
    }

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager, @NonNull
            Fragment fragment, int frameId, String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);

        transaction.commit();
    }

    public static void popFragment(@NonNull FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, String tag) {
        if (!fragmentManager.popBackStackImmediate(tag, 0)) {
            addFragment(fragmentManager, fragment, frameId, tag);
        }
    }

}
