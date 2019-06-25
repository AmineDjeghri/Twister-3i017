package tools;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorJSON {
	
	public static JSONObject serviceAccepted(String message) {
		JSONObject json = new JSONObject();
		
		try {
			json.put("Message of success",message);
		} catch (JSONException j) {
			
			j.printStackTrace();
			return serviceRefused("JSONException", 100);
		} 
		return json;
	}
	
	public static JSONObject serviceRefused(String message , int codeErreur) {
		JSONObject json = new JSONObject();
		
		try {
			json.put("Error Message",message);
			json.put("Error Code", codeErreur);
		} catch (JSONException j) {
			
			j.printStackTrace();
			return serviceRefused("JSONException", 100);
		} 
		return json;
	}
	

}
