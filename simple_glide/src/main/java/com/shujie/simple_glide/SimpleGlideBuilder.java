package com.shujie.simple_glide;

import android.content.Context;

/**
 * @date: 2022/5/23
 * @author: linshujie
 */
public class SimpleGlideBuilder {
    public SimpleGlideBuilder(Context context) {
    }

    public SimpleGlide build() {
        RequestManagerRetriever retriever = new RequestManagerRetriever();
        return SimpleGlide.getInstance(retriever);
    }
}
