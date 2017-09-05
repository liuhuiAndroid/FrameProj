package com.xjgj.mall.ui.coupon;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.statefullayout.StatefulLayout;
import com.android.frameproj.library.statefullayout.StatusfulConfig;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.CouponEntity;
import com.xjgj.mall.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.xjgj.mall.Constants.RESULT_CHOOSE_COUPON;

/**
 * Created by lh on 2017/9/5.
 * 选择优惠券
 */

public class ChooseCouponActivity extends BaseActivity implements ChooseCouponContract.View {

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
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.statefulLayout)
    StatefulLayout mStatefulLayout;

    @Inject
    ChooseCouponPresenter mCouponPresenter;

    private CommonAdapter mCommonAdapter;
    private List<CouponEntity> mCouponEntities = new ArrayList<>();


    @Override
    public int initContentView() {
        return R.layout.activity_choose_coupon;
    }

    @Override
    public void initInjector() {
        DaggerChooseCouponComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mCouponPresenter.attachView(this);
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("选择优惠券");
        mCouponPresenter.couponList();
    }

    @Override
    public void showLoading() {
        mStatefulLayout.showLoading();
    }

    @Override
    public void hideLoading(int type) {
        if (type == -1) {
            StatusfulConfig statusfulConfig = new StatusfulConfig.Builder()
                    .setOnErrorStateButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //                            mPresenter.orderDetail(mOrderId);
                        }
                    }).build();
            mStatefulLayout.showError(statusfulConfig);
        } else if (type == 0) {
            StatusfulConfig statusfulConfig = new StatusfulConfig.Builder()
                    .build();
            mStatefulLayout.showEmpty(statusfulConfig);
        } else if (type == 1) {
            mStatefulLayout.showContent();
        }
    }


    @Override
    public void couponListSuccess(List<CouponEntity> couponEntities) {
        mCouponEntities.clear();
        mCouponEntities.addAll(couponEntities);
        if (mCommonAdapter == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ChooseCouponActivity.this));
            mCommonAdapter = new CommonAdapter<CouponEntity>(ChooseCouponActivity.this, R.layout.coupon_listitem, mCouponEntities) {
                @Override
                protected void convert(ViewHolder holder, final CouponEntity couponEntity, int position) {
                    holder.setText(R.id.tv_title, couponEntity.getCouponTitle());
                    holder.setText(R.id.tv_detail, couponEntity.getCouponDescribe());
                    holder.setText(R.id.tv_count, "￥" + couponEntity.getAmount());
                    holder.setText(R.id.tv_time, "有效期：" + couponEntity.getValidStartTime() + "至" + couponEntity.getValidEndTime());
                }
            };
            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    CouponEntity couponEntity = mCouponEntities.get(position);
                    setResult(RESULT_CHOOSE_COUPON,
                            new Intent()
                                    .putExtra("couponId", couponEntity.getCouponId())
                                    .putExtra("amount", couponEntity.getAmount()));
                    ChooseCouponActivity.this.finish();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mRecyclerView.setAdapter(mCommonAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCouponPresenter.detachView();
    }

}