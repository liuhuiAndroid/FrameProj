package com.xjgj.mall.ui.maprouteoverlay;

import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by lh on 2017/8/16.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface MapRouteOverlayComponent {
    void inject(MapRouteOverlayActivity activity);
}
