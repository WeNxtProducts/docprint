package com.wenxt.docprint.dms.serviceimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public List<Map<String, Object>> generateReportgrid(Map<String, String> requestParams) {
		String repGlId = requestParams.get("REP_GL_ID");

		String dpsTempLoc = jdbcTemplate.queryForObject(getDpsTempLocQuery, new Object[] { repGlId }, String.class);

		String jmWhereString = jdbcTemplate.queryForObject(getJmWhereStringQuery, new Object[] { dpsTempLoc },
				String.class);

		for (Map.Entry<String, String> entry : requestParams.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith("REP_VALUE_")) {
				String value = entry.getValue();
				// Replace placeholders with actual values
				jmWhereString = jmWhereString.replace(":" + key, "'" + value + "'");
			}
		}

		try {
			// Execute the dynamically created SQL query
			List<Map<String, Object>> result = jdbcTemplate.queryForList(jmWhereString);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ReportParameter> getReportBuildersParameters(String screenName) {
		// Get the query from the configuration
		String query = queryConfig.getReportParametersQuery();

		// Use JdbcTemplate with ResultSetExtractor
		return jdbcTemplate.query(query, new Object[] { screenName }, new ResultSetExtractor<List<ReportParameter>>() {
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
	}
}
