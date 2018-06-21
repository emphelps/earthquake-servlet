package com.stockcharts.earthquake.servlet;

import java.util.Comparator;
import org.json.JSONObject;

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

    public String getId() {
        return id;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }
    
    @Override 
    public String toString()
    {
        JSONObject jo = new JSONObject(this);
        return jo.toString();
    }
    
    public static Comparator<Earthquake> MAGNITUDE = new Comparator<Earthquake>()
    {
        @Override
        public int compare(Earthquake one, Earthquake two)
        {
            return Float.compare(one.magnitude, two.magnitude);
        }
    };
    
    public static Comparator<Earthquake> TIME = new Comparator<Earthquake>()
    {
        @Override
        public int compare(Earthquake one, Earthquake two)
        {
            return Long.compare(one.time, two.time);
        }
    };
    
    public static Comparator<Earthquake> LATITUDE = new Comparator<Earthquake>()
    {
        @Override
        public int compare(Earthquake one, Earthquake two)
        {
            return Float.compare(one.latitude, two.latitude);
        }
    };
    
    public static Comparator<Earthquake> LONGITUDE = new Comparator<Earthquake>()
    {
        @Override
        public int compare(Earthquake one, Earthquake two)
        {
            return Float.compare(one.longitude, two.longitude);
        }
    };
    
}