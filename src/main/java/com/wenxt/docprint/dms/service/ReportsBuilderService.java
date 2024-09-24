package com.wenxt.docprint.dms.service;

import java.util.List;
import java.util.Map;

import com.wenxt.docprint.dms.dto.ReportParameter;

public interface ReportsBuilderService {
	List<Map<String, Object>> generateReportgrid(Map<String, String> requestParams);

//	List<ReportParameter> getReportParameters();

	List<ReportParameter> getReportBuildersParameters(String screenName);
}
