package com.deepai.locationdemo.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.deepai.locationdemo.MeApplication;

/**
 * 定位工具类
 */
public class LocationUtil {
    private AMapLocationClient locationClient = null;

    private AMapLocationClientOption locationOption;

    private LocationResultListener resultListener;

    private long intervalTime = 2000;

    private long httpTimeOut = 30000;

    private boolean isNeedAddress = true, isCache = true;

    private boolean isFirstLocation = false, isSingleLocation = true;

    public static final int LOCATION_MODE_BATTERY = 0;

    public static final int LOCATION_MODE_DEVICE = 1;

    public static final int LOCATION_MODE_HIGH = 2;

    public LocationUtil() {
        locationOption = new AMapLocationClientOption();
        // 初始化定位
        initLocation();
    }

    /**
     * 设置定位信息回调接口
     *
     * @param resultListener 回调接口
     */
    public void setResultListener(LocationResultListener resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * 设置定位模式
     *
     * @param modeType 0,1,2
     */
    public void setLocationMode(int modeType) {
        if (null == locationOption) {
            locationOption = new AMapLocationClientOption();
        }
        switch (modeType) {
            case LOCATION_MODE_BATTERY:
                locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
                break;
            case LOCATION_MODE_DEVICE:
                locationOption.setLocationMode(AMapLocationMode.Device_Sensors);
                break;
            case LOCATION_MODE_HIGH:
                locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
                break;
        }
    }

    /**
     * 设置option操作
     *
     * @param isNeedAddress    设置逆地理编码
     * @param isCache          设置缓存
     * @param isFirstLocation  设置是否优先返回GPS定位结果，只有在高精度模式下的单次定位有效
     * @param isSingleLocation 设置是否是单次定位，true,会自动变为单次定位，持续定位时不要使用
     * @param intervalTime     设置发送定位请求的时间间隔，最小值为1000，如果小于1000，按照1000算
     * @param httpTimeOut      设置网络请求超时时间
     */
    public void setLocationOption(boolean isNeedAddress, boolean isCache,
                                  boolean isFirstLocation, boolean isSingleLocation,
                                  long intervalTime, long httpTimeOut) {
        this.isNeedAddress = isNeedAddress;
        this.isCache = isCache;
        this.isFirstLocation = isFirstLocation;
        this.isSingleLocation = isSingleLocation;
        this.intervalTime = intervalTime;
        this.httpTimeOut = httpTimeOut;
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(MeApplication.getInstance().getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        // 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setGpsFirst(isFirstLocation);
        // 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setHttpTimeOut(httpTimeOut);
        // 可选，设置定位间隔。默认为2秒
        mOption.setInterval(intervalTime);
        // 可选，设置是否返回逆地理地址信息。默认是true
        mOption.setNeedAddress(isNeedAddress);
        // 可选，设置是否单次定位。默认是false
        mOption.setOnceLocation(isSingleLocation);
        // 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setOnceLocationLatest(isSingleLocation);
        // 设置是否开启缓存,默认为true
        mOption.setLocationCacheEnable(isCache);
        // 可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);
        // 可选，设置是否使用传感器。默认是false
        // mOption.setSensorEnable(false);
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //返回定位结果
                if (resultListener != null) {
                    resultListener.locationSuccess(loc);
                }
            } else {
                resultListener.locationFail(-1);
            }
        }
    };

    /**
     * 根据控件的选择，重新设置定位参数
     */
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(isNeedAddress);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(isFirstLocation);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(isCache);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(isSingleLocation);
        //设置是否使用传感器
        // locationOption.setSensorEnable();
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(intervalTime);
        // 设置网络请求超时时间
        locationOption.setHttpTimeOut(httpTimeOut);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 返回定位结果接口
     */
    public interface LocationResultListener {
        void locationSuccess(AMapLocation location);

        void locationFail(int failCode);
    }
}
