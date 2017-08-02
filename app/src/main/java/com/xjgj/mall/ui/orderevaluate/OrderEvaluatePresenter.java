package com.xjgj.mall.ui.orderevaluate;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/8/2.
 */

public class OrderEvaluatePresenter implements OrderEvaluateContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private OrderEvaluateContract.View mView;

    @Inject
    public OrderEvaluatePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void orderComment(int orderId, String level, String content) {
        if (level.equals("0.0")) {
            ToastUtil.showToast("要给司机一个评分后才能评价哦");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast("请输入对司机的印象");
            return;
        }
        mView.showLoading();
        disposables.add(mCommonApi.orderComment(orderId,level,content)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> stringHttpResult) throws Exception {
                        return mCommonApi.flatResponse(stringHttpResult);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        ToastUtil.showToast(s);
                        mView.orderCommentSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull OrderEvaluateContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
