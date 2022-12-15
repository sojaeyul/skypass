package com.koreanair.common.db;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBConnectionPoolManager {
	public static String dbServer;
	public static String user;
	public static String pw;
	
	public static BasicDataSource ds = null;
	
	public DBConnectionPoolManager() {
		setupDataSource();
	}
	
	private void setupDataSource() {
		ds = new BasicDataSource();
		String url = dbServer;
		String className = "org.postgresql.Driver";
		String userName =user;
		String passWord = pw;
		
		ds.setDriverClassName(className);
		ds.setUrl(url);
		ds.setUsername(userName);
		ds.setPassword(passWord);
		ds.setMaxTotal(20);//동시에 사용할 수 있는 최대 커넥션 개수
		ds.setInitialSize(5);//BasicDataSource 클래스 생성 후 최초로 getConnection() 메서드를 호출할 때 커넥션 풀에 채워 넣을 커넥션 개수
		ds.setMaxIdle(20);//커넥션 풀에 반납할 때 최대로 유지될 수 있는 커넥션 개수
		ds.setMaxWaitMillis(5000);
		ds.setPoolPreparedStatements(true);
	}
}
