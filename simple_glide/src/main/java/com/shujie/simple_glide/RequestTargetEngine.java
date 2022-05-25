package com.shujie.simple_glide;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.shujie.simple_glide.cache.ActiveCache;
import com.shujie.simple_glide.cache.MemoryCache;
import com.shujie.simple_glide.cache.disk.my.DiskLruCacheImpl;
import com.shujie.simple_glide.utils.Tool;

/**
 * @date: 2022/5/23
 * @author: linshujie
 */
public class RequestTargetEngine implements LifecycleCallback, ValueCallBack, ResponseListener {
    private static final String TAG = "RequestTargetEngine";
    // FIXME: 2022/5/23 后续优化为系统内存的百分比
    private static final int MEMORY_MAX_SIZE = 1024 * 1024 * 60;
    private ActiveCache activeCache;
    private MemoryCache memoryCache;
    private DiskLruCacheImpl diskLruCache;
    private String path;
    private Context simpleGlideContext;
    //磁盘使用的key
    private String key;

    private ImageView imageView;

    public RequestTargetEngine() {
        if (activeCache == null) {
            activeCache = new ActiveCache(this);
        }
        if (memoryCache == null) {
            memoryCache = new MemoryCache(MEMORY_MAX_SIZE);
        }
        this.diskLruCache = new DiskLruCacheImpl();
    }

    @Override
    public void glideInitAction() {
        Log.d(TAG, "glideInitAction: ");
    }

    @Override
    public void glideStopAction() {
        Log.d(TAG, "glideStopAction: ");
    }

    @Override
    public void glideRecycleAction() {
        Log.d(TAG, "glideRecycleAction: ");
        if (activeCache != null) {
            activeCache.recycleActive();
        }
    }

    public void into(ImageView iv) {
        this.imageView = iv;
        Tool.checkNotEmpty(imageView);
        Tool.assertMainThread();

        Value value = cacheAction();
        if (imageView != null){
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    /**
     * 尝试从缓存获取图片资源，若取不到从磁盘或者网络获取{@link RequestTargetEngine#responseSuccess(Value)}
     *
     * @return
     */
    private Value cacheAction() {
        //活动缓存
        Value value = activeCache.get(key);

        if (null != value) {
            Log.d(TAG, "cacheAction: 从活动缓存获取图片");
            return value;
        }

        //内存缓存
        value = memoryCache.get(key);
        if (value != null) {
            Log.d(TAG, "cacheAction: 从内存缓存获取图片");

            //把内存缓存的图片移动到活动缓存中
            activeCache.put(key, value);
            memoryCache.remove(key);
            return value;
        }

        //磁盘缓存
        value = diskLruCache.get(key);
        if (null != value) {
            Log.d(TAG, "cacheAction: 从磁盘缓存获取图片");
            activeCache.put(key, value);
            return value;
        }

        //从网络或者其他io加载资源
        value = new LoadDataManager().loadResource(path, this, simpleGlideContext);
        if (value != null) {
            Log.d(TAG, "cacheAction: 缓存找到不，从网络加载图片，请稍等...");
            return value;
        }

        // TODO: 2022/5/24 从磁盘io获取资源

        return null;
    }

    /**
     * 赋值
     *
     * @param path 网络图片资源路径
     * @param requestManagerContext 用户调用api传进来的上下文
     */
    public void loadValueInitAction(String path, Context requestManagerContext) {
        Log.d(TAG, "loadValueInitAction() called with: path = [" + path + "], requestManagerContext = [" + requestManagerContext + "]");
        this.path = path;
        this.simpleGlideContext = requestManagerContext;
        this.key = new Key(path).getKey();
    }

    @Override
    public void valueNonUserListener(String key, Value value) {
        if (key != null && value != null) {
            memoryCache.put(key, value);
        }
    }

    @Override
    public void responseSuccess(Value value) {
        if (value != null) {
            saveCache(key, value);

            imageView.setImageBitmap(value.getBitmap());
        }
    }

    /**
     * 保存到磁盘缓存中
     *
     * @param key
     * @param value
     */
    private void saveCache(String key, Value value) {
        Log.d(TAG, "saveCache() called with: key = [" + key + "], value = [" + value + "]");
        value.setKey(key);
        if (diskLruCache != null) {
            diskLruCache.put(key, value);
        }
    }

    @Override
    public void responseException(Exception e) {
        Log.d(TAG, "responseException() called with: e = [" + e + "]");
    }
}
