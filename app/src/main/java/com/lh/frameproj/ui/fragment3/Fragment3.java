package com.lh.frameproj.ui.fragment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.TimeUtils;
import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.widget.wheel.Info;
import com.android.frameproj.library.widget.wheel.time.TimePickerPopWindow;
import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.main.MainComponent;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment3 extends BaseFragment {

    @BindView(R.id.roundImageView)
    ImageView mRoundImageView;


    /**
     * 选择图片返回的图片信息
     */
    private List<LocalMedia> selectList = new ArrayList<>();

    public static BaseFragment newInstance() {
        Fragment3 fragment3 = new Fragment3();
        return fragment3;
    }

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_3;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        showContent(true);
    }

    @Override
    public void initData() {

    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_exit)
    public void exit_sys() {
        new MaterialDialog.Builder(getActivity())
                .title("退出登录")
                .content("是否确认退出登录？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    /**
     * 选择头像
     */
    @OnClick(R.id.roundImageView)
    public void mRoundImageView() {
        PictureSelector.create(Fragment3.this)
                .openGallery(PictureMimeType.ofImage()) //图片
                .theme(R.style.picture_default_style) // 主题样式
                .maxSelectNum(1) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(true)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        ImageLoaderUtil.getInstance().loadImage(localMedia.getPath(), mRoundImageView);

                        Logger.i("localMedia.getPath() = " + localMedia.getPath());
                        Logger.i("localMedia.getCompressPath() = " + localMedia.getCompressPath());
                    }
                    break;
            }
        }
    }


    // ==========================   时间选择控件  =================================
    private static final String[] HOUR =
            new String[]{
                    "00时", "01时", "02时", "03时", "04时", "05时", "06时", "07时",
                    "08时", "09时", "10时", "11时", "12时", "13时", "14时", "15时",
                    "16时", "17时", "18时", "19时", "20时", "21时", "22时", "23时"};

    private static final String[] MINUT = new String[]{"00分", "10分", "20分", "30分", "40分", "50分"};
    //日期
    private List<Info> day_list = new ArrayList<Info>();
    //时
    private HashMap<String, List<Info>> hour_map = new HashMap<String, List<Info>>();
    //分
    private HashMap<String, List<Info>> minut_map = new HashMap<String, List<Info>>();
    private TimePickerPopWindow cityPickerPopWindow = null;
    private int dayPosition;

    /**
     * 测试android-wheel
     */
    @OnClick(R.id.relativeGoYyzz)
    public void mRelativeGoYyzz() {
        initUpdateTime();
        if (day_list.size() > 0 && hour_map.size() > 0 && minut_map.size() > 0) {
            cityPickerPopWindow = new TimePickerPopWindow(baseActivity, "", day_list, hour_map, minut_map, 0);
            cityPickerPopWindow.showAtLocation(baseActivity.findViewById(R.id.btn_exit), Gravity.BOTTOM, 0, 0);
            cityPickerPopWindow.setOnInterface(new TimePickerPopWindow.OnCitySelectorListener() {
                @Override
                public void onCitySelectorListener(String dayPo, String showContent) {
                    if (TextUtils.isEmpty(dayPo) && TextUtils.isEmpty(showContent)) {

                    } else {
                        dayPosition = Integer.parseInt(dayPo);
                        if (showContent.contains("立即用车")) {
                            ToastUtil.showToast("现在");
                        } else {
                            String con = showContent.replace("时", "").replace("分", "");
                            ToastUtil.showToast(con);
                        }
                    }

                }
            });
        } else {
            ToastUtil.showToast("获取数据失败");
        }
    }

    private void initUpdateTime() {
        day_list.clear();
        hour_map.clear();
        minut_map.clear();
        String currentTime = TimeUtils.getCurrentTimeMillis();
        for (int i = 0; i < 5; i++) {
            String otherTime = (Long.valueOf(currentTime) + i * 60 * 60 * 24) + "";
            String dayTime = "";
            if (i == 0) {
                dayTime = "今天";
            } else if (i == 1) {
                dayTime = "明天";
            } else {
                dayTime = TimeUtils.getDayTime(otherTime);
            }
            List<Info> dayList = new ArrayList<Info>();

            if (i == 0) {
                String hourTime = TimeUtils.getHourTime(otherTime);
                // position是当前的时间
                int position = 0;
                for (int j = 0; j < HOUR.length; j++) {
                    if (HOUR[j].equals(hourTime)) {
                        position = j;
                        break;
                    }

                }
                for (int l = position + 1; l < HOUR.length; l++) {

                    List<Info> minutList = new ArrayList<Info>();
                    // 上面一个if没用，暂时不需要立即用车
                    if (position == l) {
                        Info info = new Info();
                        info.setId(dayTime + HOUR[l]);
                        info.setCity_name("立即用车");
                        dayList.add(info);
                        Info info3 = new Info();
                        info3.setId("");
                        info3.setCity_name("");
                        minutList.add(info3);
                        minut_map.put(dayTime + HOUR[l], minutList);
                    } else {
                        Info info = new Info();
                        info.setId(dayTime + HOUR[l]);
                        info.setCity_name(HOUR[l]);
                        dayList.add(info);
                        int currentMin = 0;
                        if (l == (position + 1)) {
                            // 获取当前的分钟比较算出currentMin
                            String minuTime = TimeUtils.getMinuTime(otherTime);
                            try {
                                int minuTimeInt = Integer.parseInt(minuTime);
                                int minuTimeIntUnit = minuTimeInt / 10;
                                currentMin = minuTimeIntUnit + 1;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        for (int k = currentMin; k < MINUT.length; k++) {
                            Info info3 = new Info();
                            info3.setId(MINUT[k]);
                            info3.setCity_name(MINUT[k]);
                            minutList.add(info3);
                        }
                        minut_map.put(dayTime + HOUR[l], minutList);
                    }


                }

            } else {
                for (int j = 0; j < HOUR.length; j++) {
                    Info info = new Info();
                    info.setId(HOUR[j]);
                    info.setCity_name(HOUR[j]);
                    dayList.add(info);
                    List<Info> minutList = new ArrayList<Info>();
                    for (int k = 0; k < MINUT.length; k++) {
                        Info info3 = new Info();
                        info3.setId(MINUT[k]);
                        info3.setCity_name(MINUT[k]);
                        minutList.add(info3);
                    }
                    minut_map.put(HOUR[j], minutList);
                }
            }

            Info info = new Info();
            info.setId(dayTime);
            info.setCity_name(dayTime);
            day_list.add(info);
            hour_map.put(dayTime, dayList);
        }
    }

}
