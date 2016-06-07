package com.giffar.ctour.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.adapters.ListMemberAdapter;
import com.giffar.ctour.callbacks.OnClubCallBack;
import com.giffar.ctour.callbacks.OnMemberCallback;
import com.giffar.ctour.controllers.ClubController;
import com.giffar.ctour.controllers.MemberController;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.helpers.DateHelper;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.cache.Resource;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 4/25/2016.
 */
public class JoinClubDetailActivity extends BaseActivity implements View.OnClickListener {
    ListView lvMembers;
    private List<Member> lsmembers;
    ListMemberAdapter listMemberAdapter;
    TextView tvNameClub,tvSince,tvOfficial,tvDescription;
    CircleImageView ivLogo;
    FancyButton btnSubmit;
    ClubController clubController;
    ProgressDialog progressDialog;
    Bundle bundle;
    String id;
    DateHelper dateHelper;
    MemberController memberController;
    @Override
    public void initView() {
        memberController = new MemberController(context);
        dateHelper = new DateHelper();
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        progressDialog.show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        bundle = getIntent().getExtras();
        id = bundle.getString(Club.ID);
        clubController = new ClubController(context);
        lvMembers = (ListView) findViewById(R.id.lv_member);
        btnSubmit = (FancyButton) findViewById(R.id.btn_submit);
        View headerView = View.inflate(context,R.layout.header_fragment_club,null);
        tvDescription = (TextView) headerView.findViewById(R.id.tv_description);
        tvNameClub = (TextView) headerView.findViewById(R.id.tv_name_club);
        tvOfficial = (TextView) headerView.findViewById(R.id.tv_official);
        tvSince = (TextView) headerView.findViewById(R.id.tv_since);
        ivLogo = (CircleImageView) headerView.findViewById(R.id.iv_logo);
        lvMembers.addHeaderView(headerView);
        lsmembers = new ArrayList<>();
        listMemberAdapter = new ListMemberAdapter(context);
        listMemberAdapter.setData(lsmembers);
        lvMembers.setAdapter(listMemberAdapter);


    }

    @Override
    public void setUICallbacks() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_join_club_detail;
    }

    @Override
    public void updateUI() {
        showCustomActionBar(R.layout.view_transparent_actionbar);
        setLeftIcon(R.drawable.navigation_back);
        hideActionBarTitle();
        setRightIcon(0);
        getDetailClub();
    }
    public void getDetailClub(){
        RequestParams params = new RequestParams();
        params.put(Club.ID_CLUB,id);
        clubController.postResponseDetailClub(params, new OnClubCallBack() {
            @Override
            public void OnSuccess(List<Club> clubs, String message) {

            }

            @Override
            public void OnSuccess(Club club, String message) {
                tvSince.setText("since "+dateHelper.formatlongtoDate(Long.valueOf(club.getCreated_date()),"yyyy"));
                String official = null;
                int colortext = 0;
                if (club.getStatus().equals("0")){
                    official = "Unofficial";
                    colortext = R.color.redeem_booking_ok;
                }else if (club.getStatus().equals("1")){
                    official = "Official";
                    colortext = R.color.blue;
                }
                tvOfficial.setText(official);
                tvOfficial.setTextColor(colortext);
                tvNameClub.setText(club.getName_club());
                tvDescription.setText(club.getDescription());
                loadImage(Preferences.URL_CLUB+club.getClub_logo(),ivLogo,R.drawable.sliders_avatar_sample_empty);
                getDataPosition();
            }

            @Override
            public void OnFailed(String message) {

            }

            @Override
            public void OnFinish() {

            }
        });
    }
    private void getDataPosition(){
        RequestParams params =  new RequestParams();
        params.put(Club.ID_CLUB,id);

        memberController.postResponseListMember(params, new OnMemberCallback() {
            @Override
            public void onSuccess(Member member, String message) {

            }

            @Override
            public void onSuccess(List<Member> members, String message) {
                lsmembers.addAll(members);
                for (int i = 0; i < lsmembers.size(); i++) {
                    if (lsmembers.get(i).getStatus().equals("1")){
                        Member member = lsmembers.get(i);
                        lsmembers.remove(i);
                        lsmembers.add(0,member);
                    }
                }
                listMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
//        Member onTour = new Member("jafran","","0538294928","jafran@gmail.com");
//        Member onTour1 = new Member("devi","","05321312328","devi@gmail.com");
//        Member onTour2 = new Member("saarah","","0538294928","saarah@gmail.com");
//        Member onTour3 = new Member("galang","","0538294928","galang@gmail.com");
//        Member onTour5 = new Member("sira","","05382ss94928","sira@gmail.com");
//        members.add(onTour);
//        members.add(onTour1);
//        members.add(onTour2);
//        members.add(onTour3);
//        members.add(onTour5);
//        listMemberAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit){
            progressDialog.show();
            RequestParams params = new RequestParams();
            params.put(Club.ID_CLUB,id);
            params.put("id_member", APP.getConfig(context,Preferences.LOGGED_USER_ID));
            clubController.postResponseJoinClub(params, new OnClubCallBack() {
                @Override
                public void OnSuccess(List<Club> clubs, String message) {

                }

                @Override
                public void OnSuccess(Club club, String message) {
                    APP.setConfig(context,Preferences.CLUB_ID,id);
                    APP.setConfig(context,Preferences.STATUS_MEMBER,"0");
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
    }
}
