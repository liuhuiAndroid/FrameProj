package com.xjgj.mall.ui.fragment2.order_working;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.adapter.wrapper.LoadMoreWrapper;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.widget.MyPtrClassicFrameLayout;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.OrderEntity;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.cancelorder.CancelOrderActivity;
import com.xjgj.mall.ui.decoration.DividerGridItemDecoration;
import com.xjgj.mall.ui.main.MainComponent;
import com.xjgj.mall.ui.orderdetail.OrderDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 服务中
 */
public class OrderWorkingFragement extends BaseFragment implements OrderWorkingContract.View,
        LoadMoreWrapper.OnLoadMoreListener, PtrHandler {

    public static BaseFragment newInstance() {
        OrderWorkingFragement orderWorkingFragement = new OrderWorkingFragement();
        return orderWorkingFragement;
    }

    @Inject
        OrderWorkingPresenter mPresenter;
        @BindView(R.id.recyclerView)
        RecyclerView mRecyclerView;
        @BindView(R.id.ptr_layout)
        MyPtrClassicFrameLayout mPtrLayout;

        @Override
        public void initInjector() {
            getComponent(MainComponent.class).inject(this);
        }

        @Override
        public int initContentView() {
            return R.layout.fragment_order_waiting_accept;
        }

        @Override
        public void getBundle(Bundle bundle) {
        }

        private LinearLayoutManager mLinearLayoutManager;
        private CommonAdapter mCommonAdapter;
        private LoadMoreWrapper mLoadMoreWrapper;

        @Override
        public void initUI(View view) {
            mPresenter.attachView(this);

            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mPtrLayout.setPtrHandler(this);
            //显示时间
            mPtrLayout.setLastUpdateTimeRelateObject(this);
            //viewpager滑动时禁用下拉
            mPtrLayout.disableWhenHorizontalMove(true);
        }

        @Override
        public void initData() {
        }

        private boolean isFirst = true;
        /**
         * 判断fragment是否是被用户可见
         * @param isVisibleToUser
         */
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if(isVisibleToUser) {
                if (!isFirst) {
                    layoutPostDelayed();
                } else {
                    isFirst = false;
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            layoutPostDelayed();
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {
            Logger.i("test hideLoading");
            showContent(true);
        }

        /**
         * 得到数据，刷新RecyclerView
         */
        @Override
        public void renderOrderList(final List<OrderEntity> orderEntities) {
            Logger.i("得到数据，刷新RecyclerView");
            if (mCommonAdapter == null) {
                mCommonAdapter = new CommonAdapter<OrderEntity>(getActivity(), R.layout.item_order, orderEntities) {
                    @Override
                    protected void convert(ViewHolder holder, final OrderEntity orderEntity, int position) {
                        holder.setText(R.id.textTime, orderEntity.getCreateTime().concat("  (").concat(orderEntity.getCarType()).concat(")"));
                        holder.setText(R.id.textState, "服务中");
                        holder.setText(R.id.textStart, orderEntity.getStartAddress());
                        holder.setText(R.id.textEnd,  orderEntity.getGoalAddress());

                        holder.getView(R.id.textDicuss).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textShengShu).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.VISIBLE);
                        holder.setText(R.id.textShengShu, getResources().getString(R.string.cancle_order));
                        holder.setText(R.id.textOrderAgain, getResources().getString(R.string.order_again));
                        holder.setText(R.id.textDicuss, getResources().getString(R.string.order_confirm));

                        //取消订单
                        holder.getView(R.id.textShengShu).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), CancelOrderActivity.class);
                                intent.putExtra("orderId",orderEntity.getOrderId());
                                startActivity(intent);
                            }
                        });

                        //确认完成
                        holder.getView(R.id.textDicuss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new MaterialDialog.Builder(getActivity())
                                        .title("提示")
                                        .content("是否确认完成订单？")
                                        .positiveText("确定")
                                        .negativeText("取消")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                mPresenter.orderConfirm(orderEntity.getOrderId());
                                            }
                                        })
                                        .show();
                            }
                        });
                    }
                };
                mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra("orderId", orderEntities.get(position).getOrderId());
                        startActivity(intent);
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
                mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
                mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(getActivity()).inflate(R.layout.footer_view_load_more, mRecyclerView, false));
                mLoadMoreWrapper.setOnLoadMoreListener(this);
                mRecyclerView.setAdapter(mLoadMoreWrapper);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity(), 0));
            } else {
                mCommonAdapter.setDatas(orderEntities);
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        }

        @Override
        public void onRefreshCompleted() {
            if (mPtrLayout != null && mPtrLayout.isShown()) {
                mPtrLayout.refreshComplete();
            }
        }

        @Override
        public void onLoadCompleted(boolean isLoadAll) {
            mLoadMoreWrapper.setLoadAll(isLoadAll);
        }

        @Override
        public void onError(Throwable throwable) {
            showError(true);
            loadError(throwable);
        }

        @Override
        public void onRefresh() {
            layoutPostDelayed();
        }

        @Override
        public void onEmpty() {
        setEmptyText("暂无服务中订单");
        showEmpty(true);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    /**
     * 自动下拉刷新
     */
    public void layoutPostDelayed() {
        mPtrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.autoRefresh();
            }
        }, 100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mLinearLayoutManager != null) {
            boolean result = false;
            if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                final View topChildView = mRecyclerView.getChildAt(0);
                result = topChildView.getTop() == 0;
            }
            return result && PtrDefaultHandler
                    .checkContentCanBePulledDown(frame, content, header);
        } else {
            return PtrDefaultHandler
                    .checkContentCanBePulledDown(frame, content, header);
        }
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        Logger.i("test onRefreshBegin");
        mPresenter.onRefresh();
    }

    @Override
    public void onReloadClicked() {
        mPresenter.onRefresh();
    }

}