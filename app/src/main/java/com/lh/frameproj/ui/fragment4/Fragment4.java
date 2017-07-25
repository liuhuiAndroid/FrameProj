package com.lh.frameproj.ui.fragment4;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.android.frameproj.library.adapter.SmartFragmentStatePagerAdapter;
import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.main.MainComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment4 extends BaseFragment implements Fragment4Contract.View{

    @Inject
    Fragment4Presenter mFragment4Presenter;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
//    @BindView(R.id.vp_task)
//    ViewPager mVpTask;

    public static BaseFragment newInstance() {
        Fragment4 fragment4 = new Fragment4();
        return fragment4;
    }

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_4;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        mFragment4Presenter.attachView(this);
//        CarPagerAdapter carPagerAdapter = new CarPagerAdapter(getChildFragmentManager());
//        mVpTask.setAdapter(carPagerAdapter);
//        mTabLayout.setupWithViewPager(mVpTask);
//
//        //防止频繁的销毁视图
//        mVpTask.setOffscreenPageLimit(4);
    }

    @Override
    public void initData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void renderCarList(List<String> orders) {

    }

    @Override
    public void onError() {

    }

    public static class CarPagerAdapter  extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public CarPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragment4Presenter.detachView();
    }

}
