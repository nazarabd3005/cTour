package com.giffar.ctour.controllers;

import android.content.Context;

import com.giffar.ctour.controllers.parsers.ChattingParser;
import com.giffar.ctour.entitys.Chatting;
import com.giffar.ctour.services.CTourRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nazar on 5/16/2016.
 */
public class ChattingController {
    Context context;
    ChattingParser chattingParser;
    public ChattingController(Context context){
        this.context = context;
        this.chattingParser = new ChattingParser(context);
    }

    public void postResponseSendMessage(RequestParams params){
        CTourRestClient.post("sendmessage",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    public void postResponselistChatting(RequestParams params){
        CTourRestClient.post("listchat",params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray list = response.optJSONArray("list");
                List<Chatting> chattings = chattingParser.parseJsonArray(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
