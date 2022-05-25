package com.shujie.simple_glide;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 *
 *
 * @date: 2022/5/23
 * @author: linshujie
 */
public class RequestManager {
    private static final String FRAGMENT_ACTIVITY_NAME = "FRAGMENT_ACTIVITY_NAME";
    private static final int NEXT_HANDLER_MSG = 0;
    private static RequestTargetEngine callback;


    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });
    FragmentActivity fragmentActivity;
    private Context requestManagerContext;

    /**
     * 通过fragmentActivity 创建出空白fragment，用于监听生命周期
     *
     * @param fragmentActivity
     */
    public RequestManager(FragmentActivity fragmentActivity) {
        if (callback == null) {
            callback = new RequestTargetEngine();
        }
        this.fragmentActivity = fragmentActivity;
        requestManagerContext = fragmentActivity;

        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME);
        if (null == fragment) {
            fragment = new FragmentActivityFragmentManager(callback);

            supportFragmentManager.beginTransaction().add(fragment, FRAGMENT_ACTIVITY_NAME).commitAllowingStateLoss();
        }

        //防止创建两个fragment，确保只有一个fragment，第二层保障
        handler.sendEmptyMessage(NEXT_HANDLER_MSG);
    }

    public RequestTargetEngine load(String path) {
        // TODO: 2022/5/23
        //移除
        handler.removeMessages(NEXT_HANDLER_MSG);
        callback.loadValueInitAction(path, requestManagerContext);
        return callback;
    }
}
