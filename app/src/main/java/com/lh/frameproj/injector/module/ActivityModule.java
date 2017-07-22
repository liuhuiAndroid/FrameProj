package com.lh.frameproj.injector.module;

import android.app.Activity;

import com.lh.frameproj.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 * 创建类实例级别Module维度要高于Inject维度。
 */
@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    // Provides最终解决第三方类库依赖注入问题
    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }

}
