package com.koreanair.service;

import java.io.File;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.koreanair.dao.SpParsingMasterDAO;

public class CreateJsonParsingDataService {

	SpParsingMasterDAO spParsingMasterDAO = null;
	
	public CreateJsonParsingDataService()  throws Exception {
		this.spParsingMasterDAO = new SpParsingMasterDAO();
	}
	
	public void createParsingData() throws Exception {
        JsonFactory jsonFactory = new MappingJsonFactory();  
        File jsonFile = new File("D:\\test.json");
        JsonParser jsonParser = jsonFactory.createParser(jsonFile); // json 파서 생성  
		try {
	        int i = 0;
	        while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회  
	            String fieldName = jsonParser.getCurrentName(); // 필드명, 필드값 토큰인 경우 필드명, 나머지 토큰은 null 리턴  
	  
	            if ("recode".equals(fieldName)) {  
	                jsonParser.nextToken(); // 필드값 토큰으로 이동  
	                while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회 
	                	jsonParser.nextToken();
	                	i++;
	                	String treeValue = jsonParser.readValueAsTree().toString();
	                	//System.out.println("recode"+i+" :: " + treeValue);
	                	//JsonUtil.parsingData(treeValue);
	                	//JsonSimpleUtil.parsingData(i, treeValue);
	                	//if(i==6)break;
	                	spParsingMasterDAO.jsonSave(treeValue);
	                	System.out.println("recode"+i+" :: " + treeValue);
	                	//if(i==6)break;
	                }
	            }
	        }
		}finally {
			jsonParser.close(); 
		}
	}
	
	public static void main(String[] args) throws Exception {
		CreateJsonParsingDataService service = new CreateJsonParsingDataService();
		service.createParsingData();
	}
}
