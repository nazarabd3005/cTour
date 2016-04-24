package com.giffar.ctour.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.giffar.ctour.APP;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by egiadtya on 3/31/15.
 */
public class GCMController {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String SENDER_ID = "622922702457";
    private GoogleCloudMessaging gcm;
    private static GCMController instance;
    private Context context;
    private String regid;


    public static GCMController getInstance(Context context) {
        if (instance == null) {
            instance = new GCMController();
        }
        instance.context = context;
        return instance;
    }

    public void getGCMId() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            APP.log("No valid Google Play Services APK found.");
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                APP.log("This device is not supported.");
                return false;
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            APP.log("Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            APP.log("App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        APP.log("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        APP.setConfig(context,PROPERTY_REG_ID,regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    private void clearRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
        editor.apply();
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void sendGCMIdToServer() {
        JSONObject info = new JSONObject();
        try {
            info.put("id", regid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Worker.mePushBsc(APP.getSession(context), info, APP.getMallKey(), new Callback() {
//            @Override
//            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
//                APP.log("-----Send GCM id to server-----");
//                if (object != null) APP.log(object.toString());
//                if (error != null) {
//                    APP.log(error.toString());
//                    clearRegistrationId(context);
//                }
//            }
//        });
    }

    public void registerInBackground() {
        AsyncTask registerDevice = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    APP.log("ini pesan : "+regid);
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o){

                APP.log(o.toString());
            }
        };
        registerDevice.execute();
    }
}
