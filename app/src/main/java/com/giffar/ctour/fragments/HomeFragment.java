package com.giffar.ctour.fragments;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.adapters.TimelineAdapter;
import com.giffar.ctour.callbacks.OnStatusCallback;
import com.giffar.ctour.controllers.StatusController;
import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.Timeline;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

/**
 * Created by nazar on 18/04/16.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    List<Timeline> timelines = new ArrayList<>();
    TimelineAdapter timelineAdapter;
    ListView lvTimeline;
    CircleButton btnStatus;
    StatusController statusController;
    public static final String TIMELINE = "timeline";
    TextView tvNoTImeline;
    @Override
    public void initView(View view) {
        statusController = new StatusController(activity);
        btnStatus = (CircleButton) view.findViewById(R.id.btn_status);
        timelineAdapter = new TimelineAdapter(activity);
        lvTimeline = (ListView) view.findViewById(R.id.ls_timeline);
        timelineAdapter.setData(timelines);
        lvTimeline.setAdapter(timelineAdapter);
        tvNoTImeline = (TextView) view.findViewById(R.id.tv_notimeline);
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateProfileReceiver, new IntentFilter(TIMELINE));

    }

    @Override
    public void setUICallbacks() {
        btnStatus.setOnClickListener(this);
    }

    @Override
    public void updateUI() {
//        getBaseActivity().showActionBarLogo();
//        getBaseActivity().hideActionBarTitle();
//        getBaseActivity().setActionBarTitle();
        getBaseActivity().setActionBarTitle("HOME");
            getDataTimeline();
    }


    public void getDataTimeline(){
        timelines.clear();
        RequestParams params = new RequestParams();
        params.put(Club.ID_CLUB, APP.getConfig(activity, Preferences.CLUB_ID));
        statusController.postResponseListStatus(params, new OnStatusCallback() {
            @Override
            public void OnSuccess(List<Timeline> clubs, String message) {
                timelines.addAll(clubs);
                timelineAdapter.notifyDataSetChanged();
                if (timelines.size()>0){
                    tvNoTImeline.setVisibility(View.GONE);
                }
            }

            @Override
            public void OnSuccess(Timeline timeline, String message) {

            }

            @Override
            public void OnFailed(String message) {
                    tvNoTImeline.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnFinish() {

            }
        });
    }

    @Override
    public String getPageTitle() {
        return "HOME";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }


    BroadcastReceiver updateProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getDataTimeline();
        }
    };
    @Override
    public void onClick(View v) {
        if (v == btnStatus){
            CreateStatusFragment createStatusFragment = new CreateStatusFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(createStatusFragment.getPageTitle());
            ft.setCustomAnimations(R.animator.fade_in, 0, 0, R.animator.fade_out);
            ft.replace(R.id.drawer_layout, createStatusFragment, createStatusFragment.getPageTitle());
            ft.commit();
        }
    }
}
