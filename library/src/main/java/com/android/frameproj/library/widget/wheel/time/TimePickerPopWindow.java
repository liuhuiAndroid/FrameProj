package com.android.frameproj.library.widget.wheel.time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.frameproj.library.R;
import com.android.frameproj.library.widget.wheel.Info;

import java.util.HashMap;
import java.util.List;


/**
 * 城市选择popUpWindow
 */
public class TimePickerPopWindow extends PopupWindow {
    private Context context;
    private LayoutInflater mInflater;
    private View dateView;
    //城市选择器对象
    private TimePicker productPicker;

    private TextView text_cancle;
    private TextView text_yes;
    private TextView text_title;
    //    private String cityId = "";

    public TimePickerPopWindow(Context context, String title, List<Info> province, HashMap<String, List<Info>> city, HashMap<String, List<Info>> couny, int provincePosition) {
        this.context = context;
        initWindow(title, province, city, couny, provincePosition);

    }


    /**
     * 初始化popUpWindow
     *
     * @param title
     */
    private void initWindow(String title, List<Info> province, HashMap<String, List<Info>> city, HashMap<String, List<Info>> couny, int provincePosition) {
        // TODO Auto-generated method stub
        mInflater = LayoutInflater.from(context);
        dateView = mInflater.inflate(R.layout.time_picker_choose_pop, null);
        text_title = (TextView) dateView.findViewById(R.id.text_title);
        text_cancle = (TextView) dateView.findViewById(R.id.text_cancle);
        text_yes = (TextView) dateView.findViewById(R.id.text_yes);

        productPicker = (TimePicker) dateView.findViewById(R.id.citypicker);
        productPicker.initDatas(province, city, couny, provincePosition);
        productPicker.setVisibility(View.VISIBLE);
        text_title.setText(title);
        text_cancle.setClickable(true);
        text_yes.setClickable(true);
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCitySelectorListener.onCitySelectorListener("","");
                dismiss();
            }
        });
        text_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                str = productPicker.getCity_string();
                String[] strs = str.split(",");
                onCitySelectorListener.onCitySelectorListener(strs[0], strs[1]);
                dismiss();
            }
        });
        setContentView(dateView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(false);
        setOutsideTouchable(true);

    }

    public OnCitySelectorListener onCitySelectorListener = null;

    public interface OnCitySelectorListener {
        public void onCitySelectorListener(String dayPosition, String showContent);

    }

    public void setOnInterface(OnCitySelectorListener onCitySelectorListener) {
        this.onCitySelectorListener = onCitySelectorListener;
    }


}