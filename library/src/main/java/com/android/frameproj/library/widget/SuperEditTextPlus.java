package com.android.frameproj.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.frameproj.library.R;

/**
 * Created by we-win on 2017/7/26.
 */

public class SuperEditTextPlus extends LinearLayout {
    private String bottomString = "";
    private TextView bottomText;
    private View bottomTextContainer;
    private Context context;
    private String hintText = "";
    private LinearLayout leftBtn;
    private View mRootView;
    private String middleString = "";
    private TextView middleText;
    private View middleTextContainer;
    private ImageView rightBtn;
    private SuperEditTextListener superEditTextListener;
    private String topString = "";
    private TextView topText;
    private View topTextContainer;

    public SuperEditTextPlus(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initView(paramContext);
    }

    public SuperEditTextPlus(Context paramContext, String paramString) {
        super(paramContext);
        this.hintText = paramString;
        initView(paramContext);
    }

    public void checkString() {

        if ("".equals(this.topString.trim())) {
            this.topTextContainer.setVisibility(View.GONE);
        } else {
            this.topTextContainer.setVisibility(View.VISIBLE);
        }

        if (!"".equals(this.middleString.trim())) {
            this.middleTextContainer.setVisibility(View.GONE);
        } else {
            this.middleTextContainer.setVisibility(View.GONE);
        }

        if (!"".equals(this.bottomString.trim())) {
            this.bottomTextContainer.setVisibility(View.GONE);
        } else {
            this.bottomTextContainer.setVisibility(View.GONE);
        }

    }

    private void initView(Context paramContext) {
        this.context = paramContext;
        this.mRootView = this;
        View localView = View.inflate(paramContext, R.layout.super_edittext_plus, null);
        this.topText = ((TextView) localView.findViewById(R.id.top_text));
        this.middleText = ((TextView) localView.findViewById(R.id.middle_text));
        this.bottomText = ((TextView) localView.findViewById(R.id.bottom_text));

        this.rightBtn = ((ImageView) localView.findViewById(R.id.iv_right_btn));
        this.leftBtn = ((LinearLayout) localView.findViewById(R.id.left_btn));

        this.middleTextContainer = localView.findViewById(R.id.middle_text_container);
        this.topTextContainer = localView.findViewById(R.id.top_text_container);
        this.bottomTextContainer = localView.findViewById(R.id.bottom_text_container);

        this.leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (SuperEditTextPlus.this.superEditTextListener != null)
                    SuperEditTextPlus.this.superEditTextListener.onLeftBtnClicked(SuperEditTextPlus.this.mRootView);
            }
        });
        this.rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (SuperEditTextPlus.this.superEditTextListener != null)
                    SuperEditTextPlus.this.superEditTextListener.onRightBtnClicked(SuperEditTextPlus.this.mRootView);
            }
        });
        addView(localView);
    }

    private void setTopText(String paramString, boolean paramBoolean) {
        if (paramBoolean) {
            this.topText.setText(paramString);
            this.topText.setTextColor(getResources().getColor(R.color.md_grey_500));
            return;
        }
        setTopText(paramString);
    }

    public boolean getTextState() {
        checkString();
        return false;
    }

    public void setBottomText(String paramString1, String paramString2) {
        if (this.bottomText != null) {
            this.bottomString = (paramString1 + paramString2).trim();
            this.bottomText.setText(this.bottomString);
            getTextState();
        }
    }

    public void setHintText(String paramString) {
        this.hintText = paramString;
        setTopText(this.hintText, true);
        this.topTextContainer.setVisibility(View.VISIBLE);
        this.middleTextContainer.setVisibility(View.GONE);
        this.bottomTextContainer.setVisibility(View.GONE);
    }

    public void setListener(SuperEditTextListener paramSuperEditTextListener) {
        this.superEditTextListener = paramSuperEditTextListener;
    }

    public void setMiddleText(String paramString) {
        if (this.middleText != null) {
            this.middleString = paramString;
            this.middleText.setText(this.middleString);
            getTextState();
        }
    }

    public void setRightBtnIcon(int paramInt) {
        if (this.rightBtn != null)
            this.rightBtn.setImageResource(paramInt);
    }

    public void setTopText(String paramString) {
        if (this.topText != null) {
            this.topString = paramString;
            this.topText.setText(this.topString);
            this.topText.setTextColor(Color.parseColor("#1d1d1d"));
            getTextState();
        }
    }

    public static abstract interface SuperEditTextListener {

        public abstract void onLeftBtnClicked(View paramView);

        public abstract void onRightBtnClicked(View paramView);

    }
}