package com.xjgj.mall.bean;

/**
 * Created by lh on 2017/8/28.
 */

public class UpdateAppEntity {

    private ResultStatusBean resultStatus;
    private ResultValueBean resultValue;

    public ResultStatusBean getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusBean resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ResultValueBean getResultValue() {
        return resultValue;
    }

    public void setResultValue(ResultValueBean resultValue) {
        this.resultValue = resultValue;
    }

    public static class ResultStatusBean {
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ResultValueBean {
        private int versionNo;
        private int versionIosNo;
        private String versionUrl;
        private String versionIosUrl;

        public int getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(int versionNo) {
            this.versionNo = versionNo;
        }

        public int getVersionIosNo() {
            return versionIosNo;
        }

        public void setVersionIosNo(int versionIosNo) {
            this.versionIosNo = versionIosNo;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public void setVersionUrl(String versionUrl) {
            this.versionUrl = versionUrl;
        }

        public String getVersionIosUrl() {
            return versionIosUrl;
        }

        public void setVersionIosUrl(String versionIosUrl) {
            this.versionIosUrl = versionIosUrl;
        }
    }
}
