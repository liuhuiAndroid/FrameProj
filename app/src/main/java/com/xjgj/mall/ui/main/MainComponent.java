package com.xjgj.mall.ui.main;

import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.module.ActivityModule;
import com.xjgj.mall.ui.fragment1.Fragment1;
import com.xjgj.mall.ui.fragment2.order_waiting_accept.OrderWaitingAcceptFragment;
import com.xjgj.mall.ui.fragment2.order_waiting_evaluate.OrderWaitingEvaluateFragment;
import com.xjgj.mall.ui.fragment2.order_working.OrderWorkingFragement;
import com.xjgj.mall.ui.fragment3.Fragment3;
import com.xjgj.mall.ui.fragment4.Fragment4;

import dagger.Component;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {
        ActivityModule.class,MainModule.class
})
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(Fragment1 fragment);

    void inject(Fragment3 fragment);

    void inject(OrderWaitingAcceptFragment fragment);
    void inject(OrderWorkingFragement fragment);
    void inject(OrderWaitingEvaluateFragment fragment);

    void inject(Fragment4 fragment);
}
