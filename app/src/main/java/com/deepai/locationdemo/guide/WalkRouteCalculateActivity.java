package com.deepai.locationdemo.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.NaviLatLng;
import com.deepai.locationdemo.R;


public class WalkRouteCalculateActivity extends BaseActivity {

    public static void goInto(Context context) {
        Intent intent = new Intent(context, WalkRouteCalculateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mAMapNavi.calculateWalkRoute(new NaviLatLng(39.925846, 116.435765), new NaviLatLng(39.925846, 116.532765));

    }
}
