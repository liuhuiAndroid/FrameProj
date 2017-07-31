package com.xjgj.mall.bean;

/**
 * Created by we-win on 2017/7/31.
 */

public class HomepageEntity {

    private int flgAuthBusiness;
    private String realName;
    private String birthDay;
    private String avatarUrl;
    private int flgAuthRealName;
    private int sex;
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getFlgAuthBusiness() {
        return flgAuthBusiness;
    }

    public void setFlgAuthBusiness(int flgAuthBusiness) {
        this.flgAuthBusiness = flgAuthBusiness;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getFlgAuthRealName() {
        return flgAuthRealName;
    }

    public void setFlgAuthRealName(int flgAuthRealName) {
        this.flgAuthRealName = flgAuthRealName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
