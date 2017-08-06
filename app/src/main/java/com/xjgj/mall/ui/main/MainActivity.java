package com.xjgj.mall.ui.main;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.interf.CallbackChangeFragment;
import com.android.frameproj.library.util.NetWorkUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.xjgj.mall.Constants;
import com.xjgj.mall.MyApplication;
import com.xjgj.mall.R;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.injector.HasComponent;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.util.CommonEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements MainContract.View
        , HasComponent<MainComponent>,CallbackChangeFragment {

    @BindView(R.id.frame_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.bnve)
    BottomNavigationViewEx mBottomNavigationViewEx;

    @Inject
    MainPresenter mPresenter;
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

    @Inject
    CommonApi mCommonApi;

    @Inject
    Bus mBus;

    private MainComponent mMainComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportFragmentManager().getFragments() != null
                && getSupportFragmentManager().getFragments().size() > 0) {
            getSupportFragmentManager().getFragments().clear();
        }
    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule(this))
                .build();
        mMainComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mBus.register(this);

        mTextHandle.setText("全部订单");
        mTextHandle.setTextSize(14);
        mTextHandle.setTextColor(getResources().getColor(R.color.z5b5b5b));
        mTextHandle.setClickable(true);
        mTextHandle.setVisibility(View.VISIBLE);

        // 禁止所有动画效果
        mBottomNavigationViewEx.enableAnimation(false);
        mBottomNavigationViewEx.enableShiftingMode(false);
        mBottomNavigationViewEx.enableItemShiftingMode(false);
        // 自定义图标和文本大小
        //        mBottomNavigationViewEx.setIconSize(widthDp, heightDp);
        //        mBottomNavigationViewEx.setTextSize(sp);
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.i_choose:
                        mPresenter.onTabClick(0);
                        break;
                    case R.id.i_order:
                        mPresenter.onTabClick(1);
                        break;
                    case R.id.i_mine:
                        mPresenter.onTabClick(2);
                        break;
                }
                return true;
            }
        });

        mPresenter.attachView(this);
        mPresenter.initFragment();
    }

    @Override
    public void setTitle(int position, String title) {
        mTextTitle.setText(title);
        if(position == 1){
            mTextHandle.setVisibility(View.VISIBLE);
        }else{
            mTextHandle.setVisibility(View.GONE);
        }
    }

    /**
     * 筛选条件
     */

    @Override
    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    @Override
    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }


    @OnClick(R.id.text_handle)
    public void mTextHandle() {
        // 0 新建(待接单),1 已接单, 2  服务中，3 已完成, 4 已取消, 5 已评价,6 申诉中
        final List<Type> typeList = new ArrayList<>();
        typeList.add(new Type(-1, "全部订单"));
        typeList.add(new Type(0, "待接单"));
        typeList.add(new Type(1, "已接单"));
        typeList.add(new Type(2, "服务中"));
        typeList.add(new Type(3, "已完成"));
        typeList.add(new Type(4, "已取消"));
        typeList.add(new Type(5, "已评价"));
        typeList.add(new Type(6, "申诉中"));
        typeList.add(new Type(7, "已过期"));
        new MaterialDialog.Builder(MainActivity.this)
                .title("筛选条件")
                .items(typeList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Type type = typeList.get(which);
                        mBus.post(new CommonEvent().new OrderTypeChangeEvent(type.getId(),type.getName(),true));
                        mTextHandle.setText(type.getName());
                    }
                })
                .show();
    }

    @Subscribe
    public void orderTypeChangeEvent(CommonEvent.OrderTypeChangeEvent orderTypeChangeEvent) {
        if(!orderTypeChangeEvent.isRefresh()){
            mTextHandle.setText(orderTypeChangeEvent.getName());
        }
    }

    @Override
    public void changeFragment(int which) {
        mBottomNavigationViewEx.setCurrentItem(which);
    }

    class Type {
        public Type(int id, String name) {
            this.id = id;
            this.name = name;
        }
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadLog();
    }

    /**
     * TODO 有空吧这个放到Presenter里面去
     */
    private void uploadLog() {
        //检查是否有日志文件 要是有的话上传
        String savePath = "";
        File logDir = null;
        if (NetWorkUtils.isNetworkConnected(MainActivity.this)) {
            try {
                //判断是否挂载了SD卡
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    savePath = MyApplication.getContext().getExternalCacheDir() + Constants.LH_LOG_PATH;
                    logDir = new File(savePath);
                    if (!logDir.exists()) {
                        logDir.mkdirs();
                    }
                    if (logDir.list().length > 0) {
                        File[] files = logDir.listFiles();
                        if (null == files) {
                            return;
                        } else {
                            for (final File file :
                                    files) {

                                FileInputStream fin = new FileInputStream(file);
                                int length = fin.available();
                                byte[] buffer = new byte[length];
                                fin.read(buffer);
                                final String res = new String(buffer, "UTF-8");
                                fin.close();

                               mCommonApi.uploadErrorFiles(MainActivity.this.getPackageName(),"android",
                                       android.os.Build.VERSION.RELEASE, android.os.Build.MODEL,res)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<ResponseBody>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                                                if (file.exists()) {
                                                    if (file.isFile()) {
                                                        file.delete();
                                                    }
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                                throwable.printStackTrace();
                                            }
                                        });
                            }

                        }
                    } else {

                    }
                    //没有挂载SD卡，无法写文件
                    if (logDir == null || !logDir.exists() || !logDir.canWrite()) {
                        return;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        mPresenter.detachView();
    }
}
