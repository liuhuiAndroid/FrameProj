package com.xjgj.mall.ui.orderdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.OrderDetailEntity;
import com.xjgj.mall.bean.TerminiEntity;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.improveorder.ImproveOrderActivity;
import com.xjgj.mall.ui.widget.StarBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.xjgj.mall.Constants.REQUEST_IMPROVE_ORDER_CODE;

/**
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity implements OrderDetailContract.View {

    @Inject
    OrderDetailPresenter mPresenter;
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
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    private String mContactMobile;

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
        mPresenter.orderDetail(getIntent().getIntExtra("orderId", -1));

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void orderDetailResult(final OrderDetailEntity orderDetailEntity) {
        if (orderDetailEntity != null) {
            mTextCarType.setText(orderDetailEntity.getCarName());
            mTextUseCarTime.setText(orderDetailEntity.getServiceTime());
            if(TextUtils.isEmpty(orderDetailEntity.getServiceType())){
                mTextOtherCost.setText("暂无");
            }else {
                mTextOtherCost.setText(orderDetailEntity.getServiceType());
            }
            if(TextUtils.isEmpty(orderDetailEntity.getRemark())){
                mTextOrderBeiZhu.setText("暂无");
            }else {
                mTextOrderBeiZhu.setText(orderDetailEntity.getRemark());
            }

            if(orderDetailEntity.getVolume() ==0){
                mTextSize.setText("暂无");
            }else{
                mTextSize.setText(orderDetailEntity.getVolume()+"立方");
            }
            if(orderDetailEntity.getWeight() == 0){
                mTextWeight.setText("暂无");
            }else{
                mTextWeight.setText(orderDetailEntity.getWeight()+"公斤");
            }

            if (orderDetailEntity.getStatus() != 0 && !TextUtils.isEmpty(orderDetailEntity.getContactMobile())) {
                mUserInfoLayout.setVisibility(View.VISIBLE);
                ImageLoaderUtil.getInstance().loadImage(orderDetailEntity.getAvatarUrl(), mImageHeader);
                mTextUserName.setText(orderDetailEntity.getContactName());
                mContactMobile = orderDetailEntity.getContactMobile();
                mImageCallPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+mContactMobile));
                        startActivity(intent);
                    }
                });
                mTextPaiZhao.setText(orderDetailEntity.getCarNo());
                mTextUserPhone.setText(orderDetailEntity.getContactMobile());
            } else {
                mUserInfoLayout.setVisibility(View.GONE);
            }

            // 0 新建(待接单),1 已接单, 2  服务中，3 已完成, 4 已取消, 5 已评价,6 申诉中
            if(orderDetailEntity.getStatus() == 3 || orderDetailEntity.getStatus() == 4
                    || orderDetailEntity.getStatus() == 5){
                mLinearOrderAgain.setVisibility(View.VISIBLE);
            }else{
                mLinearOrderAgain.setVisibility(View.GONE);
            }

            if (orderDetailEntity.getAddressList() != null) {
                mLinearOrderAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderDetailActivity.this, ImproveOrderActivity.class);
                        List<TerminiEntity> terminiEntities = new ArrayList<TerminiEntity>();
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < orderDetailEntity.getAddressList().size(); i++) {
                            OrderDetailEntity.AddressListBean addressListBean = orderDetailEntity.getAddressList().get(i);
                            TerminiEntity terminiEntity = new TerminiEntity(addressListBean.getAddress(),addressListBean.getAddressName(),
                                    addressListBean.getLongitude(),addressListBean.getLatitude(),
                                    addressListBean.getReceiverName(),addressListBean.getReceiverPhone());
                            terminiEntities.add(terminiEntity);
                        }
                        bundle.putSerializable("tempTerminiEntity", (Serializable) terminiEntities);
                        intent.putExtras(bundle);
                        intent.putExtra("cartypeName", orderDetailEntity.getCarName() + "");
                        intent.putExtra("cartype", orderDetailEntity.getCarType()+"");
                        startActivityForResult(intent, REQUEST_IMPROVE_ORDER_CODE);
                    }
                });
            }
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
