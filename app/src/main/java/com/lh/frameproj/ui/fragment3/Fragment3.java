package com.lh.frameproj.ui.fragment3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.main.MainComponent;

import butterknife.OnClick;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment3 extends BaseFragment {

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_3;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {

    }

    @Override
    public void initData() {

    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_exit)
    public void exit_sys(){
        new MaterialDialog.Builder(getActivity())
                .title("退出登录")
                .content("是否确认退出登录？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

}
