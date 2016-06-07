package com.giffar.ctour.controllers.parsers;

import android.content.Context;

import com.giffar.ctour.entitys.Chatting;

import org.json.JSONObject;

/**
 * Created by nazar on 5/16/2016.
 */
public class ChattingParser extends BaseParser<Chatting> {
    public ChattingParser(Context context) {
        super(context);
    }

    @Override
    public Chatting parseJsonObject(JSONObject jObj) {
        Chatting chatting = new Chatting();
        chatting.setId(jObj.optString(Chatting.ID));
        chatting.setMessage(jObj.optString(Chatting.MESSAGE));
        chatting.setSender_id(jObj.optString(Chatting.SENDER_ID));
        chatting.setReceiver_id(jObj.optString(Chatting.RECEIVER_ID));
        chatting.setSender_name(jObj.optString(Chatting.SENDER_NAME));
        chatting.setCreate_at(jObj.optString(Chatting.CREATED_AT));
        return chatting;
    }
}
