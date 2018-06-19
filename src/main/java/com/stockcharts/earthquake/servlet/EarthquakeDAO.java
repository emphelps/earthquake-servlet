package com.stockcharts.earthquake.servlet;

import static com.stockcharts.earthquake.servlet.EarthquakesServlet.DATABASE_URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


public class EarthquakeDAO {
    
    private static final Logger logger = Logger.getLogger(EarthquakeDAO.class.getName());
    
    public static List<Earthquake> getEarthquakes()
    {
        String queury = "SELECT * FROM InternDB.Earthquakes";
        List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
        
        try(Connection conn = DriverManager.getConnection(DATABASE_URL);
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
    
}
