package com.brandanswers.dashboard.funson;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.brandanswers.dashboard.orchestrator.Func;

public class ListManager extends BaseDataAccessFunson{
	
	 public static JSONObject get(JSONObject params, Func functiondetails) throws IOException, ParseException {
			JSONObject obj = getData("getUser", params, result -> {
				JSONObject innerObj = new JSONObject();
					try {
						if(result.next()) {
							try {
								innerObj.put("username", result.getString("user_name"));
								innerObj.put("password", result.getString("password"));
							} catch (JSONException e) {
								e.printStackTrace();
							} 
							
						}
					} catch (SQLException e) {
	
						e.printStackTrace();
					}
				return innerObj;

	});
	return obj;
}
	
}
