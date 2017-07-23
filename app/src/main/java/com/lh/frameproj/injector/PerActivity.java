package com.lh.frameproj.injector;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 * 通过自定义Scope注解可以更好的管理创建的类实例的生命周期。
 * Scope的真正用处就在于Component的组织。
 */
@Scope // 注明是Scope
@Documented // 标记在文档
@Retention(RetentionPolicy.RUNTIME) // 运行时级别
public @interface PerActivity {
}
