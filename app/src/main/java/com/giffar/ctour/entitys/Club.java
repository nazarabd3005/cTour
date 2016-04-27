package com.giffar.ctour.entitys;

/**
 * Created by nazar on 4/25/2016.
 */
public class Club extends BaseEntity {
    public final String ID_CLUB = "club";
    public final String NAME_CLUB = "name_club";
    public final String MERK = "merk";
    public final String CATEGORY = "category";
    public final String COUNT_MEMBER = "count_member";
    public final String DESCRIPTION = "description";
    public final String CREATED_DATE = "create_Date";

    String id_club;
    String name_club;
    String merk;
    String category;
    String cout_member;
    String description;
    String created_date;

    public Club(String name_club,String merk,String created_date){
        this.name_club = name_club;
        this.merk = merk;
        this.created_date= created_date;
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
