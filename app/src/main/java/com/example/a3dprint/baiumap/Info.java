package com.example.a3dprint.baiumap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjs12638 on 2016/9/12.
 */

public class Info implements Serializable{
    private static final long serialVersionUID=-123456734;
    private double latitude;
    private double longitude;
    private String name;
    private int ImgId;
    private String gender;
    private String distance;
    private Info(double latitude,double longitude,String name,int ImgId,String gender,String distance){
        this.latitude=latitude;
        this.longitude=longitude;
        this.ImgId=ImgId;
        this.name=name;
        this.gender=gender;
        this.distance=distance;
    }
    public static List<Info> infos =new ArrayList<Info>();
    static {
        infos.add(new Info(39.994028,116.234233,"人生",R.drawable.icon_map,"female","1456"));
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImgId() {
        return ImgId;
    }

    public void setImgId(int imgId) {
        ImgId = imgId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }




}
