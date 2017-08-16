package com.xjgj.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lh on 2017/8/16.
 */

public class CarAddressEntity {

    private int status;//1 单点 2 多点
    private String collectTime;
    private List<CarAddressBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public List<CarAddressBean> getData() {
        return data;
    }

    public void setData(List<CarAddressBean> data) {
        this.data = data;
    }

    public static class CarAddressBean implements Serializable {
        private double longitude;
        private double latitude;

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
    }

}
