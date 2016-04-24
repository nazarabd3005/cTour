package com.giffar.ctour.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.giffar.ctour.R;
import com.giffar.ctour.activities.MainActivity;
import com.giffar.ctour.adapters.NotificationAdapter;
import com.giffar.ctour.entitys.Notification;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.helpers.GoogleAnalyticsHelper;
import com.giffar.ctour.models.NotificationModel;
import com.giffar.ctour.models.TimelineModel;

import java.util.List;

public class NotificationFragment extends BaseFragment implements OnItemClickListener {
    public static final String UPDATE_NOTIFICATION_ACTION = "update_notification_action";
    public static final String SHOW_OFFER_DETAIL = "show_offer_detail";
    private ListView lvNotif;
    private NotificationAdapter adapter;
    private List<Notification> notifications;
    private NotificationModel notificationModel;
    private View btnClearNotif;
    private ProgressDialog progressDialog;
    private TextView tvNoNotif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NotificationAdapter(activity);
        notificationModel = new NotificationModel(activity);
        notifications = notificationModel.all();
        adapter.setData(notifications);

        LocalBroadcastManager.getInstance(activity).registerReceiver(updateNotificationReceiver, new IntentFilter(UPDATE_NOTIFICATION_ACTION));
        LocalBroadcastManager.getInstance(activity).registerReceiver(showOfferDetailReceiver, new IntentFilter(SHOW_OFFER_DETAIL));
        progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading...");
        notificationModel.updateReadNotif();
    }

    @Override
    public void initView(View view) {
        lvNotif = (ListView) view.findViewById(R.id.lv_right_menu);
        btnClearNotif = view.findViewById(R.id.btn_clear_notification);
        tvNoNotif = (TextView) view.findViewById(R.id.tv_no_notif);
    }

    @Override
    public void setUICallbacks() {
        lvNotif.setOnItemClickListener(this);
        btnClearNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationModel.deleteAll(notifications);
                notifications.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void updateUI() {
        lvNotif.setAdapter(adapter);
        hcData();
        if (notifications.size()>0){
            tvNoNotif.setVisibility(View.INVISIBLE);
        }else{
            tvNoNotif.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String getPageTitle() {
        return null;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(updateNotificationReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(showOfferDetailReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Notification notification = adapter.getItem(position);
        if (notification.getType().equals("costudy")){

        }else if (notification.getType().equals("RSVN")){

        }else if (notification.getType().equals("invite")){
        }

    }

    private void showOfferDetail(Notification notification) {
        try {
            Timeline timeline = (Timeline) new TimelineModel(activity).find(notification.getId());
            if (timeline == null) {
                getOfferDetail(notification);
            } else {
                timeline.setAction(notification.getAction());
                showOfferDetail(timeline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOfferDetail(Timeline timeline) {
        GoogleAnalyticsHelper.eventTracker(activity, GoogleAnalyticsHelper.OFFER_DETAIL_PAGE, GoogleAnalyticsHelper.OPEN_ACTION, timeline.getName());

    }

    private void getOfferDetail(final Notification notification) {
//        progressDialog.show();
//        JSONObject info = new JSONObject();
//        try {
//            info.put("lat", BaseActivity.getLatitude(activity));
//            info.put("lon", BaseActivity.getLongitude(activity));
//            info.put("id", notification.getId());
//            info.put("point", "Y");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
////
    }

    private final BroadcastReceiver updateNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (notifications.size() > 0)
                notifications.clear();
            notifications.addAll(notificationModel.all());
            adapter.notifyDataSetChanged();
        }
    };

    private void hcData(){
        Notification notification = new Notification("you had 1 costudy","VIEW","12876782323","https://awdawdawdawd/dwada/a","colala","costudy");
        Notification notification1 = new Notification("invitation!","VIEW","12876782323","https://awdawdawdawd/dwada/a","zouk main room","invite");
        Notification notification2 = new Notification("RSVN tonight","VIEW","12876782323","https://awdawdawdawd/dwada/a","zouk main room","RSVN");
        notifications.add(notification);
        notifications.add(notification1);
        notifications.add(notification2);
    }

    private final BroadcastReceiver showOfferDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notifId = intent.getStringExtra(Notification.ID);
            Notification notification = (Notification) notificationModel.find(notifId);
            notification.setAction(intent.getStringExtra(Notification.ACTION));
            showOfferDetail(notification);
        }
    };

}
