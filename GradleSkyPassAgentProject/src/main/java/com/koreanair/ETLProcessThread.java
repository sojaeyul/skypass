package com.koreanair;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.biz.JsonDataParsingService;
import com.koreanair.common.db.MyBatisConnectionFactory;
import com.koreanair.dao.SpParsingMasterDAO;
import com.koreanair.dao.SpParsingMasterLogDAO;

public class ETLProcessThread implements Callable<String>{ 
	private final static Logger log = LoggerFactory.getLogger(ETLProcessThread.class);
	
	private SqlSessionFactory sqlSessionFactory = null;
	String threadName;
	ETLMainJob parent;
	HashMap<String, Object> jsonMap;
	
	public ETLProcessThread(ETLMainJob parent, String threadName, HashMap<String, Object> jsonMap){
		this.parent = parent;
		this.threadName = threadName;
		this.jsonMap = jsonMap;
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}
	
	public String call(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			//1. parsing
			JsonDataParsingService parsingService = new JsonDataParsingService();
			parsingService.parsingData(jsonMap);			
			
			
			//2. log insert
			SpParsingMasterLogDAO logDAO = new SpParsingMasterLogDAO();
			logDAO.jsonSave(sqlSession, jsonMap);
			//logDAO.jsonDelete(sqlSession, seq); 
			
			//3. master delete
			SpParsingMasterDAO masterDAO = new SpParsingMasterDAO();
			masterDAO.jsonDelete(sqlSession, jsonMap);
			//masterDAO.jsonSave(sqlSession, jsonString);
			
		}catch(Exception ex){
			log.error("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆ETLProcess Thread ERROR☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆", ex);
		} finally {
	    	sqlSession.commit();
	    	sqlSession.close();
	    }
		
		return threadName + "번 콜라블 종료";
	}
}
