package com.lh.frameproj.ui.fragment1;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment1Presenter implements Fragment1Contract.Presenter {

    private Fragment1Contract.View mView;

    @Inject
    public Fragment1Presenter() {
    }

    @Override
    public void onThreadReceive() {
        List<String> data = new ArrayList<>();
        data.add("测试hha1");
        data.add("测试hha2");
        data.add("再次测试hha3");

        mView.onRefreshCompleted(data);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onReload() {

    }

    @Override
    public void attachView(@NonNull Fragment1Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
