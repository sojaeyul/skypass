package com.koreanair.biz;

import java.io.File;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.ibatis.io.Resources;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.koreanair.common.util.ComUtil;
import com.koreanair.common.util.EnvManager;
import com.koreanair.common.util.Environment;
import com.koreanair.dao.SpParsingMasterDAO;

public class CreateJsonParsingDataThreadService {
	private final static Logger log = LoggerFactory.getLogger(CreateJsonParsingDataService.class);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private ExecutorService executorService = Executors.newFixedThreadPool(1000);
	private ArrayList<Future<String>> callableFutureList = new ArrayList<Future<String>>();
	
	private String jsonFilePath="";
	SpParsingMasterDAO spParsingMasterDAO = null;
	
	public CreateJsonParsingDataThreadService()  throws Exception {
		this.spParsingMasterDAO = new SpParsingMasterDAO();
		
        Environment env = EnvManager.getEnvironment();
        jsonFilePath = env.getProperty("json.file.path");		
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
	                    				int callCnt = 0;
	                    				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	                    					//1. USE JSON DATA
	                    					callCnt++;
	                    					
	                    					jsonParser.nextToken();
	                    					String jsonData = jsonParser.readValueAsTree().toString();
	                    					
	                    					String threadName = "Thread_"+callCnt;		
	                    					Callable<String> thread = new MasterJsonParsingThread(jsonData, mdeMetaVO, callCnt) ;				
	                    					callableFutureList.add(executorService.submit(thread));
	                    					
	                    				}//JsonToken.END_ARRAY
	
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
	
	class MasterJsonParsingThread  implements Callable<String>{ 
		private String jsonData = "";
		private long callCnt=0;
		private String threadName = "";
		
		private HashMap<String, Object> mdeMetaVO;
		public MasterJsonParsingThread(String jsonData, HashMap<String, Object> mdeMetaVO, long  callCnt) {
			this.jsonData = jsonData;
			this.threadName = "threadName_"+callCnt;
			this.mdeMetaVO = mdeMetaVO;
			
		}
		
		public String call(){
			try {
				//System.out.println(threadName+ "=="+this.jsonData);
				HashMap<String, Object> contentVO = new HashMap<String, Object>();
				
				//1. parsing start		
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject)jsonParser.parse(this.jsonData);
				
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
				spParsingMasterDAO.jsonSave(contentVO);
				//parsing end

			}catch(Exception ex){
				log.error("error", ex);
			}			
			return threadName + "Thread End";
		}
	}
}
