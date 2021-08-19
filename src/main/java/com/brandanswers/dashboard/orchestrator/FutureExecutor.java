package com.brandanswers.dashboard.orchestrator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Component
@Scope("singleton")
@PropertySource("classpath:orchestrator.properties")
@Configuration
public class FutureExecutor {

	private static final String ORCHESTRATOR_TYPE_INSTANCE = "instance";
	private static final String ORCHESTRATOR_TYPE_SPRING = "spring";
	private static final String ORCHESTRATOR_TYPE_STATIC = "static";
	
	@Value("${orchestrator.type}")
	private String orchestratorType;
	
	@Value("${orchestrator.threadCount}")
	private int threadCount;
	
	private ExecutorService executor;
	public FutureExecutor() {
		executor = Executors.newFixedThreadPool(10);
	}
	
	public List<Object> executeMethods(JSONObject params,List<Func> functions) throws InterruptedException,ReflectiveOperationException,ExecutionException {
		List<Object> objList = new ArrayList<>();
		List<Callable<Object>> callableTasks = new  ArrayList<Callable<Object>>();
		
		functions.forEach(x-> {
			Callable<Object> callableTask = ()-> {
				Object resultObj = null;
				if (orchestratorType.equals(ORCHESTRATOR_TYPE_INSTANCE)) {
					resultObj = this.invokeByInstance(params, x);
				} else if (orchestratorType.equals(ORCHESTRATOR_TYPE_STATIC)) {
					resultObj = this.invokeByStatic(params, x);
				} else if (orchestratorType.equals(ORCHESTRATOR_TYPE_SPRING)) {
					resultObj = this.invokeBySpring(params, x);
				}
				return resultObj;
			};
			
			callableTasks.add(callableTask);
		});
		List<Future<Object>> futures = this.executor.invokeAll(callableTasks);
	
		futures.forEach(x->{
			Future<Object> item = x;
			try {
				objList.add(item.get());
			} catch (InterruptedException | ExecutionException e) {
				throw new OrchestratorException(e.getMessage(),e);
			}
		});
		return objList;
	}
	
	
	private Object invokeByInstance(JSONObject apiParams, Func function) throws ReflectiveOperationException {
		Class<?> cls = Class.forName(function.getClassName());
		Object obj = cls.getConstructor().newInstance();
		Method method = cls.getDeclaredMethod(function.getName(), JSONObject.class, Func.class);
		return method.invoke(obj, apiParams, function);
	}

	private Object invokeByStatic(JSONObject apiParams, Func function) throws ReflectiveOperationException {
		Class<?> cls = Class.forName(function.getClassName());
		Method method = cls.getDeclaredMethod(function.getName(), JSONObject.class, Func.class);

		return method.invoke(null, apiParams, function);
	}

	private Object invokeBySpring(JSONObject apiParams, Func function) throws ReflectiveOperationException {
		Object obj = SpringContext.getBean(function.getClassName());
		Method method = obj.getClass().getDeclaredMethod(function.getName(), JSONObject.class, Func.class);
		return method.invoke(obj, apiParams, function);
	}
}