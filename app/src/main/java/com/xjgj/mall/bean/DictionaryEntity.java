package com.xjgj.mall.bean;

import java.util.List;

/**
 * Created by we-win on 2017/8/3.
 */

public class DictionaryEntity {


    private String dictionaryType;

    private List<DataBean> data;

    public String getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(String dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String dictionaryName;
        private int dictionaryId;
        private String dictionaryValue;
        private String describe;
        private int sort;
        private String dictionaryCode;

        public String getDictionaryName() {
            return dictionaryName;
        }

        public void setDictionaryName(String dictionaryName) {
            this.dictionaryName = dictionaryName;
        }

        public int getDictionaryId() {
            return dictionaryId;
        }

        public void setDictionaryId(int dictionaryId) {
            this.dictionaryId = dictionaryId;
        }

        public String getDictionaryValue() {
            return dictionaryValue;
        }

        public void setDictionaryValue(String dictionaryValue) {
            this.dictionaryValue = dictionaryValue;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getDictionaryCode() {
            return dictionaryCode;
        }

        public void setDictionaryCode(String dictionaryCode) {
            this.dictionaryCode = dictionaryCode;
        }
    }
}
