package com.xrzx.commonlibrary.callback;

/**
 * @Description 结果回调
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public interface ResultCallBack<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
