package com.brandanswers.dashboard.funson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.brandanswers.dashboard.orchestrator.Func;
import com.brandanswers.dashboard.orchestrator.OrchestratorException;

public class RADManager extends BaseDataAccessFunson {
	private static final String ERR_EMPLOYEE_ALREADY_EXIST = "ERR-0001";

	public static JSONObject existingEmployeeValidation(JSONObject params, Func functiondetails) throws IOException {
		System.out.println("params"+params);
		JSONObject obj = getData("existingEmployeeValidation", params, result -> {
			JSONObject innerObj = new JSONObject();
			try {
				if (result.next()) {
					throw new OrchestratorException(String.format("Employee Already Exist"), ERR_EMPLOYEE_ALREADY_EXIST);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return innerObj;
		});
		return obj;
	}

	public static JSONObject getRADDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		System.out.print(params);
		 String query;
			if(params.has("emp_status")) {
			query= "getRADDetails";
			}
			else {
				query="searchRADDetails";
			}
		JSONObject obj = getData(query, params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
				try {
					while (result.next()) {
						JSONObject resObj =new JSONObject();
						resObj.put("role", result.getString("role"));
						resObj.put("name", result.getString("name"));
						resObj.put("photo", result.getString("photo"));
						resObj.put("roleStatus", result.getString("role_status"));
						resObj.put("radID", result.getString("display_id"));
						resObj.put("emp_id", result.getInt("emp_id"));
						resObj.put("Project", result.getString("on_project"));
						resObj.put("joiningDate", result.getDate("hiring_date"));
						resObj.put("department", result.getString("department"));
						resObj.put("salary", result.getInt("salary"));
						resObj.put("emp_status", result.getInt("emp_status"));
						resultList.add(resObj);
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("radTeamDetails", resultList);
			return innerObj;

		});
		return obj;
	}
	
	public static JSONObject getTotalRAD(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();

				try {
					if(result.next()) {
						innerObj.put("totalRADians", result.getInt("rowcount"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getRADInternCount(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params,result -> {
			JSONObject responseObj = new JSONObject();
			int radIntern = 0;
			int radEngineer = 0;
			int furlough = 0;
				try {
					
					while(result.next()) {	
						if(result.getString("role").equals("RADIntern")) {
							radIntern=result.getInt("count");
							responseObj.put("radIntern", result.getInt("count"));
						}
						if(result.getString("role").equals("RAD engineer")) {
							radEngineer=result.getInt("count");
							responseObj.put("radEngineer", result.getInt("count"));
							}
						if(result.getString("role").equals("Furlough")) {
							furlough=result.getInt("count");
							responseObj.put("furlough", result.getInt("count"));
							}
                  }
					responseObj.put("totalRAD",furlough + radEngineer + radIntern);
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return responseObj;

		});
		return obj;
	}
	public static JSONObject getRoleStatusNumbers(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			AtomicInteger i = new AtomicInteger();
			AtomicInteger i1 = new AtomicInteger();
			AtomicInteger i2 = new AtomicInteger();
			AtomicInteger i3 = new AtomicInteger();
				try {
					while(result.next()) {
						if(result.getString("role_status").equals("Backend Developer")) {
							innerObj.put("backendDevelopers",i.incrementAndGet());
						}
						if(result.getString("role_status").equals("Frontend Developer")) {
							innerObj.put("frontendDevelopers",i1.incrementAndGet());
						}
						if(result.getString("role_status").equals("Mobile Developer")) {
							innerObj.put("mobileDevelopers",i2.incrementAndGet());
						}
						if(result.getString("role_status").equals("UX Designer")) {
							innerObj.put("UX",i3.incrementAndGet());
						}
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData("getDetails", params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> listResult = new ArrayList<JSONObject>();
				try {
					while(result.next()) {
						JSONObject lastDigit = new JSONObject();
						lastDigit.put("lastDigit",
								result.getString("display_id").substring(result.getString("display_id").lastIndexOf("/") + 1));
						listResult.add(lastDigit);
                  }
					
						JSONObject lastDigit = new JSONObject();
						listResult.add(lastDigit.put("lastDigit", "99"));
					Collections.sort(listResult, new Comparator<JSONObject>() {
						@Override
						public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
							int compare = 0;
							Integer keyA = jsonObjectA.getInt("lastDigit");
							Integer keyB = jsonObjectB.getInt("lastDigit");
							compare = Integer.compare(keyA, keyB);
							return compare;
						}
					});
					
				} catch (JSONException | SQLException e) {

					e.printStackTrace();
				}
				JSONObject id = listResult.get(listResult.size() - 1);
			
				Integer idLastPart = Integer.parseInt(id.getString("lastDigit")) + 1;
				innerObj.put("Response", idLastPart);
				
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getFileDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData("getFileDetails", params, result -> {
			JSONObject innerObj = new JSONObject();
			
				try {
					while (result.next()) {
						
						innerObj.put("photo", result.getString("photo"));
						innerObj.put("resumePath", result.getString("resume"));
						innerObj.put("idproofPath", result.getString("id_proof_photo"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject insertRAD(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		String query;
		JSONObject params4=new JSONObject();
		if(params.has("empID") && params.getString("photo").isEmpty()){
			Integer emp_id=params.getInt("empID");
			params4.put("emp_id", emp_id);
			JSONObject fileDetails=getFileDetails(params4,functiondetails);
			String photo=fileDetails.has("photo")?fileDetails.getString("photo"):"";
			params.put("photo", photo);
			}
		if(params.has("empID") && params.getString("resumePath").isEmpty()){
			Integer emp_id=params.getInt("empID");
			params4.put("emp_id", emp_id);
			JSONObject fileDetails=getFileDetails(params4,functiondetails);
			String resumePath=fileDetails.has("resumePath")?fileDetails.getString("resumePath"):"";
			params.put("resumePath", resumePath);
			}
		if(params.has("empID") && params.getString("idproofPath").isEmpty()){
			Integer emp_id=params.getInt("empID");
			params4.put("emp_id", emp_id);
			JSONObject fileDetails=getFileDetails(params4,functiondetails);
			String idproofPath=fileDetails.has("idproofPath")?fileDetails.getString("idproofPath"):"";
			params.put("idproofPath",idproofPath);
			}
		
		if(!params.has("empID") && params.has("workMail")) {
		query= "insertRAD";
		}
		else if(params.has("empID")&& params.has("workMail")) {
			query="insertEmpRAD";
		}
		else {
			query="insertWorkEmpRAD";
		}
		String roleText=params.getString("role");
		String role="{role:"+roleText+"}";
		JSONObject paramRole = new JSONObject(role); 
		JSONObject generatedID =getDetails(paramRole,  functiondetails);
		String employeeName = ((String) params.get("f_name")).concat(" ").concat((String) params.get("l_name"));
		params.put("name", employeeName.trim());
		
		String workMail=params.has("workMail")?params.getString("workMail"):null;
		JSONObject param = new JSONObject(); 
		param.put("workMail", workMail);
		JSONObject Validation=params.has("workMail")?existingEmployeeValidation(param,functiondetails):null;
		

		String internID="RAD/INT/";
		String engineerID="RAD/ENG/";
                String furloughID="RAD/FUR/";
		String mentorID="TEMP/MEN/";
		JSONObject param2=new JSONObject();
		if(params.has("empID")) {
		String empId=params.getString("empID");
		param2.put("emp_id", empId);
		}
		JSONObject roleDetails=params.has("empID")?getExistingRADDetails(param2,functiondetails):null;
		System.out.println(roleDetails!= null);
		if(roleDetails!= null && roleDetails.get("role").equals("RADIntern") && params.getString("role").equals("RAD engineer")) {
			String id=engineerID.concat(generatedID.get("Response").toString());
		params.put("display_id", id);
		}
		if(!params.has("display_id")&&params.getString("role").equals("RADIntern")) {
			String id=internID.concat(generatedID.get("Response").toString());
			params.put("display_id", id);
		}
		if(!params.has("display_id")&& params.getString("role").equals("RAD engineer")) {
			String id=engineerID.concat(generatedID.get("Response").toString());
			params.put("display_id", id);
		}
                if(!params.has("display_id")&& params.getString("role").equals("Furlough")) {
			String id=furloughID.concat(generatedID.get("Response").toString());
			params.put("display_id", id);
		}
		if(!params.has("display_id")&& params.getString("role").equals("Mentor")) {
			String id=mentorID.concat(generatedID.get("Response").toString());
			params.put("display_id", id);
		}
		System.out.println(params.getString("photo"));
		params.remove("previousResult");
		JSONObject obj = setData(query, params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		return obj;
		}
	public static JSONObject getDepartmentDetails(JSONObject params, Func functiondetails)
				throws IOException, ParseException {
			JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
				JSONObject innerObj = new JSONObject();
				ArrayList<JSONObject> result_list = new ArrayList<JSONObject>();
					try {
						while (result.next()) {
							JSONObject resObj =new JSONObject();
							
							resObj.put("dept_id", result.getString("dept_id"));
							resObj.put("dept_name", result.getString("dept_name"));
							
							result_list.add(resObj);
	                  }
					} catch (JSONException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				innerObj.put("department", result_list);
				return innerObj;

			});
			return obj;
		}

	 public static JSONObject getRoleList(JSONObject params, Func functiondetails)
				throws IOException, ParseException {
		 JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
				JSONObject innerObj = new JSONObject();
				ArrayList<String> resultList = new ArrayList<String>();
				
				
					try {
						while (result.next()) {
								
							resultList.add( result.getString("role"));
	                  }
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
				innerObj.put("roleList", resultList);
				
				return innerObj;

			});
			return obj;
		}
	 public static JSONObject roleDescriptionList(JSONObject params, Func functiondetails)
				throws IOException, ParseException {
		 JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
				JSONObject innerObj = new JSONObject();
				ArrayList<String> resultList = new ArrayList<String>();
					try {
						while (result.next()) {
								
							resultList.add( result.getString("role_status"));
	                  }
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
				innerObj.put("roleDescription", resultList);
				
				return innerObj;

			});
			return obj;
		}

	public static JSONObject searchIndividualRAD(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
				try {
					while (result.next()) {
						innerObj.put("deptID", result.getString("dept_id"));
						innerObj.put("firstName", result.getString("f_name"));
						innerObj.put("lastName", result.getString("l_name"));
						innerObj.put("name", result.getString("name"));
						innerObj.put("photo", result.getString("photo"));
						innerObj.put("idProof", result.getString("id_proof"));
						innerObj.put("role", result.getString("role"));
						innerObj.put("roleStatus", result.getString("role_status"));
						innerObj.put("accountNumber", result.getString("account_number"));
						innerObj.put("ifscCode", result.getString("ifsc_code"));
						innerObj.put("bankName", result.getString("bank_name"));
						innerObj.put("branchName", result.getString("branch_name"));
						innerObj.put("radID", result.getString("display_id"));
						innerObj.put("personalMail", result.getString("personal_mail"));
						innerObj.put("contactNumber", result.getString("contact_number"));
						innerObj.put("dateOfBirth", result.getDate("dob"));
						innerObj.put("project", result.getString("on_project"));
						innerObj.put("joiningDate", result.getDate("hiring_date"));
						innerObj.put("terminationDate", result.getDate("termination_date"));
						innerObj.put("gender", result.getString("gender"));
						innerObj.put("employeeStatus", result.getInt("emp_status"));
						innerObj.put("status", result.getString("status"));
						innerObj.put("department", result.getString("department"));
						innerObj.put("salary", result.getString("salary"));
						innerObj.put("aadharNumber", result.getString("convert_from"));
						innerObj.put("workMail", result.getString("work_mail"));
						innerObj.put("totalSalary", result.getInt("total_salary"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject getExistingRADDetails(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData("getExistingRADDetails", params, result -> {
			JSONObject innerObj = new JSONObject();

				try {
					if(result.next()) {
						innerObj.put("role", result.getString("role"));
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}
	public static JSONObject updateRAD(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		String query=params.has("workMail")?"updateRAD":"updateRADWorkMail";
		String workMail=params.has("workMail")?params.getString("workMail"):null;
		JSONObject param = new JSONObject(); 
		param.put("workMail", workMail);
		JSONObject Validation=params.has("workMail")?existingEmployeeValidation(param,functiondetails):null;
		
		String roleText=params.getString("role");
		String role="{role:"+roleText+"}";
		JSONObject paramRole = new JSONObject(role); 
		JSONObject generatedID =getDetails(paramRole,  functiondetails);
		String employeeName = ((String) params.get("f_name")).concat(" ").concat((String) params.get("l_name"));
		params.put("name", employeeName.trim());
		
		String empId=params.getString("emp_id");
		String emp="{emp_id:"+empId+"}";
		JSONObject param2 = new JSONObject(emp); 
		JSONObject roleDetails=getExistingRADDetails(param2,functiondetails);
		
		String engineerID="RAD/ENG/";
		if(!roleDetails.isEmpty()&&roleDetails.get("role").equals("RADIntern")&& params.getString("role").equals("RAD engineer")) {
			String id=engineerID.concat(generatedID.get("Response").toString());
		params.put("display_id", id);
		}
	
	   params.remove("previousResult");
		System.out.println(params);
		
		JSONObject obj = setData(query, params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		
		return obj;
		}
	public static JSONObject updateStatus(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		System.out.println(params);
		JSONObject obj = setData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			innerObj.put("Response", "Data Inserted");
			return innerObj;
		});
		
		return obj;
		}
	public static JSONObject getResume(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();

				try {
					if(result.next()) {
						innerObj.put("file", result.getString("resume"));
						innerObj.put("fileName", result.getString("resume").substring(result.getString("resume").lastIndexOf("\\") + 1));
            }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			return innerObj;

		});
		return obj;
	}

	public static JSONObject getMiscellaneous(JSONObject params, Func functiondetails)
			throws IOException, ParseException {
		JSONObject obj = getData(functiondetails.getQueryName(), params, result -> {
			JSONObject innerObj = new JSONObject();
			ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
				try {
					while (result.next()) {
						JSONObject resObj =new JSONObject();
						
						resObj.put("mis_id", result.getInt("mis_id"));
						resObj.put("mis_type", result.getString("mis_type"));
						
						resultList.add(resObj);
                  }
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			innerObj.put("miscellaneous", resultList);
			return innerObj;

		});
		return obj;
	}
}
