package com.deepai.locationdemo;

import android.app.Application;

/**
 * Created by hasee on 2016/11/1.
 */

public class MeApplication extends Application {
    private static MeApplication meApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        meApplication = this;
    }

    public static MeApplication getInstance() {
        return meApplication;
    }

}
