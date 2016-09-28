package com.lh.frameproj.ui.fragment1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lh.frameproj.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WE-WIN-027 on 2016/8/13.
 *
 * @des ${TODO}
 */
public class Fragment1Adapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> mStrings;

    public Fragment1Adapter(Context context, List<String> stringList) {
        this.context = context;
        this.mStrings = stringList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_fragment1, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new CalculateDetailTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalculateDetailTitleViewHolder) {
            ((CalculateDetailTitleViewHolder) holder).mTvTime.setText(mStrings.get(position));
            ((CalculateDetailTitleViewHolder) holder).mTvTitle.setText("title");
            ((CalculateDetailTitleViewHolder) holder).mTvContent.setText("content");
        }
    }


    @Override
    public int getItemCount() {
        if (mStrings == null) {
            return 0;
        }
        return mStrings.size();
    }


    class CalculateDetailTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        public CalculateDetailTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
