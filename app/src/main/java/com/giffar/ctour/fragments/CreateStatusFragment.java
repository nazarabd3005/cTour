package com.giffar.ctour.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.activities.MapsActivity;
import com.giffar.ctour.callbacks.OnStatusCallback;
import com.giffar.ctour.callbacks.OnstatusplanningCallback;
import com.giffar.ctour.controllers.StatusController;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.helpers.DateHelper;
import com.giffar.ctour.helpers.PictureHelper;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 5/16/2016.
 */
public class CreateStatusFragment extends BaseFragment implements View.OnClickListener {
    FancyButton btnStatus;
    ImageView btnCancel,ivPhotoStatus;
    StatusController statusController;
    Spinner spType;
    EditText etTitle,etDescription;
    RelativeLayout btnLocation;
    TextView tvLocation,tvUploadCheck;
    PictureHelper pictureHelper = PictureHelper.getInstance(activity,this);
    DateHelper dateHelper = new DateHelper();
    private String latitude,longitude,address = "";
    public static final int MAP_RECEIVE = 500;
    int upload_image = 0;
    String path;
    ProgressDialog progressDialog;
    private static final String[] typespinner = {"Status","Event","Suggestion"};
    ArrayAdapter<String> dataCategory;
    OnstatusplanningCallback onstatusplanningCallback;
    @Override
    public void initView(View view) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        onstatusplanningCallback = ((OnstatusplanningCallback) activity);
        statusController = new StatusController(activity);
        etDescription = (EditText) view.findViewById(R.id.et_description);
        etTitle = (EditText) view.findViewById(R.id.et_title);
        spType = (Spinner) view.findViewById(R.id.spinner_find_question);
        btnLocation = (RelativeLayout) view.findViewById(R.id.btn_location);
        btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        btnStatus = (FancyButton) view.findViewById(R.id.btn_status);
        ivPhotoStatus = (ImageView) view.findViewById(R.id.iv_photo_status);
        tvUploadCheck = (TextView) view.findViewById(R.id.tv_upload_check);
        dataCategory = new ArrayAdapter<String>(activity,R.layout.list_spinner_category , typespinner);
        dataCategory.setDropDownViewResource(R.layout.simple_dropdown_list_white);
        spType.setAdapter(dataCategory);

    }

    @Override
    public void setUICallbacks() {
        btnCancel.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        ivPhotoStatus.setOnClickListener(this);
    }

    @Override
    public void updateUI() {
        if (!getAddress().equals("")){
            tvLocation.setText(getAddress());
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == MAP_RECEIVE){
//            setAddress(data.getExtras().getString(Timeline.ADDRESS));
//            setLatitude(data.getExtras().getString(Timeline.LATITUDE));
//            setLongitude(data.getExtras().getString(Timeline.LONGITUDE));
//            updateUI();
//        }
//    }
    @Override
    public String getPageTitle() {
        return "CREATE STATUS";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_create_status;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel){
            getFragmentManager().beginTransaction().remove(this).commit();
        }
        if (v == btnStatus){

            onCreateStatus();
//            getFragmentManager().beginTransaction().remove(this).commit();
        }
        if (v== btnLocation){
            Intent intent = new Intent(activity, MapsActivity.class);
            startActivityForResult(intent,MAP_RECEIVE);
        }
        if (v==ivPhotoStatus){
            pictureHelper.pickFromGalleryFragment();
        }
    }


    public void onCreateStatus(){
        progressDialog.show();
        String filename = "";
        Bitmap bitmap = null;
        if (upload_image==1){
           bitmap = ((BitmapDrawable) ivPhotoStatus.getDrawable()).getBitmap();
            if (!bitmap.equals(null)){
                filename=path.substring(path.lastIndexOf("/")+1);
            }
        }
        RequestParams params = new RequestParams();
        params.put(Timeline.TITLE,etTitle.getText().toString());
        params.put(Timeline.DESCRIPTION,etDescription.getText().toString());
        params.put(Timeline.TYPE,String.valueOf(spType.getSelectedItemId()+1));
        if (upload_image==1){
            params.put(Timeline.IMAGE,new ByteArrayInputStream(getByteImage(bitmap)),filename);
            params.put("upload_photo","1");
        }else{
            params.put("upload_photo","2");
        }
        params.put(Timeline.LATITUDE,getLatitude());
        params.put(Timeline.LONGITUDE,getLongitude());
        params.put(Timeline.ID_MEMBER, APP.getConfig(activity, Preferences.LOGGED_USER_ID));
        params.put(Timeline.ID_CLUB,APP.getConfig(activity, Preferences.CLUB_ID));
        params.put(Timeline.CREATED_AT,dateHelper.formatDateToMillis(DateHelper.DATE_FORMAT,dateHelper.getDate()));
        Log.i("CHECK",params.toString());
        statusController.postResponseCreateStatus(params, new OnStatusCallback() {
            @Override
            public void OnSuccess(List<Timeline> clubs, String message) {

            }

            @Override
            public void OnSuccess(Timeline timeline, String message) {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                onstatusplanningCallback.OnBack();
                closeFragment();
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

    public void closeFragment(){
        getFragmentManager().beginTransaction().remove(this).commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String imagePath;
            Uri selectedImage = null;
            if (requestCode == PictureHelper.PICK_FROM_GALLERY) {
                selectedImage = data.getData();
            } else if (requestCode == PictureHelper.TAKE_PICTURE) {
                selectedImage = pictureHelper.getSelectedImage();
            }

            if (selectedImage != null) {
                imagePath = pictureHelper.getPath(activity, selectedImage);
                path = imagePath;
                loadImage(imagePath);
            }
        }
        if (requestCode == MAP_RECEIVE){
            setAddress(data.getExtras().getString(Timeline.ADDRESS));
            setLatitude(data.getExtras().getString(Timeline.LATITUDE));
            setLongitude(data.getExtras().getString(Timeline.LONGITUDE));
            updateUI();
        }
    }
    private void loadImage(final String path) {
        ImageSize targetSize = new ImageSize(150, 150);
        pictureHelper.getImageLoader().loadImage("file:///" + path, targetSize, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            }


            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
            }

            @Override
            public void onLoadingComplete(String uri, View view, Bitmap loadedImage) {
                    try {
                        int degrees = pictureHelper.getImageRotation(path);
                        loadedImage = pictureHelper.rotate(loadedImage, degrees);
                        ivPhotoStatus.setImageBitmap(loadedImage);
                    } catch (IOException e) {
                        ivPhotoStatus.setImageBitmap(loadedImage);
                        e.printStackTrace();
                    }
                tvUploadCheck.setVisibility(View.INVISIBLE);
                upload_image = 1;
            }
        });
    }
    public byte[] getByteImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
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
}
