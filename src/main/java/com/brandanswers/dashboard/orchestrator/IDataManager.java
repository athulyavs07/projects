package com.brandanswers.dashboard.orchestrator;
import java.util.function.Function;





public interface IDataManager<Result> {
	public <O,P>  O selectData(Query queryData,String key,P params,Function<Result,O> transformData);
	public <O,P> O insertData(Query queryData,String key,P params,Function<Result, O> transformData);
	
}
