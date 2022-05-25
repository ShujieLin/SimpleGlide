package com.shujie.simple_glide.cache.disk.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.Printer;

import com.shujie.simple_glide.Value;
import com.shujie.simple_glide.cache.disk.DiskLruCache;
import com.shujie.simple_glide.utils.Tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @date: 2022/5/24
 * @author: linshujie
 */
public class DiskLruCacheImpl {
    private static final String TAG = "DiskLruCacheImpl";
    private final String DISKLRU_CACHE_DIR = "disk_lru_cache_dir";

    private DiskLruCache diskLruCache;
    private final int APP_VERSION = 1;
    private final int VALUE_COUNT = 1;
    private final int MAX_SIZE = 1024 * 1024 * 10;

    public DiskLruCacheImpl() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + DISKLRU_CACHE_DIR);
        Log.d(TAG, "DiskLruCacheImpl: file = " + file.getAbsolutePath());
        try {
            diskLruCache = DiskLruCache.open(file,APP_VERSION,VALUE_COUNT,MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Value get(String key) {
        Tool.checkNotEmpty(key);
        InputStream inputStream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null){
                Value value = new Value();
                inputStream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //设置并保存
                value.setBitmap(bitmap);
                value.setKey(key);
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "get: e:" + e.getMessage());
                }
            }
        }

        return null;
    }

    public void put(String key, Value value) {
        Tool.checkNotEmpty(key);
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = diskLruCache.edit(key);
            outputStream = editor.newOutputStream(0);
            Bitmap bitmap = value.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }finally {
            {
                try {
                    if (editor != null) {
                        editor.commit();
                    }
                    diskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
