package com.xjgj.mall.ui.cancelorder;

import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.module.ActivityModule;
import com.xjgj.mall.ui.cancelorder.driver_reasons.DriverReasonsFragment;
import com.xjgj.mall.ui.cancelorder.my_reasons.MyReasonsFragment;

import dagger.Component;

/**
 * Created by we-win on 2017/8/3.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface CancelOrderComponent {
    void inject(CancelOrderActivity activity);

    void inject(DriverReasonsFragment driverReasonsFragment);
    void inject(MyReasonsFragment myReasonsFragment);

}
