package com.giffar.ctour.services;

import android.content.Context;

import com.giffar.ctour.APP;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by wafdamufti on 10/1/15.
 */
public class CTourRestClient {
    private static final String BASE_URL = "http://www.ctour.web.id/API/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), params, responseHandler);
        post(url, params, responseHandler, false, null);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Boolean useToken, Context context) {
//        try{
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
//            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
//            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            client.setSSLSocketFactory(socketFactory);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        client.post(getAbsoluteUrl(url), params, responseHandler);
        if (useToken) {
            client.addHeader("token", APP.getConfig(context, APP.TOKEN_KEY));
        }

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
