package com.xjgj.mall.ui.fragment4;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.xjgj.mall.bean.CarTypeEntity;

import java.util.List;

/**
 * Created by we-win on 2017/7/26.
 */

public class VehiclePagerAdapter extends PagerAdapter {

    private ViewPager mViewPager;

    private List<CarTypeEntity> mCarTypeEntities;

    public VehiclePagerAdapter(ViewPager viewPager, List<CarTypeEntity> carTypeEntities) {
        mViewPager = viewPager;
        mCarTypeEntities = carTypeEntities;
    }



    @Override
    public int getCount() {
        return mCarTypeEntities.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CarTypeEntity carTypeEntity = mCarTypeEntities.get(position%mCarTypeEntities.size());
        ImageView imageView = new ImageView(mViewPager.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageLoaderUtil.getInstance().loadImage(carTypeEntity.getPhotoPath(),imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCarTypeEntities.get(position%mCarTypeEntities.size()).getCarTypeName();
    }

    public List<CarTypeEntity> getCarTypeEntities() {
        return mCarTypeEntities;
    }
}