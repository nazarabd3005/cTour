package com.giffar.ctour.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.giffar.ctour.R;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.services.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;
    private String latitude;
    private String longitude;
    private AppCompatEditText etAddress;
    private FancyButton btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        GPSTracker gpsTracker = new GPSTracker(context);
        Double latitude2 = gpsTracker.getLatitude();
        Double longitude2 = gpsTracker.getLongitude();
        LatLng sydney = new LatLng(latitude2, longitude2);
        setLongitude(longitude2.toString());
        setLatitude(latitude2.toString());
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ctour)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                Double latitude1 = latLng.latitude;
                Double longitude1 = latLng.longitude;
                setLatitude(latitude1.toString());
                setLongitude(longitude1.toString());
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ctour)));
                etAddress.setText(getCompleteAddressString(latitude1,longitude1));
            }
        });
    }

    @Override
    public void initView() {
        etAddress = (AppCompatEditText) findViewById(R.id.et_address);
        btnSubmit = (FancyButton) findViewById(R.id.btn_submit);

    }

    @Override
    public void setUICallbacks() {
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_maps;
    }

    @Override
    public void updateUI() {

    }


    @Override
    public void onClick(View v) {
     if (v==btnSubmit){
         Intent intent = new Intent();
         intent.putExtra(Timeline.LONGITUDE, getLongitude());
         intent.putExtra(Timeline.LATITUDE,getLatitude());
         intent.putExtra(Timeline.ADDRESS,etAddress.getText().toString());
         setResult(500, intent);
         finish();
     }
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Timeline.LONGITUDE, "");
        intent.putExtra(Timeline.LATITUDE,"");
        intent.putExtra(Timeline.ADDRESS,"");
        setResult(500, intent);
        finish();
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}
