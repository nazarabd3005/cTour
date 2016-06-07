package com.giffar.ctour.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.giffar.ctour.activities.OnTourActivity;
import com.giffar.ctour.fragments.LeftSliderFragment;

/**
 * Created by egiadtya on 3/31/15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
            Intent intent1 = new Intent(OnTourActivity.EMERGENCY);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
}
