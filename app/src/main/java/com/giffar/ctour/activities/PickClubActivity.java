package com.giffar.ctour.activities;

import android.view.View;
import android.widget.RelativeLayout;

import com.giffar.ctour.R;

/**
 * Created by nazar on 4/25/2016.
 */
public class PickClubActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout btnJoin;
    RelativeLayout btnCreate;
    @Override
    public void initView() {
        btnJoin = (RelativeLayout) findViewById(R.id.btn_join);
        btnCreate = (RelativeLayout) findViewById(R.id.btn_create);
    }

    @Override
    public void setUICallbacks() {
        btnCreate.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_pick_club;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onClick(View v) {
        if (v == btnCreate)
            changeActivity(CreateClubActivity.class,false,null,0);
        else if(v == btnJoin)
            changeActivity(ListClubActivity.class,false,null,0);
    }
}
