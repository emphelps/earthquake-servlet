package com.stockcharts.earthquake.servlet;

import java.io.IOException;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.json.JSONArray;

@Path("data")
public class EarthquakesResource {
    
    private final Logger logger = Logger.getLogger(EarthquakesResource.class.getName());
    
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEarthquakes()
    {
        List<Earthquake> earthquakes = new LinkedList<>();
        
        try{
            earthquakes = EarthquakeDAO.getEarthquakesFromFeed();
            
        }catch(IOException e)
        {
            logger.error("error reading earthquakes from feed", e);
        }
        
        return new JSONArray(earthquakes).toString();
    }
    
}
