package com.lh.frameproj.injector;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by lh on 2017/7/23.
 */

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApp {
}
