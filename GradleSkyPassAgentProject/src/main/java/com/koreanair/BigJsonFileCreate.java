package com.koreanair;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import com.koreanair.dao.SpParsingMasterDAO;

public class BigJsonFileCreate {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		SpParsingMasterDAO spParsingMasterDAO = new SpParsingMasterDAO();
		String preString = "{\r\n"
				+ "	\"id\": \"63588bc3fda4b0e4462c1cd4\",\r\n"
				+ "	\"loyaltySystem\": {\r\n"
				+ "		\"clientCode\": \"DPA\",\r\n"
				+ "		\"programCode\": \"DPA\"\r\n"
				+ "	},\r\n"
				+ "	\"service\": {\r\n"
				+ "		\"name\": \"MASSIVE_DATA_EXPORT\",\r\n"
				+ "		\"version\": 1\r\n"
				+ "	},\r\n"
				+ "	\"recordsCount\": 6,\r\n"
				+ "	\"statistics\": {\r\n"
				+ "		\"createdAt\": \"2022-10-26T01:22:11.266Z\",\r\n"
				+ "		\"processingStartedAt\": \"2022-10-26T01:22:11.142Z\",\r\n"
				+ "		\"processingEndedAt\": \"2022-10-26T01:22:11.266Z\",\r\n"
				+ "		\"successes\": 6,\r\n"
				+ "		\"errors\": 0\r\n"
				+ "	},\r\n"
				+ "	\"records\": [\r\n"
				+ "		{\r\n"
				+ "			\"index\": 0,\r\n"
				+ "			\"identifier\": \"63588bc3fda4b0e4442c1cd4\",\r\n"
				+ "			\"status\": \"SUCCESS\",\r\n"
				+ "			\"response\": {\r\n"
				+ "				\"MDEMeta\": {\r\n"
				+ "					\"created\": {\r\n"
				+ "						\"at\": \"2022-10-26T01:22:11.148Z\",\r\n"
				+ "						\"via\": {\r\n"
				+ "							\"execution\": \"BATCH\",\r\n"
				+ "							\"batch\": {\r\n"
				+ "								\"identifier\": \"63588bc3fda4b0e4452c1cd4\",\r\n"
				+ "								\"file\": \"MDE.SERVICE.DPA.UAT.1A_DPA_LCP_FEED.63588bc3fda4b0e4442c1cd4.0.D26102022.T012211.json\"\r\n"
				+ "							}\r\n"
				+ "						}\r\n"
				+ "					},\r\n"
				+ "					\"targetSystem\": \"DPA\",\r\n"
				+ "					\"feedId\": \"63588bc3fda4b0e4442c1cd4\",\r\n"
				+ "					\"totalResourceCount\": 6,\r\n"
				+ "					\"entailedResources\": [\r\n"
				+ "						{\r\n"
				+ "							\"resource\": \"ACTIVITY\",\r\n"
				+ "							\"count\": 3\r\n"
				+ "						},\r\n"
				+ "						{\r\n"
				+ "							\"resource\": \"BILLING\",\r\n"
				+ "							\"count\": 3\r\n"
				+ "						}\r\n"
				+ "					]\r\n"
				+ "				},\r\n"
				+ "				\"serviceMDEEntries\": [";
		String postString = "				]\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "	]\r\n"
				+ "}";
		
		int i=0;
		int z= 0;
		String szDbgFile = "D:\\bingJsonData.json";			
		FileOutputStream dbgStream0 = new FileOutputStream(szDbgFile, true);
		PrintWriter pw0 = new PrintWriter(dbgStream0);
		pw0.println(preString); 
		pw0.close();

		while(true) {
			i++;
			if(i >=1000000) {
			//if(i >3) {
				break;
			}

			List<HashMap<String, Object>> alist= spParsingMasterDAO.jsonContentList(new HashMap<String, Object>());
	        if(alist!=null && alist.size()>0) {
				String szMsg="";
				
				for(HashMap<String, Object> map: alist) {
					if(z!=0) {
						 szMsg = szMsg + ",";
					}

					szMsg = szMsg + (String)map.get("jsondata");
					z++;
				}
				FileOutputStream dbgStream = new FileOutputStream(szDbgFile, true);
				PrintWriter pw = new PrintWriter(dbgStream);
				pw.println(szMsg); 
				pw.close();
	        }
	        
	        System.out.println("완료-" + i);
		}//while
		FileOutputStream dbgStreaz = new FileOutputStream(szDbgFile, true);
		PrintWriter pwz = new PrintWriter(dbgStreaz);
		pwz.println(postString); 
		pwz.close();	
		
		
	}

}
