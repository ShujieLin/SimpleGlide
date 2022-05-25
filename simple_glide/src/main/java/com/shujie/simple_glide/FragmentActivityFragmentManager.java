package com.shujie.simple_glide;

import android.util.Log;

import androidx.fragment.app.Fragment;

/**
 * 空白fragment的实现类，监听生命activity或者fragment的生命周期，随着
 *
 * @date: 2022/5/23
 * @author: linshujie
 */
public class FragmentActivityFragmentManager extends Fragment {
    private static final String TAG = "FragmentActivityFragmen";
    private LifecycleCallback callback;

    public FragmentActivityFragmentManager(LifecycleCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "SimpleGlide空白fragment onStart: ");
        if (callback != null){
            callback.glideInitAction();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "SimpleGlide空白fragment  onStop: ");
        if (callback != null){
            callback.glideStopAction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SimpleGlide空白fragment  onDestroy: ");
        if (callback != null){
            callback.glideRecycleAction();
        }
    }
}
