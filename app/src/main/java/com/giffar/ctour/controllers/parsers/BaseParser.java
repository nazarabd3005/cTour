package com.giffar.ctour.controllers.parsers;

import android.content.Context;


import com.giffar.ctour.callbacks.OnParseJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseParser<T> implements OnParseJson<T> {
    public static final String JSON_STATUS = "success";
    public static final String JSON_ARRAY_ROOT = "list";
    public static final String JSON_MORE_KEY = "more";
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_EVENT   ="event";
    public static final String JSON_ROOM   ="room";
    private String parseMessage = "";
    private List<T> result;
    protected Context context;

    public BaseParser(Context context) {
        result = new ArrayList<T>();
        this.context = context;
    }

    public boolean parse(String json) {
        JSONObject resObj;
        try {
            resObj = new JSONObject(json);
            if (resObj.optBoolean(JSON_STATUS)) {
                JSONArray jsonArr = resObj.optJSONArray(JSON_ARRAY_ROOT);
                parseMessage = resObj.optString(JSON_MESSAGE);
                result = parseJsonArray(jsonArr);
                return true;
            } else {
                parseMessage = resObj.optString(JSON_MESSAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<T> parseJsonArray(JSONArray jArr) {
        List<T> list = new ArrayList<T>();
        if (jArr != null) {
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject jsonObject = jArr.optJSONObject(i);
                if (jsonObject != null) {
                    list.add(parseJsonObject(jsonObject));
                }
            }
        }
        return list;
    }

    public String getParseMessage() {
        return parseMessage;
    }

    public List<T> getResult() {
        return result;
    }
}