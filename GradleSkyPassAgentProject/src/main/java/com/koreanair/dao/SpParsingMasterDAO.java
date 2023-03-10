package com.koreanair.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.db.MybatisModelImpl;
import com.koreanair.common.util.ComUtil;


public class SpParsingMasterDAO {
	private final static Logger log = LoggerFactory.getLogger(SpParsingMasterDAO.class);
	
	private MybatisModelImpl mybatisModelImpl = null;
    public SpParsingMasterDAO() throws Exception{
        this.mybatisModelImpl = new MybatisModelImpl();
    }
    
    public HashMap<String, Object> docAuthorSelect(HashMap<String, Object> argInfoVO) throws Exception{
    	String DAOName = "SKYPASS.SP_PARSING_MASTER";
    	String MethodName = "jsonDataSelect";
    	HashMap<String, Object> view = this.mybatisModelImpl.getData(argInfoVO, DAOName, MethodName);
    	
        return view;

    }
    
    public Long jsonDataSelectListCnt(HashMap<String, Object> argInfoVO) throws Exception {
    	String appServerNum = ComUtil.NVL(System.getProperty("app.server.num"));
    	
    	String DAOName = "SKYPASS.SP_PARSING_MASTER";
    	String MethodName = "jsonDataSelectListCnt";
    	if(!appServerNum.equals("") && !appServerNum.equals("1")) {
    		MethodName = "jsonDataSelectListCntBigBang";
    		//argInfoVO.put("tablename", "sp_parsing_master"+appServerNum);
    		argInfoVO.put("tablename", "sp_parsing_master");
    	}
    	HashMap<String, Object> view = this.mybatisModelImpl.getData(argInfoVO, DAOName, MethodName);
        return (Long)view.get("cnt");

    }
	
    public List<HashMap<String, Object>> jsonContentList(HashMap<String, Object> argInfoVO) throws Exception {
    	String appServerNum = ComUtil.NVL(System.getProperty("app.server.num"));
    	
    	String DAOName = "SKYPASS.SP_PARSING_MASTER";
    	String MethodName = "jsonDataSelectList";
    	if(!appServerNum.equals("") && !appServerNum.equals("1")) {
    		MethodName = "jsonDataSelectListBigBang";
    		//argInfoVO.put("tablename", "sp_parsing_master"+appServerNum);
    		argInfoVO.put("tablename", "sp_parsing_master");
    	}
    	
    	List<HashMap<String, Object>> alist = this.mybatisModelImpl.getDataList(argInfoVO, DAOName, MethodName);
        return alist;

    }
    
	public int jsonSave(HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataInsert";    	
    	/*
        for (Entry<String, Object> entrySet : contentVO.entrySet()) {
            log.debug(String.format("[%-20s][%s]", entrySet.getKey(), entrySet.getValue()));
            
        }
        */
    	/*
        contentVO.forEach((key, value) -> log.debug(String.format("[%-20s][%s]", key, value)));        
        log.debug("=========================================================================");
        */
    	i = this.mybatisModelImpl.addData(contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonDelete(HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataDelete";
    	
    	i = this.mybatisModelImpl.deleteData(contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int tableTruncate() throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER";
    	String MethodNameS = "tableTruncate";
    	
    	i = this.mybatisModelImpl.deleteData(new HashMap<String, Object>(), DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonSave(SqlSession sqlSession, HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataInsert";
    	
    	/*
        for (Entry<String, Object> entrySet : contentVO.entrySet()) {
            log.debug(String.format("[%-20s][%s]", entrySet.getKey(), entrySet.getValue()));
            
        }
        */
    	/*
        contentVO.forEach((key, value) -> log.debug(String.format("[%-20s][%s]", key, value)));        
        log.debug("=========================================================================");
        */
    	
    	i = this.mybatisModelImpl.addData(sqlSession, contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonDelete(SqlSession sqlSession, HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataDelete";
    	
    	i = this.mybatisModelImpl.deleteData(sqlSession, contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	

	
	public static void main(String[] args) throws Exception {
//		SpParsingMasterDAO dao = new SpParsingMasterDAO();
//		List<HashMap<String, Object>> list = dao.jsonContentList(new HashMap<String, Object>());
//		
//		list.forEach(System.out::println);
		
		for(int i=0; i<6; i++) {
			if(i%2 ==0) {
				System.out.println("i ::" + i);
			}
		}
	}
	
}
