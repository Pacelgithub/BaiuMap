package com.example.a3dprint.baiumap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.net.InetAddresses;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //定位相关
    private LocationClient mLocationClient;
    MyLocationListener mLocationListener;
    private boolean isFirstIn =true;
    private Context context;
    private double latitude;
    private double longitude;
    private BitmapDescriptor bitmapDescriptor;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private MyLocationConfiguration.LocationMode mLocationMode;
    //实时位置共享相关
    public boolean ShareLocation=true;
    public boolean ReceiveLocation=true;
    //覆盖物相关
    private BitmapDescriptor mMarker1;
    private BitmapDescriptor mMarker2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        this.context=this;
        initView();
        initLocation();
        initmarker();
        StartLocationSevice();
        StartLocationShare();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle l =new Bundle();
                Info info= (Info) l.getSerializable("info");
                return true;
            }
        });

    }
    private void initView(){
        mMapView=(MapView)findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu= MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }
    private void initLocation(){
        mLocationClient=new LocationClient(this);
        mLocationListener=new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option =new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.icon_mlocation);
        myOrientationListener=new MyOrientationListener(context);
        myOrientationListener.setMonOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void OnOrientationChanged(float x) {
                mCurrentX=x;
            }
        });
    }
    private void initmarker(){
        mMarker1=BitmapDescriptorFactory.fromResource(R.drawable.icon_malemark);
        mMarker2=BitmapDescriptorFactory.fromResource(R.drawable.icon_femalemark);


    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted())
        mLocationClient.start();
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public void StartLocationSevice(){
        Intent intent= new Intent(getApplicationContext(),LocationService.class);
        startService(intent);
    }
    public void StopLocationService(){
        Intent intent = new Intent(getApplicationContext(),LocationService.class);
        stopService(intent);
    }
    public void StartLocationShare(){
        Intent intent =new Intent(getApplicationContext(),LocationShare.class);
        startService(intent);
    }
    public void StopLocationShare(){
        Intent intent =new Intent(getApplicationContext(),LocationShare.class);
        stopService(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_myLocation:
                center2myLocation(latitude,longitude);
                break;
            case R.id.action_share:
                Intent intent=new Intent(this,ShareAction.class);
                startActivity(intent);
                break;
            case R.id.action_mark:
                break;
            case R.id.LocMode_common:
                mLocationMode= MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case R.id.LocMode_compass:
                mLocationMode= MyLocationConfiguration.LocationMode.COMPASS;
                break;
            case R.id.MapMode_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.MapMode_satellite:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.MapMode_traffic:
                if(mBaiduMap.isTrafficEnabled()){
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通(off)");

                }else
                {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通(on)");
                }
                break;
            case R.id.AllowToReceive:
                if (ReceiveLocation) {
                    ReceiveLocation = false;
                    item.setTitle(R.string.NoAllowToReceive);
                    Intent intent1= new Intent(this,LocationService.class);
                    intent1.putExtra("message","fk");
                    stopService(intent1);
                }else{
                    ReceiveLocation = true;
                    item.setTitle(R.string.AllowToReceive);
                }
                break;
            case R.id.AllowToShare:
                if(ShareLocation){
                    ReceiveLocation=false;
                    item.setTitle(R.string.NoAllowToShare);
                }else{
                    ReceiveLocation=true;
                    item.setTitle(R.string.AllowToShare);
                }
                break;
            case R.id.addon:
                addOverlays(Info.infos);
                break;
            default:
                break;
        }
        super.onOptionsItemSelected(item);
        return true;
    }
//添加覆盖物
    private void addOverlays(List<Info> infos) {
        mBaiduMap.clear();
        LatLng latlng=null;
        BitmapDescriptor mMarker=null;
        Marker marker =null;
        OverlayOptions options;
        for(Info info:infos){
            if(info.getGender().equals("male")){
                mMarker=mMarker1;
            }else{
                mMarker=mMarker2;
            }
            latlng=new LatLng(info.getLatitude(),info.getLongitude());
            options=new MarkerOptions().position(latlng).icon(mMarker).zIndex(5);
            marker= (Marker) mBaiduMap.addOverlay(options);
            Bundle l =new Bundle();
            l.putSerializable("info",info);
            marker.setExtraInfo(l);
        }
        MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.setMapStatus(msu);



    }

    private void center2myLocation(double latitude,double longitude){
        LatLng latLng =new LatLng(latitude,longitude);
        MapStatusUpdate msu =MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);

    }
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data= new MyLocationData.Builder()
                    .direction(mCurrentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
            MyLocationConfiguration config=new MyLocationConfiguration(mLocationMode,true,bitmapDescriptor);
            mBaiduMap.setMyLocationConfigeration(config);
            latitude=bdLocation.getLatitude();
            longitude=bdLocation.getLongitude();
            if(isFirstIn){
                LatLng latLng =new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate msu =MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn =false;
                //Toast.makeText(context,bdLocation.getLongitude()+"",Toast.LENGTH_LONG).show();

            }
        }
    }
}

