package com.xjgj.mall.ui.widget.state;


import com.xjgj.mall.R;

/**
 * Created by sll on 2015/3/13.
 */
public class ErrorState extends AbstractShowState {
    @Override
    public void show(boolean animate) {
        showViewById(R.id.epf_error, animate);
    }

    @Override
    public void dismiss(boolean animate) {
        dismissViewById(R.id.epf_error, animate);
    }
}
