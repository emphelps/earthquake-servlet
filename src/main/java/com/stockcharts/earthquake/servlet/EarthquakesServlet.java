package com.stockcharts.earthquake.servlet;

import static com.stockcharts.earthquake.servlet.Main.DATABASE_URL;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.*;
import org.json.JSONObject;


public class EarthquakesServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(EarthquakesServlet.class.getName());

    @Override
    public  void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        
        logger.warn("==================================================");
        logger.warn("           sample-servlet : init()");
        logger.warn("==================================================");
        
        logger.warn("==================================================");
        logger.warn("           sample-servlet : init() - COMPLETE");
        logger.warn("==================================================");
    }
    
    @Override
    public void destroy()
    {
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        logger.warn("           sample-servlet : destroy()");
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        logger.warn("           sample-servlet : destroy() - COMPLETE");
        logger.warn("<<<<<<<<<<<<<<<<<<<< ######## >>>>>>>>>>>>>>>>>>>>");
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        List<Earthquake> earthquakes = getEarthquakes();
        JSONArray ja = new JSONArray();
        
        for(int i = 0; i < earthquakes.size(); i++)
        {
            ja.put(new JSONObject(earthquakes.get(i)));
        }
        
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        
    }
    
    private static List<Earthquake> getEarthquakes()
    {
        String queury = "SELECT * FROM InternDB.Earthquakes";
        List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
        
        try(Connection conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement stmt = conn.prepareStatement(queury)){
           
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
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
