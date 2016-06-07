package com.giffar.ctour.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.giffar.ctour.activities.OnTourActivity;

/**
 * Created by nazar on 5/22/2016.
 */
public class remoteBroadcastReceiver extends BroadcastReceiver {

    public remoteBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            // do something
            Intent intent1 = new Intent(OnTourActivity.BUTTON_HEADSET_EMERGENCY);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
//            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
        }
        abortBroadcast();
    }
}
