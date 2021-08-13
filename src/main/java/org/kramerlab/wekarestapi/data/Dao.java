package org.kramerlab.wekarestapi.data;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.experiment.InstanceQuery;

public class Dao {

    private static String  dbName;
    private static String  dbHost;
    private static Integer dbPort;
    private static String  dbUrl;
    private static String  dbPass;
    private static String  dbUser;

    private static Properties dbProperties = new Properties();
    private static final Logger LOG = Logger.getLogger(Dao.class.getName());

    public Dao() throws SQLException {
        //jdbc:postgresql://128.199.1.222:5432/delati
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("config/db.properties");                
        try {
            dbProperties.load(is);                                                       
            dbName = dbProperties.getProperty("db.name");
            dbHost = dbProperties.getProperty("db.host");                        
            dbPort = Integer.parseInt(dbProperties.getProperty("db.port")); 
            dbUrl="jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;                                         
            if (dbProperties.getProperty("db.user") != null) 
                dbUser=dbProperties.getProperty("db.user");
            if (dbProperties.getProperty("db.password") != null) 
                dbPass=dbProperties.getProperty("db.password");                                    
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "No DB properties file found!", ex);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "Database configuration can not be loaded!");
        }          
    }
    
    public InstanceQuery setConnectionDB(){
        InstanceQuery iQ = null;        
        try {       
            iQ = new InstanceQuery();
            iQ.setDatabaseURL(dbUrl);
            iQ.setUsername(dbUser);
            iQ.setPassword(dbPass);            
            iQ.connectToDatabase(); 
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);                        
        }
        
        return iQ;
    }    
}