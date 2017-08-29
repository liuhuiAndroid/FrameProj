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
        private int versionCode;
        private String versionName;
        private String versionUrl;

        private String packageSize;
        private String updateDetail;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public void setVersionUrl(String versionUrl) {
            this.versionUrl = versionUrl;
        }

        public String getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(String packageSize) {
            this.packageSize = packageSize;
        }

        public String getUpdateDetail() {
            return updateDetail;
        }

        public void setUpdateDetail(String updateDetail) {
            this.updateDetail = updateDetail;
        }
    }
}
