package com.xjgj.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by we-win on 2017/8/1.
 */

public class OrderDetailEntity {

    private String contactName;
    private String contactMobile;
    private String avatarUrl;
    private int flgTogether;

    private double starLevel;
    private double amount;
    private int orderId;
    private int counts;
    private double tripMeter;
    private String cancelType;
    private double weight;
    private String remark;
    private String serviceTime;
    private double volume;
    private int carType;
    private String carName;
    private String createTime;
    private int status;
    private String serviceType;
    private String carNo;
    private List<EvaluationBean> evaluation;
    private List<AddressListBean> addressList;
    private List<CarAddressBean> carAddress;

    public List<CarAddressBean> getCarAddress() {
        return carAddress;
    }

    public void setCarAddress(List<CarAddressBean> carAddress) {
        this.carAddress = carAddress;
    }

    public int getFlgTogether() {
        return flgTogether;
    }

    public void setFlgTogether(int flgTogether) {
        this.flgTogether = flgTogether;
    }

    public double getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(double starLevel) {
        this.starLevel = starLevel;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public double getTripMeter() {
        return tripMeter;
    }

    public void setTripMeter(double tripMeter) {
        this.tripMeter = tripMeter;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<EvaluationBean> getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(List<EvaluationBean> evaluation) {
        this.evaluation = evaluation;
    }

    public List<AddressListBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressListBean> addressList) {
        this.addressList = addressList;
    }

    public static class EvaluationBean {
        private int userId;
        private float level;
        private String createTime;
        private String content;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public float getLevel() {
            return level;
        }

        public void setLevel(float level) {
            this.level = level;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
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

    public static class AddressListBean {
        private String address;
        private String receiverPhone;
        private int addressSort;
        private String receiverName;
        private double latitude;
        private String addressName;
        private double longitude;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReceiverPhone() {
            return receiverPhone;
        }

        public void setReceiverPhone(String receiverPhone) {
            this.receiverPhone = receiverPhone;
        }

        public int getAddressSort() {
            return addressSort;
        }

        public void setAddressSort(int addressSort) {
            this.addressSort = addressSort;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
