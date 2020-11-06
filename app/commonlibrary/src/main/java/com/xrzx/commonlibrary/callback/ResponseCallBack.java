package com.xrzx.commonlibrary.callback;

/**
 * @Description 响应回调
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public interface ResponseCallBack<T> {
    void onSuccess(T data);
    void onError(Exception e);
}
