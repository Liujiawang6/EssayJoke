package com.qiyei.opensource.injection.component;

import com.qiyei.opensource.injection.module.UserModelModule;
import com.qiyei.opensource.ui.activity.DaggerDemoActivity;

import dagger.Component;

/**
 * @author Created by qiyei2015 on 2018/10/7.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
@Component(modules = UserModelModule.class)
public interface UserModelComponent {

    /**
     * 需要注入的对象
     * @param activity
     */
    void inject(DaggerDemoActivity activity);
}
