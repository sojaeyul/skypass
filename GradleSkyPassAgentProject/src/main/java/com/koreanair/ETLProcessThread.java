package com.koreanair;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.koreanair.service.JsonDataParsingService;

public class ETLProcessThread implements Callable<String>{ 
	String threadName;
	ETLMainJob parent;
	HashMap<String, Object> jsonMap;
	
	public ETLProcessThread(ETLMainJob parent, String threadName, HashMap<String, Object> jsonMap){
		this.parent = parent;
		this.threadName = threadName;
		this.jsonMap = jsonMap;
	}
	
	public String call(){
		try{
			//1. parsing
			JsonDataParsingService parsingService = new JsonDataParsingService();
			parsingService.parsingData(jsonMap);			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return threadName + "번 콜라블 종료";
	}
}
