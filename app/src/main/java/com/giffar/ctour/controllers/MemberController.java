package com.giffar.ctour.controllers;

import android.content.Context;
import android.util.Log;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.callbacks.OnMemberCallback;
import com.giffar.ctour.controllers.parsers.MemberParser;
import com.giffar.ctour.entitys.Member;
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
public class MemberController {
    Context context;
    MemberParser memberParser;
    public MemberController(Context context){
        this.context = context;

        this.memberParser =new MemberParser(context);
    }

    public void postResponseLogin(RequestParams params, final OnMemberCallback onMemberCallback){
        CTourRestClient.post("login.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONObject d = response.optJSONObject("d");
                    Member member = memberParser.parseJsonObject(d);
                    APP.setConfig(context, Preferences.LOGGED_USER_ID,member.getId());
                    APP.setConfig(context,Preferences.USER_LOGIN,"Y");
                    APP.setConfig(context,Preferences.CLUB_ID,member.getId_club());
                    APP.setConfig(context,Preferences.STATUS_MEMBER,member.getStatus());
                    onMemberCallback.onSuccess(member,response.optString("message"));
                }else{
                    onMemberCallback.onFailed(response.optString("message"));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    onMemberCallback.onFailed(errorResponse.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onMemberCallback.OnFinish();
            }
        });
    }

    public void PostResponseRegister(RequestParams params, final OnMemberCallback onMemberCallback){
        CTourRestClient.post("register.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONObject d = response.optJSONObject("d");
                    Member member = memberParser.parseJsonObject(d);
                    APP.setConfig(context, Preferences.LOGGED_USER_ID,member.getId());
                    APP.setConfig(context,Preferences.USER_LOGIN,"Y");
                    APP.setConfig(context,Preferences.CLUB_ID,member.getId_club());
                    onMemberCallback.onSuccess(member,response.optString("message"));
                }else{
                    onMemberCallback.onFailed(response.optString("message"));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    onMemberCallback.onFailed(errorResponse.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onMemberCallback.OnFinish();
            }
        });
    }

    public void PostResponseJoinClub(RequestParams params){
        CTourRestClient.post("joinclub",params,new JsonHttpResponseHandler(){
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

    public void postResponseDetailMember(RequestParams params){
        CTourRestClient.post("detailmember",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONObject d = response.optJSONObject("d");

                Member member = memberParser.parseJsonObject(d);

                APP.setConfig(context, Preferences.LOGGED_USER_ID,member.getId());
                APP.setConfig(context,Preferences.USER_LOGIN,"Y");
                APP.setConfig(context,Preferences.CLUB_ID,member.getId_club());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void postResponseListMember(RequestParams params, final OnMemberCallback onMemberCallback){
        CTourRestClient.post("clubmember.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONArray list = response.optJSONArray("list");
                    List<Member> members = memberParser.parseJsonArray(list);
                    onMemberCallback.onSuccess(members,response.optString("message"));
                }else{
                    onMemberCallback.onFailed(response.optString("message"));
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
                onMemberCallback.OnFinish();
            }
        });
    }
    public void postResponseNearbyMember(RequestParams params, final OnMemberCallback onMemberCallback){
        CTourRestClient.post("nearbymember.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONArray list = response.optJSONArray("list");
                    List<Member> members = memberParser.parseJsonArray(list);
                    onMemberCallback.onSuccess(members,response.optString("message"));
                }else{
                    onMemberCallback.onFailed(response.optString("message"));
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
                onMemberCallback.OnFinish();
            }
        });
    }

    public void postResponseListMemberTouring(RequestParams params, final OnMemberCallback onMemberCallback){
        CTourRestClient.post("listjoinplanningmember.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONArray list = response.optJSONArray("list");
                    List<Member> members = memberParser.parseJsonArray(list);
                    onMemberCallback.onSuccess(members,response.optString("message"));
                }else{
                    onMemberCallback.onFailed(response.optString("message"));
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
                onMemberCallback.OnFinish();
            }
        });
    }
    public void postResponseListMemberNearby(RequestParams params, final OnMemberCallback onMemberCallback){
        CTourRestClient.post("listmembernearby",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONArray list = response.optJSONArray("list");
                    List<Member> members = memberParser.parseJsonArray(list);
                    onMemberCallback.onSuccess(members,response.optString("message"));
                }else{
                    onMemberCallback.onFailed(response.optString("message"));
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
                onMemberCallback.OnFinish();
            }
        });
    }
}
