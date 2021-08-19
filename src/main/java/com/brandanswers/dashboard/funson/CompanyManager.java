package com.brandanswers.dashboard.funson;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.brandanswers.dashboard.orchestrator.Func;

public class CompanyManager extends BaseDataAccessFunson {

	public static JSONObject getTotalEmployees(JSONObject params, Func functiondetails)
			 {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				if (result.next()) {
					try {
						innerObj.put("totalNoOfEmployees", result.getInt("rowcount"));
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
	public static JSONObject getTotalTerminatedEmployees(JSONObject params, Func functiondetails)
		{
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				if (result.next()) {
					try {
						innerObj.put("totalNoOfTerminatedEmployees", result.getInt("rowcount"));
					} catch (JSONException e) {
						e.printStackTrace();
					} 				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return innerObj;

		});
		return obj;
	}

}
