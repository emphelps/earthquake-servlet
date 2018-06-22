package com.stockcharts.earthquake.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import com.stockcharts.commons.net.*;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    
    private static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    private static final String EARTHQUAKES_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    public static final String DATABASE_URL = "jdbc:mariadb:aurora://scc-intern-db.couiu6erjuou.us-east-1.rds.amazonaws.com:3306/InternDB?user=intern&password=stockcharts2018&trustServerCertificate=true&connectTimeout=5000";
    
    public static void main(String args[]) throws IOException
    {
      
        List<Earthquake> earthquakes = EarthquakeDAO.getEarthquakesFromFeed();
        
        Collections.sort(earthquakes, Earthquake.MAGNITUDE);
        System.out.println(earthquakes.toString());
               
    }
    
    private static List<Earthquake> getEarthquakes()
    {
        // select * -> select all columns
        String queury = "SELECT * FROM InternDB.Earthquakes";
        List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
        
        // trying w resources t connect to database?
        // associating creation of connection
        // try guarantees that it will close, whether successful or not
        try(Connection conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement stmt = conn.prepareStatement(queury)){
            // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            // protecting against sql injection (from users)
            // ensuring that the queuery doesnt have anything that could
            // hurt our table (sql injection)
            
            // fetch results set
            ResultSet rs = stmt.executeQuery();
            
//            int numCols = rs.getMetaData().getColumnCount();
//            for(int i = 1; i <= numCols; i++)
//            {
//                System.out.println("column name at " + i + ": " + rs.getMetaData().getColumnName(i));
//            }
            
            while(rs.next())
            {
                // use fluid constructors
                Earthquake newEarthquake = new Earthquake()
                        .withId(rs.getString("id"))
                        .withMagnitude((rs.getFloat("magnitude")))
                        .withLatitude((rs.getFloat("latitude")))
                        .withLongitude((rs.getFloat("longitude")))
                        .withPlace((rs.getString("place")))
                        .withTime((rs.getLong("time")));
              
                earthquakeList.add(newEarthquake);
            }
            
        } catch(SQLException e)
        {
            System.out.println("error querying database" + e);
        }
        
        return earthquakeList;
    }
    
}

// creat earthquake class - fluid constructor????
// create method that returns list of fearquakes...rn select *, no magnitude > 5 for rn
