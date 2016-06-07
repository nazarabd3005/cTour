package com.giffar.ctour.activities;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.adapters.ListMemberAdapter;
import com.giffar.ctour.adapters.OnTourAdapter;
import com.giffar.ctour.callbacks.OnActionbarListener;
import com.giffar.ctour.callbacks.OnMemberCallback;
import com.giffar.ctour.callbacks.OnTourCallback;
import com.giffar.ctour.controllers.MemberController;
import com.giffar.ctour.controllers.PathJSONParser;
import com.giffar.ctour.controllers.TouringController;
import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.entitys.OnTour;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.entitys.Touring;
import com.giffar.ctour.models.UserModel;
import com.giffar.ctour.services.GPSTracker;
import com.giffar.ctour.services.HttpConnection;
import com.giffar.ctour.services.remoteBroadcastReceiver;
import com.giffar.ctour.utils.Request;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 5/2/2016.
 */
public class OnTourActivity extends BaseActivity implements OnMapReadyCallback,RoutingListener, View.OnClickListener {
    ListView lvTourMember;
    List<Member> tourList;
    ListMemberAdapter listMemberAdapter;
    TouringController touringController;
    TextView tvDestination,tvDistance;
    private GoogleMap mMap;
    private Double latstart,longstart,latdes,longdes = 0.0;
    String id;
    LatLng start,waypoint,end;
    private static final int[] COLORS = new int[]{R.color.blue,R.color.redeem_booking_ok,R.color.card_layout_bg,R.color.cardview_light_background,R.color.primary_dark_material_light};
    public static final String EMERGENCY = "emergency";
    public static final String BUTTON_HEADSET_EMERGENCY = "button_set_emergency";
    public static final int MEDIA_BUTTON_INTENT_EMPIRICAL_PRIORITY_VALUE =10000000;
    List<Polyline> polylines;
    ProgressDialog progressDialog;
    MemberController memberController;
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    FancyButton  btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        showCustomActionBar(R.layout.view_transparent_actionbar);
        setLeftIcon(R.drawable.navigation_back);
        hideActionBarTitle();
        setRightIcon(0);
        setActionbarListener(new OnActionbarListener() {

            @Override
            public void onRightIconClick() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLeftIconClick() {
                onBackPressed();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocalBroadcastManager.getInstance(context).registerReceiver(updateProfileReceiver, new IntentFilter(EMERGENCY));
        LocalBroadcastManager.getInstance(context).registerReceiver(buttonheadsetclicked, new IntentFilter(BUTTON_HEADSET_EMERGENCY));
    }
    @Override
    public void initView() {
//        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
//        remoteBroadcastReceiver r = new remoteBroadcastReceiver();
//        registerReceiver(r, filter);
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        memberController = new MemberController(context);
        id = getIntent().getExtras().getString(Touring.ID);
        polylines = new ArrayList<>();
        touringController = new TouringController(context);
        tourList = new ArrayList<>();
        lvTourMember = (ListView) findViewById(R.id.lv_tour_member);
        listMemberAdapter = new ListMemberAdapter(context);
        listMemberAdapter.setData(tourList);
        lvTourMember.setAdapter(listMemberAdapter);
        tvDestination = (TextView) findViewById(R.id.tv_destination);
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        btnSubmit = (FancyButton )findViewById(R.id.btn_submit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        onDetailTouring();

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.beep, 1);

    }

    @Override
    public void setUICallbacks() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_on_tour;
    }

    @Override
    public void updateUI() {

        getDataPosition();
    }

    public void onDetailTouring(){
        RequestParams params = new RequestParams();
        params.put("id_touring",id);
        touringController.postResponseDetailPlanningTouring(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                setLatdes(Double.valueOf(touring.getLatitude_des()));
                setLongdes(Double.valueOf(touring.getLongitude_des()));
                setLatstart(Double.valueOf(touring.getLatitude()));
                setLongstart(Double.valueOf(touring.getLongitude()));
                tvDestination.setText(getCompleteAddressString(getLatdes(),getLongdes()));
                route();

            }

            @Override
            public void OnFailed(String message) {

            }

            @Override
            public void OnFinish() {

            }
        });
    }
    private void getDataPosition(){
        tourList.clear();
        RequestParams params =  new RequestParams();
        params.put("id_touring",id);

        memberController.postResponseListMemberTouring(params, new OnMemberCallback() {
            @Override
            public void onSuccess(Member member, String message) {

            }

            @Override
            public void onSuccess(List<Member> members, String message) {
                tourList.addAll(members);
                for (int i = 0; i < tourList.size(); i++) {
                    if (tourList.get(i).getStatus().equals("1")){
                        Member member = tourList.get(i);
                        tourList.remove(i);
                        tourList.add(0,member);
                    }
                }
                listMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
    }
//    private void getDataPosition(){
//        OnTour onTour = new OnTour("jafran","","0538294928","jafran@gmail.com","50m ahead","1");
//        OnTour onTour1 = new OnTour("devi","","05321312328","devi@gmail.com","40m ahead","2");
//        OnTour onTour2 = new OnTour("saarah","","0538294928","saarah@gmail.com","30m behind","3");
//        OnTour onTour3 = new OnTour("galang","","0538294928","galang@gmail.com","15m behind","4");
//        OnTour onTour4 = new OnTour("me","","0538294928","giffar@gmail.com"," on Position","5");
//        OnTour onTour5 = new OnTour("sira","","05382ss94928","sira@gmail.com"," 10m behind","6");
//        tourList.add(onTour);
//        tourList.add(onTour1);
//        tourList.add(onTour2);
//        tourList.add(onTour3);
//        tourList.add(onTour4);
//        tourList.add(onTour5);
//        onTourAdapter.notifyDataSetChanged();
//    }
    private String getMapsApiDirectionsUrl(LatLng me, LatLng order) {
        String waypoints = "waypoints=optimize:true|"
                + me.latitude + "," + me.longitude
                + "|" + order.latitude + ","
                + order.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }
    public void route(){
        GPSTracker gPSTracker = new GPSTracker(context);
        start = new LatLng(gPSTracker.getLatitude(),gPSTracker.getLongitude());
        waypoint = new LatLng(getLatstart(),getLongstart());
        end = new LatLng(getLatdes(),getLongdes());
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start,waypoint, end)
                .build();
        routing.execute();
    }
    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int g) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        int dis = 0;
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            dis = dis + route.get(i).getDistanceValue();
//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue));
        mMap.addMarker(options);

        options = new MarkerOptions();
        options.position(waypoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green));
        mMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple));
        mMap.addMarker(options);

        tvDistance.setText(String.valueOf(dis));

    }

    @Override
    public void onRoutingCancelled() {

    }
    private final BroadcastReceiver updateProfileReceiver = new BroadcastReceiver() {
        Context context1 = context;
        @Override
        public void onReceive(Context context, Intent intent) {

            playBeep();
            updateUI();
        }
    };
    private final BroadcastReceiver buttonheadsetclicked = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onEmergencyCall();

        }
    };
    public void playBeep(){
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        if (loaded) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            Log.e("Test", "Played sound");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit){
            onEmergencyCall();
        }
    }

    public void onEmergencyCall(){
        GPSTracker gpsTracker = new GPSTracker(context);
        RequestParams params = new RequestParams();
        params.put(Timeline.ID_MEMBER, APP.getConfig(context, Preferences.LOGGED_USER_ID));
        params.put("id_touring",id);
        params.put(Member.LATITUDE,gpsTracker.getLatitude());
        params.put(Member.LONGITUDE,gpsTracker.getLongitude());
        touringController.postResponseEmergencyCall(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                getDataPosition();
            }

            @Override
            public void OnFailed(String message) {

            }

            @Override
            public void OnFinish() {

            }
        });
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        route();
        // Add a marker in Sydney and move the camera
//        GPSTracker gpsTracker = new GPSTracker(context);
//        Double latitude2 = gpsTracker.getLatitude();
//        Double longitude2 = gpsTracker.getLongitude();
//        LatLng sydney = new LatLng(latitude2, longitude2);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ctour)));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(getLatstart(), getLongstart())).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ctour)));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(getLatdes(), getLongdes())).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ctour)));
//        new MarkerOptions().position(new LatLng(latitude2, longitude2)).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ctour));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.f));

    }


    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public Double getLatstart() {
        return latstart;
    }

    public void setLatstart(Double latstart) {
        this.latstart = latstart;
    }

    public Double getLongstart() {
        return longstart;
    }

    public void setLongstart(Double longstart) {
        this.longstart = longstart;
    }

    public Double getLatdes() {
        return latdes;
    }

    public void setLatdes(Double latdes) {
        this.latdes = latdes;
    }

    public Double getLongdes() {
        return longdes;
    }

    public void setLongdes(Double longdes) {
        this.longdes = longdes;
    }
}
