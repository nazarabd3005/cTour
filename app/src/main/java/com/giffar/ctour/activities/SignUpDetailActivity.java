package com.giffar.ctour.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.R;
import com.giffar.ctour.callbacks.OnMemberCallback;
import com.giffar.ctour.controllers.GCMController;
import com.giffar.ctour.controllers.MemberController;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.customview.QuickAction;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.helpers.DateHelper;
import com.giffar.ctour.helpers.PictureHelper;
import com.giffar.ctour.services.GPSTracker;
import com.giffar.ctour.utils.Request;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
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
public class SignUpDetailActivity extends BaseActivity implements View.OnClickListener {
//    MaterialEditText etName;
//    MaterialEditText etPhone;
//    MaterialEditText etaddress;
    EditText etUsername,etPassword,etName,etBirthdate;
    CircleImageView ivPhoto;
    FancyButton btnSubmit;
    MemberController memberController;
    public String path;

    public final static String DATEFORM = "MMM dd, yyyy";
    DateHelper dateHelper;
    PictureHelper pictureHelper;
    ProgressDialog progressDialog;
    ImageLoader imageLoader;
    @Override
    public void initView() {
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        pictureHelper = PictureHelper.getInstance(context,this);
        dateHelper = new DateHelper();
        memberController = new MemberController(context);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etName = (EditText) findViewById(R.id.et_name);
        etBirthdate =(EditText) findViewById(R.id.et_birthdate);
        ivPhoto = (CircleImageView) findViewById(R.id.iv_photo);


//        etName = (MaterialEditText) findViewById(R.id.et_name);
//        etPhone = (MaterialEditText)findViewById(R.id.et_phone);
//        etaddress = (MaterialEditText) findViewById(R.id.et_address);
//        etName.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
//        etPhone.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
//        etaddress.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
        btnSubmit = (FancyButton) findViewById(R.id.btn_submit);

    }

    private void showDatePicker() {
        long defaultDate;
        String birthdate = etBirthdate.getText().toString();
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
                int ageLimit = getDiffYears(dater, datecurrent);
                if (ageLimit < 21) {

                    QuickAction quickAction = new QuickAction
                            (SignUpDetailActivity.this, "Your age must be at least 21");
                    quickAction.setMaxHeightResource(R.dimen.popup_max_height);
                    quickAction.show(etBirthdate);
                } else {
                    etBirthdate.setText(dateHelper.formatDate(DateHelper.DATE_FORMAT, date, DATEFORM));
                }
            }
        });
    }
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
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
                path = imagePath;
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
                try {
                    int degrees = pictureHelper.getImageRotation(path);
                    loadedImage = pictureHelper.rotate(loadedImage, degrees);
                    ivPhoto.setImageBitmap(loadedImage);
                } catch (IOException e) {
                    ivPhoto.setImageBitmap(loadedImage);
                    e.printStackTrace();
                }
            }

        });
    }


    @Override
    public void setUICallbacks() {
        etBirthdate.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_signup_detail;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onClick(View v) {
        if(v == btnSubmit){
            onRegister();
        }else if(v== etBirthdate){
            showDatePicker();
        }else if (v== ivPhoto){
            pictureHelper.pickFromGalery();
        }
    }

    public void onRegister(){
        progressDialog.show();
        String filename=path.substring(path.lastIndexOf("/")+1);
        GPSTracker gpsTracker = new GPSTracker(context);
        Bitmap bitmap = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();
        RequestParams params = new RequestParams();
        params.put(Member.USERNAME,etUsername.getText().toString());
        params.put(Member.PASSWORD,etPassword.getText().toString());
        params.put(Member.NAMA,etName.getText().toString());
        params.put(Member.BIRTHDATE,dateHelper.formatDateToMillis(DATEFORM,etBirthdate.getText().toString()));
        params.put(Member.LATITUDE,String.valueOf(gpsTracker.getLatitude()));
        params.put(Member.LONGITUDE,String.valueOf(gpsTracker.getLongitude()));
        params.put(Member.PHOTO,new ByteArrayInputStream(getByteImage(bitmap)),filename);
        params.put(Member.GCM_ID, APP.getConfig(context, GCMController.PROPERTY_REG_ID));
        memberController.PostResponseRegister(params, new OnMemberCallback() {
            @Override
            public void onSuccess(Member member, String message) {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                changeActivity(PickClubActivity.class,true,null,0);
            }

            @Override
            public void onSuccess(List<Member> members, String message) {

            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
//        params.put(Member.CREATED_DATE,dateHelper.formatDateToMillis(dateHelper.DATE_FORMAT,dateHelper.getDate()));

    }
    public byte[] getByteImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }
    public static String getBase64Bitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] array = baos.toByteArray();
        return Base64.encodeToString(array, Base64.DEFAULT);
    }
}
