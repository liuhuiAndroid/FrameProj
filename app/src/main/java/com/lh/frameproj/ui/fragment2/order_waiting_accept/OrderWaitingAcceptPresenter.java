package com.lh.frameproj.ui.fragment2.order_waiting_accept;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.lh.frameproj.api.common.CommonApi;
import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.injector.PerActivity;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by we-win on 2017/7/20.
 */
@PerActivity
public class OrderWaitingAcceptPresenter implements OrderWaitingAcceptContract.Presenter {


    private final CompositeDisposable disposables = new CompositeDisposable();
    private OrderWaitingAcceptContract.View mView;
    private CommonApi mCommonApi;
    private Bus mBus;

    private List<String> mStringList = new ArrayList<>();

    private int page = 1;

    @Inject
    public OrderWaitingAcceptPresenter(CommonApi commonApi, Bus mBus) {
        this.mCommonApi = commonApi;
        this.mBus = mBus;
    }

    @Override
    public void onOrderWaitingAcceptListReceive() {
        disposables.add(mCommonApi.accountVersion()
                .debounce(800, TimeUnit.MILLISECONDS)
                .doOnNext(new Consumer<HttpResult<AccountVersionEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull HttpResult<AccountVersionEntity> accountVersionEntityHttpResult) throws Exception {
                        if (page == 1) {
                            mStringList.clear();
                        }
                    }
                })
                .map(new Function<HttpResult<AccountVersionEntity>, AccountVersionEntity>() {
                    @Override
                    public AccountVersionEntity apply(@io.reactivex.annotations.NonNull HttpResult<AccountVersionEntity> stringHttpResult) throws Exception {
                        return stringHttpResult.getResultValue();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AccountVersionEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AccountVersionEntity accountVersionEntity) throws Exception {
                        if (accountVersionEntity != null) {
                            if (false) {
                                mView.onEmpty();
                            } else {
                                //TODO
                                List<String> messages = new ArrayList<String>();
                                messages.add("a");
                                messages.add("b");
                                messages.add("c");
                                messages.add("d");
                                messages.add("4");
                                messages.add("5");
                                messages.add("6");
                                messages.add("7");
                                messages.add("8");
                                messages.add("9");
                                mStringList.addAll(messages);
                                mView.hideLoading();
                                mView.renderOrderList(mStringList);
                                mView.onRefreshCompleted();
                                mView.onLoadCompleted(true);
                            }
                        } else {
                            loadError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        loadError();
                        throwable.printStackTrace();
                    }
                }));
    }


    @Override
    public void onRefresh() {
        page = 1;
        onOrderWaitingAcceptListReceive();
    }

    private void loadError() {
        if (mStringList.isEmpty()) {
            mView.onError();
        } else {
            ToastUtil.showToast("数据加载失败，请重试");
            mView.onRefreshCompleted();
            mView.onLoadCompleted(true);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        onOrderWaitingAcceptListReceive();
    }

    @Override
    public void onOrderClick(int position) {
        String s = mStringList.get(position);
        ToastUtil.showToast("点击条目:" + position + "，数据：" + s);
    }

    @Override
    public void attachView(@NonNull OrderWaitingAcceptContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }


}
