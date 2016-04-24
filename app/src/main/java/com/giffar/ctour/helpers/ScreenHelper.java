package com.giffar.ctour.helpers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by egiadtya on 3/24/15.
 */
public class ScreenHelper {
    private static ScreenHelper screenHelper;

    public static ScreenHelper getInstance() {
        if (screenHelper == null) {
            screenHelper = new ScreenHelper();
        }
        return screenHelper;
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public DisplayMetrics getDensityScreen(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public int getDPISize(Context context, int value) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (value * displayMetrics.density);
    }

}
