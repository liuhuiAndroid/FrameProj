package com.xjgj.mall.ui.businesslicence;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.util.log.Logger;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wang.avi.AVLoadingIndicatorView;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.R.id.image_back;
import static com.xjgj.mall.R.id.textState;

/**
 * 营业执照
 */

public class BusinessLicenceActivity extends BaseActivity implements BusinessLicenceContract.View {

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

    @Inject
    BusinessLicencePresenter mPresenter;
    @BindView(R.id.avLoadingIndicatorView)
    AVLoadingIndicatorView mAvLoadingIndicatorView;

    /**
     * 选择图片返回的图片信息
     */
    private List<LocalMedia> selectList = new ArrayList<>();
    private String mCompressPath;

    @Override
    public int initContentView() {
        return R.layout.activity_business_licence;
    }

    @Override
    public void initInjector() {
        DaggerBusinessLicenceComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {

        mPresenter.attachView(this);
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
        mAvLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");


    }

    /**
     * 选择图片
     */
    @OnClick(R.id.imageCard)
    public void mImageCard() {
        PictureSelector.create(BusinessLicenceActivity.this)
                .openGallery(PictureMimeType.ofImage()) //图片
                .theme(R.style.picture_default_style) // 主题样式
                .maxSelectNum(1) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(true)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        mCompressPath = localMedia.getCompressPath();
                        ImageLoaderUtil.getInstance().loadImage(localMedia.getCompressPath(), mImageCard);

                        Logger.i("localMedia.getPath() = " + localMedia.getPath());
                        Logger.i("localMedia.getCompressPath() = " + localMedia.getCompressPath());
                    }
                    break;
            }
        }
    }

    /**
     * 上传图片
     */
    @OnClick(R.id.textUpload)
    public void mTextUpload() {
        mPresenter.photoUpload(mCompressPath, 2);
    }

    @Override
    public void showLoading() {
        mAvLoadingIndicatorView.show();
        mTextUpload.setClickable(false);
    }

    @Override
    public void hideLoading() {
        Logger.i("hideLoading");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAvLoadingIndicatorView.hide();
                mTextUpload.setClickable(true);
            }
        },500);
    }

    @Override
    public void photoUploadSuccess() {
        ToastUtil.showToast("营业执照上传成功");
        finish();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
