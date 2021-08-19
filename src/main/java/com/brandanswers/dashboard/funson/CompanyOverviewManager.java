package com.brandanswers.dashboard.funson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.expression.ParseException;

import com.brandanswers.dashboard.orchestrator.Func;
import com.brandanswers.dashboard.orchestrator.OrchestratorException;

public class CompanyOverviewManager extends BaseDataAccessFunson {
	
	private static final String ERR_EMPLOYEE_NOT_EXIST = "ERR-0002";

	
public static JSONObject checkExistingEmployeeValidation(JSONObject params, Func functiondetails) 
{
	JSONObject obj = getData("checkExistingEmployeeValidation", params, result -> {
		JSONObject innerObj = new JSONObject();
		System.out.println(result);
		try {
			if (!result.next()) {
				throw new OrchestratorException(String.format("Employee Not Exist"), ERR_EMPLOYEE_NOT_EXIST);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return innerObj;
	});
	return obj;

}

	public static JSONObject getTotalNoOfProjects(JSONObject params, Func functiondetails) {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			AtomicInteger i = new AtomicInteger();
			ArrayList<String> resultList = new ArrayList<String>();
			int i1 = 0;
				try {
				  while (result.next()) {
							i1 = i.incrementAndGet();
							resultList.add( result.getString("prj_name"));
}
		
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("totalProjects", resultList);
			innerObj.put("totalNoOfProjects", i1);
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getActiveInactiveProjects(JSONObject params, Func functiondetails) {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<String> resultList = new ArrayList<String>();
			AtomicInteger i = new AtomicInteger();
			int i1 = 0;
				try {
					while (result.next()) {
							i1 = i.incrementAndGet();
							resultList.add( result.getString("prj_name"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("totalProjects", resultList);
			innerObj.put("totalNoOfProjects", i1);
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getClientDetails(JSONObject params, Func functiondetails) {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<String> resultList = new ArrayList<String>();
			AtomicInteger i = new AtomicInteger();
			int i1 = 0;
				try {
					while (result.next()) {
							i1 = i.incrementAndGet();
							resultList.add( result.getString("client_name"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("totalClientsList", resultList);
			innerObj.put("totalNoOfClients", i1);
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getTotalRAD(JSONObject params, Func functiondetails) {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();

				try {
					if(result.next()) {
						innerObj.put("total RADians", result.getInt("rowcount"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getActiveInactiveRAD(JSONObject params, Func functiondetails)
			 {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<String> resultList = new ArrayList<String>();
			AtomicInteger i = new AtomicInteger();
			int i1 = 0;
				try {
					while (result.next()) {
							i1 = i.incrementAndGet();
							resultList.add( result.getString("name"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("totalempoyees", resultList);
			innerObj.put("totalNoOfemployees", i1);
			return innerObj;

		});
		return obj;
	}
	
	public static JSONObject insertSalary(JSONObject params, Func functiondetails)
			 {
			JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
				JSONObject innerObj = new JSONObject();
				innerObj.put("Response", "Data Updated");
				return innerObj;
			});
			return obj;
		}
	public static JSONObject getProjectDetails(JSONObject params, Func functiondetails)
			 {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
				try {
					while (result.next()) {
						JSONObject resObj =new JSONObject();
						resObj.put("name", result.getString("name"));
						resObj.put("role", result.getString("role"));
						resObj.put("roleStatus", result.getString("role_status"));
						resultList.add(resObj);
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("teamMembers", resultList);
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getTotalSalary(JSONObject params, Func functiondetails)
			 {
		JSONObject obj = getData("getTotalSalary", params, result -> {
			JSONObject innerObj = new JSONObject();
				try {
					while (result.next()) {
						innerObj.put("totalSalary", result.getInt("sum"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getTotalExpenses(JSONObject params, Func functiondetails)
			{
		JSONObject obj = getData("getTotalExpenses", params, result -> {
			JSONObject innerObj = new JSONObject();
				try {
					while (result.next()) {
						innerObj.put("totalExpenses", result.getInt("sum"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getTotalAmount(JSONObject params, Func functiondetails)
			 {
		JSONObject obj = getData("getTotalAmount", params, result -> {
			JSONObject innerObj = new JSONObject();
				try {
					while (result.next()) {
						innerObj.put("totalAmount", result.getInt("sum"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getRevenueDetails(JSONObject params, Func functiondetails)
			 {
		JSONObject innerObj = new JSONObject();
		JSONObject totalSalary=getTotalSalary(params, functiondetails);
		JSONObject totalExpenses=getTotalExpenses(params, functiondetails);
		JSONObject totalAmount=getTotalAmount(params, functiondetails);
		Integer total=totalSalary.getInt("totalSalary")+totalExpenses.getInt("totalExpenses");
		Integer totalIncomeTable=totalAmount.getInt("totalAmount");
		Integer grandTotal=totalIncomeTable-total;
		innerObj.put("revenue",grandTotal);
		return innerObj;
	
		}
	
	public static JSONObject updateEmployee(JSONObject params, Func functiondetails){
		
		JSONObject obj = setData("updateEmployee", params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
		}
	public static JSONObject deleteDetails(JSONObject params, Func functiondetails)
			{
		
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Deleted");
			return innerObj;
		});
		return obj;
		}
	public static JSONObject insertClients(JSONObject params, Func functiondetails)
 {
		
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
		}
	public static JSONObject deleteClientDetails(JSONObject params, Func functiondetails)
	 {
		
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Deleted");
			return innerObj;
		});
		return obj;
		}
	
	public static JSONObject updateClients(JSONObject params, Func functiondetails)
			{
		
		JSONObject obj = setData("updateClients", params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
		}
	
	public static JSONObject insertProject(JSONObject params, Func functiondetails)
		 {
	
		
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
		}
	
	public static JSONObject updateProject(JSONObject params, Func functiondetails)
			{
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
		}
	public static JSONObject updateSalary(JSONObject params, Func functiondetails)
	 {
			JSONObject Validation=checkExistingEmployeeValidation(params,functiondetails);	
	 String query;
		if(params.has("mis_id")) {
		query= "updateSalary";
		}
		else {
			query="getUpdateSalary";
		}
	String sal=params.getString("salary");
	params.put("employee_salary",sal);

	int emp_id1=params.getInt("emp_id");
	params.put("employee_id1",emp_id1);
	params.put("emp_id3",emp_id1);
	params.put("emp_id4",emp_id1);
	params.put("emp_id5",emp_id1);

		JSONObject obj = setData(query, params, result -> {
			System.out.println(params);
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Updated");
			return innerObj;
		});
		return obj;
	}
	 public static JSONObject getProjects(JSONObject params, Func functiondetails)
				throws IOException, ParseException {
		 String query;
			if(params.has("prj_status")) {
			query= "getProjects";
			}
			else {
				query="getProject";
			}
			JSONObject obj = getData(query, params, result -> {
		
				JSONObject innerObj = new JSONObject();
				ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
					try {
						while (result.next()) {
							JSONObject resObj =new JSONObject();
							
							resObj.put("prj_id", result.getString("prj_id"));
							resObj.put("prj_name", result.getString("prj_name"));
							
							resultList.add(resObj);
	                  }
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
				innerObj.put("projectsName", resultList);
				return innerObj;

			});
			return obj;
		}
	}


