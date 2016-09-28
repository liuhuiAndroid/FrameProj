package com.lh.frameproj.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.frameproj.injector.HasComponent;

import butterknife.ButterKnife;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public abstract class BaseFragment extends Fragment{

    protected View rootView;
    protected BaseActivity baseActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(container.getContext()).inflate(initContentView(), null);
        ButterKnife.bind(this, rootView);

        if(rootView != null){
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        if (getActivity() instanceof BaseActivity) {
            baseActivity = (BaseActivity) getActivity();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initInjector();
        getBundle(getArguments());
        initUI(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    public void openActivity(Class<?> cls) {
        startActivity(new Intent(baseActivity, cls));
    }

    public abstract void initInjector();

    public abstract int initContentView();

    /**
     * 得到Activity传进来的值
     */
    public abstract void getBundle(Bundle bundle);

    /**
     * 初始化控件
     */
    public abstract void initUI(View view);

    /**
     * 在监听器之前把数据准备好
     */
    public abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressWarnings("unchecked") protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
