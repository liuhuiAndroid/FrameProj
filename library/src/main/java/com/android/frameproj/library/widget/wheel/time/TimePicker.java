package com.android.frameproj.library.widget.wheel.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.frameproj.library.R;
import com.android.frameproj.library.widget.wheel.Info;
import com.android.frameproj.library.widget.wheel.ScrollerNumberPicker;
import com.android.frameproj.library.widget.wheel.city.CitycodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 城市滚轮 用于展示城市列表
 *
 * @author zd
 */
public class TimePicker extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker counyPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;
    private Context context;
    //存放省份信息
    private List<Info> province_list = new ArrayList<Info>();
    //存放城市信息 map的 key是省份的id
    private HashMap<String, List<Info>> city_map = new HashMap<String, List<Info>>();
    //存放区（县）信息 map的 key是城市的id
    private HashMap<String, List<Info>> couny_map = new HashMap<String, List<Info>>();
    private CitycodeUtil citycodeUtil;
    private String province_code_string;
    private String country_code_string;
    private String couny_code_string;
    private String city_string;

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TimePicker(Context context) {
        super(context);
        this.context = context;
    }

    //初始化数据，从数据库中读取
    public void initDatas(List<Info> province, HashMap<String, List<Info>> city, HashMap<String, List<Info>> couny, int provincePosition) {
        //保存数据库
        province_list = province;
        city_map = city;
        couny_map = couny;
        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
        provincePicker.setData(citycodeUtil.getProvince(province_list));
        provincePicker.setDefault(provincePosition);
//        Log.d("provincePosition",provincePosition+"");
        cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil
                .getProvince_list_code().get(provincePosition)));
        cityPicker.setDefault(0);
        counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
                .getCity_list_code().get(0)));
        counyPicker.setDefault(0);
        //获取默认的code
        province_code_string = citycodeUtil.getProvince_list_code().get(0);
        country_code_string = citycodeUtil.getCity_list_code().get(0);
        couny_code_string = citycodeUtil.getCouny_list_code().get(0);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {

//				System.out.println("id-->" + id + "text----->" + text);
                if (text == null)
                    return;
                if (tempProvinceIndex != id) {
//					System.out.println("endselect");
                    String selectDay = cityPicker.getSelectedText();

                    if (selectDay == null)
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null)
                        return;
                    // 城市数组


                    cityPicker.setData(citycodeUtil.getCity(city_map,
                            citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(0);

                    counyPicker.setData(citycodeUtil.getCouny(couny_map,
                            citycodeUtil.getCity_list_code().get(0)));
                    counyPicker.setDefault(0);
                    province_code_string = citycodeUtil.getProvince_list_code().get(id);
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null)
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null)
                        return;
                    counyPicker.setData(citycodeUtil.getCouny(couny_map,
                            citycodeUtil.getCity_list_code().get(id)));
                    counyPicker.setDefault(0);
                    for (int i = 0; i < citycodeUtil.getCouny_list_code().size(); i++) {
//                        Log.d("i-----------",citycodeUtil.getCouny_list_code().get(i));
                    }
                    country_code_string = citycodeUtil.getCity_list_code().get(id);

                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
//						cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
        counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub

                if (text == null)
                    return;
                if (tempCounyIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null)
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null)
                        return;
                    // 地区数组
                    //地区的code
                    couny_code_string = citycodeUtil.getCouny_list_code()
                            .get(id);
                    int lastDay = Integer.valueOf(counyPicker.getListSize());
                    if (id > lastDay) {
//						counyPicker.setDefault(lastDay - 1);
                    }
                }
                tempCounyIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {


            }
        });

    }



    /**
     * 数据获取好后 显示
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    private String getCity_code_string() {
        return province_code_string+"|"+country_code_string+"|"+couny_code_string;
    }

    //获取省-市-区名称和 地区的code（ID）
    public String getCity_string() {
//        city_string = provincePicker.getSelectedText()+" "
//                + cityPicker.getSelectedText() +" "+ counyPicker.getSelectedText() + "," + province_code_string+"|"+country_code_string+"|"+couny_code_string;
//        return city_string;

//        city_string = cityPicker.getSelectedText() +" "+ counyPicker.getSelectedText() + "," +getCity_code_string();
        city_string = tempProvinceIndex+","+provincePicker.getSelectedText()+" "+ cityPicker.getSelectedText() +":"+ counyPicker.getSelectedText();
        return city_string;
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
