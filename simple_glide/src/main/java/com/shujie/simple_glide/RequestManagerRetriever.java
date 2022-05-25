package com.shujie.simple_glide;

import androidx.fragment.app.FragmentActivity;

/**
 * 获取RequestManager
 *
 * @date: 2022/5/23
 * @author: linshujie
 */
public class RequestManagerRetriever {
    public RequestManager get(FragmentActivity fragmentActivity) {
        return new RequestManager(fragmentActivity);
    }
}
