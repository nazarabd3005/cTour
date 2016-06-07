package com.giffar.ctour.controllers.parsers;

import android.content.Context;

import com.giffar.ctour.entitys.Touring;

import org.json.JSONObject;

/**
 * Created by nazar on 5/16/2016.
 */
public class TouringParser extends BaseParser<Touring> {
    public TouringParser(Context context) {
        super(context);
    }

    @Override
    public Touring parseJsonObject(JSONObject jObj) {
        Touring touring = new Touring();
        touring.setTitle_touring(jObj.optString(Touring.TITLE_TOURING));
        touring.setDescription_touring(jObj.optString(Touring.DESCRIPTION_TOURING));
        touring.setDate_touring(jObj.optString(Touring.DATE_TOURING));
        touring.setLatitude(jObj.optString(Touring.LATITUDE));
        touring.setLongitude(jObj.optString(Touring.LONGITUDE));
        touring.setLatitude_des(jObj.optString(Touring.LATITUDE_DES));
        touring.setLongitude_des(jObj.optString(Touring.LONGITUDE_DES));
        touring.setCreated_at(jObj.optString(Touring.CREATED_AT));
        touring.setId(jObj.optString(Touring.ID));
        touring.setStatus(jObj.optString(Touring.STATUS));
        return touring;
    }
}
