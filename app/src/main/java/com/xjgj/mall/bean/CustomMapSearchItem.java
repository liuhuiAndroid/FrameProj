package com.xjgj.mall.bean;

/**
 * Created by we-win on 2017/7/28.
 */

public class CustomMapSearchItem {

    private String name;
    private double lng;
    private double lat;

    public CustomMapSearchItem(String name, double lng, double lat) {
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
