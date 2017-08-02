package com.xjgj.mall.ui.location;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xjgj.mall.R;
import com.xjgj.mall.bean.GeoCoderResultEntity;

import java.util.List;

/**
 * Created by we-win on 2017/8/2.
 */

public class ChooseLocationListAdapter extends BaseAdapter {


    private ChooseLocationActivity mContext;
    private List<GeoCoderResultEntity.ResultBean.PoisBean> mRecyclerViewGeoCoderData;

    public ChooseLocationListAdapter(ChooseLocationActivity context, List<GeoCoderResultEntity.ResultBean.PoisBean> recyclerViewGeoCoderData) {

        mContext = context;
        mRecyclerViewGeoCoderData = recyclerViewGeoCoderData;
    }

    @Override
    public int getCount() {
        return mRecyclerViewGeoCoderData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mContext.getLayoutInflater().inflate(
                    R.layout.item_aroundmap, null);
            holder = new ViewHolder();
            holder.item_aroundmap_icon = (ImageView) convertView
                    .findViewById(R.id.item_aroundmap_icon);
            holder.item_aroundmap_name = (TextView) convertView
                    .findViewById(R.id.item_aroundmap_name);
            holder.item_aroundmap_address = (TextView) convertView
                    .findViewById(R.id.item_aroundmap_address);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GeoCoderResultEntity.ResultBean.PoisBean poisBean = mRecyclerViewGeoCoderData.get(position);
        holder.item_aroundmap_name.setText(poisBean.getName());
        holder.item_aroundmap_address.setText(poisBean.getAddr());
        if (position == 0) {
            holder.item_aroundmap_icon.setVisibility(View.VISIBLE);
            holder.item_aroundmap_name.setTextColor(Color.RED);
        } else {
            holder.item_aroundmap_icon.setVisibility(View.GONE);
            holder.item_aroundmap_name.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView item_aroundmap_icon;
        TextView item_aroundmap_name;
        TextView item_aroundmap_address;
    }
}
