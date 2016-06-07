package com.giffar.ctour.controllers;

import android.content.Context;
import android.util.Log;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.callbacks.OnClubCallBack;
import com.giffar.ctour.controllers.parsers.ClubParser;
import com.giffar.ctour.entitys.Club;
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
public class ClubController {
    Context context;
    ClubParser clubParser;
    public ClubController(Context context){
        this.context = context;
        this.clubParser = new ClubParser(context);
    }

    public void postResponseCreateClub(RequestParams params, final OnClubCallBack onClubCallBack){
        CTourRestClient.post("createclub.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONObject d= response.optJSONObject("d");
                    Club club = clubParser.parseJsonObject(d);
                    APP.setConfig(context, Preferences.STATUS_MEMBER,"1");
                    APP.setConfig(context,Preferences.CLUB_ID,club.getId());
                    onClubCallBack.OnSuccess(club,response.optString("message"));
                }else{
                    onClubCallBack.OnFailed(response.optString("message"));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Log.e("ERROR",errorResponse.toString());
                }catch (Exception e){

                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onClubCallBack.OnFinish();
            }
        });
    }
    public void postResponseListClub(RequestParams params, final OnClubCallBack onClubCallBack){
        CTourRestClient.post("listclub.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONArray list = response.optJSONArray("list");
                    List<Club> clubs = clubParser.parseJsonArray(list);
                    onClubCallBack.OnSuccess(clubs,response.optString("message"));
                }else{
                    onClubCallBack.OnFailed(response.optString("message"));
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
                onClubCallBack.OnFinish();
            }
        });
    }


    public void postResponseDetailClub(RequestParams params, final OnClubCallBack onClubCallBack){
        CTourRestClient.post("detailclub.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONObject d = response.optJSONObject("d");
                    Club club = clubParser.parseJsonObject(d);
                    onClubCallBack.OnSuccess(club,response.optString("message"));
                }else{
                    onClubCallBack.OnFailed(response.optString("message"));
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
                onClubCallBack.OnFinish();
            }
        });
    }

    public void postResponseJoinClub(RequestParams params, final OnClubCallBack onClubCallBack){
        CTourRestClient.post("joinclub.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
//                    JSONObject d = response.optJSONObject("d");
                    Club club=null;
                    onClubCallBack.OnSuccess(club,response.optString("message"));
                }else{
                    onClubCallBack.OnFailed(response.optString("message"));
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
                onClubCallBack.OnFinish();
            }
        });
    }
}
