package com.deepai.locationdemo.marker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.deepai.locationdemo.R;
import com.deepai.locationdemo.util.LocationUtil;
import com.deepai.locationdemo.util.ToastUtil;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class InfoWindowActivity extends Activity implements OnClickListener,
        LocationUtil.LocationResultListener {
    private MarkerOptions markerOption;

    private AMap aMap;

    private MapView mapView;

    private Marker marker;

    private AMapLocation aMapLocation;

    private LocationUtil locationUtil;

    public static void goInto(Context context) {
        Intent intent = new Intent(context, InfoWindowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_activity);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
        // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        Button clearMap = (Button) findViewById(R.id.clearMap);
        clearMap.setOnClickListener(this);
        Button resetMap = (Button) findViewById(R.id.resetMap);
        resetMap.setOnClickListener(this);
        locationUtil = new LocationUtil();
        locationUtil.setResultListener(this);
        locationUtil.startLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        locationUtil.stopLocation();
        super.onStop();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        locationUtil.destroyLocation();
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        LatLng latLng;
        String titleTip;
        String detailsTip;
        if (aMapLocation != null) {
            latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            titleTip = aMapLocation.getCity();
            detailsTip = aMapLocation.getAddress();
        } else {
            latLng = new LatLng(39.0, 116.0);
            titleTip = "标题";
            detailsTip = "详情";
        }
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latLng)
                .title(titleTip)
                .snippet(detailsTip)
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 清空地图上所有已经标注的marker
             */
            case R.id.clearMap:
                if (aMap != null) {
                    aMap.clear();
                }
                break;
            /**
             * 重新标注所有的marker
             */
            case R.id.resetMap:
                if (aMap != null) {
                    aMap.clear();
                    addMarkersToMap();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void locationSuccess(AMapLocation location) {
        this.aMapLocation = location;
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker != null) {
                        marker.hideInfoWindow();
                    }
                    return false;
                }
            });
            // 往地图上添加marker
            addMarkersToMap();
        }
    }

    @Override
    public void locationFail(int failCode) {
        ToastUtil.show(this, "定位失败");
    }
}
