package com.xjgj.mall.ui.businesslicence;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.R.id.image_back;
import static com.xjgj.mall.R.id.textState;

/**
 * 营业执照
 */

public class BusinessLicenceActivity extends BaseActivity {

    @BindView(image_back)
    ImageView mImageBack;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.image_handle)
    ImageView mImageHandle;
    @BindView(R.id.text_handle)
    TextView mTextHandle;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(textState)
    TextView mTextState;
    @BindView(R.id.imageCard)
    ImageView mImageCard;
    @BindView(R.id.textUpload)
    TextView mTextUpload;

    @Override
    public int initContentView() {
        return R.layout.activity_business_licence;
    }

    @Override
    public void initInjector() {
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("营业执照");
        mTextState.setText("营业执照扫描件照片");
    }

    @Override
    public void initUiAndListener() {

    }

    /**
     * 选择图片
     */
    @OnClick(R.id.imageCard)
    public void mImageCard(){

    }

    /**
     * 上传图片
     */
    @OnClick(R.id.textUpload)
    public void mTextUpload(){

    }

}
