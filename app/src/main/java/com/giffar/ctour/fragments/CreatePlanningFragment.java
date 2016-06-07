package com.giffar.ctour.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.activities.MainActivity;
import com.giffar.ctour.activities.MapsActivity;
import com.giffar.ctour.callbacks.OnTourCallback;
import com.giffar.ctour.callbacks.OnstatusplanningCallback;
import com.giffar.ctour.controllers.StatusController;
import com.giffar.ctour.controllers.TouringController;
import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.entitys.Touring;
import com.giffar.ctour.helpers.DateHelper;
import com.giffar.ctour.helpers.PictureHelper;
import com.loopj.android.http.RequestParams;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 5/16/2016.
 */
public class CreatePlanningFragment extends BaseFragment implements View.OnClickListener {
    FancyButton btnCreate;
    ImageView btnCancel;
    TouringController touringController;
    ProgressDialog progressDialog;
    public static final int MAP_RECEIVE = 500;
    private String latitude,longitude,address,latitude1,longitude1,address1 = "";
    private int islocation = 0;
    DateHelper dateHelper = new DateHelper();
    EditText etTitle,etDescription;
    RelativeLayout btnLocation,btnDestination,btnTourDate;
    TextView tvLocation,tvDestination,tvTourDate;
    OnstatusplanningCallback onstatusplanningCallback;
    public final static String DATEFORM = "MMM dd, yyyy";
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
        etDescription = (EditText) view.findViewById(R.id.et_description);
        etTitle = (EditText) view.findViewById(R.id.et_title);
        btnLocation = (RelativeLayout) view.findViewById(R.id.btn_start);
        tvLocation = (TextView) view.findViewById(R.id.tv_start);
        btnDestination = (RelativeLayout) view.findViewById(R.id.btn_destionation);
        tvDestination = (TextView) view.findViewById(R.id.tv_destination);
        btnTourDate = (RelativeLayout) view.findViewById(R.id.btn_tour_date);
        tvTourDate = (TextView) view.findViewById(R.id.tv_tour_date);
        touringController = new TouringController(activity);
        btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        btnCreate =(FancyButton) view.findViewById(R.id.btn_create);
    }

    @Override
    public void setUICallbacks() {
        btnCancel.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnDestination.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        btnTourDate.setOnClickListener(this);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public String getPageTitle() {
        return "CREATE PLANNING";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_create_planning_touring;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel){
            getFragmentManager().beginTransaction().remove(this).commit();
        }
        if (v == btnCreate){
                onCreateTouring();
        }
        if (v== btnDestination){
            Intent intent = new Intent(activity, MapsActivity.class);
            setIslocation(2);
            startActivityForResult(intent,MAP_RECEIVE);
        }
        if (v== btnLocation){
            Intent intent = new Intent(activity, MapsActivity.class);
            setIslocation(1);
            startActivityForResult(intent,MAP_RECEIVE);

        }
        if (v== btnTourDate){
            showDatePicker();
        }
    }

    public void onCreateTouring(){
        progressDialog.show();
        RequestParams params =new RequestParams();
        params.put(Timeline.ID_MEMBER, APP.getConfig(activity, Preferences.LOGGED_USER_ID));
        params.put(Touring.TITLE_TOURING,etTitle.getText().toString());
        params.put(Touring.DESCRIPTION_TOURING,etDescription.getText().toString());
        params.put(Touring.LATITUDE,getLatitude());
        params.put(Touring.LONGITUDE,getLongitude());
        params.put(Touring.LATITUDE_DES,getLatitude1());
        params.put(Touring.LONGITUDE_DES,getLongitude1());
        params.put(Touring.CREATED_AT,dateHelper.formatDateToMillis(DateHelper.DATE_FORMAT,dateHelper.getDate()));
        params.put(Touring.DATE_TOURING,dateHelper.formatDateToMillis(DATEFORM,tvTourDate.getText().toString()));
        params.put(Touring.ID_CLUB,APP.getConfig(activity,Preferences.CLUB_ID));
        touringController.postResponseCreatePlanningTouring(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                onBackFagment();
            }

            @Override
            public void OnFailed(String message) {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
    }
    private void onBackFagment(){
        getFragmentManager().beginTransaction().remove(this).commit();
        onstatusplanningCallback.OnBack();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_RECEIVE){
            if (getIslocation()==1){
                setAddress(data.getExtras().getString(Timeline.ADDRESS));
                setLatitude(data.getExtras().getString(Timeline.LATITUDE));
                setLongitude(data.getExtras().getString(Timeline.LONGITUDE));
                tvLocation.setText(getAddress());
            }else{
                setAddress1(data.getExtras().getString(Timeline.ADDRESS));
                setLatitude1(data.getExtras().getString(Timeline.LATITUDE));
                setLongitude1(data.getExtras().getString(Timeline.LONGITUDE));
                tvDestination.setText(getAddress1());
            }
            updateUI();
        }
    }
    private void showDatePicker() {
        long defaultDate;
        String birthdate = tvTourDate.getText().toString();
        if (birthdate != null && !birthdate.equalsIgnoreCase("")) {
            defaultDate = dateHelper.formatDateToMillis("dd-MM-yyyy", birthdate);
        } else {
            defaultDate = System.currentTimeMillis();
        }
        dateHelper.showDatePicker(activity, defaultDate, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                int _month = monthOfYear + 1;
                String date = year + "-" + _month + "-" + dayOfMonth;
                String formatedDate = dateHelper.formatDate(DateHelper.DATE_FORMAT, date, "dd-MM-yyyy");
                Date dater = dateHelper.parseToDate(formatedDate, "dd-MM-yyyy");
                Date datecurrent = c.getTime();

                tvTourDate.setText(dateHelper.formatDate(DateHelper.DATE_FORMAT, date, DATEFORM));

            }
        });
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(String latitude1) {
        this.latitude1 = latitude1;
    }

    public String getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(String longitude1) {
        this.longitude1 = longitude1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public int getIslocation() {
        return islocation;
    }

    public void setIslocation(int islocation) {
        this.islocation = islocation;
    }
}
