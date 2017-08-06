package com.xjgj.mall.ui.cancelorder.my_reasons;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.util.log.Logger;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.cancelorder.CancelOrderComponent;
import com.xjgj.mall.util.CommonEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * Created by we-win on 2017/8/3.
 */

public class MyReasonsFragment extends BaseFragment implements MyReasonsContract.View {


    @Inject
    MyReasonsPresenter mPresenter;
    @BindView(R.id.linearAdd)
    LinearLayout mLinearAdd;
    @BindView(R.id.imageOne)
    ImageView mImageOne;
    @BindView(R.id.imageTwo)
    ImageView mImageTwo;
    @BindView(R.id.editShaoHua)
    EditText mEditShaoHua;
    @BindView(R.id.textLimit)
    TextView mTextLimit;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @Inject
    Bus mBus;
    @BindView(R.id.avLoadingIndicatorView)
    AVLoadingIndicatorView mAvLoadingIndicatorView;

    private int maxLength = 0;
    private int length = 60;
    private int mOrderId;

    public static BaseFragment newInstance(int orderId) {
        MyReasonsFragment myReasonsFragment = new MyReasonsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderId", orderId);
        myReasonsFragment.setArguments(bundle);
        return myReasonsFragment;
    }


    @Override
    public void initInjector() {
        getComponent(CancelOrderComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_reasons;
    }

    @Override
    public void getBundle(Bundle bundle) {
        mOrderId = bundle.getInt("orderId");
    }

    @Override
    public void initUI(View view) {
        showContent(true);
        mPresenter.attachView(this);
        mBus.register(this);
        mPresenter.dictionaryQuery();
        mEditShaoHua.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0) {

                    try {
                        maxLength = s.toString().length();
                        if (maxLength > length) {
                            s.delete(length, str.length());


                        } else {

                            if (mTextLimit != null) {

                                if (length == 60) {
                                    if (maxLength < length) {
                                        mTextLimit.setText(maxLength + "/" + length);
                                    } else {
                                        SpannableString msp = new SpannableString("60/60");
                                        msp.setSpan(new ForegroundColorSpan
                                                (getResources().getColor(R.color.red)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
                                        mTextLimit.setText(msp);
                                    }
                                }


                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mTextLimit != null) {
                        if (length == 60) {
                            mTextLimit.setText("0/60");
                        }

                    }

                }

            }

        });

        mTextLimit.setText("0/60");
        mAvLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");
    }

    @Override
    public void initData() {
    }

    @Override
    public void showLoading() {
        mAvLoadingIndicatorView.show();
    }

    @Override
    public void hideLoading() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAvLoadingIndicatorView.hide();
            }
        },500);
    }

    @Override
    public void dictionaryQuerySuccess(List<DictionaryEntity> dictionaryEntitieList) {
        DictionaryEntity dictionaryEntity = dictionaryEntitieList.get(0);
        for (int i = 0; i < dictionaryEntity.getData().size(); i++) {
            View v = View.inflate(getActivity(), R.layout.cancle_reason_item, null);
            ((TextView) v.findViewById(R.id.textContent)).setText(dictionaryEntity.getData().get(i).getDictionaryName());
            ((TextView) v.findViewById(R.id.textContent)).setTag(dictionaryEntity.getData().get(i).getDictionaryId());
            v.setTag(i);
            v.setClickable(true);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    for (int j = 0; j < mLinearAdd.getChildCount(); j++) {
                        ((ImageView) mLinearAdd.getChildAt(j).findViewById(R.id.imageShow)).setImageResource(R.drawable.chkbox_null);
                    }
                    ((ImageView) mLinearAdd.getChildAt(tag).findViewById(R.id.imageShow)).setImageResource(R.drawable.chkbox_chk);
                    cancelType = (int) mLinearAdd.getChildAt(tag).findViewById(R.id.textContent).getTag();
                }
            });
            mLinearAdd.addView(v);
        }
    }

    @Override
    public void cancelOrderSuccess() {
        getActivity().finish();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mBus.unregister(this);
    }

    private int currentChoose; // 1  2
    private String mCompressPathOne;
    private String mCompressPathTwo;

    /**
     * 选择图片
     */
    @OnClick(R.id.imageOne)
    public void mImageOne() {
        currentChoose = 1;
        choosePhoto();
    }

    /**
     * 选择图片
     */
    @OnClick(R.id.imageTwo)
    public void mImageTwo() {
        currentChoose = 2;
        choosePhoto();
    }

    private void choosePhoto() {
        PictureSelector.create(getActivity())
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        if (currentChoose == 1) {
                            mCompressPathOne = localMedia.getCompressPath();
                            ImageLoaderUtil.getInstance().loadImage(localMedia.getCompressPath(), mImageOne);
                        } else if (currentChoose == 2) {
                            mCompressPathTwo = localMedia.getCompressPath();
                            ImageLoaderUtil.getInstance().loadImage(localMedia.getCompressPath(), mImageTwo);
                        }

                        Logger.i("localMedia.getPath() = " + localMedia.getPath());
                        Logger.i("localMedia.getCompressPath() = " + localMedia.getCompressPath());
                    }
                    break;
            }
        }
    }

    //取消类型
    private int cancelType = -1;

    /**
     * 取消订单事件
     */
    @Subscribe
    public void onTabSelectedEvent(CommonEvent.CancleOrderEvent event) {
        if (event.getPosition() == 0) {
            if (cancelType == -1) {
                ToastUtil.showToast("请选择取消原因");
                return;
            }
            int reasonType = 1;
            String remark = mEditShaoHua.getText().toString();
            List<String> pathList = new ArrayList<>();
            if (mCompressPathTwo != null && !TextUtils.isEmpty(mCompressPathTwo)) {
                pathList.add(mCompressPathTwo);
            }
            if (mCompressPathOne != null && !TextUtils.isEmpty(mCompressPathOne)) {
                pathList.add(mCompressPathOne);
            }
            mPresenter.cancelOrder(mOrderId, reasonType, cancelType, remark, pathList);
        }
    }

}
