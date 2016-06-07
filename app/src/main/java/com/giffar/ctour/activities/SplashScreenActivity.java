package com.giffar.ctour.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.controllers.GCMController;
import com.giffar.ctour.helpers.DateHelper;
import com.giffar.ctour.helpers.GoogleAnalyticsHelper;
import com.giffar.ctour.models.NotificationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplashScreenActivity extends AppCompatActivity {
    private TextView tLoadingMessage;
    private Context context;
    private int REQUEST_MULTIPLE_PERMISSION = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splashscreen);
        tLoadingMessage = (TextView) this.findViewById(R.id.tLoadingMessage);
        context = this;
        GCMController.getInstance(context).registerInBackground();
//        clearData();
        clearDailyNotification();
//        syncronizeData(context);
        if (!APP.checkConfig(this, Preferences.NOTIFICATION)) {
            APP.setConfig(this, Preferences.NOTIFICATION, "Y");
        }

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();

            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
                permissionsNeeded.add("Phone State");
            if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
                permissionsNeeded.add("Contacts");
            if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
                permissionsNeeded.add("SMS");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(context, message,
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_MULTIPLE_PERMISSION);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    infoFix(context, "cTour App", "This app must have storage and phone permission to run", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_MULTIPLE_PERMISSION);
                return;
            }
        }else{
//            sendDeviceInfo();
            isLogin(context);
        }
    }

    public static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .setCancelable(false)
                .create()
                .show();
    }

    public static void infoFix(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("Ok", listener)
                .setNegativeButton(null, null)
                .setCancelable(false)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_MULTIPLE_PERMISSION) {

            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);

            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                // EbizuSDK.PostProcess(activity_context);
                // All Permissions Granted
//                sendDeviceInfo();
                isLogin(context);
            } else {
                // Permission Denied
                Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();
                infoFix(context, "ZOUK App", "This app must have storage and phone permission to run", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        }
    }

    private void isLogin(Context context) {
        if (APP.isHasLogin(context)) {
            if (APP.getConfig(context,Preferences.CLUB_ID).equals("null")|| APP.getConfig(context, Preferences.CLUB_ID).equals("")){
                goJoinActivity();
            }else
                goMainActivity();
        } else {
            goNextActivity();
        }
    }


    AsyncTask gcmtask = new AsyncTask() {
        @Override
        protected void onPostExecute(Object o) {
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleAnalyticsHelper.screenTracker(this, "Splashscreen");
    }

    private void clearDailyNotification() {
        if (APP.checkConfig(this, Preferences.LAST_CLEAR_NOTIF_DATE)) {
            String lastDate = APP.getConfig(this, Preferences.LAST_CLEAR_NOTIF_DATE);
            if (!lastDate.equalsIgnoreCase(DateHelper.getInstance().getDate())) {
                NotificationModel model = new NotificationModel(this);
                model.deleteAll(model.all());
                APP.setConfig(this, Preferences.LAST_CLEAR_NOTIF_DATE, DateHelper.getInstance().getDate());
                APP.log("---CLEAR NOTIFICATION---");
            }
        } else {
            APP.setConfig(this, Preferences.LAST_CLEAR_NOTIF_DATE, DateHelper.getInstance().getDate());
        }
    }
    public void goJoinActivity() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Class<?> destination = PickClubActivity.class;
                Intent intent = new Intent(SplashScreenActivity.this, destination);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
    public void goMainActivity() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Class<?> destination = MainActivity.class;
                Intent intent = new Intent(SplashScreenActivity.this, destination);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void goNextActivity() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Class<?> destination = LoginActivity.class;
                Intent intent = new Intent(SplashScreenActivity.this, destination);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


//    private void sendDeviceInfo() {
//        TelephonyManager phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        JSONObject info = new JSONObject();
//        JSONObject data = new JSONObject();
//        RequestParams params = new RequestParams();
//        try {
//            info.put("med_manufacture", "Android");
//            info.put("med_model", Build.MODEL);
//            if (phone.getLine1Number() == null) {
//                info.put("med_msisdn", "");
//            } else {
//                info.put("med_msisdn", phone.getLine1Number());
//            }
//
//            info.put("med_device_id", phone.getDeviceId());
//            info.put("med_device_token", "0");
//            info.put("med_imsi", phone.getNetworkOperatorName());
//            info.put("med_status", "0");
//            data.put("t", "");
//            data.put("c", Preferences.COMPANY_ID);
//            data.put("d", info);
//            params.put("r", data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.i("PARAMS", params.toString());
//        CTourRestClient.post("devices", params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                Log.i("RESPONSE_DEVICE", response.toString());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                try {
//                    Log.i("DEVICE ERROR", errorResponse.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}