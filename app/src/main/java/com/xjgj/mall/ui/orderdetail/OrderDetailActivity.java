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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.xjgj.mall.Constants.REQUEST_IMPROVE_ORDER_CODE_FROM_DETAIL;
import static com.xjgj.mall.Constants.RESULT_IMPROVE_ORDER_CODE_FROM_DETAIL;

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
    CircleImageView mImageHeader;
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
    @BindView(R.id.textCount)
    TextView mTextCount;
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

            if (orderDetailEntity.getAddressList() != null && orderDetailEntity.getAddressList().size() > 0) {

                mStarBar.setStarMark((float) orderDetailEntity.getStarLevel());

                int len = orderDetailEntity.getAddressList().size();

                for (int j = 0; j < orderDetailEntity.getAddressList().size(); j++) {

                    OrderDetailEntity.AddressListBean addressListBean = orderDetailEntity.getAddressList().get(j);

                    View view = getLayoutInflater().inflate(R.layout.xing_cheng, null);

                    if (j == 0) {
                        view.findViewById(R.id.viewShow).setVisibility(View.GONE);
                        ((TextView) view.findViewById(R.id.textD)).setText(getResources().getString(R.string.begin_d));
                    } else {
                        if (len == 1) {
                            ((TextView) view.findViewById(R.id.textD)).setText(getResources().getString(R.string.target_d));
                        } else {
                            if (j != len - 1) {
                                ((TextView) view.findViewById(R.id.textD)).setText(getResources().getString(R.string.pass_d));
                            } else {
                                ((TextView) view.findViewById(R.id.textD)).setText(getResources().getString(R.string.target_d));
                            }
                        }

                        if (j == len - 1) {
                            view.findViewById(R.id.viewShow).setVisibility(View.GONE);
                        } else {
                            view.findViewById(R.id.viewShow).setVisibility(View.VISIBLE);
                        }
                    }

                    ((TextView) view.findViewById(R.id.textDAddress)).setText(addressListBean.getAddress());
                    ((TextView) view.findViewById(R.id.textDetailsAddress)).setText(addressListBean.getAddressName());
                    ((TextView) view.findViewById(R.id.textConstantUser)).setText(addressListBean.getReceiverName());
                    ((TextView) view.findViewById(R.id.textConstantUserName)).setText(addressListBean.getReceiverPhone());
                    mLinearAddXingCheng.addView(view);
                }
            }

            mTextCarType.setText(orderDetailEntity.getCarName());
            mTextUseCarTime.setText(orderDetailEntity.getServiceTime());
            if (TextUtils.isEmpty(orderDetailEntity.getServiceType())) {
                mTextOtherCost.setText("暂无");
            } else {
                mTextOtherCost.setText(orderDetailEntity.getServiceType());
            }
            if (TextUtils.isEmpty(orderDetailEntity.getRemark())) {
                mTextOrderBeiZhu.setText("暂无");
            } else {
                mTextOrderBeiZhu.setText(orderDetailEntity.getRemark());
            }

            if (orderDetailEntity.getVolume() == 0) {
                mTextSize.setText("暂无");
            } else {
                mTextSize.setText(orderDetailEntity.getVolume() + "立方");
            }
            if (orderDetailEntity.getWeight() == 0) {
                mTextWeight.setText("暂无");
            } else {
                mTextWeight.setText(orderDetailEntity.getWeight() + "公斤");
            }

            if (orderDetailEntity.getCounts() == 0) {
                mTextCount.setText("暂无");
            } else {
                mTextCount.setText(orderDetailEntity.getCounts() + "个");
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
                        intent.setData(Uri.parse("tel:" + mContactMobile));
                        startActivity(intent);
                    }
                });
                mTextPaiZhao.setText(orderDetailEntity.getCarNo());
                mTextUserPhone.setText(orderDetailEntity.getContactMobile());
            } else {
                mUserInfoLayout.setVisibility(View.GONE);
            }

            // 0 新建(待接单),1 已接单, 2  服务中，3 已完成, 4 已取消, 5 已评价,6 申诉中
            if (orderDetailEntity.getStatus() == 3 || orderDetailEntity.getStatus() == 4
                    || orderDetailEntity.getStatus() == 5) {
                mLinearOrderAgain.setVisibility(View.VISIBLE);
            } else {
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
                            TerminiEntity terminiEntity = new TerminiEntity(addressListBean.getAddress(), addressListBean.getAddressName(),
                                    addressListBean.getLongitude(), addressListBean.getLatitude(),
                                    addressListBean.getReceiverName(), addressListBean.getReceiverPhone());
                            terminiEntities.add(terminiEntity);
                        }
                        bundle.putSerializable("tempTerminiEntity", (Serializable) terminiEntities);
                        intent.putExtras(bundle);
                        intent.putExtra("cartypeName", orderDetailEntity.getCarName() + "");
                        intent.putExtra("cartype", orderDetailEntity.getCarType() + "");
                        intent.putExtra("comefrom", 1);// 1代表从订单详情进入，2代表从找车进入
                        startActivityForResult(intent, REQUEST_IMPROVE_ORDER_CODE_FROM_DETAIL);
                    }
                });
            }

            if(orderDetailEntity.getStatus() == 5){
                mLinearComments.setVisibility(View.VISIBLE);
                mLinearOthersShow.setVisibility(View.GONE);
                mLinearMeShow.setVisibility(View.GONE);
                for (int i = 0; i < orderDetailEntity.getEvaluation().size(); i++) {
                    OrderDetailEntity.EvaluationBean evaluationBean = orderDetailEntity.getEvaluation().get(i);
                    // 评价方式 1 商户评价 2 司机评价
                    if(evaluationBean.getType() == 1 ){
                        mLinearMeShow.setVisibility(View.VISIBLE);
                        mTextMeComments.setText(evaluationBean.getContent());
                        mStarMeBarShow.setStarMark(evaluationBean.getLevel());
                    }else if(evaluationBean.getType() == 2){
                        mLinearOthersShow.setVisibility(View.VISIBLE);
                        mTextOthersComments.setText(evaluationBean.getContent());
                        mStarOthersBarShow.setStarMark(evaluationBean.getLevel());
                    }
                }

            }else{
                mLinearComments.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMPROVE_ORDER_CODE_FROM_DETAIL && resultCode == RESULT_IMPROVE_ORDER_CODE_FROM_DETAIL) {
            finish();
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
