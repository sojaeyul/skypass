



import java.io.File;
/*
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
*/

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;


public class JsonParsingTest { 
	public static void main(String[] args) {
		/*
		try {
	        File f = new File("D:\\test.json");
	        if (f.exists()){
	            InputStream is = new FileInputStream("D:\\test.json");
	            String jsonTxt = IOUtils.toString(is, "UTF-8");
	            System.out.println(jsonTxt);
	            JSONObject json = new JSONObject(jsonTxt);       
	            String a = json.getString("1000");
	            System.out.println(a);   
	        }			
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		try {
	        JsonFactory jsonFactory = new MappingJsonFactory();  
	        File jsonFile = new File("D:\\test.json");
	        JsonParser jsonParser = jsonFactory.createParser(jsonFile); // json 파서 생성  
	  
	        int i = 0;
	   
	        while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회  
	            String fieldName = jsonParser.getCurrentName(); // 필드명, 필드값 토큰인 경우 필드명, 나머지 토큰은 null 리턴  
	  
	            if ("recode".equals(fieldName)) {  
	                jsonParser.nextToken(); // 필드값 토큰으로 이동  
	                while (jsonParser.nextToken() != JsonToken.END_OBJECT) { // END_OBJECT(}) 가 나올 때까지 토큰 순회 
	                	jsonParser.nextToken();
	                	i++;
	                	String treeValue = jsonParser.readValueAsTree().toString();
	                	//System.out.println("recode"+i+" :: " + treeValue);
	                	//JsonUtil.parsingData(treeValue);
	                	JsonSimpleUtil.parsingData(i, treeValue);
	                	//if(i==6)break;
	                }
	            }
	          
	        }  
	  
	        jsonParser.close(); 
		}catch(Exception ex) {
			ex.printStackTrace();
		}
        
	}
}
