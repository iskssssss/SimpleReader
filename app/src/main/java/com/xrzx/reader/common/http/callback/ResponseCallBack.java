package com.xrzx.reader.common.http.callback;

public interface ResponseCallBack<T> {
    void onSuccess(T data);
    void onError(Exception e);
}
