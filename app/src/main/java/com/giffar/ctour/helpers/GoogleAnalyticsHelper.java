package com.giffar.ctour.helpers;

import android.app.Activity;

import com.giffar.ctour.APP;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by egiadtya on 3/11/15.
 */
public class GoogleAnalyticsHelper {
    public static final String OFFER_DETAIL_PAGE = "Offer Detail";
    public static final String EVENT_DETAIL_PAGE = "Event Detail";
    public static final String CONTEST_DETAIL_PAGE = "Contest Detail";
    public static final String OPEN_ACTION = "Open";
    public static final String SHARE_ACTION = "Share";
    public static final String SAVE_ACTION = "Save";
    public static final String DELETE_ACTION = "Delete";
    public static final String REDEEM_ACTION = "Redeem";

    public static void eventTracker(Activity activity, String category, String action, String label) {
        Tracker t = ((APP) activity.getApplication()).getTracker();
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public static void screenTracker(Activity activity, String screenName) {
        Tracker t = ((APP) activity.getApplication()).getTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
