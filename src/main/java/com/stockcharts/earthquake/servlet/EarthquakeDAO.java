package com.stockcharts.earthquake.servlet;

import com.stockcharts.commons.net.RestRequest;
import com.stockcharts.commons.net.RestResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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
        // fetches, parses, puts into response object
        RestResponse response = new RestRequest(EarthquakesServlet.EARTHQUAKES_URL).doGet();
        
        String responseBody = response.getBody();
        
        //System.out.println(responseBody);
        
        JSONObject jo = new JSONObject(response.getBody());
        JSONArray features = jo.getJSONArray("features");
        
        List<Earthquake> earthquakes = new ArrayList<>();
        
        for(int i = 0 ; i < features.length(); i++)
        {
            JSONObject o = features.getJSONObject(i);
            String id = o.getString("id");
            float magnitude = o.getJSONObject("properties").getFloat("mag");
            float latitude = o.getJSONObject("geometry").getJSONArray("coordinates").getFloat(1);
            float longitude = o.getJSONObject("geometry").getJSONArray("coordinates").getFloat(0);
            String place = o.getJSONObject("properties").getString("place");
            long time = o.getJSONObject("properties").getLong("time");
        
        
        earthquakes.add(new Earthquake()
                .withId(id)
                .withMagnitude(magnitude)
                .withLatitude(latitude)
                .withLongitude(longitude)
                .withPlace(place)
                .withTime(time));
        }
        
        return earthquakes;
    }
}
