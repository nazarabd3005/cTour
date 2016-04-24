package com.giffar.ctour.callbacks;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wafdamufti on 11/17/15.
 */
public abstract class Callback {

    public void onStart() {
    }

    public void onProgress(int step, int percentage) {
    }

    public abstract void onFinish(JSONObject object, JSONArray array, JSONObject error);
}
