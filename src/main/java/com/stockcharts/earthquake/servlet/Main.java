package com.stockcharts.earthquake.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main {
    
    private static final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mariadb:aurora://scc-intern-db.couiu6erjuou.us-east-1.rds.amazonaws.com:3306/InternDB?user=intern&password=stockcharts2018&trustServerCertificate=true&connectTimeout=5000";
    
    public static void main(String args[])
    {
        try
        {
            Class.forName(DB_DRIVER_CLASS); // load the driver class
        } catch(ClassNotFoundException e) // if we cant find the diver class
        {
            System.out.println("Driver class not found" + e);
            return;
        }
        
        List<Earthquake> earthquakes = new ArrayList();
        earthquakes = getEarthquakes();
               
    }
    
    private static List<Earthquake> getEarthquakes()
    {
        // select * -> select all columns
        String queury = "SELECT * FROM InternDB.Earthquakes WHERE magnitude > 5";
        List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
        
        // trying w resources t connect to database?
        // associating creation of connection
        // try guarantees that it will close, whether successful or not
        try(Connection conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement stmt = conn.prepareStatement(queury)){
            // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            // protecting against sequal injection (from users)
            // ensuring that the queuery doesnt have anything that could
            // hurt our table (sequal injection)
            
            // fetch results set
            ResultSet rs = stmt.executeQuery();
            
//            int numCols = rs.getMetaData().getColumnCount();
//            for(int i = 1; i <= numCols; i++)
//            {
//                System.out.println("column name at " + i + ": " + rs.getMetaData().getColumnName(i));
//            }
            
            while(rs.next())
            {
                
                Earthquake newEarthquake = new Earthquake();
                
                newEarthquake = newEarthquake.withId(rs.getString("id"));
                newEarthquake = newEarthquake.withMagnitude(rs.getFloat("float"));
                newEarthquake = newEarthquake.withLongitude(rs.getFloat("longitude"));
                newEarthquake = newEarthquake.withLatitude(rs.getFloat("latitude"));
                newEarthquake = newEarthquake.withPlace(rs.getString("place"));
                newEarthquake = newEarthquake.withTime(rs.getLong("time"));
              
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
