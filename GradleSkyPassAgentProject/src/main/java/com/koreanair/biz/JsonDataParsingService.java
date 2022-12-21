package com.koreanair.biz;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonDataParsingService {
	private final static Logger log = LoggerFactory.getLogger(JsonDataParsingService.class);
    
    
   	
	public void parsingData(HashMap<String, Object> jsonMap, SqlSession sqlSession) throws Exception {
		String jsonString =  (String)jsonMap.get("jsondata"); 			

		
		//log.debug("jsonString :: " + jsonString);
		
		//1. parsing start
		/*
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject)jsonParser.parse(jsonString);
		JSONObject data = (JSONObject)jsonObj.get("data");
		if(data.containsKey("related")) {
			JSONArray totalArry = (JSONArray)data.get("related");
			for(int i = 0; i < totalArry.size(); i++) {
				JSONObject obj = (JSONObject)totalArry.get(i);
			}
		}
		*/
		//parsing end
						


	}
	
	
}
