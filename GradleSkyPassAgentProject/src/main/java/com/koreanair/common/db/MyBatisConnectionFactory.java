package com.koreanair.common.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.util.EnvManager;
import com.koreanair.common.util.Environment;

public class MyBatisConnectionFactory {
	private final static Logger log = LoggerFactory.getLogger(MyBatisConnectionFactory.class); 
    private static SqlSessionFactory sqlSessionFactory;
 
    static {
        try {        
        	
            String resource = "config/mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);            
 
            if (sqlSessionFactory == null) {
                Environment env = EnvManager.getEnvironment();       
                
                Properties prop = new Properties();
                prop.put("driver", env.getProperty("db.driver"));
                prop.put("url", env.getProperty("db.url"));
                prop.put("username", env.getProperty("db.username"));
                prop.put("password", env.getProperty("db.password"));
                
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
