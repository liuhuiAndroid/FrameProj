package com.xjgj.mall.ui.orderdetail;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xjgj.mall.R;
import com.xjgj.mall.bean.OrderDetailEntity;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.widget.StarBar;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity implements OrderDetailContract.View{

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
    @BindView(R.id.imageHeader)
    ImageView mImageHeader;
    @BindView(R.id.imageCallPhone)
    ImageView mImageCallPhone;
    @BindView(R.id.textUserName)
    TextView mTextUserName;
    @BindView(R.id.textUserPhone)
    TextView mTextUserPhone;
    @BindView(R.id.textFwTimes)
    TextView mTextFwTimes;
    @BindView(R.id.textPaiZhao)
    TextView mTextPaiZhao;
    @BindView(R.id.starBar)
    StarBar mStarBar;
    @BindView(R.id.userInfoLayout)
    RelativeLayout mUserInfoLayout;
    @BindView(R.id.textCarType)
    TextView mTextCarType;
    @BindView(R.id.textUseCarTime)
    TextView mTextUseCarTime;
    @BindView(R.id.textSize)
    TextView mTextSize;
    @BindView(R.id.textWeight)
    TextView mTextWeight;
    @BindView(R.id.textOtherCost)
    TextView mTextOtherCost;
    @BindView(R.id.textOrderBeiZhu)
    TextView mTextOrderBeiZhu;
    @BindView(R.id.linearAddXingCheng)
    LinearLayout mLinearAddXingCheng;
    @BindView(R.id.linearOrderAgain)
    LinearLayout mLinearOrderAgain;
    @BindView(R.id.starMeBarShow)
    StarBar mStarMeBarShow;
    @BindView(R.id.textMeComments)
    TextView mTextMeComments;
    @BindView(R.id.linearMeShow)
    LinearLayout mLinearMeShow;
    @BindView(R.id.starOthersBarShow)
    StarBar mStarOthersBarShow;
    @BindView(R.id.textOthersComments)
    TextView mTextOthersComments;
    @BindView(R.id.linearOthersShow)
    LinearLayout mLinearOthersShow;
    @BindView(R.id.linearComments)
    LinearLayout mLinearComments;
    @BindView(R.id.tetCancelReason)
    TextView mTetCancelReason;
    @BindView(R.id.imageOneShow)
    ImageView mImageOneShow;
    @BindView(R.id.imageTwoShow)
    ImageView mImageTwoShow;
    @BindView(R.id.textShowAgreeTime)
    TextView mTextShowAgreeTime;
    @BindView(R.id.textAgreeCancel)
    TextView mTextAgreeCancel;
    @BindView(R.id.textDisAgreeCancel)
    TextView mTextDisAgreeCancel;
    @BindView(R.id.editDisagree)
    EditText mEditDisagree;
    @BindView(R.id.textLimit)
    TextView mTextLimit;
    @BindView(R.id.textSubmit)
    TextView mTextSubmit;
    @BindView(R.id.linearDisagreeContent)
    LinearLayout mLinearDisagreeContent;
    @BindView(R.id.linearOrderCancel)
    LinearLayout mLinearOrderCancel;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @Inject
    OrderDetailPresenter mPresenter;

    @Override
    public int initContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initInjector() {
        DaggerOrderDetailComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("订单详情");
        mPresenter.orderDetail(getIntent().getIntExtra("orderId",-1));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void orderDetailResult(OrderDetailEntity orderDetailEntity) {
        if(orderDetailEntity!=null){
            mTextCarType.setText(orderDetailEntity.getCarType());
            mTextUseCarTime.setText(orderDetailEntity.getServiceTime());
            mTextSize.setText(orderDetailEntity.getVolume()+"");
            mTextWeight.setText(orderDetailEntity.getWeight()+"");
            mTextOtherCost.setText(orderDetailEntity.getOrderId()+"");
        }
    }

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
