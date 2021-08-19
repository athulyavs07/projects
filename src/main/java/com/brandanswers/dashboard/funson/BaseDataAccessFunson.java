package com.brandanswers.dashboard.funson;

import java.sql.ResultSet;
import java.util.function.Function;

import org.json.JSONObject;

import com.brandanswers.dashboard.orchestrator.IDataManager;
import com.brandanswers.dashboard.orchestrator.PostgresDataManager;
import com.brandanswers.dashboard.orchestrator.QueryProcessor;
import com.brandanswers.dashboard.orchestrator.SpringContext;
import com.fasterxml.jackson.core.JsonProcessingException;


public abstract class BaseDataAccessFunson {
	
	protected static QueryProcessor queryProcessor = SpringContext.getBean(QueryProcessor.class);
	private static IDataManager<ResultSet> dataManager= SpringContext.getBean(PostgresDataManager.class);
	protected static PropertyHelper propertyHelper= SpringContext.getBean(PropertyHelper.class);
	
	public static <T> T getData(String name,JSONObject params,Function<ResultSet, T> transformResult) {
		return queryProcessor.select(name, params, dataManager, transformResult);
	}
	public static <T> T getData(String name,String queryKey,JSONObject params,Function<ResultSet, T> transformResult) {
		return queryProcessor.select(name, queryKey, params, dataManager, transformResult);
	}
	
	// TO DO: Implement Insert logic here
	public static <T> T setData(String name,JSONObject params,Function<ResultSet, T> transformResult) {
		try {
			return queryProcessor.insert(name, params, dataManager, transformResult);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
