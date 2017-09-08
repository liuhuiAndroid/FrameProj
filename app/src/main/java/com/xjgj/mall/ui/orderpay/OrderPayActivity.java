package com.xjgj.mall.ui.orderpay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.frameproj.library.util.DialogManager;
import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.CouponEntity;
import com.xjgj.mall.bean.PayAlipayEntity;
import com.xjgj.mall.bean.PayResult;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.coupon.ChooseCouponActivity;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.REQUEST_CHOOSE_COUPON;
import static com.xjgj.mall.Constants.RESULT_CHOOSE_COUPON;

/**
 * Created by lh on 2017/8/25.
 * 订单支付
 */
public class OrderPayActivity extends BaseActivity implements OrderPayContract.View {

    private static final int SDK_PAY_FLAG = 1;

    @BindView(R.id.image_back)
    ImageView mImageBack;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.image_handle)
    ImageView mImageHandle;
    @BindView(R.id.text_handle)
    TextView mTextHandle;
    @BindView(R.id.etPriceOldOCI)
    EditText mEtPriceOldOCI;
    @BindView(R.id.payType)
    RadioGroup mPayType;
    @BindView(R.id.aliPay)
    RadioButton mAliPay;
    @BindView(R.id.textNext)
    TextView mTextNext;

    @Inject
    OrderPayPresenter mOrderPayPresenter;

    @BindView(R.id.tv_coupon)
    TextView mTvCoupon;
    private Dialog mLoadingDialog;
    private int mOrderId;
    private String mOutTradeNo;


    @Override
    public int initContentView() {
        return R.layout.activity_order_pay;
    }

    @Override
    public void initInjector() {
        DaggerOrderPayComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {

        mOrderId = getIntent().getIntExtra("orderId", -1);
        mOrderPayPresenter.attachView(this);

        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("订单支付");

        mPayType.check(mAliPay.getId());
    }

    /**
     * 支付
     */
    @OnClick(R.id.textNext)
    public void mTextNext() {
        String trim = mEtPriceOldOCI.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtil.showToast("请填写支付金额");
            return;
        }
        mOrderPayPresenter.payOrder(mOrderId, trim, mCouponId);
    }

    @Override
    public void showLoading() {
        mLoadingDialog = DialogManager.getInstance().createLoadingDialog(OrderPayActivity.this);
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading(int type) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void payOrderResult(final PayAlipayEntity payAlipayEntity) {

        mOutTradeNo = payAlipayEntity.getOutTradeNo();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPayActivity.this);
                Map<String, String> result = alipay.payV2(payAlipayEntity.getPayString(), true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    @Override
    public void payConfirmResult(CouponEntity couponEntity) {
        if (couponEntity == null || couponEntity.getAmount() == 0) {
            ToastUtil.showToast("支付成功");
        } else {
            ToastUtil.showToast("支付成功，恭喜获取一个优惠券");
        }
        finish();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        // Toast.makeText(OrderPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        payConfirm(mOutTradeNo);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 确认支付
     *
     * @param outTradeNo
     */
    private void payConfirm(String outTradeNo) {
        mOrderPayPresenter.payConfirm(mOutTradeNo);
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderPayPresenter.detachView();
    }

    /**
     * 选择优惠券
     */
    @OnClick(R.id.ll_coupon)
    public void mLlCoupon() {
        Intent intent_coupon = new Intent(OrderPayActivity.this, ChooseCouponActivity.class);
        startActivityForResult(intent_coupon, REQUEST_CHOOSE_COUPON);
    }


    private int mCouponId = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_COUPON && resultCode == RESULT_CHOOSE_COUPON) {
            mCouponId = data.getIntExtra("couponId", -1);
            int amount = data.getIntExtra("amount", -1);
            mTvCoupon.setText("-￥" + amount);
        }
    }

}
