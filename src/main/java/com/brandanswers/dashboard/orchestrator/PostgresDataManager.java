package com.brandanswers.dashboard.orchestrator;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;


@PropertySource("classpath:orchestrator.properties")
@Configuration
@Scope("singleton")
public class PostgresDataManager implements IDataManager<ResultSet> {

	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	private static final String ERR_SELECT_ERROR = "CDM-0001";
	private static final String ERR_UPDATE_ERROR = "CDM-0002";

	private Connection connection;
	private PreparedStatement preparedStatement;
	@PostConstruct
	private void init() throws SQLException {
		 connection = DriverManager.getConnection("jdbc:postgresql://34.194.147.55:5432/brandanswersDB",
				"postgres", "postgres");
		System.out.println(connection);
	}
	@Override
	public <O, P> O insertData(Query queryData, String key, P params, Function<ResultSet, O> transformData) {
		JSONObject apiParams = (JSONObject) params;
		System.out.println(apiParams);
		AtomicInteger i = new AtomicInteger();
		try {
			preparedStatement = this.connection.prepareStatement(queryData.getQuery(key));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		ResultSet results = null;
		
		try {
			if (queryData.getParameters().keySet().size() == 0) {
				// checking whether there is any
				results =preparedStatement.executeQuery();
			} else {
				
				queryData.getParameters().keySet().forEach(c -> { String paramName =c;
					try {
						int i1=i.incrementAndGet();
						this.add(preparedStatement, paramName, apiParams,queryData.getParameters().get(paramName),i1);
						}
					 catch (SQLException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				});
				
	    preparedStatement.execute();
	    results=preparedStatement.getResultSet();
	    
			}
		} catch(Exception e) {
			throw new OrchestratorException(e.toString(),ERR_UPDATE_ERROR,e);
		
	}
		return transformData!=null?transformData.apply(results):null;

	}

	@Override
	public <O, P> O selectData(Query queryData, String key, P params, Function<ResultSet, O> transformData) {
		AtomicInteger i = new AtomicInteger();
		JSONObject apiParams = (JSONObject) params;
		try {
			
        preparedStatement = this.connection.prepareStatement(queryData.getQuery(key));
		} catch (SQLException e1) {
			e1.printStackTrace();
		} // Preparing query

		ResultSet results = null;

		try {
			if (queryData.getParameters().keySet().size() == 0) {
				// checking whether there is any
				results = preparedStatement.executeQuery();
			} 
				 else {
					queryData.getParameters().keySet().forEach(c -> { String paramName =c;
				try {
					int i1=i.incrementAndGet();
					this.add(preparedStatement, paramName, apiParams,queryData.getParameters().get(paramName),i1);
					
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} } );
				results =preparedStatement.executeQuery(); 
				 }
				 
		} catch (Exception e) {
			throw new OrchestratorException(e.toString(), ERR_SELECT_ERROR, e);
		}

		return transformData.apply(results);
	}

	private PreparedStatement add(PreparedStatement statement, String name, JSONObject params, String type,int i)
			throws SQLException, ParseException {
		System.out.println(statement);
			switch (type) {
			case "Integer": {
				System.out.println(params.getInt(name));
				statement.setInt(i, params.getInt(name));
				break;
			}
			case "Float": {
				statement.setFloat(i, params.getFloat(name));
				break;
			}
			case "Decimal": {
				statement.setBigDecimal(i, params.getBigDecimal(name));
				break;
			}
			case "Boolean": {
				statement.setBoolean(i, params.getBoolean(name));
				break;
			}
			case "UUID": {
				statement.setObject(i, UUID.fromString(params.getString(name)));
				break;
			}
			case "long": {
				statement.setLong(i, params.getLong(name));
				break;
			}
			case "Date": {
				Date date =Date.valueOf("1900-01-01");
				String date2=params.getString(name);
				if(!date2.equals("")) {
				 date=Date.valueOf(date2);
				 }
				statement.setDate(i,date);
				break;
			}
			case "Array<String>": {
				ArrayList<String> listdata = new ArrayList<String>();
				JSONArray jArray = params.getJSONArray(name);
				if (jArray != null) {
					for (int j = 0; j < jArray.length(); j++) {
						listdata.add(jArray.getString(i));
					}
				}
				statement.setArray(1, (Array) listdata);
				break;
			}
			case "String":
			default: {
				statement.setString(i,params.getString(name));
			}
			}
		return statement;
		
	}


}
