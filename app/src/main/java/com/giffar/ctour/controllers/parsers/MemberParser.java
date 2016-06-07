package com.giffar.ctour.controllers.parsers;

import android.content.Context;

import com.giffar.ctour.entitys.Member;

import org.json.JSONObject;

/**
 * Created by nazar on 5/16/2016.
 */
public class MemberParser extends BaseParser<Member> {
    public MemberParser(Context context) {
        super(context);
    }

    @Override
    public Member parseJsonObject(JSONObject jObj) {
        Member member = new Member();
        member.setId(jObj.optString(Member.ID));
//        member.setEmail(jObj.optString(Member.EMAIL));
        member.setNama(jObj.optString(Member.NAMA));
        member.setPhone(jObj.optString(Member.PHONE));
        member.setBirthdate(jObj.optString(Member.BIRTHDATE));
        member.setUsername(jObj.optString(Member.USERNAME));
        member.setPassword(jObj.optString(Member.PASSWORD));
        member.setLatitude(jObj.optString(Member.LATITUDE));
        member.setLongitude(jObj.optString(Member.LONGITUDE));
        member.setId_club(jObj.optString(Member.ID_CLUB));
        member.setStatus(jObj.optString(Member.STATUS));
        member.setGcm_id(jObj.optString(Member.GCM_ID));
        member.setPhoto(jObj.optString(Member.PHOTO));
        member.setStatus_join(jObj.optString(Member.STATUS_JOIN));
        member.setDistance(jObj.optString(Member.DISTANCE));
        member.setClub(jObj.optString(Member.CLUB));
        return member;
    }
}
