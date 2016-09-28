package com.lh.frameproj.injector.component;

import android.app.Activity;

import com.lh.frameproj.injector.PerActivity;
import com.lh.frameproj.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

}
