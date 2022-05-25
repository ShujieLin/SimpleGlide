package com.shujie.simple_glide.cache;

import android.os.Build;
import android.util.LruCache;

import com.shujie.simple_glide.Value;

/**
 * 内存缓存
 *
 * @date: 2022/5/23
 * @author: linshujie
 */
public class MemoryCache extends LruCache<String, Value> {
    public MemoryCache(int memoryMaxSize) {
        super(memoryMaxSize);

    }

    /**
     * 最开始的时候
     * int result = value.getBitmap().getRowBytes(); // native
     * API 12  3.0
     * int result = value.getBitmap().getByteCount(); // java
     * API  19 4.4
     * int result = value.getBitmap().getAllocationByteCount();
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(String key, Value value) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.KITKAT){
            return value.getBitmap().getAllocationByteCount();
        }
        return value.getBitmap().getByteCount();
    }
}
