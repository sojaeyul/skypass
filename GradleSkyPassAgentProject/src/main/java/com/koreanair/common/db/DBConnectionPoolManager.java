package com.koreanair.common.db;

import java.io.Reader;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.util.EnvManager;
import com.koreanair.common.util.Environment;

public class DBConnectionPoolManager {
	private final static Logger log = LoggerFactory.getLogger(DBConnectionPoolManager.class);
	
	public static BasicDataSource ds = null;
	
	public DBConnectionPoolManager() throws Exception {
		setupDataSource();
	}
	
	private void setupDataSource() throws Exception{
        Environment env = EnvManager.getEnvironment();        
        
		ds = new BasicDataSource();
		
		ds.setDriverClassName(env.getProperty("db.driver"));
		ds.setUrl(env.getProperty("db.url"));
		ds.setUsername(env.getProperty(".db.username"));
		ds.setPassword(env.getProperty("db.password"));
		ds.setMaxTotal(20);//동시에 사용할 수 있는 최대 커넥션 개수
		ds.setInitialSize(5);//BasicDataSource 클래스 생성 후 최초로 getConnection() 메서드를 호출할 때 커넥션 풀에 채워 넣을 커넥션 개수
		ds.setMaxIdle(20);//커넥션 풀에 반납할 때 최대로 유지될 수 있는 커넥션 개수
		ds.setMaxWaitMillis(5000);
		ds.setPoolPreparedStatements(true);

	}
}
