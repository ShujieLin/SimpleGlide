package com.shujie.simple_glide.cache;

import com.shujie.simple_glide.Value;
import com.shujie.simple_glide.ValueCallBack;
import com.shujie.simple_glide.utils.Tool;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动缓存
 *
 * @date: 2022/5/23
 * @author: linshujie
 */
public class ActiveCache {
    private ValueCallBack valueCallback;

    //作为缓存的容器
    private Map<String, Value> mapList = new HashMap<>();

    public ActiveCache(ValueCallBack valueCallback) {
        this.valueCallback = valueCallback;
    }

    public Value get(String key) {
        Value value = mapList.get(key);
        if (value != null){
            return value;
        }
        return null;
    }

    public void put(String key, Value value) {
        Tool.checkNotEmpty(key);

        value.setCallback(this.valueCallback);

        mapList.put(key,value);
    }

    /**
     * 清空缓存，回收内存
     */
    public void recycleActive() {
        for (Map.Entry<String, Value> valueEntry : mapList.entrySet()) {
            valueEntry.getValue().recycle();
            mapList.remove(valueEntry.getKey());
        }
    }
}
