package com.koreanair;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
	List<HashMap<String, Object>> jsonList;
	
	public ETLProcessThread(ETLMainJob parent, String threadName, List<HashMap<String, Object>> jsonList){
		this.parent = parent;
		this.threadName = threadName;
		this.jsonList = jsonList;
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}
	
	public String call(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			
            Comparator<HashMap<String, Object>> seqASC = Comparator.comparing((HashMap<String, Object> map) -> (Integer) map.get("seq"));//내림차순
            Comparator<HashMap<String, Object>> membershipresourceidDESC = Comparator.comparing((HashMap<String, Object> map) ->(String)map.get("membershipresourceid")).reversed();//오름차순
            Comparator<HashMap<String, Object>> createdAtDESC = Comparator.comparing((HashMap<String, Object> map) ->(String)map.get("createdat")).reversed();//오름차순
            
            jsonList.sort(Comparator.comparing((HashMap<String, Object> map) -> (String) map.get("membershipid"))
            				.thenComparing(membershipresourceidDESC)
            				.thenComparing(seqASC)
            				.thenComparing(createdAtDESC)
            				);
			
            for(HashMap<String, Object> jsonMap : jsonList) {
            	
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
            }
		}catch(Exception ex){
			log.error("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆ETLProcess Thread ERROR☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆", ex);
		} finally {
	    	sqlSession.commit();
	    	sqlSession.close();
	    }
		
		return threadName + "번 콜라블 종료";
	}
}
