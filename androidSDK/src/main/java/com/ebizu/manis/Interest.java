package com.ebizu.manis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kazao
 */
public class Interest {

    private String id;
    private String name;
    private String description;
    private String image;
    private Value interest;

    public Interest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Value getInterest() {
        return interest;
    }

    public void setInterest(Value interest) {
        this.interest = interest;
    }

    public enum Value {

        Y, N
    }

    public static Interest create(String id, boolean interest) {
        Interest in = new Interest();
        in.setId(id);
        in.setInterest(interest ? Value.Y : Value.N);
        return in;
    }

    public static JSONObject create(Interest interest) {
        return create(interest, false);
    }

    public static JSONObject create(Interest interest, boolean reset) {
        return create(new Interest[]{interest}, reset);
    }

    public static JSONObject create(Interest[] interests) {
        return create(interests, false);
    }

    public static JSONObject create(Interest[] interests, boolean reset) {
        JSONObject json = new JSONObject();
        try {
            if (reset) {
                json.put("reset", "Y");
            }
            JSONArray array = new JSONArray();
            for (Interest interest : interests) {
                JSONObject in = new JSONObject();
                in.put("id", interest.id);
                in.put("interest", interest.interest == Value.Y ? "Y" : "N");
                array.put(in);
            }
            json.put("interests", array);
        } catch (JSONException e) {
        }
        return json;
    }
}
