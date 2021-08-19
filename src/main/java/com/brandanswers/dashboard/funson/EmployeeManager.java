package com.brandanswers.dashboard.funson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.brandanswers.dashboard.orchestrator.Func;

public class EmployeeManager extends BaseDataAccessFunson{
	
	public static JSONObject getEmployeeDetails(JSONObject params, Func functiondetails) {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
				try {
					if(result.next()) {
						JSONObject listResult = new JSONObject();
						try {
							listResult.put("employeeID", result.getInt("eid"));
							listResult.put("employeeName", result.getString("name"));
							listResult.put("employeeRole", result.getString("role"));
							listResult.put("workMail", result.getString("work_mail"));
							listResult.put("personalMail", result.getString("personal_mail"));
							listResult.put("contactNumber", result.getString("contact_number"));
							listResult.put("photo", result.getString("photo"));
							listResult.put("dateOfBirth", result.getDate("dob"));
							listResult.put("projectName", result.getString("on_project"));
							listResult.put("hiringDate", result.getDate("hiring_date"));
							listResult.put("terminationDate", result.getDate("termination_date"));
							listResult.put("gender", result.getString("gender"));
							listResult.put("employeeStatus", result.getInt("emp_status"));
							resultList.add(listResult);
						} catch (JSONException e) {
							e.printStackTrace();
						} 
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("Response",resultList);
			return innerObj;

});
return obj;
}
	public static JSONObject insertEmployee(JSONObject params, Func functiondetails) throws IOException, ParseException {
	
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
}

}
