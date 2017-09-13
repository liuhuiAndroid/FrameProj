package com.xjgj.mall.ui.custommap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.util.CommonUtils;
import com.android.frameproj.library.widget.CustomSearchView;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.GPSConfig;
import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.AddressEntity;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.util.popup.TextPopup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.RESULT_CHOOSE_LOCATION_CODE_CUSTOM_MAP;


/**
 * Created by lh on 2017/8/28.
 */

public class CustomMapActivity extends BaseActivity implements OnMapTouchListener,
        MapEventsListener, CustomMapContract.View {

    private static final Integer LAYER1_ID = 0; // 图层1
    private static final Integer LAYER2_ID = 1; // 图层2
    @BindView(R.id.shadeView)
    View mShadeView;
    @BindView(R.id.searchView)
    CustomSearchView mSearchView;
    @BindView(R.id.btn_common)
    Button mBtnCommon;
    @BindView(R.id.list_result)
    RecyclerView mListResult;
    @BindView(R.id.ll_search_result)
    LinearLayout mLlSearchResult;

    private RelativeLayout mRelativeLayout;
    //下一个对象的Id
    private int nextObjectId;
    private MapWidget mMapWidget;

    //弹框
    private TextPopup mapObjectInfoPopup;

    @Inject
    CustomMapPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nextObjectId = 0;
        initMap(savedInstanceState);

        initMapObjects();
        initMapEventsListener();
        mMapWidget.centerMap();
    }

    @Override
    public int initContentView() {
        return R.layout.activity_custom_map;
    }

    @Override
    public void initInjector() {
        DaggerCustomMapComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
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
                CommonUtils.hideSoftInput(CustomMapActivity.this);
            }

            @Override
            public void onQueryChanged(String paramString, int paramInt1, int paramInt2, int paramInt3) {
                if (paramString != null && !TextUtils.isEmpty(paramString.trim())) {
                    mPresenter.addressList(paramString);
                }
            }


            @Override
            public void afterTextChanged(Editable paramEditable) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapWidget.saveState(outState);
    }

    /**
     * 初始化地图
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mMapWidget = new MapWidget(savedInstanceState, this,
                "map",// root name of the map under assets folder.
                13); // initial zoom level

        OfflineMapConfig config = mMapWidget.getConfig();
        config.setPinchZoomEnabled(true); // 设置缩放手势
        config.setFlingEnabled(true);    // 设置地图惯性滚动
        config.setMaxZoomLevelLimit(20);
        config.setZoomBtnsVisible(true); // 设置嵌入式缩放按钮可见

        // GPS接收器的配置
        GPSConfig gpsConfig = config.getGpsConfig();
        gpsConfig.setPassiveMode(false);
        gpsConfig.setGPSUpdateInterval(500, 5);

        // 位置标记的配置
        MapGraphicsConfig graphicsConfig = config.getGraphicsConfig();
        graphicsConfig.setAccuracyAreaColor(0x550000FF); // 蓝色、透明
        graphicsConfig.setAccuracyAreaBorderColor(Color.BLUE); // 蓝色、不透明

        mRelativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mRelativeLayout.addView(mMapWidget);
        mRelativeLayout.setBackgroundColor(Color.parseColor("#515556")); // 灰色背景

        //创建两个新的图层
        mMapWidget.createLayer(LAYER1_ID);
        mMapWidget.createLayer(LAYER2_ID);
    }

    /**
     * 初始化地图内的对象
     */
    private void initMapObjects() {
        mapObjectInfoPopup = new TextPopup(this, (RelativeLayout) findViewById(R.id.mainLayout));
        addPOI();
    }

    /**
     * 使用地理坐标显示地图对象
     */
    private void addPOI() {
        Layer layer = mMapWidget.getLayerById(LAYER1_ID);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        MapObject poiSport = new MapObject(nextObjectId,
                drawable,
                0, 0, // 像素坐标
                11, 33, // 轴心点
                true, // Touchable
                true); // Scalable
        layer.addMapObject(poiSport);

        Location location = new Location("");
        location.setLatitude(31.3068380000);
        location.setLongitude(121.4648500000);

        poiSport.moveTo(location);

        nextObjectId += 1;
    }

    /**
     * 处理地图对象上的触摸事件
     */
    private void initMapEventsListener() {
        // 接收MapObject的touch事件
        mMapWidget.setOnMapTouchListener(this);

        // 接收地图缩放事件
        mMapWidget.addMapEventsListener(this);

        // 接收地图滚动事件
        mMapWidget.setOnMapScrolledListener(new OnMapScrollListener() {
            public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
                handleOnMapScroll(v, event);
            }
        });
    }


    private void showLocationsPopup(int x, int y, String text) {
        RelativeLayout mapLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
        mapObjectInfoPopup.setText(text);
        mapObjectInfoPopup.setOnClickListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mapObjectInfoPopup != null) {
                        mapObjectInfoPopup.hide();
                    }
                }

                return false;
            }
        });
        mapObjectInfoPopup.show(mapLayout, x, y);
    }


    /**
     * 接收MapObject的touch事件
     *
     * @param v
     * @param event
     */
    @Override
    public void onTouch(MapWidget v, MapTouchedEvent event) {
        List objectTouchEvents = event.getTouchedObjectIds();
        int mapX = event.getMapX();
        int mapY = event.getMapY();
        int screenX = event.getScreenX();
        int screenY = event.getScreenY();

        if (objectTouchEvents.size() == 1) {
            ObjectTouchEvent objectTouchEvent = (ObjectTouchEvent) objectTouchEvents.get(0);
            long layerId = objectTouchEvent.getLayerId();
            Object objectId = objectTouchEvent.getObjectId();
            Toast.makeText(CustomMapActivity.this, "Touched object " + objectId + " on layer" +
                    layerId + ", x: " + mapX + " y: " + mapY, Toast.LENGTH_SHORT).show();

            Location location = new Location("");
            location.setLatitude(3.2127012756213316);
            location.setLongitude(73.03406774997711);
            mMapWidget.scrollMapTo(location);

            //显示弹框
            showLocationsPopup(screenX, screenY, "Shows where user touched");
        }
    }

    @Override
    public void onPreZoomIn() {
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onPostZoomIn() {
    }

    @Override
    public void onPreZoomOut() {
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onPostZoomOut() {
    }

    /**
     * 处理地图的滚动事件
     */
    private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) {
        // When user scrolls the map we receive scroll events
        // 当用户滚动地图时，我们会收到滚动事件
        // This is useful when need to move some object together with the map
        // 这对于需要将一些对象与地图一起移动是很有用的

        int dx = event.getDX(); // Number of pixels that user has scrolled horizontally 用户水平滚动的像素数
        int dy = event.getDY(); // Number of pixels that user has scrolled vertically 用户竖直滚动的像素数

        if (mapObjectInfoPopup.isVisible()) {
            mapObjectInfoPopup.moveBy(dx, dy);
        }
    }


    // ========================================== 搜索相关

    /**
     * 点击阴影隐藏搜索布局
     */
    @OnClick(R.id.shadeView)
    public void mShadeView() {
        mSearchView.clearQueryContent();
        mSearchView.setEditTextFocus(false);
        mLlSearchResult.setVisibility(View.GONE);
        dismissShade();
        CommonUtils.hideSoftInput(CustomMapActivity.this);
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

    private List<AddressEntity> mSearchItems = new ArrayList<>();

    @Override
    public void addressListResultSuccess(List<AddressEntity> addressEntities) {
        if (addressEntities != null) {
            mLlSearchResult.setVisibility(View.VISIBLE);
            mSearchItems.clear();
            mSearchItems.addAll(addressEntities);
            showShade();
            if (mSearchItems.size() > 0) {
                if (mCommonAdapterSearch == null) {
                    mListResult.setLayoutManager(new LinearLayoutManager(CustomMapActivity.this));
                    mCommonAdapterSearch = new CommonAdapter<AddressEntity>(CustomMapActivity.this, R.layout.location_search_listitem, mSearchItems) {
                        @Override
                        protected void convert(ViewHolder holder, final AddressEntity addressEntity, int position) {
                            holder.setText(R.id.textview_formmatted_address_head, addressEntity.getMerchantName());
                            holder.setText(R.id.textview_formmatted_address, addressEntity.getAddress());
                        }
                    };
                    mCommonAdapterSearch.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                            List<AddressEntity> datas = mCommonAdapterSearch.getDatas();
                            if (datas != null) {
                                if (datas != null && datas.size() > position) {
                                    setResult(RESULT_CHOOSE_LOCATION_CODE_CUSTOM_MAP, new Intent()
                                            .putExtra("name", datas.get(position).getMerchantName())
                                            .putExtra("addr", datas.get(position).getAddress())
                                            .putExtra("longitude", datas.get(position).getLongitude())
                                            .putExtra("latitude", datas.get(position).getLatitude()));
                                    finish();

                                    //                                android.location.Location location = new Location("");
                                    //                                location.setLatitude(31.144247);
                                    //                                location.setLongitude(121.605669);
                                    //                                mMapWidget.scrollMapTo(location);
                                    dismissShade();
                                    mListResult.setVisibility(View.GONE);
                                    CommonUtils.hideSoftInput(CustomMapActivity.this);

                                }
                            }
                            //                    finish();
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

                mListResult.setVisibility(View.VISIBLE);
            } else {

                mListResult.setVisibility(View.GONE);

            }

        }

    }

    private CommonAdapter mCommonAdapterSearch;


    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}