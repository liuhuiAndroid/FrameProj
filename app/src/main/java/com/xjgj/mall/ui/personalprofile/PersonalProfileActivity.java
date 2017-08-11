package com.xjgj.mall.ui.personalprofile;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.statefullayout.StatefulLayout;
import com.android.frameproj.library.statefullayout.StatusfulConfig;
import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.util.log.Logger;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.User;
import com.xjgj.mall.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2017/8/9.
 * 个人资料
 */

public class PersonalProfileActivity extends BaseActivity implements PersonalProfileContract.View, View.OnClickListener {

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
    @BindView(R.id.tou)
    TextView mTou;
    @BindView(R.id.image_header_photo)
    ImageView mImageHeaderPhoto;
    @BindView(R.id.text_ni_cheng)
    EditText mTextNiCheng;
    @BindView(R.id.text_sex)
    TextView mTextSex;
    @BindView(R.id.text_phone)
    TextView mTextPhone;
    @BindView(R.id.text_d_name)
    EditText mTextDName;
    @BindView(R.id.text_address)
    EditText mTextAddress;
    @BindView(R.id.text_p_number)
    EditText mTextPNumber;

    @Inject
    PersonalProfilePresenter mPresenter;
    @BindView(R.id.statefulLayout)
    StatefulLayout mStatefulLayout;

    @Override
    public int initContentView() {
        return R.layout.activity_personal_profile;
    }

    @Override
    public void initInjector() {
        DaggerPersonalProfileComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mTextTitle.setText("编辑个人资料");
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextHandle.setText("保存");
        mTextHandle.setTextSize(14);
        mTextHandle.setTextColor(getResources().getColor(R.color.z5b5b5b));
        mTextHandle.setClickable(true);
        mTextHandle.setOnClickListener(this);

        mPresenter.mallInformation();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoadingContent() {
        mStatefulLayout.showLoading();
        mTextHandle.setVisibility(View.GONE);
    }

    @Override
    public void hideLoadingContent(int type) {
        // type -1:失败 0：成功
        if (type == -1) {
            StatusfulConfig statusfulConfig = new StatusfulConfig.Builder()
                    .setOnErrorStateButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.mallInformation();
                        }
                    }).build();
            mStatefulLayout.showError(statusfulConfig);
            mTextHandle.setVisibility(View.GONE);
        } else if (type == 0) {
            mStatefulLayout.showContent();
            mTextHandle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void mallInfoCompleteSuccess(String s) {
        ToastUtil.showToast(s);
    }


    @Override
    public void mallInformationSuccess(User user) {
        ImageLoaderUtil.getInstance().loadCircleImage(user.getAvatarUrl(), R.drawable.header, mImageHeaderPhoto);
        mTextNiCheng.setText(user.getRealName());
        if (user.getSex() == 1) {
            mTextSex.setText("男");
        } else if (user.getSex() == 0) {
            mTextSex.setText("女");
        } else {
            mTextSex.setText("未填");
        }

        mTextPhone.setText(user.getMobile());
        mTextDName.setText(user.getCompanyName());
        mTextAddress.setText(user.getAddress());
        mTextPNumber.setText(user.getBerth());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存
            case R.id.text_handle:
                // String realName, int sex, String address, String companyName,
                // String berth, String headIcon, String birthDay
                String nickname = mTextNiCheng.getText().toString().trim();
                String sex = mTextSex.getText().toString().trim();
                String phoneNum = mTextPhone.getText().toString().trim();
                String companyName = mTextDName.getText().toString().trim();
                String address = mTextAddress.getText().toString().trim();
                String pNumber = mTextPNumber.getText().toString().trim();
                int intSex = 0;
                if (sex.equals("男")) {
                    intSex = 1;
                } else if (sex.equals("女")) {
                    intSex = 0;
                } else {
                    intSex = -1;
                }
                mPresenter.mallInfoComplete(nickname, intSex, address, companyName, pNumber, mCompressPath, null);
                break;
        }
    }

    /**
     * 修改头像
     */
    public void headIcon(View view) {
        PictureSelector.create(PersonalProfileActivity.this)
                .openGallery(PictureMimeType.ofImage()) //图片
                .theme(R.style.picture_default_style) // 主题样式
                .maxSelectNum(1) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .compressGrade(Luban.FIRST_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
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

    /**
     * 选择图片返回的图片信息
     */
    private List<LocalMedia> selectList = new ArrayList<>();
    private String mCompressPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        final LocalMedia localMedia = selectList.get(0);
                        mCompressPath = localMedia.getCompressPath();
                        ImageLoaderUtil.getInstance().loadCircleImage(localMedia.getCompressPath(), R.drawable.header, mImageHeaderPhoto);
                        Logger.i("localMedia.getPath() = " + localMedia.getPath());
                        Logger.i("localMedia.getCompressPath() = " + localMedia.getCompressPath());
                    }
                    break;
            }
        }
    }

    /**
     * 修改昵称
     */
    public void realName(View view) {

    }

    /**
     * 修改性别
     */
    public void sex(View view) {
        final List<String> typeList = new ArrayList<>();
        typeList.add("男");
        typeList.add("女");
        new MaterialDialog.Builder(PersonalProfileActivity.this)
                .items(typeList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String sex = typeList.get(which);
                        mTextSex.setText(sex);
                    }
                })
                .show();
    }

    /**
     * 修改手机号
     */
    public void phone(View view) {

    }

    /**
     * 修改单位
     */
    public void company(View view) {

    }

    /**
     * 修改地址
     */
    public void address(View view) {

    }

    /**
     * 修改铺位号
     */
    public void pNumber(View view) {

    }

}
