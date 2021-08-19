package com.brandanswers.dashboard.orchestrator;


import org.json.JSONObject;

public interface IParamMapper {
	
	JSONObject map(JSONObject inputParams,String pattern);
}
