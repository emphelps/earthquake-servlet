package com.stockcharts.earthquake.servlet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Application;

@ApplicationPath("servlet")
public class EarthquakeApplication extends Application{
    
    @PostConstruct
    public void init()
    {
        
    }
    
    @PreDestroy
    public void destroy()
    {
        
    }
    
}
