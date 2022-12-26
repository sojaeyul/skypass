package com.koreanair.biz;

import java.io.File;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.koreanair.common.util.ComUtil;
import com.koreanair.dao.SpParsingMasterDAO;

public class CreateJsonParsingDataService {
	private final static Logger log = LoggerFactory.getLogger(CreateJsonParsingDataService.class);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
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
	        	if ("id".equals(ComUtil.NVL(fieldName)) 
	        			|| "batchInstanceId".equals(ComUtil.NVL(fieldName))
	        			|| "seqNumber".equals(ComUtil.NVL(fieldName))
	        			|| "loyaltySystem".equals(ComUtil.NVL(fieldName))
	        			|| "partner".equals(ComUtil.NVL(fieldName))
	        			|| "service".equals(ComUtil.NVL(fieldName))
	        			|| "additionalData".equals(ComUtil.NVL(fieldName))
	        			|| "recordsCount".equals(ComUtil.NVL(fieldName))        			
	        			|| "statistics".equals(ComUtil.NVL(fieldName))
	        			|| "businessStatistics".equals(ComUtil.NVL(fieldName))
	        		) {  
	        		jsonParser.nextToken();  
	        		jsonParser.readValueAsTree();
	        		//log.debug("fieldName :: " + fieldName + " :: " +jsonParser.readValueAsTree());        	
	        	}else if ("records".equals(ComUtil.NVL(fieldName))) {
	        		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	        			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        				String recordsSubfieldName = jsonParser.getCurrentName();  
	                		if ("index".equals(ComUtil.NVL(recordsSubfieldName)) 
	                    			|| "identifier".equals(ComUtil.NVL(recordsSubfieldName))
	                    			|| "status".equals(ComUtil.NVL(recordsSubfieldName))
	                    		) {  
	                			jsonParser.nextToken();  
	                    		jsonParser.readValueAsTree();       	
	                    	}else if ("response".equals(ComUtil.NVL(recordsSubfieldName))) {
	                    		HashMap<String, Object> mdeMetaVO = new HashMap<String, Object>();
	                    		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	                    			String responseSubfieldName = jsonParser.getCurrentName(); 
	                    			if("MDEMeta".equals(ComUtil.NVL(responseSubfieldName)))  {
	                					jsonParser.nextToken();  
	                					String mdeMetaJsonData = jsonParser.readValueAsTree().toString();
	                					
	                					//1. parsing start
	                					JSONParser mdeMetaJsonParser = new JSONParser();
	                					JSONObject mdeMetaJsonObj = (JSONObject)mdeMetaJsonParser.parse(mdeMetaJsonData);
	                					JSONObject data = (JSONObject)mdeMetaJsonObj.get("created");
	                					
	                					mdeMetaVO.put("createdat", formatter2.format(formatter.parse((String)data.get("at"))));
	                					//parsing end
	                					
	                    			}else if("serviceMDEEntries".equals(ComUtil.NVL(responseSubfieldName)))  {
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
	
	public void createTokenParsingData(JsonParser jsonParser, HashMap<String, Object> mdeMetaVO) throws Exception {
		List<HashMap<String, Object>> jsonContentList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> jsonContentVO = new HashMap<String, Object>();
		HashMap<String, Object> contentVO = new HashMap<String, Object>();
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {			
			JsonLocation jsonLocation = jsonParser.currentTokenLocation();
			//log.debug("jsonLocation :: " + jsonLocation);
			//log.debug("readValueAsTree :: " + jsonParser.readValueAsTree().toString());			
			String mDEEntriesSubfieldName = jsonParser.getCurrentName(); 
			if("membershipResourceId".equals(mDEEntriesSubfieldName)) {
				 jsonParser.nextToken();  
				 contentVO.put("membershipresourceid", jsonParser.getText());
				 jsonContentVO.put("membershipResourceId", jsonParser.getText());
			}else if("membershipId".equals(mDEEntriesSubfieldName)) {
				jsonParser.nextToken();  
				contentVO.put("membershipid", jsonParser.getText());
				jsonContentVO.put("membershipId", jsonParser.getText());
			}else if("exportReasons".equals(mDEEntriesSubfieldName)) {
				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
					HashMap<String, Object> jsonContentSubVO = new HashMap<String, Object>();
					while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
						String fieldName2= jsonParser.getCurrentName();
						if("resource".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("resource", jsonParser.getText());
							jsonContentSubVO.put("resource", jsonParser.getText());
						}else if("action".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("action", jsonParser.getText());	
							jsonContentSubVO.put("action", jsonParser.getText());
						}else if("operation".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("operation", jsonParser.getText());
							jsonContentSubVO.put("operation", jsonParser.getText());
						}else if("activityId".equals(fieldName2)) {
							jsonParser.nextToken();  
							contentVO.put("activityid", jsonParser.getText());
							jsonContentSubVO.put("activityId", jsonParser.getText());
						}
					}
					jsonContentList.add(jsonContentSubVO);
				}
				jsonContentVO.put("exportReasons", jsonContentList);
			}else if("serviceMDEData".equals(mDEEntriesSubfieldName)) {
				 jsonParser.nextToken();  
				 //contentVO.put("jsondata", jsonParser.readValueAsTree().toString());
				 jsonContentVO.put("serviceMDEData", new JSONParser().parse(jsonParser.readValueAsTree().toString()));
			}
		}//JsonToken.END_ARRAY
		
		
		JSONObject jsonContentVOTojsonObject = new JSONObject(jsonContentVO);
		contentVO.put("jsondata", jsonContentVOTojsonObject.toJSONString());
		
		//contentVO.putAll(mdeMetaVO);
		mdeMetaVO.forEach((key, value) -> contentVO.merge(key, value, (v1, v2) -> v2));
		spParsingMasterDAO.jsonSave(contentVO);
	}
	
	
	public void createParsingData(JsonParser jsonParser, HashMap<String, Object> mdeMetaVO) throws Exception {
		HashMap<String, Object> contentVO = new HashMap<String, Object>();
		jsonParser.nextToken();
		String jsonData = jsonParser.readValueAsTree().toString();
		
		//1. parsing start		
		JSONParser jsonParserS = new JSONParser();
		JSONObject jsonObj = (JSONObject)jsonParserS.parse(jsonData);
		
		JSONArray exportReasons = (JSONArray)jsonObj.get("exportReasons");
		
		contentVO.put("membershipresourceid", jsonObj.get("membershipResourceId"));
		contentVO.put("membershipid", jsonObj.get("membershipId"));
		
		for (Object obj : exportReasons) {
			JSONObject data = (JSONObject)obj;
			contentVO.put("resource", data.get("resource"));
			contentVO.put("action", data.get("action"));
			contentVO.put("operation", data.get("operation"));
			contentVO.put("activityid", data.get("activityId"));
		} 
		
		contentVO.put("jsondata", jsonObj.toJSONString());
		
		//contentVO.putAll(mdeMetaVO);
		mdeMetaVO.forEach((key, value) -> contentVO.merge(key, value, (v1, v2) -> v2));
		//spParsingMasterDAO.jsonSave(contentVO);
	}	
	
	public void tableTruncate(boolean check) throws Exception {
		if(check) {
			Long cnt = spParsingMasterDAO.jsonDataSelectListCnt(new HashMap<String, Object>());
			if(cnt <=0) {
				spParsingMasterDAO.tableTruncate();
			}
		}else if(!check){
			spParsingMasterDAO.tableTruncate();
		}
	}
	
	public static void main(String[] args) throws Exception {
		//CreateJsonParsingDataService service = new CreateJsonParsingDataService();
		//service.createMoveParsingData();
				
	}
}
