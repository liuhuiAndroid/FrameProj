package com.android.frameproj.library.statefullayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.frameproj.library.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * Created by lh on 2017/8/8.
 */

public class StatefulLayout extends FrameLayout {

    private static final String TAG_LOADING = "ProgressActivity.TAG_LOADING";
    private static final String TAG_EMPTY = "ProgressActivity.TAG_EMPTY";
    private static final String TAG_ERROR = "ProgressActivity.TAG_ERROR";

    //四种状态
    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    LayoutInflater inflater;
    View view;
    LayoutParams layoutParams;

    List<View> contentViews = new ArrayList<>();

    //加载中页面
    FrameLayout loadingStateFrameLayout;
    ProgressBar loadingStateProgressBar;
    //空页面
    FrameLayout emptyStateFrameLayout;
    ImageView emptyStateImageView;
    TextView emptyStateTitleTextView;
    TextView emptyStateContentTextView;
    //错误页面
    FrameLayout errorStateFrameLayout;
    ImageView errorStateImageView;
    TextView errorStateTitleTextView;
    TextView errorStateContentTextView;
    Button errorStateButton;

    //当前的状态
    private String state = CONTENT;

    public StatefulLayout(Context context) {
        super(context);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatefulLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 重写addView方法防止重复加载
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {
            contentViews.add(child);
        }
    }

    public void showContent() {
        switchState(CONTENT, null);
    }

    public void showLoading() {
        switchState(LOADING, null);
    }

    public void showEmpty(StatusfulConfig statusfulConfig) {
        switchState(EMPTY, statusfulConfig);
    }

    public void showError(StatusfulConfig statusfulConfig) {
        switchState(ERROR, statusfulConfig);
    }

    private void switchState(String state, StatusfulConfig statusfulConfig) {
        this.state = state;

        switch (state) {
            case CONTENT:
                setContentState();
                break;
            case LOADING:
                setLoadingState();
                break;
            case EMPTY:
                setEmptyState(statusfulConfig);
                break;
            case ERROR:
                setErrorState(statusfulConfig);
                break;
        }
    }

    private void setLoadingView() {
        if (loadingStateFrameLayout == null) {
            view = inflater.inflate(R.layout.stateful_layout_loading_view, null);
            loadingStateFrameLayout = (FrameLayout) view.findViewById(R.id.frame_layout_progress);
            loadingStateFrameLayout.setTag(TAG_LOADING);

            loadingStateProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_loading);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = CENTER_IN_PARENT;

            addView(loadingStateFrameLayout, layoutParams);
        } else {
            loadingStateFrameLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView(StatusfulConfig statusfulConfig) {
        if (emptyStateFrameLayout == null) {
            view = inflater.inflate(R.layout.stateful_layout_empty_view, null);
            emptyStateFrameLayout = (FrameLayout) view.findViewById(R.id.frame_layout_empty);
            emptyStateFrameLayout.setTag(TAG_EMPTY);

            emptyStateImageView = (ImageView) view.findViewById(R.id.image_icon);
            emptyStateTitleTextView = (TextView) view.findViewById(R.id.text_title);
            emptyStateContentTextView = (TextView) view.findViewById(R.id.text_content);

            //设置属性
            emptyStateImageView.setImageResource(R.drawable.stateful_layout_ic_empty);
            if (statusfulConfig != null) {

                if (statusfulConfig.getEmptyImageResId() != 0) {
                    emptyStateImageView.setImageResource(statusfulConfig.getEmptyImageResId());
                }

                if (statusfulConfig.getEmptyTextTitle() != null) {
                    emptyStateTitleTextView.setText(statusfulConfig.getEmptyTextTitle());
                }

                if (statusfulConfig.getEmptyTextContent() != null) {
                    emptyStateContentTextView.setText(statusfulConfig.getEmptyTextContent());
                }

            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = CENTER_IN_PARENT;

            addView(emptyStateFrameLayout, layoutParams);
        } else {
            emptyStateFrameLayout.setVisibility(VISIBLE);
        }
    }

    private void setErrorView(StatusfulConfig statusfulConfig) {
        if (errorStateFrameLayout == null) {
            view = inflater.inflate(R.layout.stateful_layout_error_view, null);
            errorStateFrameLayout = (FrameLayout) view.findViewById(R.id.frame_layout_error);
            errorStateFrameLayout.setTag(TAG_ERROR);

            errorStateImageView = (ImageView) view.findViewById(R.id.image_icon);
            errorStateTitleTextView = (TextView) view.findViewById(R.id.text_title);
            errorStateContentTextView = (TextView) view.findViewById(R.id.text_content);
            errorStateButton = (Button) view.findViewById(R.id.button_retry);

            //设置属性
            errorStateImageView.setImageResource(R.drawable.stateful_layout_ic_error);
            if (statusfulConfig != null) {

                if (statusfulConfig.getErrorImageResId() != 0) {
                    errorStateImageView.setImageResource(statusfulConfig.getErrorImageResId());
                }

                if (statusfulConfig.getErrorTextTitle() != null) {
                    errorStateTitleTextView.setText(statusfulConfig.getErrorTextTitle());
                }

                if (statusfulConfig.getErrorTextContent() != null) {
                    errorStateContentTextView.setText(statusfulConfig.getErrorTextContent());
                }

                if (statusfulConfig.getErrorButtonText() != null) {
                    errorStateButton.setText(statusfulConfig.getErrorButtonText());
                }

                if (statusfulConfig.getOnErrorStateButtonClickListener() != null) {
                    errorStateButton.setOnClickListener(statusfulConfig.getOnErrorStateButtonClickListener());
                }
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = CENTER_IN_PARENT;

            addView(errorStateFrameLayout, layoutParams);
        } else {
            errorStateFrameLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * Helpers
     */

    private void setContentState() {
        hideLoadingView();
        hideEmptyView();
        hideErrorView();

        setContentVisibility(true);
    }

    private void setLoadingState() {
        hideEmptyView();
        hideErrorView();

        setLoadingView();
        setContentVisibility(false);
    }

    private void setEmptyState(StatusfulConfig statusfulConfig) {
        hideLoadingView();
        hideErrorView();

        setEmptyView(statusfulConfig);
        setContentVisibility(false);
    }

    private void setErrorState(StatusfulConfig statusfulConfig) {
        hideLoadingView();
        hideEmptyView();

        setErrorView(statusfulConfig);
        setContentVisibility(false);
    }

    private void setContentVisibility(boolean visible) {
        for (View v : contentViews) {
            v.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void hideLoadingView() {
        if (loadingStateFrameLayout != null) {
            loadingStateFrameLayout.setVisibility(GONE);
        }
    }

    private void hideEmptyView() {
        if (emptyStateFrameLayout != null) {
            emptyStateFrameLayout.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (errorStateFrameLayout != null) {
            errorStateFrameLayout.setVisibility(GONE);
        }
    }

}
