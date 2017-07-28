package com.xjgj.mall.bean;

/**
 * Created by we-win on 2017/7/28.
 */

public class SearchItem {

    private String address;
    private String city;
    private int index;
    private boolean isHistory;
    private int keywordLenth;
    private double lat;
    private double lng;
    private String name;
    private String poid;

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public int getIndex() {
        return this.index;
    }

    public int getKeywordLenth() {
        return this.keywordLenth;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public String getName() {
        return this.name;
    }

    public String getPoid() {
        return this.poid;
    }

    public boolean isHistory() {
        return this.isHistory;
    }

    public void setAddress(String paramString) {
        this.address = paramString;
    }

    public void setCity(String paramString) {
        this.city = paramString;
    }

    public void setHistory(boolean paramBoolean) {
        this.isHistory = paramBoolean;
    }

    public void setIndex(int paramInt) {
        this.index = paramInt;
    }

    public void setKeywordLenth(int paramInt) {
        this.keywordLenth = paramInt;
    }

    public void setLat(double paramDouble) {
        this.lat = paramDouble;
    }

    public void setLng(double paramDouble) {
        this.lng = paramDouble;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setPoid(String paramString) {
        this.poid = paramString;
    }

}
