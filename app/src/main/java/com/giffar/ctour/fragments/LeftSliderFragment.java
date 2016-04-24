package com.giffar.ctour.fragments;

import android.app.AlertDialog;
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
import com.giffar.ctour.adapters.LeftMenuAdapter;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.customview.WrappingGridView;
import com.giffar.ctour.entitys.LeftMenu;
import com.giffar.ctour.entitys.User;
import com.giffar.ctour.helpers.AlertHelper;
import com.giffar.ctour.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

//import com.uber.sdk.android.rides.RequestButton;
//import com.uber.sdk.android.rides.RideParameters;

public class LeftSliderFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener {
    public static final String UPDATE_USER_ACTION = "update_user_action";
    public static final String CLICK_MENU_ACTION = "click_menu_action";
    private static final int[] menuTitle = {R.string.home, R.string.room, R.string.promo, R.string.event, R.string.book, R.string.custody, R.string.account, R.string.helps};

    private static final int[] menuIcon = {R.drawable.ic_home_selector, R.drawable.ic_room_selector, R.drawable.ic_promo_selector, R.drawable.ic_event_selector, R.drawable.ic_book_selector, R.drawable.ic_custody_selector, R.drawable.ic_account_selector, R.drawable.ic_uber_selector};
    //private ListView lvLeftMenu;
    private LeftMenuAdapter adapter;
    private List<LeftMenu> leftMenus;
    private int selectedPosition = -1;
    private User loggedUser;
    private TextView tvUsername;
    private CircleImageView ivUserProfile;
    private ImageView ivUserBanner, ivCloseDrawer;
    private View menuInfo, menuAccount;
    private WrappingGridView menuGridView;
    private LinearLayout accountHolder;
    private View selectedView;
    GifImageView progressBar;

    private static final String DROPOFF_ADDR = "Jalan Tun Razak Kuala Lumpur Federal Territory of Kuala Lumpur Malaysia";
    private static final float DROPOFF_LAT = 3.16376f;
    private static final float DROPOFF_LONG = 101.71627f;
    private static final String DROPOFF_NICK = "Zouk Club Kuala Lumpur";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";

    private boolean closedDrawer = true;
    public AlertDialog alert;
    //RequestButton uberButtonBlack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LeftMenuAdapter(activity);
        leftMenus = new ArrayList<LeftMenu>();
        setData();
        adapter.setData(leftMenus);
//        loggedUser = UserModel.getMe(activity);

        LocalBroadcastManager.getInstance(activity).registerReceiver(updateProfileReceiver, new IntentFilter(UPDATE_USER_ACTION));
        LocalBroadcastManager.getInstance(activity).registerReceiver(clickMenuReceiver, new IntentFilter(CLICK_MENU_ACTION));

    }

    @Override
    public void initView(View view) {
        accountHolder = (LinearLayout) view.findViewById(R.id.account_holder);
        ivUserProfile = (CircleImageView) view.findViewById(R.id.iv_user_profile);
        tvUsername = (TextView) view.findViewById(R.id.tv_username);
        progressBar = (GifImageView) view.findViewById(R.id.loading);
//        lvLeftMenu = (ListView) view.findViewById(R.id.lv_left_menu);
        menuGridView = (WrappingGridView) view.findViewById(R.id.left_menu_grid);
        ivCloseDrawer = (ImageView) view.findViewById(R.id.iv_close_drawer);
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

    @Override
    public void setUICallbacks() {
        menuGridView.setOnItemClickListener(this);
        accountHolder.setOnClickListener(this);
    }

    @Override
    public void updateUI() {
        loggedUser = UserModel.getMe(activity);
        menuGridView.setAdapter(adapter);
        if (!APP.getConfig(activity, Preferences.USER_LOGIN).equalsIgnoreCase("Y")) {
            tvUsername.setText("Guest");
        } else {
            if (loggedUser != null) {
                tvUsername.setText(addNewLine(loggedUser.getFull_name()));
//                loadImage(loggedUser.getImage(), ivUserProfile, R.drawable.sliders_avatar_sample_empty, new ImageSize(100, 100));
//                ImageLoader.getInstance().displayImage(loggedUser.getImage(),ivUserProfile);
                imagePhotoLoader(loggedUser.getImage(), R.drawable.sliders_avatar_sample_empty, ivUserProfile, 100, progressBar);
            }
        }
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, new IntentFilter("image_profil"));

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
//                fragment = new RoomFragment();
                break;
            case 2:
//                fragment = new PromotionFragment();
                break;
            case 3:
//                fragment = new EventTabFragment();
                getBaseActivity().hideActionBarLogo();
                getBaseActivity().showActionBarTitle();
                break;
            case 4:
//                fragment = new RSVNFragment();
                break;
            case 5:
//                fragment = new CustodyFragment();
                break;
            case 6:
                if (APP.getConfig(activity, Preferences.USER_LOGIN).equals("Y")) {
//                    fragment = new AccountFragment();
                } else {
                    AlertDialog.Builder builder = AlertHelper.getInstance().showAlertWithoutListener(activity, "Account Permission", "you need to login first. Login?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity) getActivity()).changeActivity(LoginActivity.class, true, null, 0);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            AlertHelper.getInstance().showAlert(MainActivity.this, "To use this App you need to turn on your Bluetooth");
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

                break;
            case 7:
//                fragment = new HelpActivity();
                launchUber();
            default:
                break;
        }
        changeFragment(fragment, position);
        view.setSelected(true);
        if (selectedView != null)
            selectedView.setSelected(false);
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
        for (int i = 0; i < menuIcon.length; i++) {
            LeftMenu menu = new LeftMenu();
            menu.setIcon(menuIcon[i]);
            menu.setTitle(getString(menuTitle[i]));
            leftMenus.add(menu);
        }
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
        if (v == menuAccount) {
            position = 7;
            if (APP.isHasLogin(activity)) {
//                fragment = new AccountFragment();
            }
        } else if (v == menuInfo) {
            position = 6;
        }
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

        if (v == accountHolder) {
            if (APP.getConfig(activity, Preferences.USER_LOGIN).equals("Y")) {
//                ((MainActivity) activity).changeActivity(EditProfileActivity.class);
            } else {
                AlertDialog.Builder builder = AlertHelper.getInstance().showAlertWithoutListener(activity, "Account Permission", "You need to login first. Login?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity) getActivity()).changeActivity(LoginActivity.class, true, null, 0);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            AlertHelper.getInstance().showAlert(MainActivity.this, "To use this App you need to turn on your Bluetooth");
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
        v.setSelected(true);
        selectedView = v;
        menuGridView.setItemChecked(-1, true);
        changeFragment(fragment, position);

    }

    public void changeFragment(BaseFragment fragment, int position) {
        if (fragment != null && selectedPosition != position) {
            getBaseActivity().setDefaultActionbarIcon();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (position == 0)
                replaceFragment(R.id.fragment_container, fragment, false);
            else
                replaceFragment(R.id.fragment_container, fragment, true);
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

            imagePhotoLoader(loggedUser.getImage(),R.drawable.sliders_avatar_sample_empty, ivUserProfile, 100, progressBar);
            tvUsername.setText(addNewLine(loggedUser.getFull_name()));
        }
    };
}
