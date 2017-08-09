package com.android.frameproj.library.statefullayout;

import android.view.View;

/**
 * Created by lh on 2017/8/9.
 *
 * Build模式给StatefulLayout设置各种属性
 */

public class StatusfulConfig {

    private int emptyImageResId;
    private String emptyTextTitle;
    private String emptyTextContent;

    private int errorImageResId;
    private String errorTextTitle;
    private String errorTextContent;
    private String errorButtonText;
    private View.OnClickListener onErrorStateButtonClickListener;

    private StatusfulConfig() {
    }

    public int getEmptyImageResId() {
        return emptyImageResId;
    }

    public void setEmptyImageResId(int emptyImageResId) {
        this.emptyImageResId = emptyImageResId;
    }

    public String getEmptyTextTitle() {
        return emptyTextTitle;
    }

    public void setEmptyTextTitle(String emptyTextTitle) {
        this.emptyTextTitle = emptyTextTitle;
    }

    public String getEmptyTextContent() {
        return emptyTextContent;
    }

    public void setEmptyTextContent(String emptyTextContent) {
        this.emptyTextContent = emptyTextContent;
    }

    public int getErrorImageResId() {
        return errorImageResId;
    }

    public void setErrorImageResId(int errorImageResId) {
        this.errorImageResId = errorImageResId;
    }

    public String getErrorTextTitle() {
        return errorTextTitle;
    }

    public void setErrorTextTitle(String errorTextTitle) {
        this.errorTextTitle = errorTextTitle;
    }

    public String getErrorTextContent() {
        return errorTextContent;
    }

    public void setErrorTextContent(String errorTextContent) {
        this.errorTextContent = errorTextContent;
    }

    public String getErrorButtonText() {
        return errorButtonText;
    }

    public void setErrorButtonText(String errorButtonText) {
        this.errorButtonText = errorButtonText;
    }

    public View.OnClickListener getOnErrorStateButtonClickListener() {
        return onErrorStateButtonClickListener;
    }

    public void setOnErrorStateButtonClickListener(View.OnClickListener onErrorStateButtonClickListener) {
        this.onErrorStateButtonClickListener = onErrorStateButtonClickListener;
    }

    public static class Builder {

        private StatusfulConfig config;

        public Builder() {
            config = new StatusfulConfig();
        }

        public Builder setEmptyImageResId(int emptyImageResId) {
            config.setEmptyImageResId(emptyImageResId);
            return this;
        }

        public Builder setEmptyTextTitle(String emptyTextTitle) {
            config.setEmptyTextTitle(emptyTextTitle);
            return this;
        }

        public Builder setEmptyTextContent(String emptyTextContent) {
            config.setEmptyTextContent(emptyTextContent);
            return this;
        }

        public Builder setErrorImageResId(int errorImageResId) {
            config.setErrorImageResId(errorImageResId);
            return this;
        }

        public Builder setErrorTextTitle(String errorTextTitle) {
            config.setErrorTextTitle(errorTextTitle);
            return this;
        }

        public Builder setErrorTextContent(String errorTextContent) {
            config.setErrorTextContent(errorTextContent);
            return this;
        }

        public Builder setErrorButtonText(String errorButtonText) {
            config.setErrorButtonText(errorButtonText);
            return this;
        }

        public Builder setOnErrorStateButtonClickListener(View.OnClickListener onErrorStateButtonClickListener) {
            config.setOnErrorStateButtonClickListener(onErrorStateButtonClickListener);
            return this;
        }

        public StatusfulConfig build() {
            return config;
        }

    }
}
