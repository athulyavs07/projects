package com.brandanswers.dashboard.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaseController {
	public static org.json.JSONObject getParams(Map<String,String> params) {
		return new org.json.JSONObject(params);
	}
	
	
	public static JSONObject convertToSimpleJsonObject(org.json.JSONObject obj) throws ParseException{
		JSONParser parser = new JSONParser();
		JSONObject resultObj = (JSONObject) parser.parse(obj.toString());
		return resultObj;
		
	}
}
