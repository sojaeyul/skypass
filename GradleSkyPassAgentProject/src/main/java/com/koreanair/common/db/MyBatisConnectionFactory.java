package com.koreanair.common.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisConnectionFactory {
 
    private static SqlSessionFactory sqlSessionFactory;
 
    static {
        try {
        	
            String envResource = "config/environments.properties";            
            Reader envReader = Resources.getResourceAsReader(envResource);            
        	
            String resource = "config/mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);            
 
            if (sqlSessionFactory == null) {
            	String skypassInstance = System.getProperty("skypass.instance");
            	
                Properties properties = new Properties();
                properties.load(envReader);
                
                Properties prop = new Properties();
                prop.put("driver", properties.getProperty(skypassInstance+".db.driver"));
                prop.put("url", properties.getProperty(skypassInstance+".db.url"));
                prop.put("username", properties.getProperty(skypassInstance+".db.username"));
                prop.put("password", properties.getProperty(skypassInstance+".db.password"));
                
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, prop);
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
