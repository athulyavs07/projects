package com.brandanswers.dashboard.orchestrator;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
@Scope("singleton")
public class QueryProcessor {
	//TO DO: Move it to properties file
	private static final String queryLookupFile = "jsons/QueryLookup.json";
	private final Map<String, Query> queryLookUp = new HashMap<String, Query>();
	
	// Querylookup JSON file attributes
	private final String PARAMETERS = "parameters";
	private final String QUERY = "query";
	private final String TYPE = "type";
	private final String NAME = "name";
	

	@PostConstruct
	private void init() {

		InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream(queryLookupFile);
		JSONTokener tokener = new JSONTokener(jsonInputStream);
		JSONObject queryData = new JSONObject(tokener);
		queryData.keySet().forEach(key->{
			JSONObject node = queryData.getJSONObject(key);
			Map<String,String> params = new HashMap<String,String>();
			node.getJSONArray(PARAMETERS).forEach(item->{
				JSONObject paramItem = (JSONObject) item;
				params.put(paramItem.getString(NAME), paramItem.getString(TYPE));
			});
			
			//params.put(key, key)
			queryLookUp.put(key, new Query(key,node.getString(QUERY),params));
		});
	}
	
	public Query getQuery(String name) {
		return this.queryLookUp.get(name);
	}
	public Query setQuery(String name, String query, Map<String,String> params) {
		queryLookUp.put(name, new Query(name,query,params));
		return this.queryLookUp.get(name);
	}
	
	
	public <Output,Result,InParams> Output select(String name,InParams inputParams,IDataManager<Result> dataManager,Function<Result, Output> transformData) {
		Optional<IDataManager<Result>> manager= Optional.of(dataManager);
		Optional<Function<Result, Output>> processData = Optional.of(transformData);
		return manager.get().selectData(this.getQuery(name),"default", inputParams, processData.get());
	}
	
	public <Output,Result,InParams> Output select(String name,String key,InParams inputParams,IDataManager<Result> dataManager,Function<Result, Output> transformData) {
		Optional<IDataManager<Result>> manager= Optional.of(dataManager);
		Optional<Function<Result, Output>> processData = Optional.of(transformData);
		return manager.get().selectData(this.getQuery(name),key, inputParams, processData.get());
	}

	public <Output,Result,InParams> Output insert(String name,InParams inputParams,IDataManager<Result> dataManager,Function<Result, Output> transformData) throws JsonMappingException, JsonProcessingException {
		Optional<IDataManager<Result>> manager= Optional.of(dataManager);
		Optional<Function<Result, Output>> processData = Optional.of(transformData);
		return manager.get().insertData(this.getQuery(name),"default", inputParams, processData.get());
	}
	public <Output,Result,InParams> Output insert(String name,String key,InParams inputParams,IDataManager<Result> dataManager,Function<Result, Output> transformData) throws JsonMappingException, JsonProcessingException {
		Optional<IDataManager<Result>> manager= Optional.of(dataManager);
		Optional<Function<Result, Output>> processData = Optional.of(transformData);
		return manager.get().insertData(this.getQuery(name),key, inputParams, processData.get());
	}

}