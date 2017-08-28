package com.xjgj.mall.ui.custommap;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.xjgj.mall.util.popup.TextPopup;

import java.util.List;


/**
 * Created by lh on 2017/8/28.
 */

public class CustomMapActivity extends AppCompatActivity implements OnMapTouchListener, MapEventsListener {

    private static final Integer LAYER1_ID = 0; // 图层1
    private static final Integer LAYER2_ID = 1; // 图层2

    private RelativeLayout mRelativeLayout;
    //下一个对象的Id
    private int nextObjectId;
    private MapWidget mMapWidget;

    //弹框
    private TextPopup mapObjectInfoPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);

        nextObjectId = 0;
        initMap(savedInstanceState);

        initMapObjects();
        initMapEventsListener();
        mMapWidget.centerMap();
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
                10); // initial zoom level

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


}