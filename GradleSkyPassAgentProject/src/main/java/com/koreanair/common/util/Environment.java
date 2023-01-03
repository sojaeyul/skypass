package com.koreanair.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Environment {
	private final static Logger log = LoggerFactory.getLogger(Environment.class);
	private static String DEFAULT_PROPERTIES = "environments.properties";
    private String propertyFile = DEFAULT_PROPERTIES;
    private Properties properties = new Properties();

    private static Context ic = null;

    public static Context getInitialContext() {
        if (ic == null) {
            synchronized (DEFAULT_PROPERTIES) {
                if (ic == null) {
                    try {
                        ic = new InitialContext();
                    } catch (Exception e) {
                        ic = null;
                    }
                }
            }
        }

        return ic;
    }

    Environment() {
        loadProperty();
    }

    public void loadProperty() {
        FileInputStream fis =null;
        InputStream fiss = null;
        try {
    		fiss = getClass().getClassLoader().getResourceAsStream(propertyFile);
    		properties.load(fiss);
        } catch (Exception e) {
        	log.debug("[Environment : loadProperty()] "+propertyFile+" 파일이 CLASSPATH에 있는지 확인하세요");
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fiss != null)
                	fiss.close();                
            } catch (IOException e) {
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public boolean reload() {
        try {
            loadProperty();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}