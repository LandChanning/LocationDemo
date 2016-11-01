package com.deepai.locationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.deepai.locationdemo.marker.InfoWindowActivity;
import com.deepai.locationdemo.route.RouteActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("测试git----","提交");

    }

    /**
     * 位置标注及信息展示
     */
    public void onLocationInfo(View view) {
        InfoWindowActivity.goInto(this);
    }

    /**
     * 路径规划
     */
    public void onRoutePlan(View view) {
        RouteActivity.goInto(this);
    }

    /**
     * 导航
     */
    public void onGuide(View view) {

    }


}
