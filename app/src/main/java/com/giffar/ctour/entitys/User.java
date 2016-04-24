package com.giffar.ctour.entitys;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by egiadtya on 2/25/15.
 */
public class User extends BaseEntity implements Parcelable {
    public static final String USER = "user";
    public static final String MEMBER_ID = "member_id";
    public static final String USER_ID = "user_id";
    public static final String IMAGE = "photo";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
    public static final String GENDER = "gender";
    public static final String IDENTITY_NO = "identity_no";
    public static final String IS_MOESLEM = "is_moeslem";
    public static final String DEVICE_ID = "device_id";
    public static final String USER_IP = "user_ip";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String MOBILE_NO = "mobile_no";
    public static final String FULL_NAME = "full_name";
    public static final String MEMBERSHIP_NO = "membership_no";
    public static final String CODE = "code";

    @DatabaseField(columnName = MEMBER_ID)
    private String member_id;
    @DatabaseField(columnName = USER_ID)
    private String user_id;
    @DatabaseField(columnName = IMAGE)
    private String image;
    @DatabaseField(columnName = EMAIL)
    private String email = "";
    @DatabaseField(columnName = PASSWORD)
    private String password = "";
    @DatabaseField(columnName = MOBILE_NO)
    private String mobile_no     = "";
    @DatabaseField(columnName = ADDRESS)
    private String address = "";
    @DatabaseField(columnName = GENDER)
    private String gender = "";
    @DatabaseField(columnName = IDENTITY_NO)
    private String identity_no;
    @DatabaseField(columnName = IS_MOESLEM)
    private String is_moeslem;
    @DatabaseField(columnName = DEVICE_ID)
    private String device_id;
    @DatabaseField(columnName = USER_IP)
    private String user_ip;
    @DatabaseField(columnName = DATE_OF_BIRTH)
    private String date_of_birth;
    @DatabaseField(columnName = FULL_NAME)
    private String full_name;
    @DatabaseField(columnName = MEMBERSHIP_NO)
    private String membership_no;
    @DatabaseField(columnName = CODE)
    private String code;

    public String getMembership_no() {
        return membership_no;
    }

    public void setMembership_no(String membership_no) {
        this.membership_no = membership_no;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentity_no() {
        return identity_no;
    }

    public void setIdentity_no(String identity_no) {
        this.identity_no = identity_no;
    }

    public String getIs_moeslem() {
        return is_moeslem;
    }

    public void setIs_moeslem(String is_moeslem) {
        this.is_moeslem = is_moeslem;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    // parceling

    public User() {
    }

    public User(Parcel in){
        String[] data = new String[11];

        in.readStringArray(data);
        this.email  = data[0];
        this.password = data[1];
        this.full_name = data[2];
        this.mobile_no = data[3];
        this.date_of_birth = data[4];
        this.gender = data[5];
        this.identity_no = data[6];
        this.is_moeslem = data[7];
        this.device_id = data[8];
        this.user_ip = data[9];
        this.image = data[10];
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.email,
                this.password,
                this.full_name,
                this.mobile_no,
                this.date_of_birth,
                this.gender,
                this.identity_no,
                this.is_moeslem,
                this.device_id,
                this.user_ip,
                this.image,
        });
    }
    public static final Creator CREATOR = new Creator() {
            public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
