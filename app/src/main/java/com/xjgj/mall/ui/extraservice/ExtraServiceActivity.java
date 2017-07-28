package com.xjgj.mall.ui.extraservice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.xjgj.mall.Constants;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by we-win on 2017/7/27.
 * 额外服务
 */

public class ExtraServiceActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_handle)
    ImageView image_handle;
    @BindView(R.id.text_handle)
    TextView text_handle;
    @BindView(R.id.relative_layout)
    RelativeLayout relative_layout;
    @BindView(R.id.linear_top)
    LinearLayout linear_top;
    @BindView(R.id.textOk)
    TextView textOk;
    @BindView(R.id.text_base_server)
    TextView text_base_server;
    @BindView(R.id.linear_xie_zhuang)
    LinearLayout linear_xie_zhuang;
    @BindView(R.id.linear_hui_dan)
    LinearLayout linear_hui_dan;
    @BindView(R.id.textHuiKuan)
    TextView textHuiKuan;
    @BindView(R.id.linear_hui_jia)
    LinearLayout linear_hui_jia;
    @BindView(R.id.relative_con)
    RelativeLayout relative_con;
    @BindView(R.id.text_shou)
    TextView text_shou;
    @BindView(R.id.image_shou)
    ImageView image_shou;
    @BindView(R.id.linear_shou)
    LinearLayout linear_shou;
    @BindView(R.id.text_hui_dan_con)
    TextView text_hui_dan_con;
    @BindView(R.id.text_hui_dan_con_)
    TextView text_hui_dan_con_;
    @BindView(R.id.linear_hui_dan_con)
    LinearLayout linear_hui_dan_con;

    @Override
    public int initContentView() {
        return R.layout.activity_extra_service;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        operateView();
    }

    private void operateView() {
        imageBack.setImageResource(R.drawable.btn_back);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setOnClickListener(this);
        textTitle.setText(getResources().getString(R.string.extra_service));
        String xieZhuang = getIntent().getStringExtra("zhuangXie");
        String huiDan = getIntent().getStringExtra("huiDan");
        String huiKuan = getIntent().getStringExtra("huiKuan");
        String huiKuanMoney = getIntent().getStringExtra("huiKuanMoney");
        if (TextUtils.isEmpty(xieZhuang)) {
            linear_xie_zhuang.setSelected(false);
            linear_xie_zhuang.setBackgroundResource(R.drawable.rectangle_yzm_gray_corner_bg);
        } else {
            linear_xie_zhuang.setSelected(true);
            linear_xie_zhuang.setBackgroundResource(R.drawable.rectangle_yzm_corner_bg);
        }
        if (TextUtils.isEmpty(huiDan)) {
            linear_hui_dan.setSelected(false);
            linear_hui_dan.setBackgroundResource(R.drawable.rectangle_yzm_gray_corner_bg);
        } else {
            linear_hui_dan.setSelected(true);
            linear_hui_dan.setBackgroundResource(R.drawable.rectangle_yzm_corner_bg);
        }
        if (TextUtils.isEmpty(huiKuanMoney)) {
            linear_hui_jia.setSelected(false);
            linear_hui_jia.setBackgroundResource(R.drawable.rectangle_yzm_gray_corner_bg);
        } else {
            linear_hui_jia.setSelected(true);
            linear_hui_jia.setBackgroundResource(R.drawable.rectangle_yzm_corner_bg);
            textHuiKuan.setText(huiKuanMoney);
        }


        linear_hui_jia.setClickable(true);
        linear_hui_dan.setClickable(true);
        linear_xie_zhuang.setClickable(true);
        linear_shou.setClickable(true);
        linear_hui_jia.setOnClickListener(this);
        linear_hui_dan.setOnClickListener(this);
        linear_xie_zhuang.setOnClickListener(this);
        linear_shou.setOnClickListener(this);
        SpannableString msp = new SpannableString("司机最晚在服务完成后第二天中午12点之前将回执单、货款免费送回始发地（不包括回程带人、带货）。");
        msp.setSpan(new ForegroundColorSpan
                (getResources().getColor(R.color.red)), 10, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        text_hui_dan_con_.setText(msp);

    }

    public void exit(View view) {
        Intent intent = new Intent();
        String xieZhuang = "";
        String huiDan = "";
        String huiKuan = "";
        String huiKuanMoney = "";
        if (linear_xie_zhuang.isSelected()) {
            xieZhuang = getResources().getString(R.string.need_xie_zhuang);
        }

        if (linear_hui_dan.isSelected()) {
            huiDan = getResources().getString(R.string.need_hui_dan);
        }

        if (linear_hui_jia.isSelected()) {
            huiKuan = getResources().getString(R.string.need_hui_kuan);
            huiKuanMoney = textHuiKuan.getText().toString().trim();
        }

        intent.putExtra("zhuangXie", xieZhuang);
        intent.putExtra("huiDan", huiDan);
        intent.putExtra("huiKuan", huiKuan);
        intent.putExtra("huiKuanMoney", huiKuanMoney);
        setResult(Constants.RESULT_EXTRA_SERVICE_CODE, intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.linear_xie_zhuang:
                boolean isSelected = false;
                int re = 0;
                if (linear_xie_zhuang.isSelected()) {
                    isSelected = false;
                    re = R.drawable.rectangle_yzm_gray_corner_bg;

                } else {
                    isSelected = true;
                    re = R.drawable.rectangle_yzm_corner_bg;

                }
                linear_xie_zhuang.setSelected(isSelected);
                linear_xie_zhuang.setBackgroundResource(re);
                break;
            case R.id.linear_hui_jia:
                isSelected = false;
                re = 0;
                if (linear_hui_jia.isSelected()) {
                    isSelected = false;
                    re = R.drawable.rectangle_yzm_gray_corner_bg;
                    textHuiKuan.setText(getResources().getString(R.string.free));
                    linear_hui_jia.setSelected(isSelected);
                    linear_hui_jia.setBackgroundResource(re);
                } else {
                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title("司机需带回")
                            .customView(R.layout.dialog_extra_service_customview, true)
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    EditText editText= (EditText) dialog.getCustomView().findViewById(R.id.edit_msg);
                                    dialogResult(editText.getText().toString().trim());
                                }
                            }).build();
                    dialog.show();
                }

                break;

            case R.id.linear_hui_dan:
                isSelected = false;
                re = 0;
                if (linear_hui_dan.isSelected()) {
                    isSelected = false;
                    re = R.drawable.rectangle_yzm_gray_corner_bg;
                    linear_hui_dan_con.setVisibility(View.GONE);
                } else {
                    isSelected = true;
                    re = R.drawable.rectangle_yzm_corner_bg;
                    linear_shou.setVisibility(View.VISIBLE);
                    linear_hui_dan_con.setVisibility(View.VISIBLE);
                    linear_shou.setSelected(true);
                    text_shou.setText(getString(R.string.shou_qi));
                    image_shou.setImageResource(R.drawable.pull_up);
                    text_hui_dan_con.setVisibility(View.VISIBLE);
                    text_hui_dan_con_.setVisibility(View.VISIBLE);
                }
                linear_hui_dan.setSelected(isSelected);
                linear_hui_dan.setBackgroundResource(re);
                break;
            case R.id.linear_shou:
                if (linear_hui_dan.isSelected()) {
                    isSelected = false;
                    text_shou.setText(getString(R.string.cha_kan));
                    image_shou.setImageResource(R.drawable.pull_down);
                    text_hui_dan_con.setVisibility(View.GONE);
                    text_hui_dan_con_.setVisibility(View.GONE);
                } else {
                    isSelected = true;
                    text_shou.setText(getString(R.string.shou_qi));
                    image_shou.setImageResource(R.drawable.pull_up);
                    text_hui_dan_con.setVisibility(View.VISIBLE);
                    text_hui_dan_con_.setVisibility(View.VISIBLE);
                }
                linear_hui_dan.setSelected(isSelected);
                break;
        }
    }

    public void dialogResult(String money) {
        if (!TextUtils.isEmpty(money)){
            if (Long.parseLong(money)!=0){
                textHuiKuan.setText(money+"元");
                linear_hui_jia.setSelected(true);
                linear_hui_jia.setBackgroundResource(R.drawable.rectangle_yzm_corner_bg);
            }else{
                textHuiKuan.setText(getResources().getString(R.string.free));
                linear_hui_jia.setSelected(false);
                linear_hui_jia.setBackgroundResource(R.drawable.rectangle_yzm_gray_corner_bg);
            }

        }else{
            textHuiKuan.setText(getResources().getString(R.string.free));
            linear_hui_jia.setSelected(false);
            linear_hui_jia.setBackgroundResource(R.drawable.rectangle_yzm_gray_corner_bg);
        }

    }


}
