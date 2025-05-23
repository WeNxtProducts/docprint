package com.wenxt.docprint.common;

import java.util.List;
import java.util.Map;

import com.wenxt.docprint.dms.model.QUERY_MASTER;

public interface CommonDao {

	QUERY_MASTER getQueryLov(Integer queryId);

	List<Map<String, Object>> getMapQuery(String qm_QUERY, Map<String, Object> queryParameters);
	
	List<LOVDTO> executeLOVQuery(String query, Map<String, Object> paramList);
	
	List<QueryParamMasterDTO> getQueryParams(int sysId);



}
