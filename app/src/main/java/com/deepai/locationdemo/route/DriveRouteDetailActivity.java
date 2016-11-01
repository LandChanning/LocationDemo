package com.deepai.locationdemo.route;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.deepai.locationdemo.R;
import com.deepai.locationdemo.guide.SingleRouteCalculateActivity;
import com.deepai.locationdemo.route.adapter.DriveSegmentListAdapter;
import com.deepai.locationdemo.util.AMapUtil;


public class DriveRouteDetailActivity extends Activity {
    private DrivePath mDrivePath;
    private DriveRouteResult mDriveRouteResult;
    private TextView mTitle, mTitleDriveRoute, mDesDriveRoute;
    private ListView mDriveSegmentList;
    private DriveSegmentListAdapter mDriveSegmentListAdapter;
    private LinearLayout llGuideTip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        getIntentData();
        init();
    }

    private void init() {
        mTitle = (TextView) findViewById(R.id.title_center);
        mTitleDriveRoute = (TextView) findViewById(R.id.firstline);
        mDesDriveRoute = (TextView) findViewById(R.id.secondline);
        mTitle.setText("驾车路线详情");
        String dur = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mDrivePath
                .getDistance());
        mTitleDriveRoute.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
        mDesDriveRoute.setText("打车约" + taxiCost + "元");
        mDesDriveRoute.setVisibility(View.VISIBLE);
        llGuideTip = (LinearLayout) findViewById(R.id.title_guide_tip);
        llGuideTip.setVisibility(View.VISIBLE);
        configureListView();
    }

    private void configureListView() {
        mDriveSegmentList = (ListView) findViewById(R.id.bus_segment_list);
        mDriveSegmentListAdapter = new DriveSegmentListAdapter(
                this.getApplicationContext(), mDrivePath.getSteps());
        mDriveSegmentList.setAdapter(mDriveSegmentListAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mDrivePath = intent.getParcelableExtra("drive_path");
        mDriveRouteResult = intent.getParcelableExtra("drive_result");
    }

    /**
     * 去导航点击
     */
    public void onGuideTipClick(View view) {
        SingleRouteCalculateActivity.goInto(this);
    }

    public void onBackClick(View view) {
        this.finish();
    }
}
