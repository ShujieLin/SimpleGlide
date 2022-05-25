package com.shujie.simple_glide;

/**
 * @date: 2022/5/23
 * @author: linshujie
 */
public interface LifecycleCallback {
    void glideInitAction();
    void glideStopAction();
    void glideRecycleAction();
}
