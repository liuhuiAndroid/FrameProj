package com.lh.frameproj.ui.fragment4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.WindowUtil;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.widget.SuperEditTextPlus;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lh.frameproj.R;
import com.lh.frameproj.bean.CarTypeEntity;
import com.lh.frameproj.bean.TerminiEntity;
import com.lh.frameproj.service.LocationService;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.delivery.DeliveryInfoActivity;
import com.lh.frameproj.ui.improveorder.ImproveOrderActivity;
import com.lh.frameproj.ui.main.MainComponent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lh.frameproj.Constants.REQUEST_DELIVERY_INFO_CODE;
import static com.lh.frameproj.Constants.RESULT_DELIVERY_INFO_CODE;
import static com.lh.frameproj.R.id.viewPager;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment4 extends BaseFragment implements Fragment4Contract.View {

    @Inject
    Fragment4Presenter mFragment4Presenter;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(viewPager)
    ViewPager mViewPager;
    @BindView(R.id.vehicleWeight)
    TextView mVehicleWeight;
    @BindView(R.id.vehicleSize)
    TextView mVehicleSize;
    @BindView(R.id.vehicleVolume)
    TextView mVehicleVolume;
    @BindView(R.id.lIndicator)
    ImageView mLIndicator;
    @BindView(R.id.rIndicator)
    ImageView mRIndicator;

    // 发货起点
    @BindView(R.id.seStPtOF)
    SuperEditTextPlus mSeStPtOF;
    // 发货终点
    @BindView(R.id.nextDestOF)
    SuperEditTextPlus mNextDestOF;

    // 选择地点的Container
    @BindView(R.id.llAddrOF)
    LinearLayout mLlAddrOF;

    @Inject
    LocationService locationService;

    /**
     * 存放起始地、途径地和终点
     */
    public Map<Integer, SuperEditTextPlus> superEditTextsMap = new HashMap();
    private int startIndex = 0;
    private int nextIndex = 1;
    //最大8个
    private int maxStation = 8;
    private VehiclePagerAdapter mVehiclePagerAdapter;

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
        showContent(true);

        mSeStPtOF.setHintText("按此输入起点");
        mNextDestOF.setHintText("按此输入目的地");
        mSeStPtOF.setTag(startIndex);
        mSeStPtOF.setListener(mSuperEditTextListener);
        mNextDestOF.setRightBtnIcon(R.drawable.ic_add);
        mNextDestOF.setTag(nextIndex);
        mNextDestOF.setListener(mSuperEditTextListener);

        superEditTextsMap.put(startIndex, mSeStPtOF);
        superEditTextsMap.put(nextIndex, mNextDestOF);

        mFragment4Presenter.attachView(this);
        mFragment4Presenter.onCarListReceive();
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
    public void renderCarList(final List<CarTypeEntity> carTypeEntities) {
        updateVehicleUI(0, carTypeEntities);
        //初始化ViewPager
        mVehiclePagerAdapter = new VehiclePagerAdapter(mViewPager, carTypeEntities);
        mViewPager.setAdapter(mVehiclePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //防止频繁的销毁视图
        mViewPager.setOffscreenPageLimit(carTypeEntities.size());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                updateVehicleUI(position, carTypeEntities);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 更新页面上车的重量、体积数据
     *
     * @param position
     * @param carTypeEntities
     */
    private void updateVehicleUI(int position, List<CarTypeEntity> carTypeEntities) {
        mLIndicator.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        mRIndicator.setVisibility(position == carTypeEntities.size() - 1 ? View.INVISIBLE : View.VISIBLE);

        mVehicleWeight.setText(carTypeEntities.get(position).getLoadWeight());
        mVehicleSize.setText(carTypeEntities.get(position).getShape());
        mVehicleVolume.setText(carTypeEntities.get(position).getLoadVolume());
    }

    /**
     * ViewPager左滑
     */
    @OnClick(R.id.lIndicator)
    public void mLIndicator() {
        int childCount = mViewPager.getChildCount();
        int currentItem = mViewPager.getCurrentItem();
        if (childCount != 0 && (currentItem - 1) >= 0) {
            mViewPager.setCurrentItem(currentItem - 1);
        }
    }

    /**
     * ViewPager右滑
     */
    @OnClick(R.id.rIndicator)
    public void mRIndicator() {
        int childCount = mViewPager.getChildCount();
        int currentItem = mViewPager.getCurrentItem();
        if (childCount != 0 && (currentItem + 1) < childCount) {
            mViewPager.setCurrentItem(currentItem + 1);
        }
    }


    @Override
    public void onError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragment4Presenter.detachView();
    }


    /**
     *
     */
    private SuperEditTextPlus.SuperEditTextListener mSuperEditTextListener = new SuperEditTextPlus.SuperEditTextListener() {
        @Override
        public void onLeftBtnClicked(View paramView) {
            int i = ((Integer) paramView.getTag()).intValue();
            toPickLocation(paramView, i);
        }

        @Override
        public void onRightBtnClicked(View paramView) {
            addAddrItem();
            assignStPt2CurrentLocation();
        }
    };

    /**
     *
     */
    private void assignStPt2CurrentLocation() {

    }

    /**
     * 新增途径地
     */
    private void addAddrItem() {
        if (false) {
            ToastUtil.showToast("最多添加100个目的地");
            return;
        }

        final SuperEditTextPlus localSuperEditTextPlus = addAddrItem2(mLlAddrOF, maxStation);
        //设置属性
        final int i = ((Integer) localSuperEditTextPlus.getTag()).intValue();
        localSuperEditTextPlus.setTag(i);
        //设置监听
        localSuperEditTextPlus.setListener(new SuperEditTextPlus.SuperEditTextListener() {
            @Override
            public void onLeftBtnClicked(View paramView) {
                toPickLocation(paramView, i);
            }

            @Override
            public void onRightBtnClicked(View paramView) {
                removeItem(mLlAddrOF, localSuperEditTextPlus);
                superEditTextsMap.remove(Integer.valueOf(i));
            }
        });
        superEditTextsMap.put(Integer.valueOf(i), localSuperEditTextPlus);
    }

    /**
     * 选择位置
     */
    private void toPickLocation(View paramView, int paramInt) {
        TerminiEntity terminiEntity = new TerminiEntity();
        Intent intent = new Intent(getActivity(),DeliveryInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("termini_info", (Serializable) terminiEntity);
        bundle.putInt("position",paramInt);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_DELIVERY_INFO_CODE);
    }

    /**
     * 新增SuperEditTextPlus
     *
     * @param paramLinearLayout
     * @param paramInt
     * @return
     */
    public static SuperEditTextPlus addAddrItem2(LinearLayout paramLinearLayout, int paramInt) {
        Context localContext = paramLinearLayout.getContext();
        LinearLayout localLinearLayout1 = (LinearLayout) LayoutInflater.from(localContext).inflate(R.layout.add_addr_item, null);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
        localLayoutParams.rightMargin = WindowUtil.dip2px(localLinearLayout1.getContext(), 8.0F);
        localLayoutParams.leftMargin = WindowUtil.dip2px(localLinearLayout1.getContext(), 8.0F);
        LinearLayout localLinearLayout2 = (LinearLayout) localLinearLayout1.findViewById(R.id.editLayout);
        int i = paramLinearLayout.getChildCount();
        SuperEditTextPlus localSuperEditTextPlus = (SuperEditTextPlus) localLinearLayout2.getChildAt(0);
        localSuperEditTextPlus.setTag(Integer.valueOf(i));
        localSuperEditTextPlus.setHintText("按此输入目的地" + i);
        localSuperEditTextPlus.setRightBtnIcon(R.drawable.ic_strike_out);
        RelativeLayout localRelativeLayout = (RelativeLayout) ((LinearLayout) paramLinearLayout.getChildAt(-1 + paramLinearLayout.getChildCount())).getChildAt(0);
        ((ImageView) localRelativeLayout.getChildAt(1)).setImageDrawable(localContext.getResources().getDrawable(R.drawable.ic_waypt));
        ((LinearLayout) localRelativeLayout.getChildAt(0)).getChildAt(1).setBackgroundColor(localContext.getResources().getColor(R.color.md_grey_300));
        paramLinearLayout.addView(localLinearLayout1, i, localLayoutParams);
        return localSuperEditTextPlus;
    }

    /**
     * 删除途径地
     *
     * @return
     */
    private void removeItem(LinearLayout paramLinearLayout, SuperEditTextPlus paramSuperEditTextPlus) {
        paramLinearLayout.removeView((View) paramSuperEditTextPlus.getParent().getParent());
        LinearLayout localLinearLayout = (LinearLayout) paramLinearLayout.getChildAt(paramLinearLayout.getChildCount() - 1);
        RelativeLayout localRelativeLayout = (RelativeLayout) localLinearLayout.getChildAt(0);
        if (localLinearLayout.getChildCount() == 2) {
            ((ImageView) localRelativeLayout.getChildAt(1)).setImageDrawable(paramLinearLayout.getContext().getResources().getDrawable(R.drawable.ic_dest));
        }
        ((LinearLayout) localRelativeLayout.getChildAt(0)).getChildAt(1).setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onStart() {
        super.onStart();
        //初始化定位服务
        initlLocationService();
        //开始定位
        locationService.start();// 定位SDK
    }

    /**
     * 初始化定位服务
     */
    private void initlLocationService() {
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }


    @Override
    public void onStop() {
        super.onStop();
        stopLocationService();
    }

    /**
     * 停止LocationService
     */
    private void stopLocationService() {
        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
    }

    private boolean isFirstLoc = true;

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (isFirstLoc) {
                    isFirstLoc = false;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mSeStPtOF.setTopText(location.getAddrStr());
                        }
                    });
                    stopLocationService();
                }
            } else {
                //申请权限
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DELIVERY_INFO_CODE && resultCode == RESULT_DELIVERY_INFO_CODE) {
            int position = data.getIntExtra("position",-1);
            TerminiEntity terminiEntity = (TerminiEntity) data.getExtras().getSerializable("terminiEntity");
            if (position != -1) {
                SuperEditTextPlus localSuperEditTextPlus = superEditTextsMap.get(position);
                localSuperEditTextPlus.setTopText(terminiEntity.getAddressName());
                localSuperEditTextPlus.setMiddleText(terminiEntity.getAddressDescribeName());
            }
        }
    }

    /**
     * 下一步
     */
    @OnClick(R.id.btnNext)
    public void mBtnNext() {
        // TODO 判断起始点

        Intent intent = new Intent(baseActivity, ImproveOrderActivity.class);
        int currentItem = mViewPager.getCurrentItem();
        List<CarTypeEntity> carTypeEntities = mVehiclePagerAdapter.getCarTypeEntities();
        if (carTypeEntities.size() > currentItem) {
            intent.putExtra("cartype", carTypeEntities.get(currentItem).getCarTypeId() + "");
            Logger.i("选择 cartype = "+ carTypeEntities.get(currentItem).getCarTypeId() + ""+
                    ";carname = "+carTypeEntities.get(currentItem).getCarTypeName());
            startActivity(intent);
        }else{
            ToastUtil.showToast("车型数据有误");
        }
    }

}
