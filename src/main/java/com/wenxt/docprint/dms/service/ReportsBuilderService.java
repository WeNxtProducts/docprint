package com.wenxt.docprint.dms.service;

import java.util.Map;

public interface ReportsBuilderService {

	String generateReportgrid(Map<String, String> requestParams);

	public String getReportBuildersParameter(String screenName);
}
