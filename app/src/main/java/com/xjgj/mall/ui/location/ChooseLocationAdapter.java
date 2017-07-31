package com.xjgj.mall.ui.location;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.frameproj.library.util.log.Logger;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.GeoCoderResultEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we-win on 2017/7/31.
 */

public class ChooseLocationAdapter extends RecyclerView.Adapter<ChooseLocationAdapter.ChooseLocationViewHolder> {


    private Context context;
    private List<GeoCoderResultEntity.ResultBean.PoisBean> mPoisBeen;

    public ChooseLocationAdapter(Context context, List<GeoCoderResultEntity.ResultBean.PoisBean> poisBeanList) {
        this.context = context;
        this.mPoisBeen = poisBeanList;
    }

    @Override
    public ChooseLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_aroundmap, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ChooseLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseLocationViewHolder holder, int position) {
        Logger.i("test onBindViewHolder");
        GeoCoderResultEntity.ResultBean.PoisBean poisBean = mPoisBeen.get(position);
        holder.mItemAroundmapName.setText(poisBean.getName());
        holder.mItemAroundmapAddress.setText(poisBean.getAddr());
        if (position == 0) {
            holder.mItemAroundmapIcon.setVisibility(View.VISIBLE);
            holder.mItemAroundmapName.setTextColor(Color.RED);
        } else {
            holder.mItemAroundmapIcon.setVisibility(View.GONE);
            holder.mItemAroundmapName.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        if (mPoisBeen == null) {
            return 0;
        } else {
            return mPoisBeen.size();
        }
    }

    class ChooseLocationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_aroundmap_icon)
        ImageView mItemAroundmapIcon;
        @BindView(R.id.item_aroundmap_name)
        TextView mItemAroundmapName;
        @BindView(R.id.item_aroundmap_address)
        TextView mItemAroundmapAddress;

        public ChooseLocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
