package com.xjgj.mall.ui.orderpay;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.xjgj.mall.R;
import com.xjgj.mall.bean.PayOrderEntity;
import com.xjgj.mall.bean.PayResult;
import com.xjgj.mall.ui.BaseActivity;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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
        mOrderPayPresenter.payOrder(1, 2);
    }

    @Override
    public void showLoading() {
        Dialog loadingDialog = DialogManager.getInstance().createLoadingDialog(OrderPayActivity.this);
        loadingDialog.show();
    }

    @Override
    public void hideLoading(int type) {

    }

    @Override
    public void payOrderResult(PayOrderEntity payOrderEntity) {

        final String orderInfo = "";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
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
                        Toast.makeText(OrderPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderPayPresenter.detachView();
    }


}
