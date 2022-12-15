package com.koreanair.common.db;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public interface Model {    

    public abstract List<HashMap<String, Object>> getDataList(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;
    public abstract HashMap<String, Object> getData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception; 
    public abstract int addData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;    
    public abstract int deleteData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;     
    public abstract int updateData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;    
    
    public abstract int addData(SqlSession sqlSession, HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;    
    public abstract int deleteData(SqlSession sqlSession, HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;     
    public abstract int updateData(SqlSession sqlSession, HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception;        
}