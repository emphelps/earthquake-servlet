package com.stockcharts.earthquake.servlet;

import com.google.common.cache.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.*;
import org.json.JSONObject;


public class EarthquakesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EarthquakesServlet.class.getName());
    private static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mariadb:aurora://scc-intern-db.couiu6erjuou.us-east-1.rds.amazonaws.com:3306/InternDB?user=intern&password=stockcharts2018&trustServerCertificate=true&connectTimeout=5000";
    public static final String EARTHQUAKES_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    
    private Cache<String, List<Earthquake>> earthquakeCache;
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        
        logger.warn("==================================================");
        logger.warn("           earthquake-servlet : init()");
        logger.warn("==================================================");
        
        logger.warn("loading driver class...");
        try
        {
            Class.forName(DB_DRIVER_CLASS); // load the driver class
        } catch(ClassNotFoundException e) // if we cant find the diver class
        {
            logger.fatal("Driver class not found ", e);
            throw new UnavailableException("Driver class not found");
        }
        logger.warn("...driver class loaded");
        
        logger.warn("Setting up Guava Cache...");
        earthquakeCache = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build();
        logger.warn("...success");
        
        logger.warn("==================================================");
        logger.warn("           earthquake-servlet : init() - COMPLETE");
        logger.warn("==================================================");
    }
    
    @Override
    public void destroy()
    {
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        logger.warn("           earthquake-servlet : destroy()");
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        logger.warn("           earthquake-servlet : destroy() - COMPLETE");
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        //List<Earthquake> earthquakes = EarthquakeDAO.getEarthquakesFromDB();
        
        List<Earthquake> earthquakes = earthquakeCache.getIfPresent("all");
       
        if(earthquakes == null)
        {
            try{
                earthquakes = EarthquakeDAO.getEarthquakesFromFeed();
                earthquakeCache.put("all", earthquakes);
            }
            catch(IOException e)
            {
                logger.error("IOException reading from Earthquakes Feed", e);
            }
        }
        
        String requestVal = request.getParameter("sort");
        
        logger.debug("requestVal: " + requestVal);
        
        if(requestVal == null) requestVal = "";
        
        switch(requestVal)
        {
            case "magnitude":
            {
                Collections.sort(earthquakes, Earthquake.MAGNITUDE);
                break;
            }
            
            case "latitude":
            {
                Collections.sort(earthquakes, Earthquake.LATITUDE);
                break;
            }
            
            case "longitude":
            {
                Collections.sort(earthquakes, Earthquake.LONGITUDE);
                break;
            }

            case "time":
            {
                Collections.sort(earthquakes, Earthquake.TIME);
                break;
            }

            default:
            {
                break;
            }
        }
        
        logger.debug("earthquakes list size: " + earthquakes.size());
        JSONArray ja = new JSONArray(earthquakes);
        
        
        logger.debug("JSONArray length: " + ja.length());
        
        try(PrintWriter out = response.getWriter())
        {
            response.setHeader("Connection", "close");
            response.setContentType("application/json");
            
            out.print(ja.toString());
            out.flush();
            
        } catch(IOException e)
        {
            logger.error("Error writing response to client", e);
        }
        
        logger.debug("successfully wrote response to client");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        
    }
    
}
