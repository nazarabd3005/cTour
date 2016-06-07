package com.giffar.ctour.entitys;

/**
 * Created by nazar on 5/2/2016.
 */
public class OnTour extends Member {
    public static final String DISTANCE = "distance";
    public static final String POSITION = "position";

    private String distance;
    private String position;

    public OnTour(String nama, String photo, String phone, String email,String distance,String position) {
        super(nama, photo, phone, email);
        this.distance = distance;
        this.position = position;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
