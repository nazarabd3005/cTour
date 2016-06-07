package com.giffar.ctour.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.callbacks.OnClubCallBack;
import com.giffar.ctour.controllers.ClubController;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.customview.QuickAction;
import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.helpers.DateHelper;
import com.giffar.ctour.helpers.PictureHelper;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 4/25/2016.
 */
public class CreateClubActivity extends BaseActivity implements View.OnClickListener {
    FancyButton btnSubmit;
    ClubController clubController;
    EditText etNameClub,etSince,etType,etDesc;
    CircleImageView ivLogo,ivLicence;
    DateHelper dateHelper;
    PictureHelper pictureHelper;

    public String path;
    public String path2;
    public int chooseiv;
    ProgressDialog progressDialog;
    public final static String DATEFORM = "MMM dd, yyyy";
    @Override
    public void initView() {
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);

        dateHelper = new DateHelper();
        pictureHelper =PictureHelper.getInstance(context,this);
        clubController = new ClubController(context);
        btnSubmit = (FancyButton) findViewById(R.id.btn_submit);
        etDesc = (EditText) findViewById(R.id.et_description);
        etNameClub = (EditText) findViewById(R.id.et_club_name);
        etSince = (EditText) findViewById(R.id.et_since);
        etType = (EditText) findViewById(R.id.et_type);
        ivLicence = (CircleImageView) findViewById(R.id.iv_licence);
        ivLogo = (CircleImageView) findViewById(R.id.iv_photo_club);
    }
    private void showDatePicker() {
        long defaultDate;
        String birthdate = etSince.getText().toString();
        if (birthdate != null && !birthdate.equalsIgnoreCase("")) {
            defaultDate = dateHelper.formatDateToMillis("dd-MM-yyyy", birthdate);
        } else {
            defaultDate = System.currentTimeMillis();
        }
        dateHelper.showDatePicker(context, defaultDate, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                int _month = monthOfYear + 1;
                String date = year + "-" + _month + "-" + dayOfMonth;
                String formatedDate = dateHelper.formatDate(DateHelper.DATE_FORMAT, date, "dd-MM-yyyy");
                Date dater = dateHelper.parseToDate(formatedDate, "dd-MM-yyyy");
                Date datecurrent = c.getTime();

                etSince.setText(dateHelper.formatDate(DateHelper.DATE_FORMAT, date, DATEFORM));

            }
        });
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
                imagePath = pictureHelper.getPath(context, selectedImage);
                if (chooseiv==1){
                    path = imagePath;
                }else
                    path2 = imagePath;

                loadImage(imagePath);
            }
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
                if (chooseiv==1){
                    try {
                        int degrees = pictureHelper.getImageRotation(path);
                        loadedImage = pictureHelper.rotate(loadedImage, degrees);
                        ivLogo.setImageBitmap(loadedImage);
                    } catch (IOException e) {
                        ivLogo.setImageBitmap(loadedImage);
                        e.printStackTrace();
                    }
                }else{
                    try {
                        int degrees = pictureHelper.getImageRotation(path);
                        loadedImage = pictureHelper.rotate(loadedImage, degrees);
                        ivLicence.setImageBitmap(loadedImage);
                    } catch (IOException e) {
                        ivLicence.setImageBitmap(loadedImage);
                        e.printStackTrace();
                    }
                }

            }

        });
    }

    @Override
    public void setUICallbacks() {
        btnSubmit.setOnClickListener(this);
        etSince.setOnClickListener(this);
        ivLicence.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_create_club;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit){
            onCreateClub();
        }else if (v== ivLicence){
            chooseiv =2;
            pictureHelper.pickFromGalery();
        }else if (v== ivLogo){
            chooseiv=1;
            pictureHelper.pickFromGalery();
        }else if (v== etSince){
            showDatePicker();
        }
    }

    public void onCreateClub(){
        progressDialog.show();
        String filename=path.substring(path.lastIndexOf("/")+1);
        String filename2=path2.substring(path2.lastIndexOf("/")+1);
        Bitmap bitmap = ((BitmapDrawable) ivLogo.getDrawable()).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable) ivLicence.getDrawable()).getBitmap();
        RequestParams params = new RequestParams();
        params.put("id_member", APP.getConfig(context, Preferences.LOGGED_USER_ID));
        params.put(Club.CLUB_NAME,etNameClub.getText().toString());
        params.put(Club.CREATED_DATE,dateHelper.formatDateToMillis(dateHelper.DATE_FORMAT,etSince.getText().toString()));
        params.put(Club.TYPE,etType.getText().toString());
        params.put(Club.DESCRIPTION,etDesc.getText().toString());
        params.put(Club.CLUB_LOGO,new ByteArrayInputStream(getByteImage(bitmap)),filename);
        params.put(Club.CLUB_LICENCE,new ByteArrayInputStream(getByteImage(bitmap2)),filename2);
        clubController.postResponseCreateClub(params, new OnClubCallBack() {
            @Override
            public void OnSuccess(List<Club> clubs, String message) {

            }

            @Override
            public void OnSuccess(Club club, String message) {
                changeActivity(MainActivity.class,true,null,0);
            }

            @Override
            public void OnFailed(String message) {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFinish() {
            progressDialog.dismiss();
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
}
