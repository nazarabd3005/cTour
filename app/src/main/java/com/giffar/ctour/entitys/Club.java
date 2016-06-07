package com.giffar.ctour.entitys;

/**
 * Created by nazar on 4/25/2016.
 */
public class Club extends BaseEntity {
    public final static String ID_CLUB = "id_club";
    public final static String NAME_CLUB = "club_name";
    public final static String MERK = "type";
    public final static String CATEGORY = "category";
    public final static String COUNT_MEMBER = "count_member";
    public final static String DESCRIPTION = "description";
    public final static String CREATED_DATE = "created_date";

    public final static String CLUB_NAME = "club_name";
    public final static String TYPE = "type";
    public final static String CLUB_LOGO = "club_logo";
    public final static String CLUB_LICENCE = "club_licence";
    public final static String STATUS = "status";


    String id_club;
    String name_club;
    String merk;
    String category;
    String cout_member;
    String description;
    String created_date;

    String club_name;
    String type;
    String club_logo;
    String club_licence;
    String status;


    public Club(){

    }
    public Club(String name_club,String merk,String created_date){
        this.name_club = name_club;
        this.merk = merk;
        this.created_date= created_date;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClub_licence() {
        return club_licence;
    }

    public void setClub_licence(String club_licence) {
        this.club_licence = club_licence;
    }

    public String getClub_logo() {
        return club_logo;
    }

    public void setClub_logo(String club_logo) {
        this.club_logo = club_logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getId_club() {
        return id_club;
    }

    public void setId_club(String id_club) {
        this.id_club = id_club;
    }

    public String getName_club() {
        return name_club;
    }

    public void setName_club(String name_club) {
        this.name_club = name_club;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCout_member() {
        return cout_member;
    }

    public void setCout_member(String cout_member) {
        this.cout_member = cout_member;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
