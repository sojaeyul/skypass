package com.koreanair.common.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionService {
	public static Connection getConnection() throws Exception{
		Connection conn = null;
		if(DBConnectionPoolManager.ds == null) {
			new DBConnectionPoolManager();
		}
		conn = DBConnectionPoolManager.ds.getConnection();
		
		return conn;
	}
	
	public static void connectionClose(Connection conn)  throws SQLException{
		if (conn !=null) {
			conn.close();
			conn=null;
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			Connection conn = getConnection();
			System.out.println("conn :: "+ conn);
			
			System.out.println("1 :: "+ DBConnectionPoolManager.ds.getNumIdle());
			connectionClose(conn);			
			System.out.println("2 :: "+ DBConnectionPoolManager.ds.getNumIdle());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

}
