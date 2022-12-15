

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;  

public class JacksonStreamBigJsonCreateService {

	public static void main(String[] args) {
		try {
			
int i=0;
String szDbgFile = "D:\\test.json";			
FileOutputStream dbgStream0 = new FileOutputStream(szDbgFile, true);
PrintWriter pw0 = new PrintWriter(dbgStream0);
pw0.println("{ \"recode\" :{"); 
pw0.close();

while(true) {
if(i >=10000000) {
	break;
}



			// TODO Auto-generated method stub
	        JsonFactory jsonFactory = new MappingJsonFactory();  
	        File jsonFile = new File("DD:\\99.KAL_PROJECT\\eclipse\\eclipse-workspace\\GradleSkyPassETLProject\\src\\test\\java\\skypassAll.json");
	        JsonParser jsonParser = jsonFactory.createParser(jsonFile); // json 파서 생성  
	        
	       
	        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        	String fieldName = jsonParser.getCurrentName(); // 필드명, 필드값 토큰인 경우 필드명, 나머지 토큰은 null 리턴  
	        	//System.out.println("fieldName :: " + fieldName);
	        	
	        	
	        	if ("id".equals(fieldName)) {  
	        		 jsonParser.nextToken(); // 필드값 토큰으로 이동  
	        		 //System.out.println("id :: " + jsonParser.getText());
	        	}else if ("loyaltySystem".equals(fieldName)) {  
	        		jsonParser.nextToken(); // 필드값 토큰으로 이동  
	                while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회  
	                    String fieldName1 = jsonParser.getCurrentName();  
	                    //System.out.println("loyaltySystemfieldName1 :: " + fieldName1);
	                    if ("clientCode".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("loyaltySystem clientCode :: " + jsonParser.getText());
	                    } else if ("programCode".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("loyaltySystem programCode :: " + jsonParser.getText());
	                    }  
	                }  
	        	}else if ("service".equals(fieldName)) {  
	        		jsonParser.nextToken(); // 필드값 토큰으로 이동  
	                while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회  
	                    String fieldName1 = jsonParser.getCurrentName();  
	                    //System.out.println("loyaltySystemfieldName1 :: " + fieldName1);
	                    if ("name".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("service name :: " + jsonParser.getText());
	                    } else if ("version".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("service version :: " + jsonParser.getText());
	                    }  
	                }  
	        	}else if ("recordsCount".equals(fieldName)) {  
	        		 jsonParser.nextToken(); // 필드값 토큰으로 이동  
	        		// System.out.println("recordsCount :: " + jsonParser.getText());  
	        	}else if ("statistics".equals(fieldName)) {  
	        		jsonParser.nextToken(); // 필드값 토큰으로 이동  
	                while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회  
	                    String fieldName1 = jsonParser.getCurrentName();  
	                    //System.out.println("loyaltySystemfieldName1 :: " + fieldName1);
	                    if ("createdAt".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("statistics createdAt :: " + jsonParser.getText());
	                    } else if ("processingStartedAt".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("statistics processingStartedAt :: " + jsonParser.getText());	                   
	                    } else if ("processingEndedAt".equals(fieldName1)) {  
	                        jsonParser.nextToken();  
	                        //System.out.println("statistics processingEndedAt :: " + jsonParser.getText());                    
		                } else if ("successes".equals(fieldName1)) {  
		                    jsonParser.nextToken();  
		                    //System.out.println("statistics successes :: " + jsonParser.getText());                  
			            } else if ("errors".equals(fieldName1)) {  
			                jsonParser.nextToken();  
			                //System.out.println("statistics errors :: " + jsonParser.getText());
			            }  	        	
	                }  		 
	        	}else if ("records".equals(fieldName)) {
	        		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	        			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        				 String recordsSubfieldName = jsonParser.getCurrentName();  
	        				 if ("response".equals(recordsSubfieldName)) {  
	        					 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        						 String responseSubfieldName = jsonParser.getCurrentName();  
	        						 //System.out.println("responseSubfieldName :: " + jsonParser.getText());
	        						 
	        						 
	        						 if("serviceMDEEntries".equals(responseSubfieldName)) {
	        							 //System.out.println("==========================================start==========================================");
	        							 while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	        								 i++;
	        								 String szMsg="";
	        								 if(i==1) {
	        									 szMsg = "\"value"+i+"\":";
	        								 }else {
	        									 szMsg = ",\"value"+i+"\":";
	        								 }
	        								 
	        								 //System.out.println("FIELD_NAME :: "+JsonToken.FIELD_NAME);
	        								 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        									 String fieldName1 = jsonParser.getCurrentName();
	        									 if("membershipResourceId".equals(fieldName1)) {
	        										 jsonParser.nextToken();  
	        										 //System.out.println("membershipResourceId :: " + jsonParser.getText() + " :: " + jsonParser.currentTokenLocation());
	        									 }else if("membershipId".equals(fieldName1)) {
	        										 jsonParser.nextToken();  
	        										 //System.out.println("membershipId :: " + jsonParser.getText() + " :: " + jsonParser.currentTokenLocation());
	        									 }else if("exportReasons".equals(fieldName1)) {
	        										 while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	        											 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        	        									 String fieldName2= jsonParser.getCurrentName();
	        	        									 if("resource".equals(fieldName2)) {
	        	        										 jsonParser.nextToken();  
	        	        										 //System.out.println("resource :: " + jsonParser.getText());
	        	        									 }else if("operation".equals(fieldName2)) {
	        	        										 jsonParser.nextToken();  
	        	        										 //System.out.println("operation :: " + jsonParser.getText());
	        	        									 }else if("activityId".equals(fieldName2)) {
	        	        										 jsonParser.nextToken();  
	        	        										 //System.out.println("activityId :: " + jsonParser.getText());
	        	        									 }
	        											 }
	        										 }
	        										 //jsonParser.nextToken();  
	        										 //System.out.println("membershipId :: " + jsonParser.getText());
	        									 }else if("serviceMDEData".equals(fieldName1)) {
	        										 jsonParser.nextToken();  
	        										 //System.out.println("currentTokenLocation :: " + jsonParser.currentTokenLocation());
	        										 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        											 String fieldName3= jsonParser.getCurrentName();    
	        											 
	        											 if("activities".equals(fieldName3)) {
	        												 jsonParser.nextToken();
	        												 szMsg = szMsg+jsonParser.readValueAsTree().toString();
	        												 //System.out.println("activities Data :: " + jsonParser.readValueAsTree());
	        											 }else if("billings".equals(fieldName3)) {	   
	        												 jsonParser.nextToken();
	        												 szMsg = szMsg+jsonParser.readValueAsTree().toString();
	        												 //System.out.println("billings Data ::   " + jsonParser.readValueAsTree()); 
	        											 }else {
	        												 jsonParser.nextToken();
	        												 szMsg = szMsg+jsonParser.readValueAsTree().toString();
	        												 //System.out.println("ETC billings ::    " + jsonParser.readValueAsTree()); 
	        											 }
	        											 //System.out.println("szMsg Data :: " + szMsg);
	        											 FileOutputStream dbgStream = new FileOutputStream(szDbgFile, true);
	        											 PrintWriter pw = new PrintWriter(dbgStream);
	        											 pw.println(szMsg); 
	        											 pw.close();
	        										 }
	        										 
	        									 }
	        								 }
	        								 
	        							 }
	        							 //System.out.println("==========================================end==========================================");
	        							 System.out.println("end cnt :: " + i);
	        							 
	        						 }else if("MDEMeta".equals(responseSubfieldName)) { 
	        							 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        								 String fieldName1 = jsonParser.getCurrentName();
	        								 if("created".equals(fieldName1)) {
	        									 jsonParser.nextToken();  
	        									 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        										 String fieldName2 = jsonParser.getCurrentName();
	        										 if("at".equals(fieldName2)) {
	        											 jsonParser.nextToken();
	        											 //System.out.println("at:: " + jsonParser.getText());
	        										 }else if("via".equals(fieldName2)) {
	        											 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        												 String fieldName3 = jsonParser.getCurrentName();
	        												 if("execution".equals(fieldName3)) {
	        													 jsonParser.nextToken();
	        													 //System.out.println("execution:: " + jsonParser.getText());
	        												 }else if("batch".equals(fieldName3)) {
	        													 while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
	        														 String fieldName4 = jsonParser.getCurrentName();
	        														 if("identifier".equals(fieldName4)) {
	        															 jsonParser.nextToken();
	        															 //System.out.println("identifier:: " + jsonParser.getText());
	        														 }else if("file".equals(fieldName4)) {
	        															 jsonParser.nextToken();
	        															 //System.out.println("file:: " + jsonParser.getText());
	        														 }	        													 
	        													 }
	        												 }
	        											 }
	        										 }	        										 
	        									 }
	        								 }else if("targetSystem".equals(fieldName1)) {
												 jsonParser.nextToken();
												 //System.out.println("targetSystem:: " + jsonParser.getText());
	        								 }else if("feedId".equals(fieldName1)) {
												 jsonParser.nextToken();
												 //System.out.println("feedId:: " + jsonParser.getText());
	        								 }else if("totalResourceCount".equals(fieldName1)) {
												 jsonParser.nextToken();
												 //System.out.println("totalResourceCount:: " + jsonParser.getText());
	        								 }else if("entailedResources".equals(fieldName1)) {
        						        		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
        						        			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
        						        	            String fieldName5 = jsonParser.getCurrentName();          						        	            
        						        	            if ("resource".equals(fieldName5)) {  
        						        	                jsonParser.nextToken();  
        						        	                //System.out.println("resource:: " + jsonParser.getText());
        						        	            } else if ("count".equals(fieldName5)) {  
        						        	                jsonParser.nextToken();  
        						        	                //System.out.println("count:: " + jsonParser.getText());
        						        	            } 
        						        			}
        						        		}
	        								 }
	        							 }
	        						 }
	        					 }
	        				 }
	        			}
	        		}
	        	}
	        	
	        }//while
	        
	        jsonParser.close();
	        
        
}//tmp while
FileOutputStream dbgStreaz = new FileOutputStream(szDbgFile, true);
PrintWriter pwz = new PrintWriter(dbgStreaz);
pwz.println("}}"); 
pwz.close();	

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
