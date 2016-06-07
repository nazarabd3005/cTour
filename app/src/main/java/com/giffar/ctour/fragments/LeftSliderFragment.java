package com.giffar.ctour.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.activities.LoginActivity;
import com.giffar.ctour.activities.MainActivity;
import com.giffar.ctour.activities.OnTourActivity;
import com.giffar.ctour.adapters.LeftMenuAdapter;
import com.giffar.ctour.callbacks.OnTourCallback;
import com.giffar.ctour.callbacks.OnstatusplanningCallback;
import com.giffar.ctour.controllers.TouringController;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.customview.WrappingGridView;
import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.LeftMenu;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.entitys.OnTour;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.entitys.Touring;
import com.giffar.ctour.entitys.User;
import com.giffar.ctour.helpers.AlertHelper;
import com.giffar.ctour.models.UserModel;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

//import com.uber.sdk.android.rides.RequestButton;
//import com.uber.sdk.android.rides.RideParameters;

public class LeftSliderFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener {
    public static final String UPDATE_USER_ACTION = "update_user_action";
    public static final String CLICK_MENU_ACTION = "click_menu_action";
    private static final int[] menuTitle = {R.string.home, R.string.event,R.string.suggestion,R.string.lost,R.string.club, R.string.account,R.string.on_tour};
    private static int[] menuTItleclik = null;
    private static final int[] menuIcon = {R.drawable.ic_home_selector, R.drawable.ic_event_selector,R.drawable.destination,R.drawable.people_ask,R.drawable.people_icon, R.drawable.ic_account_selector,R.drawable.road};
    private static final int[] menuIcon2 = {R.drawable.ic_home_selector, R.drawable.ic_event_selector,R.drawable.destination,R.drawable.people_ask,R.drawable.people_icon, R.drawable.ic_account_selector};
    private static int[] menuIconclik = null;
    //private ListView lvLeftMenu;
    private LeftMenuAdapter adapter;
    private List<LeftMenu> leftMenus;
    private int selectedPosition = -1;
    private User loggedUser;
    private ImageView  ivCloseDrawer;
    private WrappingGridView menuGridView;
    private View selectedView;
//    GifImageView progressBar;

    private static final String DROPOFF_ADDR = "Jalan Tun Razak Kuala Lumpur Federal Territory of Kuala Lumpur Malaysia";
    private static final float DROPOFF_LAT = 3.16376f;
    private static final float DROPOFF_LONG = 101.71627f;
    private static final String DROPOFF_NICK = "Zouk Club Kuala Lumpur";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";

    private boolean closedDrawer = true;
    public AlertDialog alert;
    //RequestButton uberButtonBlack;
    ProgressDialog progressDialog;
    TouringController touringController;
    String status = "";
    Touring touring1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touringController = new TouringController(activity);


//        loggedUser = UserModel.getMe(activity);

        LocalBroadcastManager.getInstance(activity).registerReceiver(updateProfileReceiver, new IntentFilter(UPDATE_USER_ACTION));
        LocalBroadcastManager.getInstance(activity).registerReceiver(clickMenuReceiver, new IntentFilter(CLICK_MENU_ACTION));

    }

    @Override
    public void initView(View view) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
//        progressBar = (GifImageView) view.findViewById(R.id.loading);
//        lvLeftMenu = (ListView) view.findViewById(R.id.lv_left_menu);
        menuGridView = (WrappingGridView) view.findViewById(R.id.left_menu_grid);
        ivCloseDrawer = (ImageView) view.findViewById(R.id.iv_close_drawer);
        adapter = new LeftMenuAdapter(activity);
        leftMenus = new ArrayList<LeftMenu>();
//       isthereTour();

        adapter.setData(leftMenus);
        menuGridView.setAdapter(adapter);
        ivCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).toggleDrawer(Gravity.START);
            }
        });

//        uberButtonBlack = (RequestButton) view.findViewById(R.id.uber_button_black);
//
//        RideParameters rideParameters = new RideParameters.Builder()
//                .setProductId(UBERX_PRODUCT_ID)
//                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
//                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
//                .build();
//
//        uberButtonBlack.setRideParameters(rideParameters);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void isthereTour(){
        RequestParams params = new RequestParams();
        params.put(Timeline.ID_MEMBER,APP.getConfig(activity,Preferences.LOGGED_USER_ID));
        touringController.postResponseIsInvitedTouring(params, new OnTourCallback() {
            @Override
            public void OnSuccess(List<Touring> clubs, String message) {

            }

            @Override
            public void OnSuccess(Touring touring, String message) {
                touring1 = touring;
                menuTItleclik = menuTitle;
                menuIconclik = menuIcon;
                status = touring.getStatus();
                if (touring.getStatus().equals("1")||touring.getStatus().equals("4") ){
                    menuTItleclik[6] = R.string.on_tour;
                }else{
                    menuTItleclik[6] = R.string.invitation;
                }
                setData();
            }

            @Override
            public void OnFailed(String message) {
                menuTItleclik = menuTitle;
                if (APP.getConfig(activity,Preferences.STATUS_MEMBER).equals("1")){
                    menuIconclik = menuIcon;
                    menuTItleclik[6] = R.string.create_planning;
                }else{
                    menuIconclik = menuIcon2;
                }
                setData();
            }

            @Override
            public void OnFinish() {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void setUICallbacks() {
        menuGridView.setOnItemClickListener(this);
    }

    @Override
    public void updateUI() {
        loggedUser = UserModel.getMe(activity);

        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, new IntentFilter("image_profil"));
        isthereTour();
    }


    @Override
    public String getPageTitle() {
        return null;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_left_slider;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new EventFragment();
                break;
            case 2:
//                fragment = new PromotionFragment();
                break;
            case 3:
                fragment = new LostFragment();
//                fragment = new EventTabFragment();
//                getBaseActivity().hideActionBarLogo();
//                getBaseActivity().showActionBarTitle();
                break;
            case 4:
                fragment = new ClubFragment();
                break;
            case 5:
                AlertDialog.Builder builder = AlertHelper.getInstance().showAlertWithoutListener(activity, "Confirmation", "Do you want to Log Out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case 6:
                if (!status.equals("")){
                    if (status.equals("1")||status.equals("4")){
                        Bundle bundle=new Bundle();
                        bundle.putString(Touring.ID,touring1.getId());
                        ((MainActivity) activity).changeActivity(OnTourActivity.class,false,bundle,0);
                    }else if (status.equals("0")){
                        JoinPlanningFragment joinPlanningFragment = new JoinPlanningFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString(Touring.ID,touring1.getId());
                        joinPlanningFragment.setArguments(bundle);
                        ft.addToBackStack(joinPlanningFragment.getPageTitle());
                        ft.setCustomAnimations(R.animator.fade_in, 0, 0, R.animator.fade_out);
                        ft.replace(R.id.drawer_layout, joinPlanningFragment, joinPlanningFragment.getPageTitle());
                        ft.commit();
                    }
                } else if (APP.getConfig(activity,Preferences.STATUS_MEMBER).equals("1")){
                    CreatePlanningFragment createPlanningFragment = new CreatePlanningFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(createPlanningFragment.getPageTitle());
                    ft.setCustomAnimations(R.animator.fade_in, 0, 0, R.animator.fade_out);
                    ft.replace(R.id.drawer_layout, createPlanningFragment, createPlanningFragment.getPageTitle());
                    ft.commit();
                }

                break;
            default:
                break;
        }
        changeFragment(fragment, position);
        view.setSelected(true);
        if (selectedView != null)
            selectedView.setSelected(false);
    }
    private void logout() {
        progressDialog.setMessage("Logout...");
        progressDialog.show();
        APP.setConfig(activity, APP.TOKEN_KEY, "");
        APP.setConfig(activity, Preferences.LOGGED_USER_ID,"");
        APP.TOKEN = "";
        try {
        } catch (Throwable e) {
            e.printStackTrace();
        }
        APP.setConfig(activity, Preferences.USER_LOGIN, "N");
        APP.setConfig(activity,Preferences.CLUB_ID,"");
        APP.setConfig(activity,Preferences.STATUS_MEMBER,"");
        activity.finish();
        progressDialog.dismiss();
        startActivity(new Intent(activity, LoginActivity.class));
    }
    public void launchUber() {
        try {
            PackageManager pm = activity.getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri = "uber://?client_id=" + getString(R.string.client_id) +
                    "&action=setPickup" +
                    "&dropoff[latitude]=" + DROPOFF_LAT +
                    "&dropoff[longitude]=" + DROPOFF_LONG +
                    "&dropoff[nickname]=" + DROPOFF_NICK +
                    "&dropoff[formatted_address]=" + DROPOFF_ADDR +
                    "&product_id=" + UBERX_PRODUCT_ID;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
            String url = "https://m.uber.com/sign-up?client_id=" + getString(R.string.client_id);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private void setData() {
        leftMenus.clear();
        for (int i = 0; i < menuIconclik.length; i++) {
            LeftMenu menu = new LeftMenu();
            menu.setIcon(menuIconclik[i]);
            menu.setTitle(getString(menuTItleclik[i]));
            leftMenus.add(menu);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alert != null) {
            alert.dismiss();
        }
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(updateProfileReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(clickMenuReceiver);
    }

    private final BroadcastReceiver updateProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loggedUser = UserModel.getMe(activity);
            updateUI();
        }
    };

    private final BroadcastReceiver clickMenuReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", -1);
            if (position > 0) {
                closedDrawer = false;
                menuGridView.performItemClick(adapter.getView(position, null, null), position, 0);
            }
        }
    };

    @Override
    public void onClick(View v) {
        BaseFragment fragment = null;
        int position = 0;
//        if (v == menuAccount) {
//            position = 7;
//            if (APP.isHasLogin(activity)) {
////                fragment = new AccountFragment();
//            }
//        } else if (v == menuInfo) {
//            position = 6;
//        }
        if (selectedView != null) {
            selectedView.setSelected(false);
        }

//        if (v == uberButtonBlack){
//
//            final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//
//            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                buildAlertMessageNoGps();
//            }else {
//            }
//        }
        v.setSelected(true);
        selectedView = v;
        menuGridView.setItemChecked(-1, true);
        changeFragment(fragment, position);

    }

    public void changeFragment(BaseFragment fragment, int position) {
        if (fragment != null && selectedPosition != position) {
            getBaseActivity().setDefaultActionbarIcon();
                replaceFragment(R.id.fragment_container, fragment, false);
            selectedPosition = position;
        }
        if (closedDrawer)
            ((MainActivity) activity).toggleDrawer(Gravity.START);
        closedDrawer = true;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loggedUser = UserModel.getMe(activity);
//            imagePhotoLoader(loggedUser.getImage(),R.drawable.sliders_avatar_sample_empty, ivUserProfile, 100, progressBar);
        }
    };


}
