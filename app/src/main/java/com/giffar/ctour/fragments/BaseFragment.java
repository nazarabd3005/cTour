/**
 * Base Fragment
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.giffar.ctour.R;
import com.giffar.ctour.activities.BaseActivity;
import com.giffar.ctour.base.interfaces.FragmentInteface;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.helpers.GoogleAnalyticsHelper;
import com.giffar.ctour.helpers.PictureHelper;
import com.giffar.ctour.helpers.TextHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

public abstract class BaseFragment extends Fragment implements FragmentInteface {
    private View view;
    protected Activity activity;
    protected boolean hasFetchDataFromAPI;
    protected LayoutInflater inflater;
    protected PictureHelper pictureHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pictureHelper = PictureHelper.getInstance(activity, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(getFragmentLayout(), container, false);
        initView(view);
        TextHelper.getInstance(activity).setFont((ViewGroup) view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
        setUICallbacks();
        if (getBaseActivity() != null)
            getBaseActivity().setActionBarTitle(getPageTitle());
    }

    public int getActionBarHeight(){
        return getBaseActivity().getActionBarHeight();
    }
    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    public BaseActivity getBaseActivity() {
        if (activity instanceof BaseActivity) {
            return ((BaseActivity) activity);
        } else {
            return null;
        }
    }

    public void replaceFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(container, fragment, fragment.getPageTitle());
        ft.commit();
    }

    public void replaceFragmentWithAnimation(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        ft.setCustomAnimations(R.animator.fade_in, 0, 0, R.animator.fade_out);
        ft.replace(container, fragment, fragment.getPageTitle());
        ft.commit();
    }

    public void addFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        ft.add(container, fragment);
        ft.commit();
    }

    public BaseFragment findFragment(String tag) {
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    public void addChildFragment(int container, BaseFragment fragment, boolean addBackToStack) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (addBackToStack) {
            ft.addToBackStack(fragment.getPageTitle());
        }
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.add(container, fragment);
        ft.commitAllowingStateLoss();
    }

    public void loadImage(String url, ImageView imageView, int stubImage) {
        pictureHelper.loadImage(url, imageView, stubImage, null);
    }

    public void loadImage(String url, ImageView imageView, int stubImage, ImageSize imageSize) {
        pictureHelper.loadImage(url, imageView, stubImage, imageSize);
    }


    public String checkNullString(String string) {
        return (string == null) ? "" : string;
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (getPageTitle() != null)
//            GoogleAnalyticsHelper.screenTracker(activity, getPageTitle());
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
    public void imagePhotoLoader(final String uri, final int imageDefault, final CircleImageView circleImageView,int size, final ProgressBar spinner){
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
