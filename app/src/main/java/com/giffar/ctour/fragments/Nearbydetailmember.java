package com.giffar.ctour.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.helpers.ScreenHelper;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 6/3/2016.
 */
public class Nearbydetailmember extends BaseDialogFragment implements View.OnClickListener {
    CircleImageView ivImage;
    TextView tvName,tvClub,tvDistance;
    Bundle bundle;
    ImageView btnCancel;
    FancyButton btnSubmit;
    @Override
    protected int getMinimumWidth() {
        return  ScreenHelper.getInstance().getScreenWidth(activity) - 100;
    }

    @Override
    public void initView(View view) {
        bundle = getArguments();
        tvClub = (TextView) view.findViewById(R.id.tv_club);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        ivImage = (CircleImageView) view.findViewById(R.id.iv_image);
        btnCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        btnSubmit = (FancyButton) view.findViewById(R.id.btn_submit);

    }

    @Override
    public void setUICallbacks() {
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void updateUI() {
        tvName.setText(bundle.getString(Member.NAMA));
        double distance = Double.valueOf(bundle.getString(Member.DISTANCE));
        distance = distance*1000;
        String mtr = "m";
        if (distance>1000){
            mtr = "km";
            distance = distance/1000;
        }
        tvDistance.setText((int) distance+" "+ mtr);
        loadImage(Preferences.URL_MEMBER+bundle.getString(Member.PHOTO),ivImage,0);
        tvClub.setText(bundle.getString(Member.CLUB));
    }

    @Override
    public String getPageTitle() {
        return null;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_dialog_user_nearby;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel){

        }
        if (v == btnSubmit){

        }
    }
}
