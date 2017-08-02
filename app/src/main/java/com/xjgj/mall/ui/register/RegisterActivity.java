package com.xjgj.mall.ui.register;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.R;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.main.MainActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.xjgj.mall.Constants.RESULT_REGISTER_CODE;
import static com.xjgj.mall.R.id.text_get_verification_code;


/**
 * Created by we-win on 2017/7/31.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

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
    @BindView(R.id.text_type)
    TextView mTextType;
    @BindView(R.id.editPhone)
    EditText mEditPhone;
    @BindView(R.id.editPassWord)
    EditText mEditPassWord;
    @BindView(R.id.editNickName)
    EditText mEditNickName;
    @BindView(R.id.editIdentifyingCode)
    EditText mEditIdentifyingCode;
    @Inject
    RegisterPresenter mPresenter;
    @BindView(text_get_verification_code)
    TextView mTextGetVerificationCode;

    private MaterialDialog mLoginDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void initInjector() {
        DaggerRegisterComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mTextTitle.setText("注册农好运");
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoginDialog = new MaterialDialog.Builder(this).title("提示").content("注册中").progress(true, 0).build();
    }


    /**
     * 注册
     */
    @OnClick(R.id.textRegister)
    public void mTextRegister() {
        String mUserName = mEditPhone.getText().toString().trim();
        String mPassword = mEditPassWord.getText().toString().trim();
        String mNickName = mEditNickName.getText().toString().trim();
        String mIdentifyingCode = mEditIdentifyingCode.getText().toString().trim();
        mPresenter.register(mUserName, mNickName, mPassword, mIdentifyingCode);
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
    public void showError(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void registerSuccess() {
        openActivity(MainActivity.class);
        setResult(RESULT_REGISTER_CODE);
        finish();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void refreshSmsCodeUi() {
        int time = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return 60 - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mTextGetVerificationCode.setEnabled(false);
                        mTextGetVerificationCode.setBackgroundResource(R.drawable.rectangle_register_phone_corner_bg);
                        mTextGetVerificationCode.setTextColor(getResources().getColor(R.color.base));
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mTextGetVerificationCode.setText("剩余" + aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mTextGetVerificationCode.setEnabled(true);
                        mTextGetVerificationCode.setText("获取验证码");
                        mTextGetVerificationCode.setBackgroundResource(R.drawable.rectangle_yzm_corner_bg);
                        mTextGetVerificationCode.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                });
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

    /**
     * 获取短信验证码
     */
    @OnClick(text_get_verification_code)
    public void mTextGetVerificationCode() {

        String mUserName = mEditPhone.getText().toString().trim();
        mPresenter.smsCodeSend(mUserName,1);
    }

}
