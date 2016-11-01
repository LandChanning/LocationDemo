package com.deepai.locationdemo;

import android.app.Application;

import com.amap.api.navi.AMapNavi;

/**
 * Created by hasee on 2016/11/1.
 */

public class MeApplication extends Application {
    private static MeApplication meApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 导航
         * setApiKey是静态方法,内部引用了Context，建议放在Application中
         * 如果你在meta-data中配置了key，那么以meta-data中的为准，此行代码
         * 可以忽略，这个方法主要是为那些不想在xml里配置key的用户使用。
         * **/
        AMapNavi.setApiKey(this, "cebb2b92b5a1687b65695581b2588a07");
        meApplication = this;
    }

    public static MeApplication getInstance() {
        return meApplication;
    }

}
