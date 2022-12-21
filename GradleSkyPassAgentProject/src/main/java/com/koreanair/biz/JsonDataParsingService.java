package com.koreanair.biz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.db.MyBatisConnectionFactory;
import com.koreanair.dto.ResourceInfoEnum;


public class JsonDataParsingService {
	private final static Logger log = LoggerFactory.getLogger(JsonDataParsingService.class);
    
    
   	
	public void parsingData(HashMap<String, Object> jsonMap, SqlSession sqlSession) throws Exception {
		String jsonString =  (String)jsonMap.get("jsondata"); 			

		ResourceInfoEnum enums = ResourceInfoEnum.valueOf("MEMBERSHIP");
		String bizClass = enums.getBizClass();
		String description = enums.getDescription();
		
		log.debug("bizClass :: " + bizClass);
		log.debug("description :: " + description);
		
		Class<?> enumCl = Class.forName(bizClass);
		Constructor<?> constructor = enumCl.getConstructor(null);
		Object obj = constructor.newInstance();
		
		Method method = enumCl.getMethod("doJsonForDBCreate", HashMap.class);
		String returnVal = (String)method.invoke(obj, jsonMap);
		
		log.debug("returnVal :: " + returnVal);
		
		//log.debug("jsonString :: " + jsonString);
		

						


	}
	
	public static void main(String[] args) throws Exception {
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("jsondata", "{aaa:bbb}");
		
		SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
		
		JsonDataParsingService service = new JsonDataParsingService();
		service.parsingData(jsonMap, sqlSessionFactory.openSession());

	}
	
}
