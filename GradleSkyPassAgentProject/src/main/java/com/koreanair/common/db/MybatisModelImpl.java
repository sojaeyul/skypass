package com.koreanair.common.db;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MybatisModelImpl implements Model{

    private SqlSessionFactory sqlSessionFactory = null;
    
    public MybatisModelImpl(){
        this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
    }
    	
	@Override
	public List<HashMap<String, Object>> getDataList(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();		
		List<HashMap<String, Object>> outputs=null;
        try {
        	outputs =  sqlSession.selectList(DAOName+"."+MethodName,argInfoVO);        	
        }finally {
        	sqlSession.close();
        }		
		return outputs;
	}

	@Override
	public HashMap<String, Object> getData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		HashMap<String, Object> outputs=null;
        try {
        	outputs = sqlSession.selectOne(DAOName+"."+MethodName, argInfoVO);
        } finally {
        	sqlSession.close();
        }        
		return outputs;
	}

	@Override
	public int addData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		int outputs = 0;
		try {
			outputs = sqlSession.insert(DAOName+"."+MethodName, argInfoVO);
	    } finally {
	    	sqlSession.commit();
	    	sqlSession.close();
	    }
		return outputs;
	}

	@Override
	public int updateData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();		
		int outputs = 0;
		try {
			outputs = sqlSession.update(DAOName+"."+MethodName, argInfoVO);
	    } finally {
	    	sqlSession.commit();
	    	sqlSession.close();
	    }
		return outputs;
	}
	
	@Override
	public int deleteData(HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		int outputs = 0;
		try {
			outputs = sqlSession.delete(DAOName+"."+MethodName, argInfoVO);
	    } finally {
	    	sqlSession.commit();
	    	sqlSession.close();
	    }		
		return outputs;
	}

	@Override
	public int addData(SqlSession sqlSession, HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		int outputs = sqlSession.insert(DAOName+"."+MethodName, argInfoVO);
		return outputs;
	}

	@Override
	public int updateData(SqlSession sqlSession, HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		int outputs = sqlSession.update(DAOName+"."+MethodName, argInfoVO);
		return outputs;
	}
	
	@Override
	public int deleteData(SqlSession sqlSession, HashMap<String, Object> argInfoVO, String DAOName, String MethodName) throws Exception {
		int outputs = sqlSession.delete(DAOName+"."+MethodName, argInfoVO);	
		return outputs;
	}
}
