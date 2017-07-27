package com.lh.frameproj.bean;

/**
 * Created by we-win on 2017/7/27.
 * 下单目的地保存
 */

public class TerminiEntity extends Entity {


    private String addressName; // 阜新路20弄
    private String addressDescribeName; // 国中酒店
    private String longitude;
    private String latitude;
    private String receiverName;
    private String receiverPhone;

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
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
