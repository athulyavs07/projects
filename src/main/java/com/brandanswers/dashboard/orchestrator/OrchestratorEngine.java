package com.brandanswers.dashboard.orchestrator;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@PropertySource("classpath:application.properties")
@Configuration
@Scope("singleton")
public class OrchestratorEngine {
	public static String errormsg;
	private static final String funsonRegistryFile = "jsons/FunsonRegistry.json";
	private static final String jsonFile = "jsons/";
	private static final Map<String, Funson> funsonRegistry = new HashMap<String, Funson>();

	private static final String interceptorFile = "jsons/interceptor.json";
	private static final Map<String, Funson> preInterceptor = new HashMap<String, Funson>();

	private static final String ORCHESTRATOR_TYPE_INSTANCE = "instance";
	private static final String ORCHESTRATOR_TYPE_SPRING = "spring";
	private static final String ORCHESTRATOR_TYPE_STATIC = "static";
	// Funson Attributes
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String FUNC_TYPE = "type";
	private static final String FUNC_TYPE_METHOD = "method";
	private static final String FUNC_TYPE_FUNSON = "funson";
	private static final String FUNC_TYPE_FUTURE = "future";
	private static final String CLASS_NAME = "class";
	private static final String FUNCTIONS = "functions";
	private static final String FUNCTION_NAME = "name";
	private static final String FUNC_PARAMS = "params";
	private static final String QUERY_NAME = "db";
	private static final String FILE_NAME = "file";
	private static final String FUNSONS = "requests";
	private static final String PARAMETERS = "parameters";
	private static final String NULLABLE = "nullable";
	private static final String RESULT = "result";
	public static final String PREV_RESULT = "previousResult";
	public static final String MAPPER = "mapper";

	// Error codes
	private static final String ERR_FUNSON_NOT_PRESENT = "ORC-0001";
	private static final String ERR_PARAMETER_NOT_PRESENT = "ORC-0002";
	private static final String ERR_FUNCTION_NAME_MISSING = "ORC-0004";

	@Value("${orchestrator.type}")
	private String orchestratorType;

	@Autowired
	private ValidationProcessor valProcessor;

	@Autowired
	private FutureExecutor futureExecutor;

	@PostConstruct
	private void init() {
		InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream(funsonRegistryFile);
		JSONTokener tokener = new JSONTokener(jsonInputStream);
		JSONObject funsonRegistryData = new JSONObject(tokener);
		funsonRegistryData.getJSONArray(FUNSONS).forEach(x -> {
			String name = ((JSONObject) x).getString(NAME);
			String file = ((JSONObject) x).getString(FILE_NAME);
			funsonRegistry.put(name, this.loadFunson(name, file));
			
		});
			InputStream jsonInterceptor = this.getClass().getClassLoader().getResourceAsStream(interceptorFile);
			JSONTokener preInter = new JSONTokener(jsonInterceptor);
			JSONObject preIntercept = new JSONObject(preInter);
			preIntercept.keySet().forEach(key -> {
				JSONObject obj = (JSONObject) preIntercept.get(key);
				obj.getJSONArray("funsons").forEach(fun->{
					Funson funs = this.loadInterceptor(fun.toString(), obj.getJSONArray("interceptors_pre"));
					preInterceptor.put(fun.toString(), funs);
				});
				});
		
	}

	private Funson loadInterceptor(String name, JSONArray jsonArray) {
		List<Func> functions = new ArrayList<Func>();
		jsonArray.forEach(x -> {
			JSONObject obj = (JSONObject) x;
			String queryName = obj.has(QUERY_NAME) ? obj.getString(QUERY_NAME) : null;
			String funcType = obj.has(FUNC_TYPE) ? obj.getString(FUNC_TYPE) : FUNC_TYPE_METHOD;
			String className = obj.has("className") ? obj.getString("className") : null;
			String mapper = obj.has(MAPPER) ? obj.getString(MAPPER) : null;
			String functionName = obj.getString("method");
			List<String> funcParams = new ArrayList<String>();
			JSONArray paramsJsonArr = obj.has(FUNC_PARAMS) ? obj.getJSONArray(FUNC_PARAMS) : new JSONArray();
			paramsJsonArr.forEach(y -> funcParams.add(y.toString()));
			Func outerFunc = new Func(funcType, className, functionName, queryName, funcParams, mapper);
			// Iterate and add the inner functions.
			JSONArray funcJsonArr = obj.has(FUNCTIONS) ? obj.getJSONArray(FUNCTIONS) : new JSONArray();
			outerFunc = this.addInnerFunctions(outerFunc, funcType, funcJsonArr);
			functions.add(outerFunc);
		});

		return new Funson(name, null, functions);
	}

	private Funson loadFunson(String name, String fileName) {
		InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream(jsonFile + fileName);
		Optional.ofNullable(jsonInputStream)
				.orElseThrow(() -> new OrchestratorException(String.format("No funson file present - %s", fileName),
						ERR_FUNSON_NOT_PRESENT));
		JSONTokener tokener = new JSONTokener(jsonInputStream);
		JSONObject funsonRegistryData = new JSONObject(tokener);
		List<Func> functions = new ArrayList<Func>();
		List<Parameter> params = new ArrayList<Parameter>();

		// Load Parameters
		funsonRegistryData.getJSONArray(PARAMETERS).forEach(x -> {
			JSONObject obj = (JSONObject) x;
			boolean isNullable = obj.has(NULLABLE) ? obj.getBoolean(NULLABLE) : false;
			List<String> validations = new ArrayList<String>();
			if (obj.has("validation")) {
				obj.getJSONArray("validation").forEach(y -> {
					validations.add((String) y);
				});
			}
			params.add(new Parameter(obj.getString(NAME), obj.getString(TYPE), isNullable, validations));
		});

		// Load Functions
		funsonRegistryData.getJSONArray(FUNCTIONS).forEach(x -> {
			JSONObject obj = (JSONObject) x;
			String queryName = obj.has(QUERY_NAME) ? obj.getString(QUERY_NAME) : null;
			String funcType = obj.has(FUNC_TYPE) ? obj.getString(FUNC_TYPE) : FUNC_TYPE_METHOD;
			String className = obj.has(CLASS_NAME) ? obj.getString(CLASS_NAME) : null;
			String mapper = obj.has(MAPPER) ? obj.getString(MAPPER) : null;
			String functionName = obj.getString(FUNCTION_NAME);
			List<String> funcParams = new ArrayList<String>();
			JSONArray paramsJsonArr = obj.has(FUNC_PARAMS) ? obj.getJSONArray(FUNC_PARAMS) : new JSONArray();
			paramsJsonArr.forEach(y -> funcParams.add(y.toString()));
			Func outerFunc = new Func(funcType, className, functionName, queryName, funcParams, mapper);
			// Iterate and add the inner functions.
			JSONArray funcJsonArr = obj.has(FUNCTIONS) ? obj.getJSONArray(FUNCTIONS) : new JSONArray();
			outerFunc = this.addInnerFunctions(outerFunc, funcType, funcJsonArr);
			functions.add(outerFunc);
		});

		return new Funson(name, params, functions);
	}

	private Func addInnerFunctions(Func outerFunc, String funcType, JSONArray funcArray) {
		// IF future then dont allow Funsons inside. Only allow functions and type as
		// method
		if (funcType.equals(FUNC_TYPE_FUTURE)) {
			funcArray.forEach(x -> {
				JSONObject obj = (JSONObject) x;
				String queryName = obj.has(QUERY_NAME) ? obj.getString(QUERY_NAME) : null;
				String innerFuncType = obj.has(FUNC_TYPE) ? obj.getString(FUNC_TYPE) : FUNC_TYPE_METHOD;
				String className = obj.has(CLASS_NAME) ? obj.getString(CLASS_NAME) : null;
				String mapper = obj.has(MAPPER) ? obj.getString(MAPPER) : null;
				String functionName = obj.getString(FUNCTION_NAME);
				List<String> funcParams = new ArrayList<String>();

				if (!innerFuncType.equals(FUNC_TYPE_METHOD)) {
					throw new OrchestratorException(
							String.format("Only method type is allowed in configuring the concurrent process - %s",
									functionName),
							ERR_FUNSON_NOT_PRESENT);
				}

				JSONArray paramsJsonArr = obj.has(FUNC_PARAMS) ? obj.getJSONArray(FUNC_PARAMS) : new JSONArray();
				paramsJsonArr.forEach(y -> funcParams.add(y.toString()));
				Func innerFunc = new Func(innerFuncType, className, functionName, queryName, funcParams, mapper);
				outerFunc.addFunctions(innerFunc);
			});

		}
		return outerFunc;
	}

	@SuppressWarnings("unchecked")
	public <T> T processRequest(JSONObject apiParams, String funsonName) {
		Funson funs = preInterceptor.get(funsonName);
		if(funs!=null)
		{
			this.invokePreInterceptor(apiParams,funs);
		}
		Funson funson = funsonRegistry.get(funsonName);
		if (funson == null) {
			throw new OrchestratorException(String.format("No funson present - %s", funsonName),
					ERR_FUNSON_NOT_PRESENT);
		}
		this.checkParameters(apiParams, funson.getParameters());
		Object result = this.invokeFunson(apiParams, funson);
		return result != null ? (T) result : null;
	}

	private Object invokePreInterceptor(JSONObject apiParams,  Funson funson) {
		JSONObject obj = new JSONObject();
		for (Func function : funson.getFunctions()) {
			try {
				Object resultObj = null;
				if (orchestratorType.equals(ORCHESTRATOR_TYPE_INSTANCE)) {
					resultObj = this.invokeByInstance(apiParams, function);
				} else if (orchestratorType.equals(ORCHESTRATOR_TYPE_STATIC)) {
					resultObj = this.invokeByStatic(apiParams, function);
				} else if (orchestratorType.equals(ORCHESTRATOR_TYPE_SPRING)) {
					resultObj = this.invokeBySpring(apiParams, function);
				}
				obj.put(RESULT, resultObj);
			} 
			catch (OrchestratorException oe) {
				throw oe;	
			}
				catch (Exception ex) {
			
				throw new OrchestratorException(ex.toString(), ex);
			}
		}
		
		return apiParams;
	}

	private void checkParameters(JSONObject apiParams, List<Parameter> params) {
		params.forEach(x -> {

			if (x.getIsNullable() == false && apiParams.has(x.getName()) == false) {
				throw new OrchestratorException(String.format("Parameter missing - %s", x.getName()),
						ERR_PARAMETER_NOT_PRESENT);
			}
			// Validate
			try {
				Object valItem = apiParams.has(x.getName()) ? apiParams.get(x.getName()) : null;
				List<String> messages = this.valProcessor.validate(x.getName(), valItem, x.getValidations());
				if (messages.size() > 0) {
					throw new ValidationException(String.format("ERROR in applying validation - %s", x.getName()),
							messages);
				}
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		});
	}

	// TO DO: Validate the parameters for each function
	private Object invokeFunson(JSONObject apiParams, Funson funson) {
		JSONObject obj = new JSONObject();
		for (Func function : funson.getFunctions()) {
			List<String> params = function.getParams();
			
			if (function.getName() == null) {
				throw new OrchestratorException(
						String.format("Function name missing for - %s", function.getClassName()),
						ERR_FUNCTION_NAME_MISSING);
			}
			if (obj.has(RESULT)) {
				apiParams.put(PREV_RESULT, obj.get(RESULT));
			}

			if (function.getType().equals(FUNC_TYPE_FUNSON)) {
				// Call the inner funson recursively
				IParamMapper mapper = this.getMapper(function.getMapper());
				Funson innerFunson = funsonRegistry.get(function.getName());
				JSONObject newApiParams = mapper.map(apiParams, function.getMapper());
				Object resultObj = this.invokeFunson(newApiParams, innerFunson);
				obj.put(RESULT, resultObj);
			} else if (function.getType().equals(FUNC_TYPE_FUTURE)) {
				try {
					IParamMapper mapper = this.getMapper(function.getMapper());
					JSONObject newApiParams = mapper.map(apiParams, function.getMapper());
					List<Object> resultObj = this.futureExecutor.executeMethods(newApiParams, function.getfunctions());
					obj.put(RESULT, resultObj);
				} catch (Exception ex) {
					throw new OrchestratorException(ex.toString(), ex);
				}
			} else {
				try {

					// Invoke functions
					Object resultObj = null;
					if (orchestratorType.equals(ORCHESTRATOR_TYPE_INSTANCE)) {
						resultObj = this.invokeByInstance(apiParams, function);
					} else if (orchestratorType.equals(ORCHESTRATOR_TYPE_STATIC)) {
						resultObj = this.invokeByStatic(apiParams, function);
					} else if (orchestratorType.equals(ORCHESTRATOR_TYPE_SPRING)) {
						resultObj = this.invokeBySpring(apiParams, function);
					}
					obj.put(RESULT, resultObj);
				} catch (Exception ex) {
					throw new OrchestratorException(ex.toString(), ex);
				}
			}

		}

		return obj.has(RESULT) ? obj.get(RESULT) : null;
	}

	private IParamMapper getMapper(String mapper) {
		return new DefaultParamMapper();
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