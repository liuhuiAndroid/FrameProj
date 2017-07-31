package com.xjgj.mall.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.main.MainActivity;
import com.xjgj.mall.ui.register.RegisterActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.REQUEST_REGISTER_CODE;
import static com.xjgj.mall.Constants.RESULT_REGISTER_CODE;

/**
 * Created by we-win on 2017/7/21.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;
    @BindView(R.id.text_type)
    TextView mTextType;
    @BindView(R.id.editPhone)
    EditText mEditPhone;
    @BindView(R.id.editPassWord)
    EditText mEditPassWord;
    @BindView(R.id.textLogin)
    TextView mTextLogin;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.text_handle)
    TextView mTextHandle;
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
        mTextTitle.setText("登录农好运");
        mTextHandle.setText("注册");
        mTextHandle.setTextSize(14);
        mTextHandle.setTextColor(getResources().getColor(R.color.z5b5b5b));
        mTextHandle.setClickable(true);
        mTextHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,REQUEST_REGISTER_CODE);
            }
        });
        mTextHandle.setVisibility(View.VISIBLE);
        mLoginDialog = new MaterialDialog.Builder(this).title("提示").content("登录中").progress(true, 0).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_REGISTER_CODE &&resultCode == RESULT_REGISTER_CODE){
            finish();
        }
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

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    /**
     * 登录按钮点击
     */
    @OnClick(R.id.textLogin)
    public void mTextLogin() {
        String mUserName = mEditPhone.getText().toString().trim();
        String mPassword = mEditPassWord.getText().toString().trim();
        mPresenter.login(mUserName, mPassword);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (!isFinishing() && mLoginDialog.isShowing()) {
            mLoginDialog.dismiss();
        }
        mLoginDialog = null;
    }

}
