package com.xrzx.commonlibrary.utils.http;

import com.xrzx.commonlibrary.callback.HttpCallBack;
import com.xrzx.commonlibrary.callback.ResponseCallBack;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Description http 工具类
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class HttpUtils {
    /**
     * OkHttpClient 实例(单例模式)
     */
    private static volatile OkHttpClient okHttpClient = null;
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(30000, TimeUnit.SECONDS);
                    builder.hostnameVerifier((hostname, session) -> true);
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 获取InputStream输入流（GET）
     *
     * @param url      地址
     * @param callBack 回调
     */
    public static void getRequestInputStreamByGet(final String url, final HttpCallBack<InputStream> callBack) {
        OkHttpClient client = HttpUtils.getOkHttpClient();
        Request request = new Request.Builder()
                .url(url).build();
        try {
            try (Response re = client.newCall(request).execute();
                 ResponseBody body = re.body()) {
                if (body == null) {
                    callBack.onError(new Exception("获取失败."));
                    return;
                }
                callBack.onSuccess(body.byteStream());
            }
        } catch (IOException e) {
            callBack.onError(e);
        }
    }

    /**
     * 获取网页html(字符串)（get）
     *
     * @param url          地址
     * @param charEncoding 编码
     * @param callBack     回调
     */
    public static void getHtmlStringByGet(final String url, final String charEncoding, final ResponseCallBack<String> callBack) {
        HttpUtils.getRequestInputStreamByGet(url, new HttpCallBack<InputStream>() {
            @Override
            public void onSuccess(InputStream in) {
                try {
                    StringBuilder response = toHtmlString(in, charEncoding);
                    callBack.onSuccess(response.toString());
                } catch (IOException e) {
                    callBack.onError(e);
                }
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    /**
     * 获取InputStream输入流（Post）
     *
     * @param url             地址
     * @param formBodyBuilder 参数字典
     * @param callBack        回调
     */
    public static void getRequestInputStreamByPost(final String url, FormBody.Builder formBodyBuilder, final HttpCallBack<InputStream> callBack) {
        OkHttpClient client = HttpUtils.getOkHttpClient();

        Request request = new Request.Builder()
                .url(url).post(formBodyBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                callBack.onError(new Exception("获取失败.", e));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    callBack.onError(new Exception("获取失败."));
                    return;
                }
                callBack.onSuccess(body.byteStream());
            }
        });
    }


    /**
     * 获取网页html(字符串)（Post）
     *
     * @param url          地址
     * @param charEncoding 编码
     * @param callBack     回调
     */
    public static void getHtmlStringByPost(final String url, FormBody.Builder formBodyBuilder, final String charEncoding, final ResponseCallBack<String> callBack) {
        HttpUtils.getRequestInputStreamByPost(url, formBodyBuilder, new HttpCallBack<InputStream>() {
            @Override
            public void onSuccess(InputStream in) {
                try {
                    StringBuilder response = toHtmlString(in, charEncoding);
                    callBack.onSuccess(response.toString());
                } catch (IOException e) {
                    callBack.onError(e);
                }
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    /**
     * 将字符流转为字符串
     *
     * @param in           InputStream
     * @param charEncoding 编码
     * @return str
     * @throws IOException io异常
     */
    public static synchronized StringBuilder toHtmlString(InputStream in, final String charEncoding) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, charEncoding))) {
            String line = reader.readLine();
            while (line != null) {
                response.append(line);
                line = reader.readLine();
            }
        }
        return response;
    }
}