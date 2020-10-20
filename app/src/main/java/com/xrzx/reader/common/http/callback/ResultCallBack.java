package com.xrzx.reader.common.http.callback;

public interface ResultCallBack<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
