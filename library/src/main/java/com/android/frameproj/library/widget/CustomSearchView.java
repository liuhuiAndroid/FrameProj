package com.android.frameproj.library.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.frameproj.library.R;

/**
 * Created by we-win on 2017/7/28.
 */

public class CustomSearchView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private ImageView btn_function_btn;
    private EditText et_search_content;
    private CustomSearchViewListener listener;
    private LinearLayout llBack;
    /**
     * 查询内容
     */
    private String queryText;
    private LinearLayout rl_right_btn;
    private TextView tv_function_text;
    private boolean mEditTextFocus;

    public CustomSearchView(Context paramContext) {
        super(paramContext);
        initView(paramContext);
    }

    public CustomSearchView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initView(paramContext);
    }

    private void initView(Context paramContext) {
        View localView = View.inflate(paramContext, R.layout.custom_search_view, null);
        this.et_search_content = ((EditText) localView.findViewById(R.id.et_search_content));
        this.llBack = ((LinearLayout) localView.findViewById(R.id.ll_back));
        this.btn_function_btn = ((ImageView) localView.findViewById(R.id.btn_search_functionBtn));
        this.tv_function_text = ((TextView) localView.findViewById(R.id.tv_search_functionTv));
        this.rl_right_btn = ((LinearLayout) localView.findViewById(R.id.rl_right_btn));
        this.llBack.setOnClickListener(this);
        this.btn_function_btn.setOnClickListener(this);
        this.rl_right_btn.setOnClickListener(this);
        this.et_search_content.setOnClickListener(this);
        this.et_search_content.addTextChangedListener(this);
        addView(localView);
    }

    public void onClick(View view) {

        if (view.getId() == R.id.btn_search_functionBtn) {
            this.et_search_content.setText("");
        } else if (view.getId() == R.id.rl_right_btn) {
            if (this.listener != null) {
                this.listener.onRightButtonClicked();
            }
        } else if (view.getId() == R.id.et_search_content) {
            if (this.listener != null) {
                this.listener.onEditTextClicked();
            }
        } else if (view.getId() == R.id.ll_back) {
            if (this.listener != null) {
                this.listener.onBackButtonClicked();
            }
        }
    }

    public void setListener(CustomSearchViewListener paramCustomSearchViewListener) {
        this.listener = paramCustomSearchViewListener;
    }

    public void editTextRequestFocus() {
        if (this.et_search_content != null) {
            this.et_search_content.setFocusable(true);
            this.et_search_content.setFocusableInTouchMode(true);
            this.et_search_content.requestFocus();
            ((InputMethodManager) this.et_search_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this.et_search_content, 1);
        }
    }

    // =============== TextWatcher的三个回调
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        this.queryText = paramCharSequence.toString();
        if (!TextUtils.isEmpty(this.queryText)) {
            this.btn_function_btn.setVisibility(View.VISIBLE);
        } else {
            this.btn_function_btn.setVisibility(View.GONE);
        }
        if (this.listener != null) {
            this.listener.onQueryChanged(this.queryText, paramInt1, paramInt2, paramInt3);
        }

    }

    public void afterTextChanged(Editable paramEditable) {//表示最终内容
        if (this.listener != null) {
            this.listener.afterTextChanged(paramEditable);
        }
    }

    public void setCityName(String cityName) {
        this.tv_function_text.setText(cityName);
    }

    public void setEditTextFocus(boolean focus) {
        if (this.et_search_content != null) {
            this.et_search_content.setFocusable(focus);
            this.et_search_content.setFocusableInTouchMode(focus);
        }
    }

    public void setEditTextContent(String content) {
        if (this.et_search_content != null) {
            this.et_search_content.setText(content);
        }
    }

    /**
     * 清除数据
     */
    public void clearQueryContent() {
        if (this.et_search_content != null)
            this.et_search_content.setText("");
    }

    public static abstract interface CustomSearchViewListener {

        /**
         * 返回按钮
         */
        public abstract void onBackButtonClicked();

        public abstract void onEditTextClicked();

        /**
         * 选择城市
         */
        public abstract void onRightButtonClicked();

        /**
         * 文字改变
         */
        public abstract void onQueryChanged(String paramString, int paramInt1, int paramInt2, int paramInt3);

        /**
         * 文字最终内容
         */
        public abstract void afterTextChanged(Editable paramEditable);

    }
}