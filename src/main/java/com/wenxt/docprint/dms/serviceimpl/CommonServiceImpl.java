package com.wenxt.docprint.dms.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.wenxt.common.CommonDao;
import com.wenxt.docprint.dms.dto.QueryParametersDTO;
import com.wenxt.docprint.dms.model.QUERY_MASTER;
import com.wenxt.docprint.dms.service.CommonService;

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
			response.put("status_msg", e.getMessage());
		}
		return response.toString();
	}

}
