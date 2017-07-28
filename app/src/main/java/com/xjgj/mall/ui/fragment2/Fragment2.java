package com.xjgj.mall.ui.fragment2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.frameproj.library.adapter.SmartFragmentStatePagerAdapter;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.fragment2.order_waiting_accept.OrderWaitingAcceptFragment;
import com.xjgj.mall.ui.fragment2.order_waiting_evaluate.OrderWaitingEvaluateFragment;
import com.xjgj.mall.ui.fragment2.order_working.OrderWorkingFragement;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment2 extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.vp_task)
    ViewPager mVpTask;

    public static BaseFragment newInstance() {
        Fragment2 fragment2 = new Fragment2();
        return fragment2;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_2;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        showContent(true);
        OrderFragmentPagerAdapter myMainFragmentPagerAdapter = new OrderFragmentPagerAdapter(getChildFragmentManager());
        myMainFragmentPagerAdapter.addFragment(new OrderWaitingAcceptFragment(),"待接单");
        myMainFragmentPagerAdapter.addFragment(new OrderWorkingFragement(),"服务中");
        myMainFragmentPagerAdapter.addFragment(new OrderWaitingEvaluateFragment(),"待评价");
        mVpTask.setAdapter(myMainFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mVpTask);

        //防止频繁的销毁视图
        mVpTask.setOffscreenPageLimit(3);
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
