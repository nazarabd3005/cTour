package com.giffar.ctour.utils;

import org.json.JSONObject;

/**
 * Created by wafdamufti on 11/17/15.
 */
public class Request {

    private String path;
    private JSONObject request = new JSONObject();
    private JSONObject data;

    public Request() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public static Request businessToken() {
        Request request = new Request();
        request.setPath("/v1/token");
        return request;
    }

    public static Request meAccount() {
        Request request = new Request();
        request.setPath("/me/account");
        return request;
    }

    // zouk apps


    public static Request meLogin() {
        Request request = new Request();
        request.setPath("/v1/auths");
        return request;
    }

    public static Request meSignUp(){
        Request request = new Request();
        request.setPath("/v1/users");
        return request;
    }

    public static Request meForgotPassword(){
        Request request = new Request();
        request.setPath("/v1/forgotpasswords");
        return request;
    }

    public static Request timeline(){
        Request request = new Request();
        request.setPath("/v1/timelines");
        return request;
    }


    public static Request meLogout() {
        Request request = new Request();
        request.setPath("/me/logout");
        return request;
    }

    public static Request mePicture() {
        Request request = new Request();
        request.setPath("/me/picture");
        return request;
    }

    public static Request meChangePassword() {
        Request request = new Request();
        request.setPath("/me/password");
        return request;
    }


    public static Request meToken() {
        Request request = new Request();
        request.setPath("/me/token");
        return request;
    }

    public static Request getBeacon() {
        Request request = new Request();
        request.setPath("/beacon/uuid");
        return request;
    }

    public static Request getBeaconPromo() {
        Request request = new Request();
        request.setPath("/beacon/promotion");
        return request;
    }

    public static Request beaconUuid() {
        Request request = new Request();
        request.setPath("/beacon/uuid");
        return request;
    }

    /*public static Request beaconPromotion()
     {
     Request request = new Request();
     request.setPath("/beacon/promotion");
     return request;
     }*/
    public static Request beaconPromotion() {
        Request request = new Request();
        request.setPath("/beacon/promotion");
        return request;
    }

    public static Request beaconIndoors() {
        Request request = new Request();
        request.setPath("/beacon/indoors");
        return request;
    }


}
