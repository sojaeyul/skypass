

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

	public static void parsingData(String jsonString) {
		jsonString = jsonString.substring(1,jsonString.length()-1);
		System.out.println("recode :: " + jsonString);
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONObject data = jsonObj.getJSONObject("data");
		System.out.println(data.toString());
		JSONArray totalArry = (JSONArray)data.getJSONArray("related");//"related":[{"id":"16425","type":"activityDetail","category":"CHILD"}]
		for(int i = 0; i < totalArry.length(); i++) {
			JSONObject obj = (JSONObject)totalArry.get(i);
		}
		System.out.println(totalArry.toString());
		
	}
}
