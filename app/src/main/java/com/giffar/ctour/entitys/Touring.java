package com.giffar.ctour.entitys;

/**
 * Created by nazar on 5/16/2016.
 */
public class Touring extends BaseEntity {
    public static final String TITLE_TOURING = "title_touring";
    public static final String DESCRIPTION_TOURING = "description_touring";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE_DES = "latitude_des";
    public static final String LONGITUDE_DES = "longitude_des";
    public static final String CREATED_AT = "created_at";
    public static final String DATE_TOURING = "date_touring";
    public static final String ID_CLUB = "id_club";
    public static final String STATUS = "status";
    private String title_touring;
    private String description_touring;
    private String latitude;
    private String longitude;
    private String latitude_des;
    private String longitude_des;
    private String created_at;
    private String date_touring;
    private String id_club;
    private String status;

    public String getLatitude_des() {
        return latitude_des;
    }

    public void setLatitude_des(String latitude_des) {
        this.latitude_des = latitude_des;
    }

    public String getLongitude_des() {
        return longitude_des;
    }

    public void setLongitude_des(String longitude_des) {
        this.longitude_des = longitude_des;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_club() {
        return id_club;
    }

    public void setId_club(String id_club) {
        this.id_club = id_club;
    }

    public String getTitle_touring() {
        return title_touring;
    }

    public void setTitle_touring(String title_touring) {
        this.title_touring = title_touring;
    }

    public String getDescription_touring() {
        return description_touring;
    }

    public void setDescription_touring(String description_touring) {
        this.description_touring = description_touring;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDate_touring() {
        return date_touring;
    }

    public void setDate_touring(String date_touring) {
        this.date_touring = date_touring;
    }
}
