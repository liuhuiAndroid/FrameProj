package com.xjgj.mall.ui.fragment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.imageloader.ImageLoaderUtil;
import com.android.frameproj.library.util.log.Logger;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.HomepageEntity;
import com.xjgj.mall.components.storage.UserStorage;
import com.xjgj.mall.ui.BaseFragment;
import com.xjgj.mall.ui.businesslicence.BusinessLicenceActivity;
import com.xjgj.mall.ui.certification.CertificationActivity;
import com.xjgj.mall.ui.main.MainComponent;
import com.xjgj.mall.ui.personalprofile.PersonalProfileActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.REQUEST_BUSINESS_LICENCE_CODE;
import static com.xjgj.mall.Constants.REQUEST_CERTIFICATION_CODE;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment3 extends BaseFragment implements Fragment3Contract.View {

    @Inject
    Fragment3Presenter mPresenter;
    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.roundImageView)
    ImageView mRoundImageView;
    @BindView(R.id.textName)
    TextView mTextName;
    @BindView(R.id.imageSex)
    ImageView mImageSex;
    @BindView(R.id.textPhone)
    TextView mTextPhone;
    @BindView(R.id.textShopName)
    TextView mTextShopName;
    @BindView(R.id.textShopAddress)
    TextView mTextShopAddress;
    @BindView(R.id.relativePersonal)
    RelativeLayout mRelativePersonal;
    @BindView(R.id.text_y_y_z_z)
    TextView mTextYYZZ;
    @BindView(R.id.text_yyzz_state)
    TextView mTextYyzzState;
    @BindView(R.id.relativeGoYyzz)
    LinearLayout mRelativeGoYyzz;
    @BindView(R.id.text_s_m_r_z)
    TextView mTextSMRZ;
    @BindView(R.id.text_smrz_state)
    TextView mTextSmrzState;
    @BindView(R.id.relativeGoSmrr)
    LinearLayout mRelativeGoSmrr;
    @BindView(R.id.btn_exit)
    Button mBtnExit;


    public static BaseFragment newInstance() {
        Fragment3 fragment3 = new Fragment3();
        return fragment3;
    }

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_3;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        showContent(true);
        mPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onLoadHomepageInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.onLoadHomepageInfo();
        }
        Logger.i("test onHiddenChanged = " + hidden);
    }

    @Override
    public void initData() {

    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_exit)
    public void exit_sys() {
        new MaterialDialog.Builder(getActivity())
                .title("退出登录")
                .content("是否确认退出登录？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mUserStorage.logout();
                        getActivity().finish();
                    }
                })
                .show();
    }

    /**
     * 选择头像
     */
    @OnClick(R.id.roundImageView)
    public void mRoundImageView() {

    }


    /**
     * 营业执照
     */
    @OnClick(R.id.relativeGoYyzz)
    public void mRelativeGoYyzz() {
        Intent intent = new Intent(getActivity(), BusinessLicenceActivity.class);
        startActivityForResult(intent, REQUEST_BUSINESS_LICENCE_CODE);
    }

    /**
     * 实名认证
     */
    @OnClick(R.id.relativeGoSmrr)
    public void mRelativeGoSmrr() {
        Intent intent = new Intent(getActivity(), CertificationActivity.class);
        startActivityForResult(intent, REQUEST_CERTIFICATION_CODE);
    }


    @Override
    public void onLoadHomepageInfoCompleted(HomepageEntity homepageEntity) {
        ImageLoaderUtil.getInstance().loadCircleImage(homepageEntity.getAvatarUrl(),R.drawable.header, mRoundImageView);
        mTextName.setText(homepageEntity.getRealName());
        mTextPhone.setText(homepageEntity.getMobile());
        if (homepageEntity.getSex() == 1) {
            mImageSex.setImageResource(R.drawable.icon_boy);
        } else {
            mImageSex.setImageResource(R.drawable.icon_girl);
        }
        showState(homepageEntity.getFlgAuthBusiness(), mTextYyzzState);

        // 营业执照
        if( homepageEntity.getFlgAuthBusiness() == 0 || homepageEntity.getFlgAuthBusiness() == 3){
            mRelativeGoYyzz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BusinessLicenceActivity.class);
                    startActivityForResult(intent, REQUEST_BUSINESS_LICENCE_CODE);
                }
            });
        }else{
            mRelativeGoYyzz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BusinessLicenceActivity.class);
                    intent.putExtra("noUpload",1);//不可上传
                    startActivityForResult(intent, REQUEST_BUSINESS_LICENCE_CODE);
                }
            });
        }

        showState(homepageEntity.getFlgAuthRealName(), mTextSmrzState);
        //实名认证
        if( homepageEntity.getFlgAuthRealName() == 0 || homepageEntity.getFlgAuthRealName() == 3){
            mRelativeGoSmrr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CertificationActivity.class);
                    startActivityForResult(intent, REQUEST_CERTIFICATION_CODE);
                }
            });
        }else{
            mRelativeGoSmrr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CertificationActivity.class);
                    intent.putExtra("noUpload",1);//不可上传
                    startActivityForResult(intent, REQUEST_CERTIFICATION_CODE);
                }
            });
        }

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    private void showState(int userType, TextView textView) {
        if (userType == 0) {
            textView.setText("未认证");
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackgroundResource(R.drawable.rectangle_card_unchecked_corner_bg);
        } else if (userType == 1) {
            textView.setText("待审核");
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackgroundResource(R.drawable.rectangle_card_unchecked_corner_bg);
        } else if (userType == 2) {
            textView.setText("已认证");
            textView.setBackgroundResource(R.drawable.rectangle_card_checked_corner_bg);
            textView.setTextColor(getResources().getColor(R.color.zfb331f));
        } else if (userType == 3) {
            textView.setText("未通过");
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackgroundResource(R.drawable.rectangle_card_unchecked_corner_bg);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    /**
     *
     */
    @OnClick(R.id.relativePersonal)
    public void mRelativePersonal(){
       openActivity(PersonalProfileActivity.class);
    }
}
