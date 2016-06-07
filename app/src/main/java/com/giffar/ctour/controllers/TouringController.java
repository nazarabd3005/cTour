package com.giffar.ctour.controllers;

import android.content.Context;
import android.util.Log;

import com.giffar.ctour.callbacks.OnTourCallback;
import com.giffar.ctour.controllers.parsers.TouringParser;
import com.giffar.ctour.entitys.Touring;
import com.giffar.ctour.services.CTourRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nazar on 5/16/2016.
 */
public class TouringController {
    Context context;
    TouringParser touringParser;
    public TouringController(Context context){
        this.context = context;
        this.touringParser = new TouringParser(context);
    }

    public void postResponseCreatePlanningTouring(RequestParams params, final OnTourCallback onTourCallback){
        CTourRestClient.post("createplanning.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    Touring touring = null;
                    onTourCallback.OnSuccess(touring,response.optString("message"));

                }else{
                    onTourCallback.OnFailed(response.optString("message"));
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
                onTourCallback.OnFinish();
            }
        });
    }

    public void postResponseJoinPlanningTouring(RequestParams params, final OnTourCallback onTourCallback){
        CTourRestClient.post("accepttouring.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    Touring touring = null;
                    onTourCallback.OnSuccess(touring,response.optString("message"));
                }else{
                    onTourCallback.OnFailed(response.optString("message"));
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
                onTourCallback.OnFinish();
            }
        });
    }
    public void postResponseEmergencyCall(RequestParams params, final OnTourCallback onTourCallback){
        CTourRestClient.post("emergencycall.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    Touring touring = null;
                    onTourCallback.OnSuccess(touring,response.optString("message"));
                }else{
                    onTourCallback.OnFailed(response.optString("message"));
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
                onTourCallback.OnFinish();
            }
        });
    }
    public void postResponseDeclinePlanningTouring(RequestParams params, final OnTourCallback onTourCallback){
        CTourRestClient.post("declinetouring",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    Touring touring = null;
                    onTourCallback.OnSuccess(touring,response.optString("message"));
                }else{
                    onTourCallback.OnFailed(response.optString("message"));
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
                onTourCallback.OnFinish();
            }
        });
    }
    public void postResponseDetailPlanningTouring(RequestParams params, final OnTourCallback onTourCallback){
        CTourRestClient.post("detailplanning.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONObject d = response.optJSONObject("d");
                    Touring touring = touringParser.parseJsonObject(d);
                    onTourCallback.OnSuccess(touring,response.optString("message"));
                }else{
                    onTourCallback.OnFailed(response.optString("message"));
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
                onTourCallback.OnFinish();
            }
        });
    }
    public void postResponseIsInvitedTouring(RequestParams params, final OnTourCallback onTourCallback){
        CTourRestClient.post("invitationdetail.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("true")){
                    JSONObject d = response.optJSONObject("d");
                    Touring touring = touringParser.parseJsonObject(d);
                    onTourCallback.OnSuccess(touring,response.optString("message"));
                }else{
                    onTourCallback.OnFailed(response.optString("message"));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    onTourCallback.OnFailed("");
                    Log.e("ERROR",errorResponse.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                try {
                    onTourCallback.OnFailed("");
                    Log.e("ERROR",responseString.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onTourCallback.OnFinish();
            }
        });
    }
}
