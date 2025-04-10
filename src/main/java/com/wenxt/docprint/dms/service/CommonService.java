package com.wenxt.docprint.dms.service;

import com.wenxt.docprint.dms.dto.QueryParametersDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface CommonService {

	String getMapQuery(Integer queryId, QueryParametersDTO queryParams);
	
	public String getQueryLOV(HttpServletRequest request);


}
