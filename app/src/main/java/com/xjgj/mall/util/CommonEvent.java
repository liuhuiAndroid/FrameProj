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


}
