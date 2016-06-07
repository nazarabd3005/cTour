package com.giffar.ctour.entitys;

/**
 * Created by nazar on 4/27/2016.
 */
public class Timeline extends BaseEntity {
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String ID_MEMBER = "id_member";
    public static final String PHOTO = "photo";
    public static final String NAME = "name";
    public static final String CREATED_AT = "created_at";
    public static final String LIKE = "like";
    public static final String COMMENT = "comment";
    public static final String ID_CLUB = "id_club";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String ADDRESS = "address";
    public static final String TYPE = "type";

    private String title;
    private String description;
    private String image;
    private String id_member;
    private String photo;
    private String name;
    private String create_at;
    private String like;
    private String comment;
    private String id_club;
    private String longitude;
    private String latitude;
    private String type;

    public Timeline(){

    }
    public Timeline(String name,String photo,String create_at,String description,String image,String like,String comment){
        this.description = description;
        this.name = name;
        this.photo = photo;
        this.create_at = create_at;
        this.image = image;
        this.like = like;
        this.comment =comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getId_club() {
        return id_club;
    }

    public void setId_club(String id_club) {
        this.id_club = id_club;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId_member() {
        return id_member;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
