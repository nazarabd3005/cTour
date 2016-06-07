package com.giffar.ctour.controllers.parsers;

import android.content.Context;

import com.giffar.ctour.entitys.Club;

import org.json.JSONObject;

/**
 * Created by nazar on 5/16/2016.
 */
public class ClubParser extends BaseParser<Club> {
    public ClubParser(Context context) {
        super(context);
    }

    @Override
    public Club parseJsonObject(JSONObject jObj) {
        Club club = new Club();
        club.setId(jObj.optString(Club.ID));
        club.setName_club(jObj.optString(Club.NAME_CLUB));
        club.setMerk(jObj.optString(Club.MERK));
        club.setCategory(jObj.optString(Club.CATEGORY));
        club.setCout_member(jObj.optString(Club.COUNT_MEMBER));
        club.setDescription(jObj.optString(Club.DESCRIPTION));
        club.setCreated_date(jObj.optString(Club.CREATED_DATE));
        club.setStatus(jObj.optString(Club.STATUS));
        club.setClub_logo(jObj.optString(Club.CLUB_LOGO));
        club.setClub_licence(jObj.optString(Club.CLUB_LICENCE));
        return club;
    }
}
