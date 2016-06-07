package com.giffar.ctour.entitys;

/**
 * Created by nazar on 5/2/2016.
 */
public class Member extends BaseEntity {
    public static final String NAMA = "nama";
    public static final String PHOTO = "photo";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String BIRTHDATE = "birthdate";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ID_CLUB = "id_club";
    public static final String STATUS = "status";
    public static final String STATUS_JOIN = "status_join";
    public static final String GCM_ID = "gcm_id";
    public static final String DISTANCE = "distance";
    public static final String CLUB = "club";

    private String nama;
    private String photo;
    private  String phone;
    private String email;
    private String birthdate;
    private String username;
    private String password;
    private String latitude;
    private String longitude;
    private String id_club;
    private String status;
    private String gcm_id;
    private String status_join;
    private String distance;
    private String club;
    public Member(){

    }

    public Member(String nama,String photo,String phone,String email){
        this.email = email;
        this.phone = phone;
        this.nama = nama;
        this.photo = photo;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus_join() {
        return status_join;
    }

    public void setStatus_join(String status_join) {
        this.status_join = status_join;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId_club() {
        return id_club;
    }

    public void setId_club(String id_club) {
        this.id_club = id_club;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getNama() {

        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
