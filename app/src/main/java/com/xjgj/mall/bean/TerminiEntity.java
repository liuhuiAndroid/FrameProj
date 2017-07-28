package com.xjgj.mall.bean;

/**
 * Created by we-win on 2017/7/27.
 * 下单目的地保存
 */

public class TerminiEntity extends Entity {


    private String addressName; // 阜新路20弄
    private String addressDescribeName; // 国中酒店
    private double longitude;
    private double latitude;
    private String receiverName;
    private String receiverPhone;

    public TerminiEntity() {
    }

    public TerminiEntity(String addressName, String addressDescribeName, double longitude, double latitude, String receiverName, String receiverPhone) {
        this.addressName = addressName;
        this.addressDescribeName = addressDescribeName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressDescribeName() {
        return addressDescribeName;
    }

    public void setAddressDescribeName(String addressDescribeName) {
        this.addressDescribeName = addressDescribeName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
