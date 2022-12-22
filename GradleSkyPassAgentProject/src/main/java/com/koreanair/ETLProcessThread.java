package com.koreanair;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.biz.JsonParsingMainService;
import com.koreanair.common.db.MyBatisConnectionFactory;
import com.koreanair.common.util.ComUtil;
import com.koreanair.dao.SpParsingMasterDAO;
import com.koreanair.dao.SpParsingMasterLogDAO;

public class ETLProcessThread implements Callable<String>{ 
	private final static Logger log = LoggerFactory.getLogger(ETLProcessThread.class);
	
	String threadName;
	ETLMainJob parent;
	List<HashMap<String, Object>> jsonList;
	
	public ETLProcessThread(ETLMainJob parent, String threadName, List<HashMap<String, Object>> jsonList){
		this.parent = parent;
		this.threadName = threadName;
		this.jsonList = jsonList;
	}
	
	public String call(){
		
		try{
            Comparator<HashMap<String, Object>> seqASC = Comparator.comparing((HashMap<String, Object> map) -> (Integer) map.get("seq"));//내림차순
            Comparator<HashMap<String, Object>> membershipresourceidDESC = Comparator.comparing((HashMap<String, Object> map) ->(String)map.get("membershipresourceid")).reversed();//오름차순
            Comparator<HashMap<String, Object>> createdAtDESC = Comparator.comparing((HashMap<String, Object> map) ->(String)map.get("createdat")).reversed();//오름차순
            
            jsonList.sort(Comparator.comparing((HashMap<String, Object> map) -> (String) map.get("membershipid"))
            				.thenComparing(membershipresourceidDESC)
            				.thenComparing(seqASC)
            				.thenComparing(createdAtDESC)
            				);
			int processorderby = 0;
            for(HashMap<String, Object> jsonMap : jsonList) {
            	processorderby++;
            	
            	bizProcess(jsonMap, processorderby);

            }
		}catch(Exception ex){
			log.error("☆ETLProcess Thread ERROR☆", ex);
		}
		
		return threadName + "번 콜라블 종료";
	}
	
	public void bizProcess(HashMap<String, Object> jsonMap, int processorderby){
		jsonMap.put("process", "C");
		jsonMap.put("processlog", "");
		try {
			//1. parsing
			try {
				JsonParsingMainService parsingService = new JsonParsingMainService();
				parsingService.parsingData(jsonMap);
			}catch(Exception ex) {
				log.error("☆ETLProcess Thread Error☆", ex);
				jsonMap.put("process", "E");
				jsonMap.put("processlog", ComUtil.PrintStackTraceToString(ex));
			}			

			//2. master delete
			SpParsingMasterDAO masterDAO = new SpParsingMasterDAO();
			masterDAO.jsonDelete(jsonMap);
			
			//3. log insert
			jsonMap.put("processthreadname", this.threadName);
			jsonMap.put("processorderby", processorderby);
			SpParsingMasterLogDAO logDAO = new SpParsingMasterLogDAO();
			logDAO.jsonSave(jsonMap);
			
		}catch(Exception ex){
			log.error("☆ETLProcess Thread ERROR☆", ex);
		} finally {
	    	
	    }
	}
}
