package com.wenxt.docprint.dms.service;

import com.wenxt.docprint.dms.dto.QueryParametersDTO;

public interface CommonService {

	String getMapQuery(Integer queryId, QueryParametersDTO queryParams);

}
