package com.giffar.ctour.fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.giffar.ctour.R;
import com.giffar.ctour.adapters.OnTourAdapter;
import com.giffar.ctour.callbacks.OnMemberCallback;
import com.giffar.ctour.controllers.MemberController;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.entitys.OnTour;
import com.giffar.ctour.services.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.markushi.ui.CircleButton;

/**
 * Created by nazar on 5/2/2016.
 */
public class LostFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {
    ListView lvMember;
    private GoogleMap mMap;
    private String latitude;
    private String longitude;
    OnTourAdapter onTourAdapter;
    List<OnTour> onTours;
    MemberController memberController;
    MapView mapView;
    CircleButton btnMe;
    GPSTracker gpsTracker;
    LocationManager locationManager;
    LatLng CENTER;
    List<Member> members1;
    private HashMap<Marker,Member> memberMarkerMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(activity);
        memberController = new MemberController(activity);
        members1 = new ArrayList<>();
        memberMarkerMap = new HashMap<Marker, Member>();
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void initView(View view) {
        btnMe = (CircleButton) view.findViewById(R.id.btn_me);
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        toolbar.setContentInsetsAbsolute(0, 0);
//        setSupportActionBar(toolbar);
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
    public void setUICallbacks() {
        btnMe.setOnClickListener(this);
    }

    @Override
    public void updateUI() {
        getDataPosition();
    }

    @Override
    public String getPageTitle() {
        return "Lost";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_lost;
    }

    private void getDataPosition(){
        RequestParams params = new RequestParams();
        params.put("latitude",gpsTracker.getLatitude());
        params.put("longitude",gpsTracker.getLongitude());
        memberController.postResponseNearbyMember(params, new OnMemberCallback() {
            @Override
            public void onSuccess(Member member, String message) {
            }

            @Override
            public void onSuccess(List<Member> members, String message) {
                members1.addAll(members);
                for (Member  member:members1
                     ) {
                    memberMarkerMap.put(placeMarker(member),member);
                }
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void OnFinish() {

            }
        });
    }

    public Marker placeMarker(Member member) {
        LatLng position = new LatLng(Double.valueOf(member.getLatitude()),Double.valueOf(member.getLongitude()));
        Marker m  = mMap.addMarker(new MarkerOptions()

                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.male_user))

                .title(member.getNama()));
        return m;

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapsInitializer.initialize(getActivity());
        locationManager = ((LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE));

        Boolean localBoolean = Boolean.valueOf(locationManager
                .isProviderEnabled("network"));

        if (localBoolean.booleanValue())
            CENTER = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());


        mMap.setIndoorEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        if (CENTER != null) {
            mMap.animateCamera(
                    CameraUpdateFactory.newLatLng(CENTER), 1750,
                    null);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Member member = memberMarkerMap.get(marker);
                Toast.makeText(activity,marker.getTitle(),Toast.LENGTH_SHORT);

                Nearbydetailmember fragment = new Nearbydetailmember();
                Bundle bundle = new Bundle();
                bundle.putString(Member.ID,member.getId());
                bundle.putString(Member.NAMA,member.getNama());
                bundle.putString(Member.DISTANCE,member.getDistance());
                bundle.putString(Member.CLUB,member.getClub());
                bundle.putString(Member.PHOTO,member.getPhoto());
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "Nearby");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==btnMe){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            if (CENTER != null) {
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLng(CENTER), 1750,
                        null);
            }
        }
    }
}
