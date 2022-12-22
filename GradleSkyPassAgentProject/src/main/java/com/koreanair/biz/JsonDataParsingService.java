package com.koreanair.biz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.dto.ResourceInfoEnum;


public class JsonDataParsingService {
	private final static Logger log = LoggerFactory.getLogger(JsonDataParsingService.class);

	public void parsingData(HashMap<String, Object> jsonMap, SqlSession sqlSession) throws Exception {	

		String resource = (String)jsonMap.get("resource");
		
		ResourceInfoEnum enums = ResourceInfoEnum.valueOf(resource);
		String bizClass = enums.getBizClass();
		String spec = enums.getSpec();
		String nodeRoot = enums.getNodeRoot();
		
		Class<?> enumCl = Class.forName(bizClass);
		Constructor<?> constructor = enumCl.getConstructor(null);
		Object obj = constructor.newInstance();
		
		Method method = enumCl.getMethod("doJsonToDBCreate", SqlSession.class, HashMap.class, String.class, String.class);
		boolean returnVal = (Boolean)method.invoke(obj, sqlSession, jsonMap, spec, nodeRoot);
		
		log.debug("returnVal :: " + returnVal);
	}
	
	public static void main(String[] args) throws Exception {
		/*
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("jsondata", "{aaa:bbb}");
		
		SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
		
		JsonDataParsingService service = new JsonDataParsingService();
		service.parsingData(jsonMap, sqlSessionFactory.openSession());
		*/
	}
	
}
