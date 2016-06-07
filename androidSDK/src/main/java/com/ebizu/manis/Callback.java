package com.ebizu.manis;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author kazao
 */
public abstract class Callback {

    public void onStart() {
    }

    public void onProgress(int step, int percentage) {
    }

    public abstract void onFinish(JSONObject object, JSONArray array, JSONObject error);
}
