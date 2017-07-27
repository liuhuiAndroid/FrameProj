package com.lh.frameproj.ui.location;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.lh.frameproj.R;
import com.lh.frameproj.bean.GeoCoderResultEntity;
import com.lh.frameproj.service.LocationService;
import com.lh.frameproj.ui.BaseActivity;
import com.lh.frameproj.ui.decoration.DividerGridItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lh.frameproj.Constants.RESULT_CHOOSE_LOCATION_CODE;


/**
 * 地图选点
 */

public class ChooseLocationActivity extends BaseActivity implements ChooseLocationContract.View {

    //百度地图
    @BindView(R.id.baiduMapView)
    MapView mBaiduMapView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    ChooseLocationPresenter mPresenter;

    private BaiduMap mBaiduMap;

    @Inject
    LocationService locationService;

    boolean isFirstLoc = true; // 是否首次定位

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;
    private int mCheck_point;

    @Override
    protected void onStart() {
        super.onStart();
        mCheck_point = getIntent().getIntExtra("check_point", -1);

        initlLocationService();
        //初始化百度地图
        initBaiduMap();
        initRecyclerView();
        //开始定位
        locationService.start();// 定位SDK

    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(ChooseLocationActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    /**
     * 初始化定位服务
     */
    private void initlLocationService() {
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    /**
     * 初始化地图
     */
    private void initBaiduMap() {
        mBaiduMapView.setMapCustomEnable(false);
        mBaiduMap = mBaiduMapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

        //设置百度地图移动位置监听
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
        // 显示比例尺
        mBaiduMapView.showScaleControl(true);
        // 显示缩放控件
        mBaiduMapView.showZoomControls(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public int initContentView() {
        return R.layout.activity_choose_location;
    }

    @Override
    public void initInjector() {
        DaggerChooseLocationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
    }


    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                if (isFirstLoc) {
                    isFirstLoc = false;
                    //                    更新地图位置
                                        MapStatus.Builder builder = new MapStatus.Builder();
                    LatLng latLng = new LatLng(latitude, longitude);
                                        float zoom = 18.0f; // 默认 18级

                    builder.target(latLng).zoom(zoom);
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
                    //改变地图状态
                    mBaiduMap.animateMapStatus(mMapStatusUpdate);
                    getCurrentData(latLng);

                }
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mBaiduMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mBaiduMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        MapView.setMapCustomEnable(false);
        mBaiduMapView.onDestroy();
        mBaiduMapView = null;
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }

    /**
     * 回到当前我在的位置
     */
    @OnClick(R.id.iv_my_location)
    public void mIvMyLocation() {
        isFirstLoc = true;
    }

    /**
     * 百度地图 位置变化监听
     */
    BaiduMap.OnMapStatusChangeListener mOnMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         * @param status 地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        /**
         * 地图状态变化中
         * @param status 当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         * @param status 地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            LatLng latLng = status.target;
            getCurrentData(latLng);
            //            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        }
    };


    /**
     * Geocoding API 是一类接口，用于提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
     *
     * @param latLng
     */
    private void getCurrentData(LatLng latLng) {
        mPresenter.geocoderApi(latLng.latitude + "," + latLng.longitude);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void geocoderResultSuccess(String dataString) {

        String geoCoderResultString = dataString.replace("renderReverse&&renderReverse(", "").replace(")", "");
        GeoCoderResultEntity geoCoderResultEntity = new Gson().fromJson(geoCoderResultString, GeoCoderResultEntity.class);

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<GeoCoderResultEntity.ResultBean.PoisBean>(ChooseLocationActivity.this, R.layout.item_aroundmap, geoCoderResultEntity.getResult().getPois()) {
                @Override
                protected void convert(ViewHolder holder, final GeoCoderResultEntity.ResultBean.PoisBean poisBean, int position) {

                    holder.setText(R.id.item_aroundmap_name, poisBean.getName());
                    holder.setText(R.id.item_aroundmap_address, poisBean.getAddr());
                    if (position == 0) {
                        holder.getView(R.id.item_aroundmap_icon).setVisibility(View.VISIBLE);
                        holder.setTextColor(R.id.item_aroundmap_name, Color.RED);
                    } else {
                        holder.getView(R.id.item_aroundmap_icon).setVisibility(View.GONE);
                        holder.setTextColor(R.id.item_aroundmap_name, Color.BLACK);
                    }

                }
            };
            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    List<GeoCoderResultEntity.ResultBean.PoisBean> datas = mCommonAdapter.getDatas();
                    //这样有问题
                    if (datas != null) {
                        if (datas != null && datas.size() > position) {
                            setResult(RESULT_CHOOSE_LOCATION_CODE, new Intent()
                                    .putExtra("name", datas.get(position).getName())
                                    .putExtra("addr", datas.get(position).getAddr())
                                    .putExtra("check_point", mCheck_point));
                        }
                    }
                    finish();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mRecyclerView.setAdapter(mCommonAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(ChooseLocationActivity.this, 0));
        } else {
            mCommonAdapter.setDatas(geoCoderResultEntity.getResult().getPois());
            mCommonAdapter.notifyDataSetChanged();
        }
    }

}
