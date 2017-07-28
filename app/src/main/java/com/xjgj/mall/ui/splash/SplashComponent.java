package com.xjgj.mall.ui.splash;

import com.xjgj.mall.injector.PerActivity;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by we-win on 2017/7/20.
 * Component会查找目标类中用Inject注解标注的属性
 * 查找到相应的属性后会接着查找该属性对应的用Inject标注的构造函数
 * 剩下的工作就是初始化该属性的实例并把实例进行赋值
 *
 * Component的新职责就是管理好Module，Component中的modules属性可以把Module加入Component，modules可以加入多个Module
 *
 * 每个页面对应一个Component，比如一个Activity页面定义一个Component，一个Fragment定义一个Component。
 * 当然这不是必须的，有些页面之间的依赖的类是一样的，可以公用一个Component。
 *
 * dependencies：依赖方式 - 一个Component是依赖于一个或多个Component
 * SubComponent：包含方式 - 一个Component是包含一个或多个Component的
 *               继承方式
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, SplashModule.class
})
public interface SplashComponent {

    // 调用Component（注入器）的injectXXX（Object）方法开始注入
    void inject(SplashActivity activity);

}
