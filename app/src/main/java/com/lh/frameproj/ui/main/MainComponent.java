package com.lh.frameproj.ui.main;

import com.lh.frameproj.injector.PerActivity;
import com.lh.frameproj.injector.component.ApplicationComponent;
import com.lh.frameproj.injector.module.ActivityModule;
import com.lh.frameproj.ui.fragment1.Fragment1;

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

}
