package com.xrzx.reader.common.callback;

/**
 * @Description http回调
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public interface HttpCallBack<T> {
    void onSuccess(T in);
    void onError(Exception e);
}
