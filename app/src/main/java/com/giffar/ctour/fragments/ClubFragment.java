package com.giffar.ctour.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.giffar.ctour.entitys.OnTour;
import com.giffar.ctour.helpers.DateHelper;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 5/2/2016.
 */
public class ClubFragment extends BaseFragment {
    ListView lvMembers;
    List<Member> members1;
    ListMemberAdapter listMemberAdapter;
    MemberController memberController;
    TextView tvNameClub,tvSince,tvOfficial,tvDescription;
    CircleImageView ivLogo;
    FancyButton btnSubmit;
    ClubController clubController;
    ProgressDialog progressDialog;
    Bundle bundle;
    String id;
    DateHelper dateHelper =new DateHelper();
    @Override
    public void initView(View view) {
        memberController = new MemberController(activity);
        clubController = new ClubController(activity);
        lvMembers = (ListView) view.findViewById(R.id.lv_member);
        btnSubmit = (FancyButton) view.findViewById(R.id.btn_submit);
        View headerView = View.inflate(activity,R.layout.header_fragment_club,null);
        tvDescription = (TextView) headerView.findViewById(R.id.tv_description);
        tvNameClub = (TextView) headerView.findViewById(R.id.tv_name_club);
        tvOfficial = (TextView) headerView.findViewById(R.id.tv_official);
        tvSince = (TextView) headerView.findViewById(R.id.tv_since);
        ivLogo = (CircleImageView) headerView.findViewById(R.id.iv_logo);
        lvMembers.addHeaderView(headerView);
        members1 = new ArrayList<>();
        listMemberAdapter = new ListMemberAdapter(activity);
        listMemberAdapter.setData(members1);
        lvMembers.setAdapter(listMemberAdapter);
    }

    @Override
    public void setUICallbacks() {

    }

    @Override
    public void updateUI() {
        getDetailClub();
    }

    @Override
    public String getPageTitle() {
        return "CLUB";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_club;
    }
    public void getDetailClub(){
        RequestParams params = new RequestParams();
        params.put(Club.ID_CLUB,APP.getConfig(activity,Preferences.CLUB_ID));
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
        members1.clear();
        RequestParams params =  new RequestParams();
        params.put(Club.ID_CLUB, APP.getConfig(activity,Preferences.CLUB_ID));

        memberController.postResponseListMember(params, new OnMemberCallback() {
            @Override
            public void onSuccess(Member member, String message) {

            }

            @Override
            public void onSuccess(List<Member> members, String message) {
                members1.addAll(members);
                for (int i = 0; i < members1.size(); i++) {
                    if (members1.get(i).getStatus().equals("1")){
                        Member member = members1.get(i);
                        members1.remove(i);
                        members1.add(0,member);
                    }
                }
                listMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void OnFinish() {
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
//    private void getDataPosition(){
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
//    }
}
