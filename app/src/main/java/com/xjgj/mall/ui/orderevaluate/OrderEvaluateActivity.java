package com.xjgj.mall.ui.orderevaluate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.util.log.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.widget.StarBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.xjgj.mall.R.id.textPendingEvaluation;


/**
 * 订单评价
 */

public class OrderEvaluateActivity extends BaseActivity implements OrderEvaluateContract.View {


    @BindView(R.id.image_back)
    ImageView mImageBack;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.image_handle)
    ImageView mImageHandle;
    @BindView(R.id.text_handle)
    TextView mTextHandle;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.linearClude)
    LinearLayout mLinearClude;
    @BindView(R.id.imageHeader)
    CircleImageView mImageHeader;
    @BindView(R.id.imageCallPhone)
    ImageView mImageCallPhone;
    @BindView(R.id.textUserName)
    TextView mTextUserName;
    @BindView(R.id.textUserPhone)
    TextView mTextUserPhone;
    @BindView(R.id.textFwTimes)
    TextView mTextFwTimes;
    @BindView(R.id.textPaiZhao)
    TextView mTextPaiZhao;
    @BindView(R.id.starBar)
    StarBar mStarBar;
    @BindView(R.id.userInfoLayout)
    RelativeLayout mUserInfoLayout;
    @BindView(R.id.starBarShow)
    StarBar mStarBarShow;
    @BindView(R.id.editShaoHua)
    EditText mEditShaoHua;
    @BindView(R.id.textLimit)
    TextView mTextLimit;
    @BindView(R.id.textPendingEvaluation)
    TextView mTextPendingEvaluation;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.avLoadingIndicatorView)
    AVLoadingIndicatorView mAvLoadingIndicatorView;
    private int maxLength = 0;
    private int length = 60;

    @Inject
    OrderEvaluatePresenter mPresenter;
    private int mOrderId;

    @Override
    public int initContentView() {
        return R.layout.activity_order_evaluate;
    }

    @Override
    public void initInjector() {
        DaggerOrderEvaluateComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {

        mOrderId = getIntent().getIntExtra("orderId",-1);
        String contactName = getIntent().getStringExtra("contactName");
        final String contactMobile = getIntent().getStringExtra("contactMobile");
        String avatarUrl = getIntent().getStringExtra("avatarUrl");
        String carNo = getIntent().getStringExtra("carNo");
        float starLevel = getIntent().getFloatExtra("starLevel",0);
        ImageLoaderUtil.getInstance().loadImage(avatarUrl, mImageHeader);
        mTextUserName.setText(contactName);
        mImageCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactMobile));
                startActivity(intent);
            }
        });
        mTextPaiZhao.setText(carNo);
        mTextUserPhone.setText(contactMobile);
        mStarBar.setStarMark(starLevel);

        mPresenter.attachView(this);
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("订单评价");
        mAvLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");

        mStarBar.setCanTouch(false);
        mStarBarShow.setCanTouch(true);
        mStarBarShow.setIntegerMark(true);
        mEditShaoHua.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0) {

                    try {
                        maxLength = s.toString().length();
                        if (maxLength > length) {
                            s.delete(length, str.length());
                        } else {
                            if (mTextLimit != null) {

                                if (length == 60) {
                                    if (maxLength < length) {
                                        mTextLimit.setText(maxLength + "/" + length);
                                    } else {
                                        SpannableString msp = new SpannableString("60/60");
                                        msp.setSpan(new ForegroundColorSpan
                                                (getResources().getColor(R.color.red)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
                                        mTextLimit.setText(msp);
                                    }
                                }


                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mTextLimit != null) {
                        if (length == 60) {
                            mTextLimit.setText("0/60");
                        }

                    }

                }

            }

        });

        mTextLimit.setText("0/60");
    }


    @Override
    public void showLoading() {
        mAvLoadingIndicatorView.show();
        mTextPendingEvaluation.setClickable(false);
    }

    @Override
    public void hideLoading() {
        Logger.i("hideLoading");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAvLoadingIndicatorView.hide();
                mTextPendingEvaluation.setClickable(true);
            }
        }, 500);
    }

    @Override
    public void orderCommentSuccess() {
        finish();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    /**
     * 评价
     */
    @OnClick(textPendingEvaluation)
    public void mTextPendingEvaluation() {
        String starCount = mStarBarShow.getStarMark() + "";
        String comment = mEditShaoHua.getText().toString().trim();
        mPresenter.orderComment(mOrderId,starCount,comment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
