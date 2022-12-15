package com.koreanair.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.koreanair.common.db.MybatisModelImpl;

public class SpParsingMasterDAO {
	
	private MybatisModelImpl mybatisModelImpl = null;
    public SpParsingMasterDAO() throws Exception{
        this.mybatisModelImpl = new MybatisModelImpl();
    }
    
    public HashMap<String, Object> docAuthorSelect(HashMap<String, Object> argInfoVO) throws Exception{
    	String DAOName = "SP_PARSING_MASTER";
    	String MethodName = "jsonDataSelect";
    	HashMap<String, Object> view = this.mybatisModelImpl.getData(argInfoVO, DAOName, MethodName);
    	
        return view;

    }
	
    public List<HashMap<String, Object>> jsonContentList(HashMap<String, Object> argInfoVO) throws Exception {
    	String DAOName = "SP_PARSING_MASTER";
    	String MethodName = "jsonDataSelectList";
    	List<HashMap<String, Object>> alist = this.mybatisModelImpl.getDataList(argInfoVO, DAOName, MethodName);
        return alist;

    }
    
	public int jsonSave(String jsonData) throws Exception {
		int i = 0;
		String DAONameS = "SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataInsert";
    	
    	HashMap<String, Object> contentVO = new HashMap<String, Object>();
    	contentVO.put("jsonData", jsonData);
    	i = this.mybatisModelImpl.addData(contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonDelete(int seq) throws Exception {
		int i = 0;
		String DAONameS = "SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataDelete";
    	
    	HashMap<String, Object> contentVO = new HashMap<String, Object>();
    	contentVO.put("seq", seq);
    	i = this.mybatisModelImpl.deleteData(contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonSave(SqlSession sqlSession, String jsonData) throws Exception {
		int i = 0;
		String DAONameS = "SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataInsert";
    	
    	HashMap<String, Object> contentVO = new HashMap<String, Object>();
    	contentVO.put("jsonData", jsonData);
    	i = this.mybatisModelImpl.addData(sqlSession, contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
	public int jsonDelete(SqlSession sqlSession, int seq) throws Exception {
		int i = 0;
		String DAONameS = "SP_PARSING_MASTER";
    	String MethodNameS = "jsonDataDelete";
    	
    	HashMap<String, Object> contentVO = new HashMap<String, Object>();
    	contentVO.put("seq", seq);
    	i = this.mybatisModelImpl.deleteData(sqlSession, contentVO, DAONameS, MethodNameS);
    	
    	return i;
	}
	
}
