/**
 * Base Activity
 *
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giffar.ctour.APP;
import com.giffar.ctour.R;
import com.giffar.ctour.base.interfaces.ActivityInterface;
import com.giffar.ctour.callbacks.OnActionbarListener;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.fragments.BaseFragment;
import com.giffar.ctour.fragments.HomeFragment;
import com.giffar.ctour.helpers.AlertHelper;
import com.giffar.ctour.helpers.FragmentHelper;
import com.giffar.ctour.helpers.Location;
import com.giffar.ctour.helpers.PictureHelper;
import com.giffar.ctour.helpers.TextHelper;
import com.giffar.ctour.utils.MCrypt;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;

import de.ailis.pherialize.Pherialize;
import pl.droidsonroids.gif.GifImageView;

public abstract class BaseActivity extends AppCompatActivity implements ActivityInterface {
    public final static int TRANSITION_IN_IN = R.anim.slide_in_left;
    public final static int TRANSITION_IN_OUT = R.anim.slide_out_right;
    public final static int TRANSITION_OUT_IN = R.anim.slide_in_right;
    public final static int TRANSITION_OUT_OUT = R.anim.slide_out_left;
    public final static int TRANSITION_REQUEST_CODE = 391;
    protected SharedPreferences preferences;
    protected Editor editor;
    private boolean showSlidingMenu = true;
    protected Context context;
    private View actionBarView;
    private FragmentHelper fragmentHelper;
    private ActionBar actionBar;
    private OnActionbarListener actionbarListener;
    private TextView tvActionBarTitle;
    private ImageView leftIcon, rightIcon, zoukLogo;
    protected DrawerLayout drawerLayout;
    public int actionBarHeight;
    protected PictureHelper pictureHelper;
    public LinearLayout notifCountHolder;
    public TextView notifCount;
    private static final int ERROR_DIALOG_REQUEST_CODE = 1;
    public final Integer REQUEST_CODE_READ_SMS = 99;
    public final Integer REQUEST_CODE_READ_PHONE_STATE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentHelper = FragmentHelper.getInstance(getFragmentManager());
        pictureHelper = PictureHelper.getInstance(this);
        context = this;
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkSMSPermission();
//        }else{
//            sendDeviceInfo();
//        }
        initSharedPreference();
        setContentView(getLayout());
        initView();
        setUICallbacks();
        showCustomActionBar();
        //setFont();
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void checkSMSPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);

        int permissionReadPhoneStateCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    REQUEST_CODE_READ_SMS);

        } else if (permissionReadPhoneStateCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_READ_PHONE_STATE); // define this constant yourself

        }
    }

    public void loadImage(String url, ImageView imageView, int stubImage) {
        pictureHelper.loadImage(url, imageView, stubImage, null);
    }

    public void loadImage(String url, ImageView imageView, int stubImage, ImageSize imageSize) {
        pictureHelper.loadImage(url, imageView, stubImage, imageSize);
    }

    private void setFont() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        TextHelper.getInstance(this).setFont(rootView);
    }

    private void initSharedPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public void showCustomActionBar() {
        showCustomActionBar(R.layout.view_custom_actionbar);
    }

    public void showDefaultActionBar() {
        showCustomActionBar(R.layout.view_default_actionbar);
    }

    public void showCustomActionBar(int resourceView) {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            actionBarView = inflater.inflate(resourceView, null, false);
            actionbarClickListener();
            tvActionBarTitle = (TextView) actionBarView.findViewById(R.id.tv_title);
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/ProximaNovaBold.otf");
            tvActionBarTitle.setTypeface(face);
            zoukLogo = (ImageView) actionBarView.findViewById(R.id.zouk_logo);
            leftIcon = (ImageView) actionBarView.findViewById(R.id.iv_action_left);
            rightIcon = (ImageView) actionBarView.findViewById(R.id.iv_action_right);
            if(resourceView == R.layout.view_default_actionbar) {
                notifCountHolder = (LinearLayout) actionBarView.findViewById(R.id.notif_count_holder);
                notifCount = (TextView) actionBarView.findViewById(R.id.notif_count);
                setNotifCounterValue("20");
            }
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setCustomView(actionBarView);
            actionBar.show();
        }
    }

    public void setNotifCounterValue(String value){
        notifCount.setText(value);
    }

    public void hideNotifCounter(){
        notifCountHolder.setVisibility(View.GONE);
    }

    public void showNotifCounter(){
        notifCountHolder.setVisibility(View.VISIBLE);
    }

    private void actionbarClickListener() {
        View actionLeft = actionBarView.findViewById(R.id.left_icon_container);
        View actionRight = actionBarView.findViewById(R.id.right_icon_container);

        actionLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (actionbarListener != null) {
                    actionbarListener.onLeftIconClick();
                }
            }
        });

        actionRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (actionbarListener != null) {
                    actionbarListener.onRightIconClick();
                }
            }
        });

    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setActionBarColor(int resColor) {
        actionBarView.setBackgroundResource(resColor);
    }

    public boolean isShowSlidingMenu() {
        return showSlidingMenu;
    }

    @Override
    public void onBackPressed() {
        int stackCount = getFragmentManager().getBackStackEntryCount();
        if (stackCount > 0) {
            getFragmentManager().popBackStack();
        } else {
            String activityName = getClass().getSimpleName();
            if (activityName.equalsIgnoreCase("MainActivity") && getFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeFragment) {
                AlertDialog.Builder confirmation = AlertHelper.getInstance().showAlertWithoutListener(this, "Confirmation", "Are you sure you want to exit?");
                confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                });
                confirmation.show();
            } else {
                exit();
            }
        }
    }

    private void exit() {
        super.onBackPressed();
    }

    public FragmentHelper getFragmentHelper() {
        return fragmentHelper;
    }

    public void replaceFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        fragmentHelper.replaceFragment(container, fragment, addBackToStack);
    }

    public void replaceFragment(int container, Fragment fragment, boolean addBackToStack) {
        fragmentHelper.replaceFragment(container, fragment, addBackToStack);
    }

    public void addFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        fragmentHelper.addFragment(container, fragment, addBackToStack);
    }

    public void addFragment(int container, Fragment fragment, boolean addBackToStack) {
        fragmentHelper.addFragment(container, fragment, addBackToStack);
    }

    public void changeActivity(Class<?> destination) {
        changeActivity(destination, false, null, 0);
    }

    public void changeActivity(Class<?> destination, int flags) {
        changeActivity(destination, false, null, flags);
    }

    public void changeActivity(Class<?> destination, boolean killActivity) {
        changeActivity(destination, killActivity, null, 0);
    }

    public void changeActivity(Class<?> destination, Bundle extra) {
        changeActivity(destination, false, extra, 0);
    }

    public void changeActivity(Class<?> destination, boolean killActivity, Bundle extra, int flags) {
        Intent intent = new Intent(context, destination);
        if (extra != null) {
            intent.putExtras(extra);
        }
        if (flags != 0) {
            intent.setFlags(flags);
        }
        startActivityForResult(intent, TRANSITION_REQUEST_CODE);
        if (killActivity) {
            finish();
        }
        overridePendingTransition(TRANSITION_IN_IN, TRANSITION_IN_OUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TRANSITION_REQUEST_CODE) {
            overridePendingTransition(TRANSITION_OUT_IN, TRANSITION_OUT_OUT);
        }
    }

    public void setActionBarTitle(String title) {
        if (tvActionBarTitle != null) {
            tvActionBarTitle.setText(title);
        }
    }

    public void hideActionBarTitle() {
        tvActionBarTitle.setVisibility(View.GONE);
    }

    public void showActionBarTitle() {
        tvActionBarTitle.setVisibility(View.VISIBLE);
    }

    public void setActionbarListener(OnActionbarListener actionbarListener) {
        this.actionbarListener = actionbarListener;
    }

    public void setLeftIcon(int drawableRes) {
        if (leftIcon != null) {
            leftIcon.setImageResource(drawableRes);
            if(drawableRes != 0) {
                if (drawerLayout != null) {
                    if (drawableRes == R.drawable.ic_slider_left)
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    else
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }else {
                leftIcon.setVisibility(View.GONE);
            }
        }
    }

    public void setRightIcon(int drawableRes) {
        if (rightIcon != null) {
            if (drawableRes == 0) {
                rightIcon.setVisibility(View.GONE);
            }
            rightIcon.setImageResource(drawableRes);
        }
    }

    public void setDefaultActionbarIcon() {
        findViewById(R.id.right_icon_container).setVisibility(View.VISIBLE);
        rightIcon.setVisibility(View.VISIBLE);
        setLeftIcon(R.drawable.ic_slider_left);
        setRightIcon(R.drawable.ic_right_slider);
    }

//    public SearchView showSearchView() {
//        findViewById(R.id.right_icon_container).setVisibility(View.GONE);
//        SearchView searchView = (SearchView) findViewById(R.id.sv_search);
//        searchView.setVisibility(View.VISIBLE);
//        return searchView;
//    }

    public void startService(Context context, Intent intent) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            String serviceName = intent.getComponent().getClassName();
            if (serviceName.equals(service.service.getClassName())) {
                APP.log(serviceName + " IS ALLREADY RUNNING");
            } else {
                context.startService(intent);
            }
        }
    }

    public static double getLatitude(Context context) {
        return Location.getInstance(context).getLatitude();
    }

    public static double getLongitude(Context context) {
        return Location.getInstance(context).getLongitude();
    }

    public int getActionBarHeight() {
        return actionBarHeight;
    }

    public void setActionBarHeight(int actionBarHeight) {
        this.actionBarHeight = actionBarHeight;
    }

    public void hideActionBarLogo() {
        zoukLogo.setVisibility(View.GONE);
    }

    public void showActionBarLogo() {
        zoukLogo.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
       GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public String addNewLine(String value) {
        String[] temp = value.split(" ");
        String newString = "";
        if (temp.length > 1) {
            temp[0] = temp[0] + "\n";
            for (int i = 0; i < temp.length; i++) {
                if (i > 0)
                    newString = newString + temp[i] + " ";
                else
                    newString = newString + temp[i];
            }
        } else {
            newString = value;
        }
        return newString;
    }

    public String encrypt(String value){
        MCrypt mcrypt = new MCrypt();
        String encrypted = "";

        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public String decrypt(String value){
        MCrypt mcrypt = new MCrypt();
        String decrypted = "";

        try {
            decrypted = String.valueOf(Pherialize.unserialize(new String(mcrypt.decrypt(value))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public void imagePhotoLoader(final String uri, final int imageDefault, final CircleImageView circleImageView, int size, final GifImageView spinner){
        ImageLoader.getInstance().displayImage(uri,circleImageView,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                spinner.setVisibility(View.GONE);
                circleImageView.setImageResource(imageDefault);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);
                try {
                    int degrees = pictureHelper.getImageRotation(uri);
                    loadedImage = pictureHelper.rotate(loadedImage, degrees);
                    circleImageView.setImageBitmap(loadedImage);
                } catch (IOException e) {
                    circleImageView.setImageBitmap(loadedImage);
                    e.printStackTrace();
                }

            }
        });
    }
}
