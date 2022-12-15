

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonSimpleUtil {

	public static void parsingData(int n, String jsonString) {
		try {
			jsonString = jsonString.substring(1,jsonString.length()-1);
			System.out.println("recode"+n+":: " + jsonString);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(jsonString);
			JSONObject data = (JSONObject)jsonObj.get("data");
			//System.out.println("		" + data.toString());
			if(data.containsKey("related")) {
				JSONArray totalArry = (JSONArray)data.get("related");//"related":[{"id":"16425","type":"activityDetail","category":"CHILD"}]
				for(int i = 0; i < totalArry.size(); i++) {
					JSONObject obj = (JSONObject)totalArry.get(i);
					//System.out.println("		" + obj.toJSONString());
				}
				//System.out.println("		" + totalArry.toString());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
