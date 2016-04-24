/**
 * Base Application
 *
 * @author egiadtya
 * 8 September 2014
 */
package com.giffar.ctour;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.giffar.ctour.controllers.ManisEncoder;
import com.giffar.ctour.utils.TypefaceUtil;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.security.Security;


public class APP extends MultiDexApplication {

    public static String TAG = Config.APP_NAME;
    private static Context mContext;
    public static String TOKEN;
    public static final String TOKEN_KEY = "token";
    public static final String IS_MOESLEM = "is_moeslem";
    private static SharedPreferences PREFERENCES;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Config.setMode(Config.MODE.DEVELOPMENT);
        setImageLoaderConfig();
        TypefaceUtil.overrideFont(mContext, "SERIF", "fonts/ProximaNovaCondSemibold.otf");
    }

    public static void log(String message) {
        if (Config.isDevelopment) {
            Log.i(Config.APP_NAME, message);
        }
    }

    public static void log(String message, String type) {
        if (Config.isDevelopment) {
            switch (type) {
                case "i":
                    Log.i(Config.APP_NAME, message);
                    break;
                case "e":
                    Log.i(Config.APP_NAME, message);
                    break;
                case "d":
                    Log.i(Config.APP_NAME, message);
                    break;
                default:
                    break;
            }
            Log.i(Config.APP_NAME, message);
        }
    }

    public static DisplayImageOptions imageOptions() {
        return new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new FadeInBitmapDisplayer(200)).build();
    }

    public static Context getContext() {
        return mContext;
    }

    private void setImageLoaderConfig() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(imageOptions())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static Session getSession(Context context) {
        Session session = Session.getInstance();
        if (session.getToken() == null) {
            if (TOKEN == null) {
                TOKEN = APP.getConfig(context, APP.TOKEN_KEY);
            }
            session.setToken(TOKEN);
        }
        return session;
    }

    public static SharedPreferences getPreference(Context context) {
        if (PREFERENCES == null) {
            PREFERENCES = context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return PREFERENCES;
    }

    public static void setConfig(Context context, String key, String value) {
        SharedPreferences preferences = getPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        String e_value = "";
        try {
            e_value = ManisEncoder.encrypt(value, context.getString(R.string.ManisEncryptKey));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            editor.putString(key, e_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
        if (key.equals(APP.TOKEN_KEY)) {
            try {
                APP.TOKEN = value;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getConfig(Context context, String key) {
        SharedPreferences preferences = getPreference(context);
        String value = "";
        try {
            value = ManisEncoder.decrypt(preferences.getString(key, ""),
                    context.getString(R.string.ManisEncryptKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void removeConfig(Context context, String key) {
        SharedPreferences preferences = getPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public static boolean checkConfig(Context context, String key) {
        SharedPreferences preferences = getPreference(context);
        return preferences.contains(key);
    }

    public static boolean isHasLogin(Context context) {
        return getConfig(context, Preferences.USER_LOGIN).equalsIgnoreCase("Y");
    }

    public static String getMallKey() {
        return getConfig(getContext(), Preferences.MALL_KEY);
    }

    public synchronized Tracker getTracker() {
        return GoogleAnalytics.getInstance(this).newTracker(getString(R.string.ga_trackingId));
    }


    public static Spanned fromHtml(String message) {
        if (message != null) {
            return Html.fromHtml(message);
        } else {
            return Html.fromHtml("");
        }
    }

}
