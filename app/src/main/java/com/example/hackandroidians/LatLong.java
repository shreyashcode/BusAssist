package com.example.hackandroidians;

public class LatLong
{
    public Double lat;
    public Double Longitude;
    public String Name;

    public LatLong(String Name, Double lat, Double Longitude)
    {
        this.Name = Name;
        this.lat = lat;
        this.Longitude = Longitude;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return Longitude;
    }

    public void setLong(Double aLong) {
        Longitude = aLong;
    }
}
