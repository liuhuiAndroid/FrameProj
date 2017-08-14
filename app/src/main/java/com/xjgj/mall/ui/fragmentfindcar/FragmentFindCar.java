package com.xjgj.mall.ui.fragmentfindcar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.frameproj.library.adapter.SmartFragmentStatePagerAdapter;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.fragment2.Fragment2;
import com.xjgj.mall.ui.fragment4.Fragment4;
import com.xjgj.mall.ui.fragmentfindcar.on_site.FragmentFindCarOnSite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lh on 2017/8/14.
 */

public class FragmentFindCar extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.vp_task)
    ViewPager mVpTask;

    public static BaseFragment newInstance() {
        FragmentFindCar fragmentFindCar = new FragmentFindCar();
        return fragmentFindCar;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_find_car;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        showContent(true);
        Fragment2.OrderFragmentPagerAdapter myMainFragmentPagerAdapter = new Fragment2.OrderFragmentPagerAdapter(getChildFragmentManager());
        myMainFragmentPagerAdapter.addFragment(Fragment4.newInstance(),"场外配送");
        myMainFragmentPagerAdapter.addFragment(FragmentFindCarOnSite.newInstance(),"场内短驳");
        mVpTask.setAdapter(myMainFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mVpTask);

        //防止频繁的销毁视图
        mVpTask.setOffscreenPageLimit(2);
    }

    @Override
    public void initData() {

    }

    public static class OrderFragmentPagerAdapter  extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public OrderFragmentPagerAdapter(FragmentManager fm) {
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

}
