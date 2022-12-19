package com.koreanair.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.koreanair.dao.SpParsingMasterDAO;
import com.koreanair.dao.SpParsingMasterLogDAO;

import com.koreanair.common.db.MyBatisConnectionFactory;

public class JsonDataParsingService {
    private SqlSessionFactory sqlSessionFactory = null;
    
    public JsonDataParsingService(){
        this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    }	
	public void parsingData(HashMap<String, Object> jsonMap) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		String jsonString =  (String)jsonMap.get("jsondata"); 			
		try {
			jsonString = jsonString.substring(1,jsonString.length()-1);
			
			//1. parsing start
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(jsonString);
			JSONObject data = (JSONObject)jsonObj.get("data");
			if(data.containsKey("related")) {
				JSONArray totalArry = (JSONArray)data.get("related");
				for(int i = 0; i < totalArry.size(); i++) {
					JSONObject obj = (JSONObject)totalArry.get(i);
				}
			}
			//parsing end
						
			//2. log insert
			SpParsingMasterLogDAO logDAO = new SpParsingMasterLogDAO();
			logDAO.jsonSave(sqlSession, jsonMap);
			//logDAO.jsonDelete(sqlSession, seq); 
			
			//1. master delete
			SpParsingMasterDAO masterDAO = new SpParsingMasterDAO();
			masterDAO.jsonDelete(sqlSession, jsonMap);
			//masterDAO.jsonSave(sqlSession, jsonString);

		}catch(Exception ex) {
			ex.printStackTrace();
		} finally {
	    	sqlSession.commit();
	    	sqlSession.close();
	    }
	}
	
	
}
