package com.wenxt.docprint.dms.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wenxt.docprint.common.CommonDao;
import com.wenxt.docprint.common.LOVDTO;
import com.wenxt.docprint.common.QueryParamMasterDTO;
import com.wenxt.docprint.dms.dto.QueryParametersDTO;
import com.wenxt.docprint.dms.model.QUERY_MASTER;
import com.wenxt.docprint.dms.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public String getMapQuery(Integer queryId, QueryParametersDTO queryParams) {
		JSONObject response = new JSONObject();
		try {
			QUERY_MASTER query = commonDao.getQueryLov(queryId);
			if (query != null) {
				List<Map<String, Object>> result = commonDao.getMapQuery(query.getQM_QUERY(),
						queryParams.getQueryParameters());

				List<Map<String, Object>> finalResult = new ArrayList<>();
				if (!result.isEmpty()) {
					for (int i = 0; i < result.size(); i++) {
						Map<String, Object> resultMap = result.get(i);
						finalResult.add(resultMap);
					}
					response.put("status", "SUCCESS");
					response.put("status_msg", "Data Fetched Successfully");

					if (finalResult.size() >= 1) {
						response.put("Data", finalResult);
					}
				} else {
					response.put("status", "SUCCESS");
					response.put("status_msg", "No Datas Found");
					response.put("Data", new JSONObject());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "FAILURE");
			response.put("status_msg", "ERROR");
		}
		return response.toString();
	}
	
	@Override
	public String getQueryLOV(HttpServletRequest request) {
		Map<String, Object> params = processParamLOV(null, request);
		int queryId = Integer.parseInt(((String) params.get("queryId")));
		params.remove("queryId");
		JSONObject response = new JSONObject();
		QUERY_MASTER query = commonDao.getQueryLov(queryId);
		if (query != null) {
			if (query.getQM_QUERY_TYPE().equals("lov")) {
				List<LOVDTO> queryResult = commonDao.executeLOVQuery(query.getQM_QUERY(), new HashMap());
				response.put("status", "SUCCESS");
				response.put("Data", queryResult);
			} else if (query.getQM_QUERY_TYPE().equals("paramlov")) {
				List<QueryParamMasterDTO> queryParams = commonDao.getQueryParams(query.getQM_SYS_ID());
				Map<String, Object> paramsMap = processParamLOV(queryParams, request);
				paramsMap.remove("queryId");
				List<LOVDTO> queryResult = commonDao.executeLOVQuery(query.getQM_QUERY(), paramsMap);
				response.put("status", "SUCCESS");
				response.put("Data", queryResult);
			}
		} else {
			response.put("status", "FAILURE");
			response.put("status_msg", "ERROR");
		}
		return response.toString();
	}
	
	private Map<String, Object> processParamLOV(List<QueryParamMasterDTO> queryParams, HttpServletRequest request) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, Object> parameteres = new HashMap<>();
		if (queryParams != null) {
			for (QueryParamMasterDTO params : queryParams) {
				if (params.getQPM_PARAM_TYPE().equals("S")) {
					parameters.put(params.getQPM_PARAM_NAME(), params.getQPM_PARAM_VALUE());
				} else if (params.getQPM_PARAM_TYPE().equals("P")) {
					String queryString = request.getQueryString();
					if (queryString != null) {
						int i = 0;
						for (String keyValue : queryString.split("&")) {
							String[] parts = keyValue.split("=");
							if (parts.length == 2 && !parts[0].equals("queryId")) {
								parameters.put(queryParams.get(i).getQPM_PARAM_NAME(), parts[1]);
								i++;
							}
						}
//						int i = 0;
//						for (String keyValue : queryString.split("&")) {
//							String[] parts = keyValue.split("=");
//							if (parts.length == 2 && !parts[0].equals("queryId")) {
//								parameteres.put(queryParams.get(i).getQPM_PARAM_NAME(), parameters.get(queryParams.get(i).getQPM_PARAM_NAME()));
//								i++;
//							}
//						}
					}
					return parameters;
				}
			}
		} else {
			String queryString = request.getQueryString();
			if (queryString != null) {
				for (String keyValue : queryString.split("&")) {
					String[] parts = keyValue.split("=");
					if (parts.length == 2) {
						parameters.put(parts[0], parts[1]);
					}
				}
			}
			return parameters;
		}
		return parameters;
	}

}
