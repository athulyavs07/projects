package com.brandanswers.dashboard.orchestrator;

import org.json.JSONObject;

public class DefaultParamMapper implements IParamMapper {

	// Error codes
	private static final String ERR_PARAMETER_MAPPER = "ORC-PMA-0001";

	@Override
	public JSONObject map(JSONObject inputParams, String pattern) {
		String[] mappingPattern = pattern!=null?pattern.split(","):new String[0];
		if(mappingPattern!=null && mappingPattern.length!=0) {
			try {
				JSONObject copyJson = new JSONObject(inputParams, JSONObject.getNames(inputParams));
				for(String item:mappingPattern) {
					String source = item.split(":")[0];
					String destination = item.split(":")[1];
					if(!source.equals(destination)) {
						copyJson.put(destination,copyJson.get(source));
						copyJson.remove(source);
					}
				}
				inputParams = copyJson;
			}
			catch(Exception e) {
				throw new OrchestratorException(String.format("Error in parameter mapper - %s", pattern), ERR_PARAMETER_MAPPER,e);
			}
			
		}
		return inputParams;
	}

}
