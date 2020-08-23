package com.qiyei.essayjoke;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qiyei.framework.FrameworkApplication;
import com.qiyei.framework.skin.SkinManager;
import com.qiyei.sdk.SDKManager;
import com.qiyei.sdk.log.LogManager;

/**
 * Email: 1273482124@qq.com
 * Created by qiyei2015 on 2017/5/8.
 * Version: 1.0
 * Description:
 */
public class BaseApplication extends FrameworkApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            SDKManager.initSDK(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //初始化皮肤管理器
        SkinManager.getInstance().init(this);
        ARouter.init(this);
        LogManager.i("ddddd","444444444");

    }

}
