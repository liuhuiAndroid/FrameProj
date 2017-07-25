package com.lh.frameproj.ui.fragment1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.frameproj.library.util.log.Logger;
import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.header.RentalsSunHeaderView;
import com.lh.frameproj.ui.main.MainComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment1 extends BaseFragment implements Fragment1Contract.View {

    private static final String TAG = "Fragment1";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.material_style_ptr_frame)
    PtrFrameLayout frame;

    @Inject
    Fragment1Presenter mFragment1Presenter;

    List<String> data;
    private Fragment1Adapter mFragment1Adapter;
    private LinearLayoutManager mLinearLayoutManager;


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

        for (int i = 0; i < 10; i++) {
            //获取当前i的值
            int selector = i;

            //调用方法
            stepNext(i);
            //打log查看当前i的值（此步多余，实际开发请忽略）
            //            Logger.e("for当前的i的值：" + i);
        }

    }

    private void stepNext(int i) {
        Logger.i("i = " + i);
    }

    //  3
    @Override
    public void initUI(View view) {
        showContent(true);
        mFragment1Presenter.attachView(this);

        data = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mFragment1Adapter = new Fragment1Adapter(getActivity(), data);
        mRecyclerView.setAdapter(mFragment1Adapter);

        // header
        final RentalsSunHeaderView header = new RentalsSunHeaderView(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setUp(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1500);
        //这个在my
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.setPtrHandler(new PtrHandler() {
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
            public void onRefreshBegin(final PtrFrameLayout frame) {
                mFragment1Presenter.onThreadReceive();
            }
        });

    }

    @Override
    public void initData() {
        frame.autoRefresh(true);
    }

    @Override
    public void onRefreshCompleted(List<String> datas) {
        data.addAll(datas);
        mFragment1Adapter.notifyDataSetChanged();
        frame.refreshComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragment1Presenter.detachView();
    }
}
