package com.xjgj.mall.bean;


/**
 * Created by we-win on 2017/7/27.
 * 下单信息保存
 */

public class OrderCarInfo extends Entity{

    private String serviceTime;
    private String volume;
    private String weight;
    private String serviceType;
    private String carType;
    private String remark;
    private String counts;
    private int flgTogether;

    public OrderCarInfo(String serviceTime, String volume, String weight, String serviceType, String carType, String remark, String counts, int flgTogether) {
        this.serviceTime = serviceTime;
        this.volume = volume;
        this.weight = weight;
        this.serviceType = serviceType;
        this.carType = carType;
        this.remark = remark;
        this.counts = counts;
        this.flgTogether = flgTogether;
    }

    public int getFlgTogether() {
        return flgTogether;
    }

    public void setFlgTogether(int flgTogether) {
        this.flgTogether = flgTogether;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }
}
