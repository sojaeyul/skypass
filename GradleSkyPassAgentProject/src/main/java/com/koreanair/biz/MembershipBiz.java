package com.koreanair.biz;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipBiz {
	private final static Logger log = LoggerFactory.getLogger(MembershipBiz.class);
	public MembershipBiz() {
		System.out.println("MembershipBiz 생성자 호출");
	}

	public String doJsonToDBCreate(HashMap<String, Object> jsonMap) {
		System.out.println("membershipBiz.doJsonForDBCreate Call!!!!!!!!!!!!!!!!!!!!!!!!" + jsonMap.toString());
		
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
