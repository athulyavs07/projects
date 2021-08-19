package com.brandanswers.dashboard.orchestrator;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Query {
	private String name;
	private Map<String,String> query;
	private Map<String,String> parameters;
	private Boolean isBatch;
	
	public Boolean getIsBatch() {
		return isBatch;
	}
	public String getName() {
		return name;
	}
	public String getQuery() {
		return query.get("default");
	}
	public String getQuery(String key) {
		return query.get(key);
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public Query(String name, String query, Map<String, String> parameters) {
		super();
		Map<String, String> queryMap= new HashMap<String, String>();
		try {
			JSONObject json = new JSONObject(query);
			json.keys().forEachRemaining(b->{
				queryMap.put(b, json.getString(b));
			});
		}
		catch(Exception e) {
			queryMap.put("default", query);
		}
		this.name = name;
		this.query = queryMap;
		this.parameters = parameters;
		this.isBatch = query.split("\\|").length>1;
	}
	public static boolean isJson(String str) {
	    try {
	    	new JSONObject(str);  
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}
}