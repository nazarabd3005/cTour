package com.giffar.ctour.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @author kazao
 */
public class Location implements LocationListener {
    private static Location instance;
    private Context context;
    private double latitude;
    private double longitude;
    private float accuracy;
    private LocationManager locationManager;
    private String provider;

    
    //CONSTANT LAT LON MALAYSIA
    double M_LAT=3.139003d;
    double M_LON=101.6868555d; 
    
    public static Location getInstance(Context context) {
        if (instance == null) {
            instance = new Location(context);
        }
        instance.setContext(context);
        return instance;
    }

    public Location(final Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        android.location.Location location;
        if (provider != null) {
            location = locationManager.getLastKnownLocation(provider);
        } else {
            AlertDialog.Builder builder = AlertHelper.getInstance().showAlertWithoutListener(context, "Location Services disable", "NuSentral needs access to your location. Please turn on Location Services in your device settings.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //builder.show();
            location = new android.location.Location("");
            location.setLatitude(M_LAT);
            location.setLongitude(M_LON);
            location.setAccuracy(100);
        }
        if (location != null) {
            onLocationChanged(location);
        } else {
            location = new android.location.Location("");
            location.setLatitude(M_LAT);
            location.setLongitude(M_LON);
            location.setAccuracy(100);
            onLocationChanged(location);
        }

    }

    public void resume() {
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 1000, 1, this);
        }
    }

    public void pause() {
        locationManager.removeUpdates(this);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void onLocationChanged(android.location.Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
