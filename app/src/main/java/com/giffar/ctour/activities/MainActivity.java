package com.giffar.ctour.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Window;

import com.ebizu.ebn.Manager;
import com.giffar.ctour.R;
import com.giffar.ctour.callbacks.OnActionbarListener;
import com.giffar.ctour.callbacks.OnstatusplanningCallback;
import com.giffar.ctour.fragments.HomeFragment;
import com.giffar.ctour.fragments.LeftSliderFragment;




public class MainActivity extends BaseActivity implements OnstatusplanningCallback {
    private BluetoothAdapter mBluetoothAdapter;
    public boolean hasLoadHomeData = false;
    private Manager beaconManager;
    Toolbar toolbar;
    private boolean isOfflineroomDownloadComplete;
    private int roomPageCount;
    private String roomSumTemps;
    public static final String UPDATE_BADGE = "update_badge";
    public final Integer REQUEST_CODE_READ_SMS = 99;
    public final Integer REQUEST_CODE_READ_PHONE_STATE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        showDefaultActionBar();

        //Testing notification method
        //BeaconController.getInstance(this).simulateNotification();

        LocalBroadcastManager.getInstance(context).registerReceiver(notificationBadgeReceiver, new IntentFilter(UPDATE_BADGE));

    }
    private final BroadcastReceiver notificationBadgeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showNotifCounter();
            setNotifCounterValue("10");
        }
    };

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        replaceFragment(R.id.fragment_container, new HomeFragment(), false);
//            replaceFragment(R.id.fragment_container, new HomeFragment(), false);
        //updateAccount();
        //  getDataRoomAPI(1);
    }

    @Override
    public void setUICallbacks() {
        setActionbarListener(actionbarListener);
    }
    public void toggleDrawer(int gravity) {
        if (drawerLayout.isDrawerOpen(gravity)) {
            drawerLayout.closeDrawer(gravity);
        } else {
            drawerLayout.closeDrawers();
            drawerLayout.openDrawer(gravity);
        }
    }


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void updateUI() {

    }
    public OnActionbarListener actionbarListener = new OnActionbarListener() {

        @Override
        public void onRightIconClick() {
            toggleDrawer(Gravity.END);
        }

        @Override
        public void onLeftIconClick() {
            toggleDrawer(Gravity.START);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(notificationBadgeReceiver);
        if (beaconManager != null)
            beaconManager.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager != null)
            beaconManager.start();
    }

    @Override
    public void OnBack() {
        Intent intent = new Intent(LeftSliderFragment.UPDATE_USER_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        Intent intent1 = new Intent(HomeFragment.TIMELINE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
}
