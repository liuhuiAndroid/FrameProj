package com.xjgj.mall.ui.comfirmorder;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.ToastUtil;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.OrderCarInfo;
import com.xjgj.mall.bean.TerminiEntity;
import com.xjgj.mall.ui.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.RESULT_CONFIRM_ORDER_CODE;
import static com.xjgj.mall.R.id.linear_d;


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
    @BindView(linear_d)
    LinearLayout mLinearD;

    @Inject
    ComfirmOrderPresenter mComfirmOrderPresenter;
    @BindView(R.id.avLoadingIndicatorView)
    AVLoadingIndicatorView mAvLoadingIndicatorView;
    @BindView(R.id.textCount)
    TextView mTextCount;
    @BindView(R.id.textCountShow)
    TextView mTextCountShow;
    @BindView(R.id.textOtherService)
    TextView mTextOtherService;
    @BindView(R.id.textOtherServiceShow)
    TextView mTextOtherServiceShow;
    @BindView(R.id.textRemarks)
    TextView mTextRemarks;
    @BindView(R.id.textRemarksShow)
    TextView mTextRemarksShow;
    @BindView(R.id.textTogetherShow)
    TextView mTextTogetherShow;
    /**
     * 订单信息
     */
    private OrderCarInfo mOrderCarInfo;
    /**
     * 订单目的地信息
     */
    private List<TerminiEntity> tempTerminiEntity;

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
        mAvLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");

        Bundle bundle = getIntent().getExtras();
        mOrderCarInfo = (OrderCarInfo) bundle.getSerializable("orderCarInfo");
        tempTerminiEntity = (List<TerminiEntity>) getIntent().getExtras().getSerializable("tempTerminiEntity");

        mTextTogetherShow.setText(mOrderCarInfo.getFlgTogether() == 1 ? "是" : "否");
        String cartypeName = getIntent().getStringExtra("cartypeName");
        if (!TextUtils.isEmpty(mOrderCarInfo.getServiceTime())) {
            mTextUseCarTime.setText(mOrderCarInfo.getServiceTime());
        } else {
            mTextUseCarTime.setText("现在");
        }
        mTextCarType.setText(cartypeName);
        if (!TextUtils.isEmpty(mOrderCarInfo.getVolume())) {
            mTextSizeShow.setText(mOrderCarInfo.getVolume() + "立方");
        } else {
            mTextSizeShow.setText("无");
        }
        if (!TextUtils.isEmpty(mOrderCarInfo.getWeight())) {
            mTextWeightShow.setText(mOrderCarInfo.getWeight() + "公斤");
        } else {
            mTextWeightShow.setText("无");
        }
        if (!TextUtils.isEmpty(mOrderCarInfo.getCounts())) {
            mTextCountShow.setText(mOrderCarInfo.getCounts() + "个");
        } else {
            mTextCountShow.setText("无");
        }
        if (!TextUtils.isEmpty(mOrderCarInfo.getServiceType())) {
            mTextOtherServiceShow.setText(mOrderCarInfo.getServiceType());
        } else {
            mTextOtherServiceShow.setText("无");
        }
        if (!TextUtils.isEmpty(mOrderCarInfo.getRemark())) {
            mTextRemarksShow.setText(mOrderCarInfo.getRemark());
        } else {
            mTextRemarksShow.setText("无");
        }

        if (tempTerminiEntity != null && tempTerminiEntity.size() > 0) {
            for (int i = 0; i < tempTerminiEntity.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_termini, null);
                TextView t = (TextView) view.findViewById(R.id.muDiDi);
                TextView textMuDiDi = (TextView) view.findViewById(R.id.textMuDiDi);
                TextView textMuDiDiDetail = (TextView) view.findViewById(R.id.textMuDiDiDetail);
                String edtalName = tempTerminiEntity.get(i).getAddressDescribeName();
                if (TextUtils.isEmpty(edtalName)) {
                    textMuDiDi.setText(tempTerminiEntity.get(i).getAddressName());
                } else {
                    textMuDiDi.setText(tempTerminiEntity.get(i).getAddressName());
                    textMuDiDiDetail.setText(tempTerminiEntity.get(i).getAddressDescribeName());
                }
                if (i == 0) {
                    t.setText(getResources().getString(R.string.begin_d));
                } else if (i != tempTerminiEntity.size() - 1) {
                    t.setText(getResources().getString(R.string.pass_d));
                } else {
                    t.setText(getResources().getString(R.string.target_d));
                }
                mLinearD.addView(view);
            }
        }
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
        }, 500);
    }

    @Override
    public void submitSuccess() {
        ToastUtil.showToast("下单成功");
        setResult(RESULT_CONFIRM_ORDER_CODE);
        finish();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
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
                mOrderCarInfo.getRemark(), mOrderCarInfo.getCounts(), new Gson().toJson(tempTerminiEntity),
                "1",mOrderCarInfo.getFlgTogether());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
