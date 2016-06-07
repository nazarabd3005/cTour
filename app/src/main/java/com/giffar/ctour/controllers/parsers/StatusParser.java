package com.giffar.ctour.controllers.parsers;

import android.content.Context;

import com.giffar.ctour.entitys.Timeline;

import org.json.JSONObject;

import java.sql.Time;

/**
 * Created by nazar on 5/16/2016.
 */
public class StatusParser extends BaseParser<Timeline> {
    public StatusParser(Context context) {
        super(context);
    }

    @Override
    public Timeline parseJsonObject(JSONObject jObj) {
        Timeline timeline = new Timeline();
        timeline.setId(jObj.optString(Timeline.ID));
        timeline.setName(jObj.optString(Timeline.NAME));
        timeline.setDescription(jObj.optString(Timeline.DESCRIPTION));
        timeline.setImage(jObj.optString(Timeline.IMAGE));
        timeline.setTitle(jObj.optString(Timeline.TITLE));
        timeline.setId_club(jObj.optString(Timeline.ID_CLUB));
        timeline.setLike(jObj.optString(Timeline.LIKE));
        timeline.setComment(jObj.optString(Timeline.COMMENT));
        timeline.setCreate_at(jObj.optString(Timeline.CREATED_AT));
        timeline.setPhoto(jObj.optString(Timeline.PHOTO));
        return timeline;
    }
}
