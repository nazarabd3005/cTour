package com.giffar.ctour.controllers;

import android.content.Context;
import android.util.Log;

import com.giffar.ctour.callbacks.OnStatusCallback;
import com.giffar.ctour.controllers.parsers.StatusParser;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.services.CTourRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nazar on 5/16/2016.
 */
public class StatusController {
    Context context;
    StatusParser statusParser;
    public StatusController(Context context){
        this.context = context;
        this.statusParser = new StatusParser(context);
    }

    public void postResponseCreateStatus(RequestParams params, final OnStatusCallback onStatusCallback){
        CTourRestClient.post("createtimeline.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    Timeline timeline=null;
                    onStatusCallback.OnSuccess(timeline,response.optString("message"));
                }else{
                    onStatusCallback.OnFailed(response.optString("message"));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Log.e("ERROR",errorResponse.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onStatusCallback.OnFinish();
            }
        });
    }
    public void postResponseListStatus(RequestParams params, final OnStatusCallback onStatusCallback){
        CTourRestClient.post("listtimeline.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONArray list = response.optJSONArray("list");
                    List<Timeline> timelines = statusParser.parseJsonArray(list);
                    onStatusCallback.OnSuccess(timelines,response.optString("message"));
                }else{
                    onStatusCallback.OnFailed(response.optString("message"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Log.e("ERROR",errorResponse.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void postResponseDetailstatus(RequestParams params){
        CTourRestClient.post("detailstatus",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONObject d = response.optJSONObject("d");
                Timeline timeline = statusParser.parseJsonObject(d);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
