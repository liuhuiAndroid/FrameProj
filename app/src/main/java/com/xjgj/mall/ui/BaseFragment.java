package com.xjgj.mall.ui;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.frameproj.library.util.ResourceUtil;
import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.widget.ProgressBarCircularIndeterminate;
import com.google.gson.JsonParseException;
import com.xjgj.mall.R;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.injector.HasComponent;
import com.xjgj.mall.ui.widget.ProgressFragment;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

import static com.xjgj.mall.Constants.BAD_GATEWAY;
import static com.xjgj.mall.Constants.FORBIDDEN;
import static com.xjgj.mall.Constants.GATEWAY_TIMEOUT;
import static com.xjgj.mall.Constants.INTERNAL_SERVER_ERROR;
import static com.xjgj.mall.Constants.NOT_FOUND;
import static com.xjgj.mall.Constants.REQUEST_TIMEOUT;
import static com.xjgj.mall.Constants.SERVICE_UNAVAILABLE;
import static com.xjgj.mall.Constants.UNAUTHORIZED;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public abstract class BaseFragment extends ProgressFragment {

    private TextView tvError, tvEmpty, tvLoading;
    private Button btnReload;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initInjector();
        getBundle(getArguments());
        initUI(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    public void openActivity(Class<?> cls) {
        startActivity(new Intent(baseActivity, cls));
    }

    public abstract void initInjector();

    public abstract int initContentView();

    /**
     * 得到Activity传进来的值
     */
    public abstract void getBundle(Bundle bundle);

    /**
     * 初始化控件
     */
    public abstract void initUI(View view);

    /**
     * 在监听器之前把数据准备好
     */
    public abstract void initData();

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(initContentView(), null);
    }

    @Override
    public View onCreateContentErrorView(LayoutInflater inflater) {
        View error = inflater.inflate(R.layout.error_view_layout, null);
        tvError = (TextView) error.findViewById(R.id.tvError);
        error.findViewById(R.id.btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReloadClicked();
            }
        });
        return error;
    }

    @Override
    public View onCreateContentEmptyView(LayoutInflater inflater) {
        View empty = inflater.inflate(R.layout.empty_view_layout, null);
        tvEmpty = (TextView) empty.findViewById(R.id.tvEmpty);
        btnReload = (Button) empty.findViewById(R.id.btnReload);
        empty.findViewById(R.id.btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReloadClicked();
            }
        });
        return empty;
    }

    @Override
    public View onCreateProgressView(LayoutInflater inflater) {
        View loading = inflater.inflate(R.layout.loading_view_layout, null);
        tvLoading = (TextView) loading.findViewById(R.id.tvLoading);
        ProgressBarCircularIndeterminate progressBar =
                (ProgressBarCircularIndeterminate) loading.findViewById(R.id.progress_view);
        progressBar.setBackgroundColor(ResourceUtil.getThemeColor(getActivity()));
        return loading;
    }

    public void setErrorText(String text) {
        tvError.setText(text);
    }

    public void setErrorText(int textResId) {
        setErrorText(getString(textResId));
    }

    public void setEmptyText(String text) {
        tvEmpty.setText(text);
    }

    public void setEmptyButtonVisible(int visible) {
        btnReload.setVisibility(visible);
    }

    public void setEmptyText(int textResId) {
        setEmptyText(getString(textResId));
    }

    public void setLoadingText(String text) {
        tvLoading.setText(text);
    }

    public void setLoadingText(int textResId) {
        setLoadingText(getString(textResId));
    }

    //Override this to reload
    public void onReloadClicked() {

    }

    @SuppressWarnings("unchecked") protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof CommonApi.APIException) {
            ToastUtil.showToast(throwable.getMessage());
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    //                    onPermissionError(ex);          //权限错误，需要实现
                    ToastUtil.showToast(getResources().getString(R.string.error_permission));
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //均视为网络错误
                    ToastUtil.showToast(getResources().getString(R.string.error_network));
                    break;
            }
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            ToastUtil.showToast(getResources().getString(R.string.error_parse));
        } else if (throwable instanceof UnknownHostException) {
            ToastUtil.showToast(getResources().getString(R.string.error_network));
        } else if (throwable instanceof SocketTimeoutException) {    //超时
//            ToastUtil.showToast(getResources().getString(R.string.error_overtime));
        } else {
//            ToastUtil.showToast(getResources().getString(R.string.error_unknow));
        }
    }

}
