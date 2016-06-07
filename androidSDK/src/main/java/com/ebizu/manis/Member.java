package com.ebizu.manis;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kazao
 */
public class Member {

    private String name;
    private String email;
    private String mobilePhoneNumber;
    private Date birthdate;
    private Gender gender;
    private String facebookId;
    private String ref_code;

    public Member() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
    
    public String getRefCode(){
    	return ref_code;
    }
    
    public void setRefCode(String ref_code){
    	this.ref_code = ref_code;
    }

    public JSONObject createJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("email", email);
            json.put("mobile_number", Utility.encode(mobilePhoneNumber));
        	json.put("birthdate", Utility.formatDate(birthdate));
            json.put("gender", gender == Gender.MALE ? "M" : "F");
            json.put("ref_code", ref_code);
            if (facebookId != null) {
                json.put("facebook_id", facebookId);
            }
        } catch (JSONException e) {
        }
        return json;
    }

    public enum Gender {

        MALE, FEMALE
    }
}
