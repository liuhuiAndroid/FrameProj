package com.xjgj.mall.util;

/**
 * Created by we-win on 2017/3/14.
 */

public class CommonEvent {

    // blog推荐是这么写的
    public class CancleOrderEvent {
        public int position;

        public CancleOrderEvent(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public class OrderTypeChangeEvent {
        public int type;
        public String name;
        public boolean isRefresh;

        public OrderTypeChangeEvent(int type, String name, boolean isRefresh) {
            this.type = type;
            this.name = name;
            this.isRefresh = isRefresh;
        }

        public boolean isRefresh() {
            return isRefresh;
        }

        public void setRefresh(boolean refresh) {
            isRefresh = refresh;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class AddressTypeChangeEvent {
        public int type;
        public String name;

        public AddressTypeChangeEvent(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
