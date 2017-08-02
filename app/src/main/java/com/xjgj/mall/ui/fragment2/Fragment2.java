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
import com.xjgj.mall.ui.fragment2.order_appealing.OrderAppealingFragment;
import com.xjgj.mall.ui.fragment2.order_cancel.OrderCanceledFragment;
import com.xjgj.mall.ui.fragment2.order_completed.OrderCompletedFragment;
import com.xjgj.mall.ui.fragment2.order_evaluated.OrderEvaluatedFragment;
import com.xjgj.mall.ui.fragment2.order_waiting_accept.OrderWaitingAcceptFragment;
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
        //  0 新建(待接单),1 已接单, 2  服务中，3 已完成, 4 已取消, 5 已评价
        myMainFragmentPagerAdapter.addFragment(OrderWaitingAcceptFragment.newInstance(),"待接单");
        myMainFragmentPagerAdapter.addFragment(OrderWorkingFragement.newInstance(),"服务中");
        myMainFragmentPagerAdapter.addFragment(OrderCompletedFragment.newInstance(),"已完成");
        myMainFragmentPagerAdapter.addFragment(OrderAppealingFragment.newInstance(),"申诉中");
        myMainFragmentPagerAdapter.addFragment(OrderCanceledFragment.newInstance(),"已取消");
        myMainFragmentPagerAdapter.addFragment(OrderEvaluatedFragment.newInstance(),"已评价");
        mVpTask.setAdapter(myMainFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mVpTask);

        //防止频繁的销毁视图
        mVpTask.setOffscreenPageLimit(6);
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
