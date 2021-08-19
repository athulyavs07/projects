package com.brandanswers.dashboard.funson;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.brandanswers.dashboard.orchestrator.Func;
import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;

public class ResetManager extends BaseDataAccessFunson {

	public static JSONObject updateToken(JSONObject params, Func functiondetails) throws IOException, ParseException {
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			System.out.println(result);
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Email sent for resetting the password");
			return innerObj;
		});
		return obj;
	}

	public static JSONObject getToken(JSONObject params, Func functiondetails) throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				if (result.next()) {
					try {
						innerObj.put("username", result.getString("user_name"));
						innerObj.put("token", result.getString("reset_password_token"));
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (SQLException e) {
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

	public static JSONObject updatePassword(JSONObject params, Func functiondetails) throws IOException {
		if(params.has("username")){
			params.put("username",params.get("username"));
		}
		else {
			JSONObject prevParams = (JSONObject) params.get(OrchestratorEngine.PREV_RESULT);
			params.put("username",prevParams.get("username"));
		}
		params.put("token","");
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Password resetted");
			return innerObj;
		});
		return obj;
	}
	public static JSONObject emailVerification(JSONObject params, Func functiondetails) throws IOException {
		JSONObject respObj = new JSONObject();
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				if (result.next()) {
					try {
						innerObj.put("username", result.getString("user_name"));
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return innerObj;
		});
		if(obj.isEmpty()) {
			respObj.put("Response", "Email is invalid");
			return respObj;
		}
		else {
			respObj.put("Response", "Verified");
			return respObj;
		}
	}
	
	public static JSONObject tokenVerification(JSONObject params, Func functiondetails) throws IOException, java.text.ParseException {
		JSONObject obj2 = new JSONObject();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				if (result.next()) {
					try {
						innerObj.put("username", result.getString("user_name"));
						innerObj.put("token", result.getString("reset_password_token"));
						innerObj.put("timestamp", result.getTimestamp("insertiontime").toString());
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return innerObj;
		});
		if(obj.isEmpty()) {
			obj2.put("Response", "Token is invalid");
			return obj2;
		}
		else {
			Date date1 = formatter.parse(obj.getString("timestamp"));
			Date date2 = formatter.parse(formatter.format(date));
			long timeDifference = date2.getTime() - date1.getTime();
			long daysDifference = (timeDifference / (1000*60*60*24)) % 365;
			if(daysDifference == 0) {
				obj2.put("Response", "Verified");
				return obj2;
			}
			else {
				obj2.put("Response", "Not Verified");
				return obj2;
			}
		}
	}
}
