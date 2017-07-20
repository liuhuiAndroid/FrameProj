package com.lh.frameproj.ui.splash;

import com.lh.frameproj.injector.PerActivity;
import com.lh.frameproj.injector.component.ApplicationComponent;
import com.lh.frameproj.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by we-win on 2017/7/20.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, SplashModule.class
})
public interface SplashComponent {

    void inject(SplashActivity activity);

}
