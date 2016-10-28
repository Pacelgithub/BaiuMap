package com.example.a3dprint.baiumap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zjs12638 on 2016/9/11.
 */

public class MyOrientationListener implements SensorEventListener{
    private SensorManager msensorManager;
    private Context context;
    private Sensor msensor;
    private float LastX;
    public MyOrientationListener(Context context){
        this.context=context;
    }
    public void start(){
        msensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if(msensorManager!=null){
            msensor=msensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(msensor!=null){
            msensorManager.registerListener(this,msensor,SensorManager.SENSOR_DELAY_UI);

        }

    }
    public void stop(){
        msensorManager.unregisterListener(this);

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
            float x=sensorEvent.values[SensorManager.DATA_X];
            if(Math.abs(x-LastX)>0.5){
                if(mOnOrientationListener!=null){
                    mOnOrientationListener.OnOrientationChanged(x);
                }
            }
            LastX=x;

        }
    }

    private OnOrientationListener mOnOrientationListener;
    public void setMonOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }
    public interface OnOrientationListener{
        void OnOrientationChanged(float x);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
