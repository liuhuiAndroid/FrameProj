package com.xjgj.mall.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.fragment1.Fragment1;
import com.xjgj.mall.ui.fragment3.Fragment3;
import com.xjgj.mall.ui.fragmentfindcar.FragmentFindCar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
public class MainPresenter implements MainContract.Presenter {

    private Context mContext;
    private MainContract.View mMainView;

    private CommonApi mCommonApi;
    private final CompositeDisposable disposables = new CompositeDisposable();
    //存储fragment
    private SparseArray<BaseFragment> fragmentTabMap = new SparseArray<>();

    //之前选中tab
    private int preSelect = -1;
    //当前选中tab
    private int nowSelect = 0;

    @Inject
    public MainPresenter(Context mContext,CommonApi commonApi) {
        this.mContext = mContext;
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void detachView() {
        mMainView = null;
    }

    @Override
    public void onTabClick(int position) {
        if (position != nowSelect) {
            preSelect = nowSelect;
            nowSelect = position;
            changeTab();
        }
    }

    @Override
    public void initFragment() {
        changeTab();
    }


    private void changeTab() {
        if (preSelect != -1) {
            mMainView.hideFragment(fragmentTabMap.get(preSelect));
        }
        BaseFragment mFragment = null;
        mFragment = fragmentTabMap.get(nowSelect);
        switch (nowSelect) {
            case 0:
                mMainView.setTitle(0,"找车");
                break;
            case 1:
                mMainView.setTitle(1,"订单");
                break;
            case 2:
                mMainView.setTitle(2,"我");
                break;
        }
        if (mFragment == null) {
            if (nowSelect == 0) {
                mFragment = FragmentFindCar.newInstance();
            } else if (nowSelect == 1) {
                mFragment = Fragment1.newInstance();
            } else if (nowSelect == 2) {
                mFragment = Fragment3.newInstance();
            }
            if (mFragment != null) {
                fragmentTabMap.put(nowSelect, mFragment);
            }
            mMainView.addFragment(mFragment);
        } else {
            mMainView.showFragment(mFragment);
        }
    }

    @Override
    public void dictionaryQuery() {
        disposables.add(mCommonApi.dictionaryQuery(6)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<DictionaryEntity>>, ObservableSource<List<DictionaryEntity>>>() {
                    @Override
                    public ObservableSource<List<DictionaryEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<DictionaryEntity>> listHttpResult) throws Exception {
                        return mCommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DictionaryEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<DictionaryEntity> dictionaryEntities) throws Exception {
                        if (dictionaryEntities != null && dictionaryEntities.size() > 0) {
                            mMainView.dictionaryQuerySuccess(dictionaryEntities);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mMainView.onError(throwable);
                    }
                }));
    }

}
