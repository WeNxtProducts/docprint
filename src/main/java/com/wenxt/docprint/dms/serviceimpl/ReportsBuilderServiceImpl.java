package com.wenxt.docprint.dms.serviceimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.wenxt.docprint.dms.dto.ReportParameter;
import com.wenxt.docprint.dms.service.ReportsBuilderService;
import com.wenxt.docprint.security.QueryConfig;

@Service
public class ReportsBuilderServiceImpl implements ReportsBuilderService {

	@Value("${spring.message.code}")
	private String messageCode;

	@Value("${spring.status.code}")
	private String statusCode;

	@Value("${spring.data.code}")
	private String dataCode;

	@Value("${spring.success.code}")
	private String successCode;

	@Value("${spring.error.code}")
	private String errorCode;

	@Value("${spring.warning.code}")
	private String warnCode;

	@Value("${query.getDpsTempLoc}")
	private String getDpsTempLocQuery;

	@Value("${query.getJmWhereString}")
	private String getJmWhereStringQuery;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QueryConfig queryConfig;

	@Override
	public String generateReportgrid(Map<String, String> requestParams) {
		String repGlId = requestParams.get("REP_GL_ID");

		// Get DPS temp location
		String dpsTempLoc = jdbcTemplate.queryForObject(getDpsTempLocQuery, new Object[] { repGlId }, String.class);

		// Get JM Where String query
		String jmWhereString = jdbcTemplate.queryForObject(getJmWhereStringQuery, new Object[] { dpsTempLoc },
				String.class);

		// Replace placeholders with actual values from requestParams
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith("REP_VALUE_")) {
				String value = entry.getValue();
				jmWhereString = jmWhereString.replace(":" + key, "'" + value + "'");
			}
		}

		try {
			// Execute the dynamically created SQL query
			List<Map<String, Object>> result = jdbcTemplate.queryForList(jmWhereString);

			// Convert the result to a JSON array
			JSONArray jsonArray = new JSONArray();
			for (Map<String, Object> row : result) {
				JSONObject jsonObject = new JSONObject(row); // Convert each row (Map) into a JSONObject
				jsonArray.put(jsonObject); // Add each JSONObject to the JSONArray
			}

			// Create the final response JSON object
			JSONObject response = new JSONObject();
			response.put("status", "SUCCESS");
			response.put("status_msg", "Report grid fetched successfully");
			response.put("data", jsonArray);

			return response.toString(); // Return as a JSON string
		} catch (Exception e) {
			// In case of an error, return a JSON error response
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("status", "ERROR");
			errorResponse.put("status_msg", "An error occurred: " + e.getMessage());
			errorResponse.put("data", new JSONArray()); // Empty array for data in case of an error

			return errorResponse.toString(); // Return the error response as a JSON string
		}
	}

	public String getReportBuildersParameter(String screenName) {
		JSONObject response = new JSONObject();
		try {
			String query = queryConfig.getReportParametersQuery();
			List<ReportParameter> reportParameters = jdbcTemplate.query(query, new Object[] { screenName },
					new ResultSetExtractor<List<ReportParameter>>() {
						@Override
						public List<ReportParameter> extractData(ResultSet resultSet) throws SQLException {
							List<ReportParameter> reportParameters = new ArrayList<>();
							while (resultSet.next()) {
								ReportParameter parameter = new ReportParameter();
								parameter.setParam_RepColunmName(resultSet.getString("param_ip_rep_col"));
								parameter.setParam_Field_Name(resultSet.getString("param_name"));
								parameter.setParam_DataType(resultSet.getString("param_type"));
								parameter.setParam_Field_Required(resultSet.getString("pm_required"));
								parameter.setParam_Field_Order(resultSet.getString("param_order"));
								reportParameters.add(parameter);
							}
							return reportParameters;
						}
					});
			response.put("status", "SUCCESS");
			response.put("status_msg", "Menu List Fetched Successfully");
			response.put("data", reportParameters);
		} catch (Exception e) {
			response.put("status", "ERROR");
			response.put("status_msg", "An unexpected error occurred: " + e.getMessage());
		}
		return response.toString();
	}
}
