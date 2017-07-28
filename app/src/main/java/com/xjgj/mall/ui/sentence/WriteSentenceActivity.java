package com.xjgj.mall.ui.sentence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.CommonUtils;
import com.xjgj.mall.Constants;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by we-win on 2017/7/27.
 * 给司机捎句话
 */

public class WriteSentenceActivity extends BaseActivity {

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
    @BindView(R.id.editShaoHua)
    EditText mEditShaoHua;
    @BindView(R.id.textLimit)
    TextView mTextLimit;
    @BindView(R.id.textOk)
    TextView mTextOk;

    private int po = 0;
    private int maxLength = 0;
    private int length = 60;
    //内容
    private String sentence;

    @Override
    public int initContentView() {
        return R.layout.activity_write_sentence;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        getData();
        operateView();
    }

    private void getData() {
        sentence = getIntent().getStringExtra("sentence");
    }

    private void operateView() {
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinish();
            }
        });
        mTextTitle.setText(getResources().getString(R.string.shao_hua));
        mEditShaoHua.setText(sentence);

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
                po = 1;
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
        if (TextUtils.isEmpty(sentence)) {
            mTextLimit.setText("0/60");
        } else {
            int len = sentence.length();
            if (len < 60) {
                mTextLimit.setText(len + "/60");
            } else {
                SpannableString msp = new SpannableString("60/60");
                msp.setSpan(new ForegroundColorSpan
                        (getResources().getColor(R.color.red)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
                mTextLimit.setText(msp);
            }
        }
    }

    /**
     * 保存数据退出
     */
    public void exit(View view) {
        CommonUtils.showSoftInput(this, mEditShaoHua);
        Intent intent = new Intent();
        intent.putExtra("sentence", mEditShaoHua.getText().toString().trim());
        setResult(Constants.RESULT_WRITE_SENTENCE_CODE, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        isFinish();
    }

    private void isFinish() {
        CommonUtils.showSoftInput(this, mEditShaoHua);
        if (po == 0) {
            finish();
        } else {
            new MaterialDialog.Builder(WriteSentenceActivity.this)
                    .title("退出")
                    .content("是否放弃保存已输入的信息？")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        }

    }


}
