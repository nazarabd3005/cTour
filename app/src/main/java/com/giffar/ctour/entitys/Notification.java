package com.giffar.ctour.entitys;

import com.j256.ormlite.field.DatabaseField;

public class Notification extends BaseEntity {

    public static final String NAME = "name";
    public static final String COMPANY = "company";
    public static final String PICTURE = "picture";
    public static final String TYPE = "type";
    public static final String BUTTON_TEXT = "button_text";
    public static final String LINK = "link";
    public static final String TIMESTAMP = "timestamp";
    public static final String ACTION = "action";
    public static final String READ = "read";
    public static final String TITLE = "title";

    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = COMPANY)
    private String company;
    @DatabaseField(columnName = PICTURE)
    private String picture;
    @DatabaseField(columnName = TITLE)
    private String title;
    @DatabaseField(columnName = TYPE)
    private String type;
    @DatabaseField(columnName = BUTTON_TEXT)
    private String buttonText;
    @DatabaseField(columnName = LINK)
    private String link;
    @DatabaseField(columnName = TIMESTAMP)
    private String timestamp;
    private String action;
    @DatabaseField(columnName = READ)
    private boolean read = false;

    public Notification(){

    }
    public Notification(String name,String company,String timestamp,String picture,String title,String type){
        this.name = name;
        this.company = company;
        this.timestamp = timestamp;
        this.picture = picture;
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
