package com.koreanair.service;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.koreanair.common.util.StringUtil;
import com.koreanair.dao.SpParsingMasterDAO;

public class CreateJsonParsingDataService {
	private final static Logger log = LoggerFactory.getLogger(CreateJsonParsingDataService.class);
	
	private String jsonFilePath="";
	SpParsingMasterDAO spParsingMasterDAO = null;
	
	public CreateJsonParsingDataService()  throws Exception {
		this.spParsingMasterDAO = new SpParsingMasterDAO();
		
		String envResource = "config/environments.properties";            
		Reader envReader = Resources.getResourceAsReader(envResource);
        Properties properties = new Properties();
        properties.load(envReader);
        String skypassInstance = System.getProperty("skypass.instance");
        jsonFilePath = properties.getProperty(skypassInstance+".json.file.path");
	}
	
	
	public void createMoveParsingData() throws Exception {
        
        JsonFactory jsonFactory = new MappingJsonFactory();  
        File jsonFile = new File(jsonFilePath);
        JsonParser jsonParser = jsonFactory.createParser(jsonFile); // json 파서 생성  
        try {

	        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        	String fieldName = jsonParser.getCurrentName(); // 필드명, 필드값 토큰인 경우 필드명, 나머지 토큰은 null 리턴      
	        	//log.debug("fieldName :: " + fieldName );        	
	        	if ("id".equals(StringUtil.NVL(fieldName)) 
	        			|| "batchInstanceId".equals(StringUtil.NVL(fieldName))
	        			|| "seqNumber".equals(StringUtil.NVL(fieldName))
	        			|| "loyaltySystem".equals(StringUtil.NVL(fieldName))
	        			|| "partner".equals(StringUtil.NVL(fieldName))
	        			|| "service".equals(StringUtil.NVL(fieldName))
	        			|| "additionalData".equals(StringUtil.NVL(fieldName))
	        			|| "recordsCount".equals(StringUtil.NVL(fieldName))        			
	        			|| "statistics".equals(StringUtil.NVL(fieldName))
	        			|| "businessStatistics".equals(StringUtil.NVL(fieldName))
	        		) {  
	        		jsonParser.nextToken();  
	        		jsonParser.readValueAsTree();
	        		//log.debug("fieldName :: " + fieldName + " :: " +jsonParser.readValueAsTree());        	
	        	}else if ("records".equals(StringUtil.NVL(fieldName))) {
	        		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	        			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        				String recordsSubfieldName = jsonParser.getCurrentName();  
	                		if ("index".equals(StringUtil.NVL(recordsSubfieldName)) 
	                    			|| "identifier".equals(StringUtil.NVL(recordsSubfieldName))
	                    			|| "status".equals(StringUtil.NVL(recordsSubfieldName))
	                    		) {  
	                			jsonParser.nextToken();  
	                    		jsonParser.readValueAsTree();       	
	                    	}else if ("response".equals(StringUtil.NVL(recordsSubfieldName))) {
	                    		HashMap<String, Object> mdeMetaVO = new HashMap<String, Object>();
	                    		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	                    			String responseSubfieldName = jsonParser.getCurrentName(); 
	                    			if("MDEMeta".equals(StringUtil.NVL(responseSubfieldName)))  {
	                					jsonParser.nextToken();  
	                					String mdeMetaJsonData = jsonParser.readValueAsTree().toString();
	                					
	                					//1. parsing start
	                					JSONParser mdeMetaJsonParser = new JSONParser();
	                					JSONObject mdeMetaJsonObj = (JSONObject)mdeMetaJsonParser.parse(mdeMetaJsonData);
	                					JSONObject data = (JSONObject)mdeMetaJsonObj.get("created");
	                					mdeMetaVO.put("createAt", (String)data.get("at"));
	                					//parsing end
	                					
	                    			}else if("serviceMDEEntries".equals(StringUtil.NVL(responseSubfieldName)))  {
	                    				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	                    					//1. USE JSON DATA
	                    					createParsingData(jsonParser, mdeMetaVO);
	                    				}//JsonToken.END_ARRAY
	                    				//createParsingData(jsonParser);
	                    			}//if
	                    		}//JsonToken.END_OBJECT
	                    	}//if response
	        			}//end Array
	        		}//end Object
	        	}//if
	        }//while
		}finally {
			jsonParser.close(); 
		}
	}
	
	public void createParsingData(JsonParser jsonParser, HashMap<String, Object> mdeMetaVO) throws Exception {
		HashMap<String, Object> contentVO = new HashMap<String, Object>();
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String mDEEntriesSubfieldName = jsonParser.getCurrentName(); 
			if("membershipResourceId".equals(mDEEntriesSubfieldName)) {
				 jsonParser.nextToken();  
				 contentVO.put("membershipResourceId", jsonParser.getText());
			}else if("membershipId".equals(mDEEntriesSubfieldName)) {
				jsonParser.nextToken();  
				contentVO.put("membershipId", jsonParser.getText());
			}else if("exportReasons".equals(mDEEntriesSubfieldName)) {
				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
					while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
						String fieldName2= jsonParser.getCurrentName();
						if("resource".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("resource", jsonParser.getText());
						}else if("action".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("action", jsonParser.getText());	 
						}else if("operation".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("operation", jsonParser.getText());
						}else if("activityId".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("activityId", jsonParser.getText());
						}
					}
				}
			}else if("serviceMDEData".equals(mDEEntriesSubfieldName)) {
				 jsonParser.nextToken();  
				 contentVO.put("jsonData", jsonParser.readValueAsTree().toString());
			}
		}//JsonToken.END_ARRAY
		
		//contentVO.putAll(mdeMetaVO);
		mdeMetaVO.forEach((key, value) -> contentVO.merge(key, value, (v1, v2) -> v2));
		spParsingMasterDAO.jsonSave(contentVO);
	}
	
	public void tableTruncate() throws Exception {
		spParsingMasterDAO.tableTruncate();
	}
	
	public void createParsingDataSmaple() throws Exception {
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
	                	HashMap<String, Object> contentVO = new HashMap<String, Object>();
	                	contentVO.put("jsonData", treeValue);
	                	spParsingMasterDAO.jsonSave(contentVO);
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
		service.createMoveParsingData();
	}
}
