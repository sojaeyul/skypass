package com.koreanair.biz;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.db.MyBatisConnectionFactory;

public class BillingsBiz {
	private final static Logger log = LoggerFactory.getLogger(BillingsBiz.class);	
	private SqlSessionFactory sqlSessionFactory = null;
	
	public BillingsBiz() {
		//log.debug("BillingsBiz 생성자 호출");
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public boolean doJsonToDBCreate(HashMap<String, Object> jsonMap, String spec, String nodeRoot) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		boolean bi = false;
		try {
			//1. parsing start		
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParser.parse((String)jsonMap.get("jsondata"));
			JSONObject mdeData = (JSONObject)jsonObj.get("serviceMDEData");
			
			JSONArray root = (JSONArray)mdeData.get(nodeRoot);
	
			for (Object obj : root) {
				JSONObject data = (JSONObject)obj;
				if(data.containsKey("data")) {
					//log.debug(data.toJSONString());
				}
			}     
			//parsing end
		
			bi=true;
			sqlSession.commit();
		}catch(Exception ex){
			sqlSession.rollback();
			throw ex;
		} finally {
	    	sqlSession.close();
	    }
		
		return bi;
	}
}
