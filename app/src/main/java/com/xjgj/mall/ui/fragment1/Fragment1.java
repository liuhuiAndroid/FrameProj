package com.xjgj.mall.ui.fragment1;

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
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.OrderEntity;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.cancelorder.CancelOrderActivity;
import com.xjgj.mall.ui.decoration.DividerGridItemDecoration;
import com.xjgj.mall.ui.main.MainComponent;
import com.xjgj.mall.ui.orderappeal.OrderAppealActivity;
import com.xjgj.mall.ui.orderdetail.OrderDetailActivity;
import com.xjgj.mall.ui.orderevaluate.OrderEvaluateActivity;
import com.xjgj.mall.util.CommonEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 订单列表
 */
public class Fragment1 extends BaseFragment implements Fragment1Contract.View, LoadMoreWrapper.OnLoadMoreListener, PtrHandler {

    @Inject
    Fragment1Presenter mFragment1Presenter;
    List<OrderEntity> data;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    Bus mBus;

    private LinearLayoutManager mLinearLayoutManager;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;


    public static BaseFragment newInstance() {
        Fragment1 fragment1 = new Fragment1();
        return fragment1;
    }

    //  0
    @Override
    public int initContentView() {
        return R.layout.fragment_1;
    }

    //  1
    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    //  2
    @Override
    public void getBundle(Bundle bundle) {

    }

    //  3
    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, rootView);
        mBus.register(this);
        showContent(true);
        mFragment1Presenter.attachView(this);

        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //viewpager滑动时禁用下拉
        mPtrLayout.disableWhenHorizontalMove(true);

        data = new ArrayList<>();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        layoutPostDelayed();
        Logger.i("test onResume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            layoutPostDelayed();
        }
        Logger.i("test onHiddenChanged = " + hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i("test setUserVisibleHint = " + isVisibleToUser);
    }

    /**
     * 自动下拉刷新
     */
    public void layoutPostDelayed() {

        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mFragment1Presenter.onRefresh(currentType);
        } else {
            mPtrLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Logger.i("test mPtrLayout.autoRefresh();");
                    mPtrLayout.autoRefresh();
                }
            }, 100);
        }
    }


    @Override
    public void onRefreshCompleted(final List<OrderEntity> datas) {
        data.clear();
        data.addAll(datas);
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<OrderEntity>(getActivity(), R.layout.item_order, data) {
                @Override
                protected void convert(ViewHolder holder, final OrderEntity orderEntity, int position) {
                    // 0 新建(待接单),1 已接单, 2  服务中，3 已完成, 4 已取消, 5 已评价,6 申诉中
                    if (orderEntity.getStatus() == 0) {
                        holder.setText(R.id.textState, "待接单");
                        holder.getView(R.id.textDicuss).setVisibility(View.GONE);
                        holder.getView(R.id.textShengShu).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);
                        holder.setText(R.id.textShengShu, getResources().getString(R.string.cancle_order));

                        //取消订单
                        holder.getView(R.id.textShengShu).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), CancelOrderActivity.class);
                                intent.putExtra("orderId", orderEntity.getOrderId());
                                startActivity(intent);
                            }
                        });
                    } else if (orderEntity.getStatus() == 1) {
                        holder.setText(R.id.textState, "已接单");
                        holder.getView(R.id.textDicuss).setVisibility(View.GONE);
                        holder.getView(R.id.textShengShu).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);
                        holder.setText(R.id.textShengShu, getResources().getString(R.string.cancle_order));

                        //取消订单
                        holder.getView(R.id.textShengShu).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), CancelOrderActivity.class);
                                intent.putExtra("orderId", orderEntity.getOrderId());
                                startActivity(intent);
                            }
                        });
                    } else if (orderEntity.getStatus() == 2) {
                        if (orderEntity.getSignType() == 3) {
                            holder.setText(R.id.textState, "服务中[运输完成]");
                        } else {
                            holder.setText(R.id.textState, "服务中");
                        }


                        holder.getView(R.id.textDicuss).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textShengShu).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);
                        holder.setText(R.id.textShengShu, getResources().getString(R.string.shen_su));
                        holder.setText(R.id.textDicuss, getResources().getString(R.string.order_confirm));

                        //订单申诉
                        holder.getView(R.id.textShengShu).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), OrderAppealActivity.class);
                                intent.putExtra("orderId", orderEntity.getOrderId());
                                startActivity(intent);
                            }
                        });

                        //确认完成
                        holder.getView(R.id.textDicuss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (orderEntity.getSignType() != 3) {
                                    new MaterialDialog.Builder(getActivity())
                                            .title("提示")
                                            .content("司机尚未运输完成，是否确认完成订单？")
                                            .positiveText("确定")
                                            .negativeText("取消")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    mFragment1Presenter.orderConfirm(orderEntity.getOrderId());
                                                }
                                            })
                                            .show();
                                } else {
                                    new MaterialDialog.Builder(getActivity())
                                            .title("提示")
                                            .content("是否确认完成订单？")
                                            .positiveText("确定")
                                            .negativeText("取消")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    mFragment1Presenter.orderConfirm(orderEntity.getOrderId());
                                                }
                                            })
                                            .show();
                                }
                            }
                        });
                    } else if (orderEntity.getStatus() == 3) {
                        holder.setText(R.id.textState, "已完成");
                        holder.getView(R.id.textDicuss).setVisibility(View.VISIBLE);
                        holder.getView(R.id.textShengShu).setVisibility(View.GONE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);
                        holder.setText(R.id.textDicuss, getResources().getString(R.string.want_discusses));

                        //我要评价
                        holder.getView(R.id.textDicuss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), OrderEvaluateActivity.class);
                                intent.putExtra("orderId", orderEntity.getOrderId());
                                intent.putExtra("contactName", orderEntity.getContactName());
                                intent.putExtra("contactMobile", orderEntity.getContactMobile());
                                intent.putExtra("avatarUrl", orderEntity.getAvatarUrl());
                                intent.putExtra("carNo", orderEntity.getCarNo());
                                intent.putExtra("starLevel", orderEntity.getStarLevel());

                                startActivity(intent);
                            }
                        });
                    } else if (orderEntity.getStatus() == 4) {
                        holder.setText(R.id.textState, "已取消");

                        holder.getView(R.id.textDicuss).setVisibility(View.GONE);
                        holder.getView(R.id.textShengShu).setVisibility(View.GONE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);

                    } else if (orderEntity.getStatus() == 5) {
                        holder.setText(R.id.textState, "已评价");

                        holder.getView(R.id.textDicuss).setVisibility(View.GONE);
                        holder.getView(R.id.textShengShu).setVisibility(View.GONE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);

                    } else if (orderEntity.getStatus() == 6) {
                        holder.setText(R.id.textState, "申诉中");

                        holder.getView(R.id.textDicuss).setVisibility(View.GONE);
                        holder.getView(R.id.textShengShu).setVisibility(View.GONE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);
                    } else if (orderEntity.getStatus() == 7) {
                        holder.setText(R.id.textState, "已过期");
                        holder.getView(R.id.textDicuss).setVisibility(View.GONE);
                        holder.getView(R.id.textShengShu).setVisibility(View.GONE);
                        holder.getView(R.id.textOrderAgain).setVisibility(View.GONE);

                    }

                    holder.setText(R.id.textStart, orderEntity.getStartAddress());
                    holder.setText(R.id.textEnd, orderEntity.getGoalAddress());

                    if (orderEntity.getServiceTime() != null) {
                        if (orderEntity.getFlgSite() == 0) { // 是否场内订单 0 场外 1 场内
                            if (orderEntity.getCarType() != null) {
                                holder.setText(R.id.textTime, orderEntity.getServiceTime().concat("  ").concat(orderEntity.getCarType()));
                            }
                        } else if (orderEntity.getFlgSite() == 1) {
                            holder.setText(R.id.textTime, orderEntity.getServiceTime().concat("  ").concat("场内短驳"));
                        }
                    }
                    if (orderEntity.getOrderType() == 1) {
                        holder.setImageResource(R.id.imageState, R.drawable.icon_real);
                    } else if (orderEntity.getOrderType() == 2) {
                        holder.setImageResource(R.id.imageState, R.drawable.icon_appointment);
                    }
                }
            };
            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (datas != null && datas.size() > position) {
                        OrderEntity orderEntity = datas.get(position);
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra("orderId", orderEntity.getOrderId());
                        intent.putExtra("flgSite", orderEntity.getFlgSite());
                        startActivity(intent);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(getActivity()).inflate(R.layout.footer_view_load_more, mRecyclerView, false));
            mLoadMoreWrapper.setOnLoadMoreListener(this);
            mRecyclerView.setAdapter(mLoadMoreWrapper);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity(), 0));
        } else {
            mLoadMoreWrapper.notifyDataSetChanged();
        }
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
            Logger.i("test refreshComplete刷新完成");
        }
        showContent(true);
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
            Logger.i("test refreshComplete刷新完成");
        }
        showError(true);
    }

    /**
     * 刷新页面
     */
    @Override
    public void onRefresh() {
        layoutPostDelayed();
    }

    @Override
    public void onEmpty(int currentType) {
        // 订单类型：0 新建(待接单),1 已接单, 2  服务中，3 已完成, 4 已取消, 5 已评价,6 申诉中
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
            Logger.i("test refreshComplete刷新完成");
        } else {
            Logger.i("test no refresh");
        }
        if (currentType == -1) {
            setEmptyText("暂无订单");
        } else if (currentType == 0) {
            setEmptyText("暂无待接单订单");
        } else if (currentType == 1) {
            setEmptyText("暂无已接单订单");
        } else if (currentType == 2) {
            setEmptyText("暂无服务中订单");
        } else if (currentType == 3) {
            setEmptyText("暂无已完成订单");
        } else if (currentType == 4) {
            setEmptyText("暂无已取消订单");
        } else if (currentType == 5) {
            setEmptyText("暂无已评价订单");
        } else if (currentType == 6) {
            setEmptyText("暂无申诉中订单");
        }
        showEmpty(true);
    }

    @Override
    public void onLoadCompleted(boolean isLoadAll) {
        mLoadMoreWrapper.setLoadAll(isLoadAll);
    }

    @Override
    public void onLoadMoreRequested() {
        mFragment1Presenter.onLoadMore();
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
        Logger.i("test 请求onRefresh");
        mFragment1Presenter.onRefresh(currentType);
    }


    private int currentType = -1;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        mFragment1Presenter.detachView();
    }

    @Subscribe
    public void orderTypeChangeEvent(CommonEvent.OrderTypeChangeEvent orderTypeChangeEvent) {
        Logger.i("test 收到orderTypeChangeEvent，currentType = " + currentType);
        currentType = orderTypeChangeEvent.getType();
        layoutPostDelayed();
    }

    @Override
    public void onReloadClicked() {
        mFragment1Presenter.onRefresh(currentType);
    }

}
