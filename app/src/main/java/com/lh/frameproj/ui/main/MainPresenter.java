package com.lh.frameproj.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.lh.frameproj.injector.PerActivity;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.fragment1.Fragment1;
import com.lh.frameproj.ui.fragment2.Fragment2;
import com.lh.frameproj.ui.fragment3.Fragment3;

import javax.inject.Inject;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
public class MainPresenter implements MainContract.Presenter {

    private Context mContext;
    private MainContract.View mMainView;

    //存储fragment
    private SparseArray<BaseFragment> fragmentTabMap = new SparseArray<>();

    //之前选中tab
    private int preSelect = -1;
    //当前选中tab
    private int nowSelect = 0;

    @Inject
    public MainPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void detachView() {
        mMainView = null;
    }

    @Override
    public void onTabClick(int position) {
        if (position != nowSelect) {
            preSelect = nowSelect;
            nowSelect = position;
            changeTab();
        }
    }

    @Override
    public void initFragment() {
        changeTab();
    }

    private void changeTab() {
        if (preSelect != -1) {
            mMainView.hideFragment(fragmentTabMap.get(preSelect));
        }
        BaseFragment mFragment = null;
        mFragment = fragmentTabMap.get(nowSelect);
        switch (nowSelect) {
            case 0:
                mMainView.setTitle("找车");
                break;
            case 1:
                mMainView.setTitle("订单");
                break;
            case 2:
                mMainView.setTitle("我");
                break;
        }
        if (mFragment == null) {
            if (nowSelect == 0) {
                mFragment = Fragment1.newInstance();
            } else if (nowSelect == 1) {
                mFragment = Fragment2.newInstance();
            } else if (nowSelect == 2) {
                mFragment = Fragment3.newInstance();
            }
            if (mFragment != null) {
                fragmentTabMap.put(nowSelect, mFragment);
            }
            mMainView.addFragment(mFragment);
        } else {
            mMainView.showFragment(mFragment);
        }
    }

}
