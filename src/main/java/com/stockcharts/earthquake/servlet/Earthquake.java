package com.stockcharts.earthquake.servlet;

public class Earthquake {
    
    private String id;
    private float magnitude;
    private float latitude;
    private float longitude;
    private String place;
    private long time;
    
    public Earthquake withId(String id)
    {
        this.id = id;
        return this;
    }
    
    public Earthquake withMagnitude(float magnitude)
    {
        this.magnitude = magnitude;
        return this;
    }
    
    public Earthquake withLatitude(float latitude)
    {
        this.latitude = latitude;
        return this;
    }
    
    public Earthquake withLongitude(float longitude)
    {
        this.longitude = longitude;
        return this;
    }
    
    public Earthquake withPlace(String place)
    {
        this.place = place;
        return this;
    }
    
    public Earthquake withTime(long time)
    {
        this.time = time;
        return this;
    }
    
    @Override 
    public String toString()
    {
        return (id + " " + Float.toString(magnitude) + " " + Float.toString(latitude) + " " + Float.toString(longitude) + " " + place + " " + Long.toString(time));
    }
    
}
