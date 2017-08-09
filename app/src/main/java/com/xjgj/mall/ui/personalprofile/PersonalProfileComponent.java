package com.xjgj.mall.ui.personalprofile;

import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by lh on 2017/8/9.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface PersonalProfileComponent {
    void inject(PersonalProfileActivity activity);
}
