package com.xjgj.mall.ui.orderevaluate;

import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by we-win on 2017/8/2.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface OrderEvaluateComponent {
    void inject(OrderEvaluateActivity activity);
}
