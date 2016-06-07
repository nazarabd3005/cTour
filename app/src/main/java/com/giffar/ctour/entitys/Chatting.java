package com.giffar.ctour.entitys;

/**
 * Created by nazar on 5/16/2016.
 */
public class Chatting extends BaseEntity {
    public static final String SENDER_ID = "sender_id";
    public static final String SENDER_NAME = "sender_name";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String MESSAGE = "message";
    public static final String CREATED_AT = "create_at";

    private String sender_id;
    private String sender_name;
    private String receiver_id;
    private String message;
    private String create_at;
    public Chatting(){

    }
    public Chatting(String sender_id,String sender_name,String receiver_id,String message,String create_at){
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.receiver_id = receiver_id;
        this.message = message;
        this.create_at = create_at;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
