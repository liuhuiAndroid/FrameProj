package com.lh.frameproj.ui.fragment2.order_working;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we-win on 2017/7/20.
 */

public class OrderWorkingFragement extends BaseFragment {
    @BindView(R.id.imageView)
    ImageView mImageView;

    @Override
    public void initInjector() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_order_working;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {

    }

    @Override
    public void initData() {
        ImageLoaderUtil.getInstance().loadImage("http://image.sports.baofeng.com/25a3dbb0c99c5e48e52e60941ed230be", R.mipmap.ic_launcher, mImageView);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
