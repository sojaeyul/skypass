package com.koreanair.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.db.MyBatisConnectionFactory;

/**
 * A membership represents a contract between an entity (individual, organization) and a loyalty program, which allows the entity to benefit from this program.
 * 
 * @author sojaeyul
 *
 */
public class MembershipBiz {
	private final static Logger log = LoggerFactory.getLogger(MembershipBiz.class);
	private SqlSessionFactory sqlSessionFactory = null;
	
	/**
	 * constructor
	 */
	public MembershipBiz() {
		//log.debug("MembershipBiz 생성자 호출");
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	/**
	 * membership create data
	 * 
	 * @param jsonMap HashMap<String, Object> 
	 * @param spec String
	 * @param nodeRoot String
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doJsonToDBCreate(HashMap<String, Object> jsonMap, String spec, String nodeRoot) throws Exception{
		SqlSession sqlSession = sqlSessionFactory.openSession();
		boolean bi = false;
		try {
			//1. parsing start		
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParser.parse((String)jsonMap.get("jsondata"));
			
			String membershipResourceId = (String)jsonObj.get("membershipResourceId");
			String membershipId = (String)jsonObj.get("membershipId");
			JSONArray exportReasons = (JSONArray)jsonObj.get("exportReasons");
			for (Object exportReasonObj : exportReasons) {
				//어떤데이터를 사용할지??
			}
			
			JSONObject serviceMdeData = (JSONObject)jsonObj.get("serviceMDEData");			
			JSONObject membershipObj = (JSONObject)serviceMdeData.get(nodeRoot);
			
			///////////////////////////////////data///////////////////////////////////////
			JSONObject dataObj = (JSONObject)membershipObj.get("data");
			
			//String type = (String)dataObj.get("type");
			String id = (String)dataObj.get("id");	
			//JSONObject selfObj = (JSONObject)dataObj.get("self");
			
			//2. metaObj
			JSONObject metaObj = (JSONObject)dataObj.get("meta");	
			Map<String, Object> metaMap = metaObjData(metaObj);
			
			//3. customMetaObj
			JSONObject customMetaObj = (JSONObject)dataObj.get("customMeta");
			Map<String, Object> customMetaMap = customMetaObjData(customMetaObj);
			
			//4. relatedArr
			JSONArray relatedArr = (JSONArray)dataObj.get("related");	
			Map<String, Object> relatedMap = relatedArrData(relatedArr);
			
			String subType = (String)dataObj.get("subType");			
			String dataMembershipId = (String)dataObj.get("membershipId");	
			
			//5. oldMembershipIdsArr
			JSONArray oldMembershipIdsArr = (JSONArray)dataObj.get("oldMembershipIds");
			Map<String, Object> oldMembershipIdsArrMap = oldMembershipIdsArrData(oldMembershipIdsArr); 
			
			JSONArray otherMembershipIdsArr = (JSONArray)dataObj.get("otherMembershipIds");	
			JSONArray externalReferencesArr = (JSONArray)dataObj.get("externalReferences");	
			JSONObject statusObj = (JSONObject)dataObj.get("status");
			String enrolmentSource = (String)dataObj.get("enrolmentSource");
			String enrolmentDate = (String)dataObj.get("enrolmentDate");
			JSONObject contactObj = (JSONObject)dataObj.get("contact");
			JSONObject programObj = (JSONObject)dataObj.get("program");	
			JSONObject mainTierObj = (JSONObject)dataObj.get("mainTier");	
			JSONArray loyaltyAwardArr = (JSONArray)dataObj.get("loyaltyAward");
			JSONArray tagsArr = (JSONArray)dataObj.get("tags");	
			JSONObject individualObj = (JSONObject)dataObj.get("individual");	
			JSONObject organizationObj = (JSONObject)dataObj.get("organization");	
			JSONArray nomineesArr = (JSONArray)dataObj.get("nominees");
			JSONArray notesArr = (JSONArray)dataObj.get("notes");	
			JSONObject enrolmentDetailObj = (JSONObject)dataObj.get("enrolmentDetail");
			///////////////////////////////////data///////////////////////////////////////

			///////////////////////////////////included///////////////////////////////////////
			JSONObject includedObj = (JSONObject)membershipObj.get("included");
			///////////////////////////////////included///////////////////////////////////////
			
			bi=true;
			sqlSession.commit();
		}catch(Exception ex){
			sqlSession.rollback();
			throw ex;
		} finally {
	    	sqlSession.close();
	    }
		
		return bi;
	}
	
	/**
	 * Meta information associated to all resources for audit purposes.
	 * 
	 * @param metaObj JSONObject
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object> metaObjData(JSONObject metaObj) throws Exception{
		Map<String, Object> metaMap = new HashMap<String, Object>();
		if(metaObj!=null) {
			metaMap.put("meta", metaObj.toJSONString());
		}
		
		return metaMap;
	}
	
	/**
	 * Meta information associated to all resources for audit purposes.
	 * 
	 * @param metaObj JSONObject
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object> customMetaObjData(JSONObject customMetaObj) throws Exception{
		Map<String, Object> customMetaMap = new HashMap<String, Object>();
		if(customMetaObj!=null) {
			customMetaMap.put("customMeta", customMetaObj.toJSONString());
		}
		
		return customMetaMap;
	}	
	
	/**
	 * Identifier of another resource.
	 * In some endpoints, the 'include' query parameter allows the API consumer to retrieve these resources at the same time as the other ones. In this case they appear in the 'included' section at the root of the reply.
	 * This included section contain for each 'type' included a map 'id' / 'value' where 'id' is the 'id' for the resource of type 'type'. [ Read-only ]
	 * 
	 * @param relatedArr JSONArray
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object> relatedArrData(JSONArray relatedArr) throws Exception{
		Map<String, Object> relatedMap = new HashMap<String, Object>();
		if(relatedArr!=null) {
			relatedMap.put("related", relatedArr.toJSONString());
		}
		
		return relatedMap;
	}		
	
	/**
	 *A list of old membership ids of the membership
	 * 
	 * @param relatedArr JSONArray
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object> oldMembershipIdsArrData(JSONArray oldMembershipIdsArr) throws Exception{
		Map<String, Object> oldMembershipIdsArrMap = new HashMap<String, Object>();
		
		List<String> arr = new ArrayList<String>();
		for (Object obj : oldMembershipIdsArr) {
			arr.add((String) obj);
		}
		
		
		return oldMembershipIdsArrMap;
	}
	
	public static void main(String[] args) throws Exception {
		String abcd = "[\"aaa\",\"bbb\",\"ccc\"]";
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonObj = (JSONArray)jsonParser.parse(abcd);
		
		for(Object obj : jsonObj) {
			String aaa = (String)obj;
			System.out.println(aaa);
		}
	}
}
