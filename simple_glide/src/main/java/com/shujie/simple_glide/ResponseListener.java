package com.shujie.simple_glide;

/**
 * 网络响应监听器
 *
 * @date: 2022/5/24
 * @author: linshujie
 */
public interface ResponseListener {
    public void responseSuccess(Value value);
    public void responseException(Exception e);
}
