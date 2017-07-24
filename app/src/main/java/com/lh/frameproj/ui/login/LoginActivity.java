package com.lh.frameproj.ui.login;

import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseActivity;
import com.lh.frameproj.ui.main.MainActivity;
import com.lh.frameproj.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by we-win on 2017/7/21.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    SPUtil mSPUtil;

    @Inject
    LoginPresenter mPresenter;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.text_type)
    TextView mTextType;
    @BindView(R.id.editPhone)
    EditText mEditPhone;
    @BindView(R.id.editIdentifyingCode)
    EditText mEditIdentifyingCode;
    @BindView(R.id.text_get_verification_code)
    TextView mTextGetVerificationCode;
    @BindView(R.id.textLogin)
    TextView mTextLogin;
    private MaterialDialog mLoginDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initInjector() {
        DaggerLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mTvTitle.setText("登录农好运");
        mLoginDialog = new MaterialDialog.Builder(this).title("提示").content("登录中").progress(true, 0).build();
    }

    @Override
    public void showLoading() {
        if (!isFinishing() && !mLoginDialog.isShowing()) {
            mLoginDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (!isFinishing() && mLoginDialog.isShowing()) {
            mLoginDialog.dismiss();
        }
    }

    @Override
    public void showUserNameError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void showPassWordError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void loginSuccess() {
        openActivity(MainActivity.class);
        finish();
    }

    /**
     * 登录按钮点击
     */
    @OnClick(R.id.textLogin)
    public void mTextLogin() {
        String mUserName = mEditPhone.getText().toString().trim();
        String mIdentifyingCode = mEditIdentifyingCode.getText().toString().trim();
        mPresenter.login(mUserName, mIdentifyingCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
