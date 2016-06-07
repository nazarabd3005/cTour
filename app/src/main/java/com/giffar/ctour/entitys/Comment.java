package com.giffar.ctour.entitys;

/**
 * Created by nazar on 5/16/2016.
 */
public class Comment extends BaseEntity {
    public static final String SENDER_ID = "sender_id";
    public static final String MESSAGE = "message";
    public static final String SENDER_NAME = "sender_name";
    public static final String CREATED_AT = "created_at";

    private String sender_id;
    private String message;
    private String sender_name;
    private String created_at;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
