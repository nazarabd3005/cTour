package com.giffar.ctour.activities;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.giffar.ctour.R;
import com.giffar.ctour.adapters.ListClubAdapter;
import com.giffar.ctour.customview.RecyclerViewHeader;
import com.giffar.ctour.entitys.Club;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazar on 4/25/2016.
 */
public class ListClubActivity extends BaseActivity {
    ListClubAdapter listClubAdapter;
    List<Club> clubList = new ArrayList<>();
    ListView lsClub;

    @Override
    public void initView() {
        lsClub = (ListView) findViewById(R.id.ls_club);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.list_club_header, null, false);
        lsClub.addHeaderView(headerView);
        listClubAdapter = new ListClubAdapter(context);
        listClubAdapter.setData(clubList);
        lsClub.setAdapter(listClubAdapter);
    }

    @Override
    public void setUICallbacks() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_list_club;
    }

    @Override
    public void updateUI() {
        prepareClubData();
    }

    public void prepareClubData(){
        Club club1 = new Club("razer","honda","2015");
        clubList.add(club1);
        Club club2 = new Club("splash","honda","2013");
        clubList.add(club2);
        Club club3 = new Club("garuda","mitzubitsi","2014");
        clubList.add(club3);
        Club club4 = new Club("fasteran","yamaha","2011");
        clubList.add(club4);
        Club club5 = new Club("blackar","bmw","2010");
        clubList.add(club5);
        listClubAdapter.notifyDataSetChanged();
    }

}
