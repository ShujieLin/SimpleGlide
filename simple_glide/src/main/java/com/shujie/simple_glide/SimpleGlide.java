package com.shujie.simple_glide;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * @date: 2022/5/23
 * @author: linshujie
 */
public class SimpleGlide {
    private static SimpleGlide instance;

    private RequestManagerRetriever retiever;

    public SimpleGlide(RequestManagerRetriever retiever) {
        this.retiever = retiever;
    }

    public static synchronized SimpleGlide getInstance(RequestManagerRetriever retiever) {
        if (instance == null){
            instance = new SimpleGlide(retiever);
        }
        return instance;
    }

    /**
     *
     * @param fragmentActivity
     * @return
     */
    public static RequestManager with(FragmentActivity fragmentActivity) {
        return getRetiever(fragmentActivity).get(fragmentActivity);
    }

    private static RequestManagerRetriever getRetiever(Context context) {
        return SimpleGlide.get(context).getRetiever();
    }

    private RequestManagerRetriever getRetiever() {
        return retiever;
    }

    /**
     * 构建SimpleGlide，将会执行{@link SimpleGlide#getInstance(RequestManagerRetriever)}
     * @param context
     * @return
     */
    private static SimpleGlide get(Context context){
        return new SimpleGlideBuilder(context).build();
    }
}
