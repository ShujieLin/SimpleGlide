package com.shujie.simple_glide;

import android.content.Context;

/**
 * @description:
 * @date: 2022/5/24
 * @author: linshujie
 */
public interface ILoadData {
    /**
     *
     * @param path
     * @param responseListener
     * @param context
     * @return
     */
    Value loadResource(String path, ResponseListener responseListener, Context context);
}
