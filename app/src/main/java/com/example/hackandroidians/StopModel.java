package com.example.hackandroidians;

import com.google.type.LatLng;

public class StopModel
{
    public String name;
    public LatLng latLng;
    public StopModel(String name, LatLng latLng)
    {
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}