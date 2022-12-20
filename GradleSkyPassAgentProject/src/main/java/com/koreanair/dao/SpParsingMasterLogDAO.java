package com.koreanair.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.koreanair.common.db.MybatisModelImpl;

public class SpParsingMasterLogDAO {
	private MybatisModelImpl mybatisModelImpl = null;
	
    public SpParsingMasterLogDAO() throws Exception{
        this.mybatisModelImpl = new MybatisModelImpl();
    }
    
    public HashMap<String, Object> docAuthorSelect(HashMap<String, Object> argInfoVO) throws Exception{
    	String DAOName = "SKYPASS.SP_PARSING_MASTER_LOG";
    	String MethodName = "jsonDataSelect";
    	HashMap<String, Object> view = this.mybatisModelImpl.getData(argInfoVO, DAOName, MethodName);
    	
        return view;

    }
	
    public List<HashMap<String, Object>> jsonContentList(HashMap<String, Object> argInfoVO) throws Exception {
    	String DAOName = "SKYPASS.SP_PARSING_MASTER_LOG";
    	String MethodName = "jsonDataSelectList";
    	List<HashMap<String, Object>> alist = this.mybatisModelImpl.getDataList(argInfoVO, DAOName, MethodName);
        return alist;

    }

	public int jsonSave(HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER_LOG";
    	String MethodNameS = "jsonDataInsert";    	

    	i = this.mybatisModelImpl.addData(contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonDelete(HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER_LOG";
    	String MethodNameS = "jsonDataDelete";
    	
    	i = this.mybatisModelImpl.deleteData(contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonSave(SqlSession sqlSession, HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER_LOG";
    	String MethodNameS = "jsonDataInsert";
    	
    	i = this.mybatisModelImpl.addData(sqlSession, contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonDelete(SqlSession sqlSession, HashMap<String, Object> contentVO) throws Exception {
		int i = 0;
		String DAONameS = "SKYPASS.SP_PARSING_MASTER_LOG";
    	String MethodNameS = "jsonDataDelete";
    	
  
    	i = this.mybatisModelImpl.deleteData(sqlSession, contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
}
