package com.giffar.ctour.fragments;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.callbacks.OnTourCallback;
import com.giffar.ctour.callbacks.OnstatusplanningCallback;
import com.giffar.ctour.controllers.TouringController;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.entitys.Touring;
import com.giffar.ctour.helpers.DateHelper;
import com.loopj.android.http.RequestParams;

import java.sql.Time;
import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 5/16/2016.
 */
public class JoinPlanningFragment extends BaseFragment implements View.OnClickListener {
    FancyButton btnJoin,btnDecline;
    ImageView btnCancel;
    TouringController touringController;
    TextView tvTitle,tvDescription,tvDestination,tvTourDate;
    String id;
    DateHelper dateHelper = new DateHelper();
    OnstatusplanningCallback onstatusplanningCallback;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onstatusplanningCallback = ((OnstatusplanningCallback) activity);

    }
    @Override
    public void initView(View view) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        onstatusplanningCallback = ((OnstatusplanningCallback) activity);
        Bundle bundle = getArguments();
        id = bundle.getString(Touring.ID);
        touringController = new TouringController(activity);
        btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        btnJoin = (FancyButton) view.findViewById(R.id.btn_join);
        btnDecline = (FancyButton) view.findViewById(R.id.btn_decline);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvDestination = (TextView) view.findViewById(R.id.tv_destination);
        tvTourDate = (TextView) view.findViewById(R.id.tv_date_touring);
    }

    @Override
    public void setUICallbacks() {
        btnDecline.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void updateUI() {
        onGetDataDetail();
    }

    public void onGetDataDetail(){
        RequestParams params = new RequestParams();
        params.put("id_touring",id);
        touringController.postResponseDetailPlanningTouring(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                tvTitle.setText(touring.getTitle_touring());
                tvDescription.setText(touring.getDescription_touring());
                tvTourDate.setText(dateHelper.formatlongtoDate(Long.valueOf(touring.getDate_touring()),"dd MMM yyyy"));
                tvDestination.setText(getCompleteAddressString(Double.valueOf(touring.getLatitude_des()),Double.valueOf(touring.getLongitude_des())));
            }

            @Override
            public void OnFailed(String message) {

            }

            @Override
            public void OnFinish() {

            }
        });
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
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
    public String getPageTitle() {
        return "JOIN PLANNING";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_join_planning;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel){
            getFragmentManager().beginTransaction().remove(this).commit();
        }
        if (v == btnDecline){
           onDeclinePlanning();
        }
        if (v == btnJoin){
            onJoinPlanning();
        }
    }
    public void onDeclinePlanning(){
        progressDialog.show();
        RequestParams params = new RequestParams();
        params.put(Timeline.ID_MEMBER, APP.getConfig(activity, Preferences.LOGGED_USER_ID));
        params.put("id_touring",id);
        touringController.postResponseJoinPlanningTouring(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                oncloseFragment();
            }

            @Override
            public void OnFailed(String message) {

            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
    }
    public void onJoinPlanning(){
        progressDialog.show();
        RequestParams params = new RequestParams();
        params.put(Timeline.ID_MEMBER, APP.getConfig(activity, Preferences.LOGGED_USER_ID));
        params.put("id_touring",id);
        touringController.postResponseJoinPlanningTouring(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                oncloseFragment();
            }

            @Override
            public void OnFailed(String message) {

            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
    }

    private void oncloseFragment(){
        getFragmentManager().beginTransaction().remove(this).commit();
        onstatusplanningCallback.OnBack();
    }
}
