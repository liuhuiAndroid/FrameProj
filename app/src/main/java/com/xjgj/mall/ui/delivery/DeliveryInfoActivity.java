package com.xjgj.mall.ui.delivery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.frameproj.library.adapter.CommonAdapter;
import com.android.frameproj.library.adapter.MultiItemTypeAdapter;
import com.android.frameproj.library.adapter.base.ViewHolder;
import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.TerminiEntity;
import com.xjgj.mall.components.storage.UserStorage;
import com.xjgj.mall.db.DaoSession;
import com.xjgj.mall.db.DestinationDao;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.custommap.CustomMapActivity;
import com.xjgj.mall.ui.location.ChooseLocationActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xjgj.mall.Constants.REQUEST_CHOOSE_LOCATION_CODE;
import static com.xjgj.mall.Constants.REQUEST_CHOOSE_LOCATION_CODE_CUSTOM_MAP;
import static com.xjgj.mall.Constants.RESULT_CHOOSE_LOCATION_CODE;
import static com.xjgj.mall.Constants.RESULT_CHOOSE_LOCATION_CODE_CUSTOM_MAP;
import static com.xjgj.mall.Constants.RESULT_DELIVERY_INFO_CODE;
import static com.xjgj.mall.R.id.textContactPhone;
import static com.xjgj.mall.R.id.textDetailsAddress;
import static com.xjgj.mall.R.id.textPersonalName;

/**
 * Created by we-win on 2017/7/27.
 * 发货位置信息
 */

public class DeliveryInfoActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.textAddress)
    EditText mTextAddress;
    @BindView(R.id.myListView)
    ListView mMyListView;
    @BindView(textDetailsAddress)
    EditText mTextDetailsAddress;
    @BindView(textPersonalName)
    EditText mTextPersonalName;
    @BindView(textContactPhone)
    EditText mTextContactPhone;
    @BindView(R.id.textOk)
    TextView mTextOk;

    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.list_result)
    RecyclerView mListResult;

    private TerminiEntity mTerminiEntity;
    private int mPosition;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private int mType;


    @Inject
    DaoSession mDaoSession;
    @Inject
    DestinationDao mDestinationDao;

    private static final String SQL_DISTINCT_ENAME = "SELECT DISTINCT " + DestinationDao.Properties.Termini.columnName + " FROM " + DestinationDao.TABLENAME
            + " ORDER BY _id DESC LIMIT 0,3";

    @Override
    public int initContentView() {
        return R.layout.activity_delivery_info;
    }

    @Override
    public void initInjector() {
        DaggerDeliveryInfoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        getData();
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(this);
        //        mTextHandle.setText(getString(R.string.switch_earth_address));
        //        mTextHandle.setTextSize(14);
        //        mTextHandle.setTextColor(getResources().getColor(R.color.z5b5b5b));
        //        mTextHandle.setClickable(true);
        //        mTextHandle.setOnClickListener(this);
        //        mTextHandle.setVisibility(View.VISIBLE);

        mType = getIntent().getIntExtra("type", -1);
        //        if (mType == 0) {
        //            mLlSearch.setVisibility(View.VISIBLE);
        //        } else if (mType == 1) {
        //            mLlSearch.setVisibility(View.GONE);
        //        }

        if (mType == 1) {
            final List<String> result = new ArrayList<String>();
            Cursor c = mDaoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
            try {
                if (c.moveToFirst()) {
                    do {
                        result.add(c.getString(0));
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }

            if (result != null && result.size() > 0) {
                mListResult.setVisibility(View.VISIBLE);
                if (mCommonAdapterSearch == null) {
                    mListResult.setLayoutManager(new LinearLayoutManager(DeliveryInfoActivity.this));
                    mCommonAdapterSearch = new CommonAdapter<String>(DeliveryInfoActivity.this, R.layout.item_delivery_termini, result) {
                        @Override
                        protected void convert(ViewHolder holder, final String s, int position) {
                            holder.setText(R.id.textview_formmatted_address, s);
                        }
                    };
                    mCommonAdapterSearch.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                            mTextDetailsAddress.setText(result.get(position));
                            mListResult.setVisibility(View.GONE);
                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                            return false;
                        }
                    });
                    mListResult.setAdapter(mCommonAdapterSearch);
                    mListResult.setItemAnimator(new DefaultItemAnimator());
                } else {
                    mCommonAdapterSearch.notifyDataSetChanged();
                }
            } else {
                mListResult.setVisibility(View.GONE);
            }
        } else {
            mListResult.setVisibility(View.GONE);
        }


    }

    private CommonAdapter mCommonAdapterSearch;

    /**
     * 页面传值
     */
    private void getData() {
        mTerminiEntity = (TerminiEntity) getIntent().getExtras().getSerializable("termini_info");
        mPosition = getIntent().getExtras().getInt("position");

        if (mPosition == 0) {
            mTextTitle.setText(getString(R.string.ship_info));
            mTextAddress.setHint(getResources().getString(R.string.input_begin_d));
            mTextPersonalName.setHint(getResources().getString(R.string.personal_name));
        } else {
            mTextTitle.setText(getString(R.string.ship_info2));
            mTextAddress.setHint(getResources().getString(R.string.input_target_d));
            mTextPersonalName.setHint(getResources().getString(R.string.personal_shou_name));
        }

        if (mTerminiEntity != null) {
            mTextAddress.setText(mTerminiEntity.getAddressName());
            mTextDetailsAddress.setText(mTerminiEntity.getAddressDescribeName());
            mTextPersonalName.setText(mTerminiEntity.getReceiverName());
            mTextContactPhone.setText(mTerminiEntity.getReceiverPhone());
            setLen(mTextAddress);
            setLen(mTextDetailsAddress);
            setLen(mTextPersonalName);
            setLen(mTextContactPhone);
        } else {
            mTerminiEntity = new TerminiEntity();
            if (mPosition == 0) {
                mTextContactPhone.setText(mUserStorage.getUser().getMobile());
                mTextPersonalName.setText(mUserStorage.getUser().getRealName());
            }
        }
    }

    private void setLen(EditText edit) {
        int len = edit.getText().toString().trim().length();
        if (len > 0) {
            edit.setSelection(len);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                if (mTerminiEntity != null) {
                    // TODO 当前页面有内容修改，需要弹出提示
                    //                    if (!mTerminiEntity.getAddressName().equals(mTextAddress.getText().toString().trim())
                    //                            || !mTerminiEntity.getAddressDescribeName().equals(mTextDetailsAddress.getText().toString().trim())
                    //                            || !mTerminiEntity.getReceiverName().equals(mTextPersonalName.getText().toString().trim())
                    //                            || !mTerminiEntity.getReceiverPhone().equals(mTextContactPhone.getText().toString().trim())) {
                    //                        isFinish();
                    //                    } else {
                    //                        finish();
                    //                    }
                    finish();
                } else {
                    finish();
                }
                break;

            //            case R.id.text_handle:
            //                Intent localIntent = new Intent(DeliveryInfoActivity.this, ChooseLocationActivity.class);
            //                //如果之前有定位需要先定位在之前的位置上,暂时不做吧
            //                localIntent.putExtra("searchString",mTextAddress.getText().toString().trim());
            //                startActivityForResult(localIntent, REQUEST_CHOOSE_LOCATION_CODE);
            //                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_LOCATION_CODE && resultCode == RESULT_CHOOSE_LOCATION_CODE) {
            String name = data.getStringExtra("name");
            String addr = data.getStringExtra("addr");
            longitude = data.getDoubleExtra("longitude", 0.0);
            latitude = data.getDoubleExtra("latitude", 0.0);

            if (!mTextAddress.getText().toString().trim().equals(name)) {
                mTextDetailsAddress.setText(addr);
            }
            mTextAddress.setText(name);
        } else if(requestCode == REQUEST_CHOOSE_LOCATION_CODE_CUSTOM_MAP && resultCode == RESULT_CHOOSE_LOCATION_CODE_CUSTOM_MAP){
            String name = data.getStringExtra("name");
            String addr = data.getStringExtra("addr");
            longitude = data.getDoubleExtra("longitude", 0.0);
            latitude = data.getDoubleExtra("latitude", 0.0);
            mTextDetailsAddress.setText(addr);
            mTextAddress.setText(name);
        }
    }

    private void isFinish() {

        new MaterialDialog.Builder(DeliveryInfoActivity.this)
                .title("提示")
                .content("是否放弃已输入的信息？")
                .positiveText("放弃")
                .negativeText("保留")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();

    }

    /**
     * 确认
     *
     * @param view
     */
    public void ok(View view) {

        String name = mTextAddress.getText().toString().trim();
        String detailName = mTextDetailsAddress.getText().toString().trim();
        String peopleName = mTextPersonalName.getText().toString().trim();
        String phone = mTextContactPhone.getText().toString().trim();
        if (TextUtils.isEmpty(detailName)) {
            ToastUtil.showToast("请补充目的地完整地址");
            return;
        }

        if (TextUtils.isEmpty(name) && mType == 0) {
            if (mPosition == 0) {
                ToastUtil.showToast("请填写始发站地址");
            } else {
                ToastUtil.showToast("请填写目的地地址");
            }
        } else {

            if (TextUtils.isEmpty(peopleName)) {
                if (mPosition == 0) {
                    ToastUtil.showToast("请填写发货人姓名");
                } else {
                    ToastUtil.showToast("请填写收货人姓名");
                }

            } else {
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast("请填写联系电话");
                } else {
                    mTerminiEntity.setAddressName(name);
                    mTerminiEntity.setAddressDescribeName(detailName);
                    mTerminiEntity.setReceiverName(peopleName);
                    mTerminiEntity.setReceiverPhone(phone);
                    mTerminiEntity.setLongitude(longitude);
                    mTerminiEntity.setLatitude(latitude);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("terminiEntity", mTerminiEntity);
                    bundle.putInt("position", mPosition);
                    intent.putExtras(bundle);
                    setResult(RESULT_DELIVERY_INFO_CODE, intent);
                    finish();
                }
            }
        }
    }

    /**
     * 选择位置
     */
    @OnClick(R.id.textAddress)
    public void mTextAddress() {

        if (mType == 0) {
            Intent localIntent = new Intent(DeliveryInfoActivity.this, ChooseLocationActivity.class);
            //如果之前有定位需要先定位在之前的位置上,暂时不做吧
            localIntent.putExtra("searchString", mTextAddress.getText().toString().trim());
            startActivityForResult(localIntent, REQUEST_CHOOSE_LOCATION_CODE);
        } else if (mType == 1) {
            Intent intent = new Intent(DeliveryInfoActivity.this, CustomMapActivity.class);
            startActivityForResult(intent,REQUEST_CHOOSE_LOCATION_CODE_CUSTOM_MAP);
        }

    }

}
