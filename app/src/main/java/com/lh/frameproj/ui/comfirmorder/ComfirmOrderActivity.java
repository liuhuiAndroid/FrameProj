package com.lh.frameproj.ui.comfirmorder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.ToastUtil;
import com.google.gson.Gson;
import com.lh.frameproj.R;
import com.lh.frameproj.bean.OrderCarInfo;
import com.lh.frameproj.bean.TerminiEntity;
import com.lh.frameproj.ui.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by we-win on 2017/7/27.
 */

public class ComfirmOrderActivity extends BaseActivity implements ComfirmOrderContract.View {

    @BindView(R.id.image_back)
    ImageView mImageBack;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.image_handle)
    ImageView mImageHandle;
    @BindView(R.id.text_handle)
    TextView mTextHandle;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.linearClude)
    LinearLayout mLinearClude;
    @BindView(R.id.textNext)
    TextView mTextNext;
    @BindView(R.id.linearBottom)
    LinearLayout mLinearBottom;
    @BindView(R.id.text1)
    TextView mText1;
    @BindView(R.id.textUseCarTime)
    TextView mTextUseCarTime;
    @BindView(R.id.text2)
    TextView mText2;
    @BindView(R.id.textCarType)
    TextView mTextCarType;
    @BindView(R.id.textSize)
    TextView mTextSize;
    @BindView(R.id.textSizeShow)
    TextView mTextSizeShow;
    @BindView(R.id.textWeight)
    TextView mTextWeight;
    @BindView(R.id.textWeightShow)
    TextView mTextWeightShow;
    @BindView(R.id.view1)
    View mView1;
    @BindView(R.id.linear_d)
    LinearLayout mLinearD;

    @Inject
    ComfirmOrderPresenter mComfirmOrderPresenter;
    /**
     * 订单信息
     */
    private OrderCarInfo mOrderCarInfo;
    /**
     * 订单目的地信息
     */
    private List<TerminiEntity> mTerminiDatas;

    @Override
    public int initContentView() {
        return R.layout.activity_comfirm_order;
    }

    @Override
    public void initInjector() {
        DaggerComfirmOrderComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mComfirmOrderPresenter.attachView(this);
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        setImgBack(mImageBack);
        mTextTitle.setText(getResources().getString(R.string.order_ok));

        Bundle bundle = getIntent().getExtras();
        mOrderCarInfo = (OrderCarInfo) bundle.getSerializable("orderCarInfo");
        mTerminiDatas = (List<TerminiEntity>) bundle.getSerializable("terminiDatas");
    }

    @Override
    public void showLoading() {
        ToastUtil.showToast("showLoading");
    }

    @Override
    public void hideLoading() {
        ToastUtil.showToast("hideLoading");
    }

    @Override
    public void submitSuccess() {
        ToastUtil.showToast("submitSuccess");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComfirmOrderPresenter.detachView();
    }

    /**
     * 下单
     * submitType :提交类型(1 下单 2 预算金额)
     */
    @OnClick(R.id.textNext)
    public void mTextNext() {
        mComfirmOrderPresenter.orderSubmit(mOrderCarInfo.getServiceTime(), mOrderCarInfo.getVolume(),
                mOrderCarInfo.getWeight(), mOrderCarInfo.getServiceType(), mOrderCarInfo.getCarType(),
                mOrderCarInfo.getRemark(), mOrderCarInfo.getCounts(), new Gson().toJson(mTerminiDatas),"1");
    }

}
