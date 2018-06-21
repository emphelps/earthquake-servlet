package com.stockcharts.earthquake.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.JSONObject;


public class EarthquakeDAO {
    
    private static final Logger logger = Logger.getLogger(EarthquakeDAO.class.getName());
    
    public static List<Earthquake> getEarthquakesFromDB()
    {
        return getEarthquakesFromDBHelper();
    }
    
    private static List<Earthquake> getEarthquakesFromDBHelper()
    {
        String queury = "SELECT * FROM InternDB.Earthquakes";
        List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
        
        try(Connection conn = DriverManager.getConnection(EarthquakesServlet.DATABASE_URL);
            PreparedStatement stmt = conn.prepareStatement(queury)){
           
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                logger.debug("creating new earthquake");
                Earthquake newEarthquake = new Earthquake()
                        .withId(rs.getString("id"))
                        .withMagnitude((rs.getFloat("magnitude")))
                        .withLatitude((rs.getFloat("latitude")))
                        .withLongitude((rs.getFloat("longitude")))
                        .withPlace((rs.getString("place")))
                        .withTime((rs.getLong("time")));
              
                earthquakeList.add(newEarthquake);
                logger.debug("new earthquake added to earthquakeList");
            }
            
            logger.debug("earthquakeList size: " + earthquakeList.size());
            
        } catch(SQLException e)
        {
            logger.error("error querying database", e);
        }
        
        logger.debug("querying database successful");
        
        return earthquakeList;
    }
    
    
    public static List<Earthquake> getEarthquakesFromFeed() throws IOException
    {
        List<Earthquake> earthquakes = new ArrayList<>();
        
        
        String id = jo.getString("id");
        float magnitude = jo.getJSONObject("properties").getFloat("mag");
        float latitude = jo.getJSONObject("geometry").getJSONArray("coordinates").getFloat(0);
        float longitude = jo.getJSONObject("geometry").getJSONArray("coordinates").getFloat(1);
        String place = jo.getJSONObject("properties").getString("place");
        long time = jo.getJSONObject("properties").getLong("time");
        
        
        earthquakes.add(new Earthquake()
                .withId(id)
                .withMagnitude(magnitude)
                .withLatitude(latitude)
                .withLongitude(longitude)
                .withPlace(place)
                .withTime(time));
        
        return earthquakes;
    }
    
}
