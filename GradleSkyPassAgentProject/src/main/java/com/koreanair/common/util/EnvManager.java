package com.koreanair.common.util;

public class EnvManager {

    private static EnvManager instance = null;
    
    private final Environment env;
    
    private EnvManager() {
        env = new Environment();
    }
    
    public synchronized static EnvManager getInstance() {
        if(instance == null) {
            instance = new EnvManager();
        }
        return instance;
    }
    
    public static Environment getEnvironment() {
        return getInstance().env; 
    }
    
    public static String getProperty(String key) {
        String value = getEnvironment().getProperty(key);
        if(value != null) {
            return value.trim();
        }
        return null;
    }
}