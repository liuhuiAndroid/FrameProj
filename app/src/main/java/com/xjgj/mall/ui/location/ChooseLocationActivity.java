package com.xjgj.mall.ui.location;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.util.CommonUtils;
import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.widget.CustomSearchView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.GeoCoderResultEntity;
import com.xjgj.mall.bean.SearchItem;
import com.xjgj.mall.service.LocationService;
import com.xjgj.mall.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.RESULT_CHOOSE_LOCATION_CODE;


/**
 * 地图选点
 */

public class ChooseLocationActivity extends BaseActivity implements ChooseLocationContract.View {

    //百度地图
    @BindView(R.id.baiduMapView)
    MapView mBaiduMapView;
    @BindView(R.id.recyclerViewGeocoder)
    ListView mRecyclerViewGeocoder;

    @Inject
    ChooseLocationPresenter mPresenter;
    // 阴影布局
    @BindView(R.id.shadeView)
    View mShadeView;
    // 搜索控件
    @BindView(R.id.searchView)
    CustomSearchView mSearchView;
    //搜索结果
    @BindView(R.id.list_result)
    RecyclerView mListResult;
    //搜索结果所在布局
    @BindView(R.id.ll_search_result)
    LinearLayout mLlSearchResult;
    private BaiduMap mBaiduMap;
    @Inject
    LocationService locationService;

    private boolean isFirstLoc = true; // 是否首次定位

    private LinearLayoutManager mLinearLayoutManager;
    private int mCheck_point;

    //选中的城市默认是上海
    private String selectCity = "上海市";
    private int loadIndex = 0;
    private List<GeoCoderResultEntity.ResultBean.PoisBean> mRecyclerViewGeoCoderData = new ArrayList<>();
    private ChooseLocationListAdapter mChooseLocationListAdapter;
    //    private ChooseLocationAdapter mCommonAdapterGeocoder;

    @Override
    protected void onStart() {
        super.onStart();
        mCheck_point = getIntent().getIntExtra("check_point", -1);

        initlLocationService();
        //初始化百度地图
        initBaiduMap();
        //开始定位
        locationService.start();// 定位SDK
        //初始化搜索
        initBaiduMapPoi();

    }

    private void initRecyclerView() {
//        Logger.i("test initRecyclerView");
//        Logger.i("test mRecyclerViewGeoCoderData.size = " + mRecyclerViewGeoCoderData.size());
//        mLinearLayoutManager = new LinearLayoutManager(ChooseLocationActivity.this);
//        mRecyclerViewGeocoder.setLayoutManager(mLinearLayoutManager);
//        mCommonAdapterGeocoder = new ChooseLocationAdapter(ChooseLocationActivity.this, mRecyclerViewGeoCoderData);
        //        mCommonAdapterGeocoder.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        //                List<GeoCoderResultEntity.ResultBean.PoisBean> datas = mCommonAdapterGeocoder.getDatas();
        //                if (datas != null) {
        //                    if (datas != null && datas.size() > position) {
        //                        setResult(RESULT_CHOOSE_LOCATION_CODE, new Intent()
        //                                .putExtra("name", datas.get(position).getName())
        //                                .putExtra("addr", datas.get(position).getAddr())
        //                                .putExtra("longitude", datas.get(position).getPoint().getX())
        //                                .putExtra("latitude", datas.get(position).getPoint().getY())
        //                                .putExtra("check_point", mCheck_point));
        //                    }
        //                }
        //                finish();
        //            }
        //
        //            @Override
        //            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        //                return false;
        //            }
        //        });
        mChooseLocationListAdapter = new ChooseLocationListAdapter(ChooseLocationActivity.this,mRecyclerViewGeoCoderData);
        mRecyclerViewGeocoder.setAdapter(mChooseLocationListAdapter);
//        mRecyclerViewGeocoder.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerViewGeocoder.addItemDecoration(new DividerGridItemDecoration(ChooseLocationActivity.this, 0));
        Logger.i("test setAdapter ============ 结束");
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
        isFirstLoc = true; // 是否首次定位
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
        mSearchView.setCityName(selectCity);
        mSearchView.setListener(new CustomSearchView.CustomSearchViewListener() {
            @Override
            public void onBackButtonClicked() {
                finish();
            }

            @Override
            public void onEditTextClicked() {
                mSearchView.editTextRequestFocus();
            }

            @Override
            public void onRightButtonClicked() {
                CommonUtils.hideSoftInput(ChooseLocationActivity.this);
                //TODO 跳转选择城市页面
            }

            @Override
            public void onQueryChanged(String paramString, int paramInt1, int paramInt2, int paramInt3) {
                searchPlaces(paramString);
            }

            @Override
            public void afterTextChanged(Editable paramEditable) {
            }
        });
    }

    private void searchPlaces(String keystr) {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(selectCity).keyword(keystr).pageNum(loadIndex));
    }

    private void searchPlacesFromNet(final String paramString) {
        //TODO 需要判断是否是城市
        if (false) {
            ToastUtil.showToast("对不起您选择的城市，服务未开通");
            return;
        }

    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Logger.i("onReceiveLocation");
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                selectCity = location.getCity();
                mSearchView.setCityName(selectCity);
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
            Logger.i("test 监听到位置变化 onMapStatusChangeFinish");
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
    public void geocoderResultSuccess(List<GeoCoderResultEntity.ResultBean.PoisBean> geoCoderResultEntity) {
        Logger.i("test geocoderResultSuccess");

        if (mChooseLocationListAdapter == null) {
            mRecyclerViewGeoCoderData.clear();
            mRecyclerViewGeoCoderData.addAll(geoCoderResultEntity);
            initRecyclerView();

        } else {
            mRecyclerViewGeoCoderData.clear();
            mRecyclerViewGeoCoderData.addAll(geoCoderResultEntity);
            Logger.i("test mRecyclerViewGeoCoderData.size = " + mRecyclerViewGeoCoderData.size());
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChooseLocationListAdapter.notifyDataSetChanged();
                    Logger.i("test notifyDataSetChanged testChangeThread:" + Thread.currentThread().getName());
                }
            }, 1000);

        }

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    // ==================================================  搜索模块
    // ==================================================  搜索模块
    private PoiSearch mPoiSearch = null;

    /**
     * 初始化搜索模块
     */
    private void initBaiduMapPoi() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                handlePoiResult(poiResult);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtil.showToast("抱歉，未找到结果");
                } else {
                    ToastUtil.showToast(result.getName() + ": " + result.getAddress());
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    /**
     * 获取到搜索结果
     *
     * @param result
     */
    private void handlePoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND
                || result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            mLlSearchResult.setVisibility(View.GONE);
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mLlSearchResult.setVisibility(View.VISIBLE);
            setSearchItem(result.getAllPoi());
        } else {
            mLlSearchResult.setVisibility(View.GONE);
        }
    }

    private List<SearchItem> mSearchItems = new ArrayList<>();

    /**
     * 整理搜索到的数据，准备展示
     *
     * @param paramList
     */
    private void setSearchItem(List<PoiInfo> paramList) {
        mSearchItems.clear();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            PoiInfo localPoiInfo = (PoiInfo) localIterator.next();

            SearchItem localSearchItem = new SearchItem();
            String str = localPoiInfo.address;
            localSearchItem.setHistory(false);
            if (!localPoiInfo.name.equals("")) {
                localSearchItem.setName(localPoiInfo.name);
            }
            localSearchItem.setAddress(str.replaceAll(localPoiInfo.city, ""));
            localSearchItem.setPoid(localPoiInfo.uid);
            if (localPoiInfo.location != null) {
                localSearchItem.setLat(localPoiInfo.location.latitude);
                localSearchItem.setLng(localPoiInfo.location.longitude);
                localSearchItem.setCity(localPoiInfo.city);
                mSearchItems.add(localSearchItem);
            }
        }
        showResultPage();
    }

    private CommonAdapter mCommonAdapterSearch;

    /**
     * 展示搜索数据
     */
    private void showResultPage() {
        showShade();
        if (mCommonAdapterSearch == null) {
            mListResult.setLayoutManager(new LinearLayoutManager(ChooseLocationActivity.this));
            mCommonAdapterSearch = new CommonAdapter<SearchItem>(ChooseLocationActivity.this, R.layout.location_search_listitem, mSearchItems) {
                @Override
                protected void convert(ViewHolder holder, final SearchItem searchItem, int position) {
                    holder.setText(R.id.textview_formmatted_address_head, searchItem.getName());
                    holder.setText(R.id.textview_formmatted_address, searchItem.getAddress());
                }
            };
            mCommonAdapterSearch.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    List<SearchItem> datas = mCommonAdapterSearch.getDatas();
                    if (datas != null) {
                        if (datas != null && datas.size() > position) {
                            setResult(RESULT_CHOOSE_LOCATION_CODE, new Intent()
                                    .putExtra("name", datas.get(position).getName())
                                    .putExtra("addr", datas.get(position).getAddress())
                                    .putExtra("longitude", datas.get(position).getLng())
                                    .putExtra("latitude", datas.get(position).getLat())
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
            mListResult.setAdapter(mCommonAdapterSearch);
            mListResult.setItemAnimator(new DefaultItemAnimator());
        } else {
            mCommonAdapterSearch.notifyDataSetChanged();
        }
    }

    /**
     * 点击阴影隐藏搜索布局
     */
    //    @OnClick(R.id.shadeView)
    public void mShadeView() {
        mSearchView.clearQueryContent();
        mSearchView.setEditTextFocus(false);
        mLlSearchResult.setVisibility(View.GONE);
        dismissShade();
        CommonUtils.hideSoftInput(ChooseLocationActivity.this);
    }

    private AlphaAnimation animation;

    /**
     * 隐藏阴影
     */
    private void dismissShade() {
        if (this.animation != null)
            this.animation.cancel();
        mShadeView.setVisibility(View.GONE);
        mShadeView.setClickable(false);
    }

    /**
     * 显示阴影
     */
    private void showShade() {
        if (mShadeView.getVisibility() == View.VISIBLE)
            return;
        this.animation = new AlphaAnimation(0.0F, 0.7F);
        this.animation.setDuration(500L);
        this.animation.setFillAfter(true);
        mShadeView.startAnimation(this.animation);
        mShadeView.setVisibility(View.VISIBLE);
        mShadeView.setClickable(true);
    }

}
