package com.example.a3dprint.baiumap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.LocationClient;

/**
 * Created by zjs12638 on 2016/9/12.
 */

public class LocationService extends Service {
    public static final String FIFE_NAME = "log.txt";
    LocationClient mLocationClient;
    private Object object = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
