package com.xrzx.reader.common.http.callback;

public interface HttpCallBack<T> {
    void onSuccess(T in);
    void onError(Exception e);
}
