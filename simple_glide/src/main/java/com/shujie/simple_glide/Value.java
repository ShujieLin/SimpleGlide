package com.shujie.simple_glide;

import android.graphics.Bitmap;

/**
 * @date: 2022/5/23
 * @author: linshujie
 */
public class Value {
    private ValueCallBack valueCallBack;
    private String key;
    private Bitmap bitmap;


    public void setCallback(ValueCallBack valueCallback) {
        this.valueCallBack = valueCallback;
    }


    public void recycle() {
        if (valueCallBack != null){
            valueCallBack.valueNonUserListener(key,this);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setKey(String key) {
        this.key = key;
    }
}