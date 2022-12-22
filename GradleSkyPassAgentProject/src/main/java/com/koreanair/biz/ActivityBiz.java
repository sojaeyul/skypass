package com.koreanair.biz;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityBiz {
	private final static Logger log = LoggerFactory.getLogger(ActivityBiz.class);
	public ActivityBiz() {
		log.debug("ActivityBiz 생성자 호출");
	}

	public boolean doJsonToDBCreate(SqlSession sqlSession, HashMap<String, Object> jsonMap, String spec, String nodeRoot) throws Exception {

		//1. parsing start		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject)jsonParser.parse((String)jsonMap.get("jsondata"));
		JSONArray root = (JSONArray)jsonObj.get(nodeRoot);

		for (Object obj : root) {
			JSONObject data = (JSONObject)obj;
			if(data.containsKey("data")) {
				log.debug(data.toJSONString());
			}
		}		
		//parsing end
		
		return true;
	}
}
