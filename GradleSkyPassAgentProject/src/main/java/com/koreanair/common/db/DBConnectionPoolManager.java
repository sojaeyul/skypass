package com.koreanair.common.db;

import java.io.Reader;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionPoolManager {
	private final static Logger log = LoggerFactory.getLogger(DBConnectionPoolManager.class);
	
	public static BasicDataSource ds = null;
	
	public DBConnectionPoolManager() throws Exception {
		setupDataSource();
	}
	
	private void setupDataSource() throws Exception{
		String envResource = "config/environments.properties";            
		Reader envReader = Resources.getResourceAsReader(envResource);

    
        Properties properties = new Properties();
        properties.load(envReader);
        
        String skypassInstance = System.getProperty("skypass.instance");
        log.debug(String.format("instance name ==> [%s]", skypassInstance));
        
		ds = new BasicDataSource();
		
		ds.setDriverClassName(properties.getProperty(skypassInstance+".db.driver"));
		ds.setUrl(properties.getProperty(skypassInstance+".db.url"));
		ds.setUsername( properties.getProperty(skypassInstance+".db.username"));
		ds.setPassword(properties.getProperty(skypassInstance+".db.password"));
		ds.setMaxTotal(20);//동시에 사용할 수 있는 최대 커넥션 개수
		ds.setInitialSize(5);//BasicDataSource 클래스 생성 후 최초로 getConnection() 메서드를 호출할 때 커넥션 풀에 채워 넣을 커넥션 개수
		ds.setMaxIdle(20);//커넥션 풀에 반납할 때 최대로 유지될 수 있는 커넥션 개수
		ds.setMaxWaitMillis(5000);
		ds.setPoolPreparedStatements(true);

	}
}
