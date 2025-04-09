package com.wenxt.docprint.dms.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryParametersDTO {
	
	@JsonProperty("queryParams")
	Map<String, Object> queryParameters;

	public Map<String, Object> getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(Map<String, Object> queryParameters) {
		this.queryParameters = queryParameters;
	}

}
