package com.koreanair.biz;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityBiz {
	private final static Logger log = LoggerFactory.getLogger(ActivityBiz.class);
	public ActivityBiz() {
		System.out.println("ActivityBiz 생성자 호출");
	}

	public String doJsonToDBCreate(HashMap<String, Object> jsonMap) {
		System.out.println("ActivityBiz.doJsonForDBCreate Call!!!!!!!!!!!!!!!!!!!!!!!!" + jsonMap.toString());
		
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
		
		return "OK";
	}
}
