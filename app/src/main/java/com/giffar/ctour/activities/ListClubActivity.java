package com.giffar.ctour.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.giffar.ctour.R;
import com.giffar.ctour.adapters.ListClubAdapter;
import com.giffar.ctour.callbacks.OnClubCallBack;
import com.giffar.ctour.controllers.ClubController;
import com.giffar.ctour.customview.RecyclerViewHeader;
import com.giffar.ctour.entitys.Club;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazar on 4/25/2016.
 */
public class ListClubActivity extends BaseActivity {
    ListClubAdapter listClubAdapter;
    List<Club> clubList;
    ListView lsClub;
    ClubController clubController;
    ProgressDialog progressDialog;
    @Override
    public void initView() {
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(true);
        progressDialog.show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

        clubController = new ClubController(context);
        lsClub = (ListView) findViewById(R.id.ls_club);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.list_club_header, null, false);
        lsClub.addHeaderView(headerView);
        listClubAdapter = new ListClubAdapter(context);
        clubList = new ArrayList<>();
        listClubAdapter.setData(clubList);
        lsClub.setAdapter(listClubAdapter);
        prepareClubData();
    }

    @Override
    public void setUICallbacks() {

     lsClub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            TODO: is header view
             if (position!=0){
                 Club club = (Club) parent.getAdapter().getItem(position);
                 Bundle bundle = new Bundle();
                 bundle.putString(Club.ID,club.getId());
                 changeActivity(JoinClubDetailActivity.class,false,bundle,0);
             }
         }
     });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_list_club;
    }

    @Override
    public void updateUI() {
        showCustomActionBar(R.layout.view_transparent_actionbar);
        setLeftIcon(R.drawable.navigation_back);
        hideActionBarTitle();
        setRightIcon(0);
    }

    public void prepareClubData(){
        RequestParams params = new RequestParams();

        clubController.postResponseListClub(params, new OnClubCallBack() {
            @Override
            public void OnSuccess(List<Club> clubs, String message) {
                clubList.addAll(clubs);
                listClubAdapter.notifyDataSetChanged();
//                Club club1 = new Club("razer","honda","2015");
//        clubList.add(club1);
//        Club club2 = new Club("splash","honda","2013");
//        clubList.add(club2);
//        Club club3 = new Club("garuda","mitzubitsi","2014");
//        clubList.add(club3);
//        Club club4 = new Club("fasteran","yamaha","2011");
//        clubList.add(club4);
//        Club club5 = new Club("blackar","bmw","2010");
//        clubList.add(club5);
//        listClubAdapter.notifyDataSetChanged();
            }

            @Override
            public void OnSuccess(Club club, String message) {

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
//        Club club1 = new Club("razer","honda","2015");
//        clubList.add(club1);
//        Club club2 = new Club("splash","honda","2013");
//        clubList.add(club2);
//        Club club3 = new Club("garuda","mitzubitsi","2014");
//        clubList.add(club3);
//        Club club4 = new Club("fasteran","yamaha","2011");
//        clubList.add(club4);
//        Club club5 = new Club("blackar","bmw","2010");
//        clubList.add(club5);
//        listClubAdapter.notifyDataSetChanged();
    }



}
