package com.wenxt.docprint.service;

import com.wenxt.docprint.model.ReportBuilderRequest;

public interface ReportBuilderService {

	String createReportBuilder(ReportBuilderRequest reportBuilderRequest);

	String updateReportBuilder(ReportBuilderRequest reportBuilderRequest, Integer rbSysId);

	String deleteReportBuilder(Integer rbSysId);

	String getReportBuilder(Integer rbSysId) throws Exception;

}
