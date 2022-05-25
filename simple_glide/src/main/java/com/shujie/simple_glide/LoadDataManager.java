package com.shujie.simple_glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2022/5/24
 * @author: linshujie
 */
public class LoadDataManager implements ILoadData,Runnable{
    private static final String TAG = "LoadDataManager";
    private String path;
    private ResponseListener responseListenr;
    private Context context;

    @Override
    public Value loadResource(String path, ResponseListener responseListener, Context context) {
        this.path = path;
        this.responseListenr = responseListener;
        this.context = context;

        Uri uri = Uri.parse(path);
        if ("HTTP".equalsIgnoreCase(uri.getScheme()) || "HTTPS".equalsIgnoreCase(uri.getScheme())){
            new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60,
                    TimeUnit.MINUTES,
                    new SynchronousQueue<Runnable>())
                    .execute(this);
        }
        return null;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);

            final int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // TODO: 2022/5/24 手续需要扩展的功能：图片缩放
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Value value = new Value();
                        value.setBitmap(bitmap);

                        //成功回调
                        responseListenr.responseSuccess(value);
                    }
                });

            }else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        responseListenr.responseException(new IllegalStateException("请求失败，getResponseCode = " + responseCode));
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: 关闭流异常 :" + e.getMessage());
                }
            }

            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
    }
}
