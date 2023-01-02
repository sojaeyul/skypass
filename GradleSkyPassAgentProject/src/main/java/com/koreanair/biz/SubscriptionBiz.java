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
 * Related membership Subscription subresources included in massive export
 * 
 * @author sojaeyul
 *
 */
public class SubscriptionBiz {
	private final static Logger log = LoggerFactory.getLogger(SubscriptionBiz.class);
	private SqlSessionFactory sqlSessionFactory = null;
	
	/**
	 * constructor
	 */
	public SubscriptionBiz() {
		//log.debug("SubscriptionBiz 생성자 호출");
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	/**
	 * Subscription create data
	 * 
	 * @param jsonMap HashMap<String, Object> 
	 * @param spec String
	 * @param nodeRoot String
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doJsonToDBCreate(HashMap<String, Object> jsonMap, String spec, String nodeRoot) throws Exception {
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
			JSONArray subscriptionsArr = (JSONArray)serviceMdeData.get(nodeRoot);
			log.debug(subscriptionsArr.toJSONString());
			for (Object subscriptionObj : subscriptionsArr) {
				JSONObject dataObj = (JSONObject)((JSONObject)subscriptionObj).get("data");
				
				//String type = (String)dataObj.get("type"); 
				String id = (String)dataObj.get("id");
				String status = (String)dataObj.get("status");
				String startDate = (String)dataObj.get("startDate");
				String endDate = (String)dataObj.get("endDate");
				String category = (String)dataObj.get("category");
				String code = (String)dataObj.get("code");
				
				//2. MetaObj
				JSONObject metaObj = (JSONObject)dataObj.get("meta");
				Map<String, Object> metaMap = metaObjData(metaObj);
				
				//3. newsletterObj
				JSONObject newsletterObj = (JSONObject)dataObj.get("newsletter");
				Map<String, Object> newsletterMap = newsletterObjData(metaObj);
				
				//4. cobrandObj
				JSONObject cobrandObj = (JSONObject)dataObj.get("cobrand");
				Map<String, Object> cobrandMap = cobrandObjData(cobrandObj);
				
				//5. partnerAccountLinkageObj
				JSONObject partnerAccountLinkageObj = (JSONObject)dataObj.get("partnerAccountLinkage");
				Map<String, Object> partnerAccountLinkageMap = partnerAccountLinkageObjData(partnerAccountLinkageObj);
				
				//6. partnerAccountLinkageObj
				JSONObject cancellationObj = (JSONObject)dataObj.get("cancellation");
				Map<String, Object> cancellationMap = cancellationObjData(cancellationObj);
				
			}    
			//parsing end
		
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
	 * Description of a Newsletter options subscription in the scope of the loyalty program.
	 * 
	 * @param cobrandObj JSONObject
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object>  newsletterObjData(JSONObject cobrandObj) throws Exception{
		Map<String, Object> newsletterMap = new HashMap<String, Object>();
		if(cobrandObj!=null) {
			//사용하지 않음
		}
		
		return newsletterMap;
	}
	
	/**
	 * Description a cobrand bank partner
	 * 
	 * @param cobrandObj JSONObject
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object>  cobrandObjData(JSONObject cobrandObj) throws Exception{
		Map<String, Object> cobrandMap = new HashMap<String, Object>();
		if(cobrandObj!=null) {
			String accountNumber  = (String)cobrandObj.get("accountNumber");
			JSONObject partnerObj = (JSONObject)cobrandObj.get("partner");
			if(partnerObj!=null) {
				String code  = (String)partnerObj.get("code");
				String branchCode  = (String)partnerObj.get("branchCode");
				String name  = (String)partnerObj.get("name");
				
				cobrandMap.put("code", code);
				cobrandMap.put("branchCode", branchCode);
				cobrandMap.put("name", name);
			}
			
			List<Map<String, Object>> creditCardArr = new ArrayList<Map<String, Object>>();
			JSONArray  creditCards= (JSONArray)cobrandObj.get("creditCards");
			for (Object obj : creditCards) {
				Map<String, Object> creditCardMap = new HashMap<String, Object>();
				JSONObject jsonLineItem = (JSONObject) obj;	
				creditCardMap.put("vendorCode", (String)jsonLineItem.get("vendorCode"));
				creditCardMap.put("codeOfCard", (String)jsonLineItem.get("codeOfCard"));
				creditCardArr.add(creditCardMap);
			}
			
			cobrandMap.put("accountNumber", accountNumber);
			cobrandMap.put("creditCards", creditCardArr);
		}
		
		return cobrandMap;
	}
	
	/**
	 * Description a partner account linkage
	 * 
	 * @param partnerAccountLinkageObj JSONObject
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object>  partnerAccountLinkageObjData(JSONObject partnerAccountLinkageObj) throws Exception{
		Map<String, Object> partnerAccountLinkageMap = new HashMap<String, Object>();
		if(partnerAccountLinkageObj!=null) {
			//사용하지 않음.
		}
		
		return partnerAccountLinkageMap;
	}
	
	/**
	 * A Cancellation subresource contains the reason of the cancellation of an Subscription
	 * 
	 * @param cancellationObj JSONObject
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	private Map<String, Object> cancellationObjData(JSONObject cancellationObj) throws Exception{
		Map<String, Object> cancellationMap = new HashMap<String, Object>();
		if(cancellationObj!=null) {
			//object로 저장.
			cancellationMap.put("cancellation", cancellationObj.toJSONString());
		}
		
		return cancellationMap;
	}	
}
