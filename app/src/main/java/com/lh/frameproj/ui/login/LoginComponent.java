package com.lh.frameproj.ui.login;

import com.lh.frameproj.injector.PerActivity;
import com.lh.frameproj.injector.component.ApplicationComponent;
import com.lh.frameproj.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by we-win on 2017/7/21.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
