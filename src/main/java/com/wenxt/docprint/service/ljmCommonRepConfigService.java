package com.wenxt.docprint.service;

import com.wenxt.docprint.model.ReportBuilderRequest;

public interface ljmCommonRepConfigService {

	String ljmCommonRepConfigCreates(ReportBuilderRequest reportBuilderRequest);

	String updateljmCommonRep(ReportBuilderRequest reportBuilderRequest, Long rEP_SYS_ID);

	String deleteljmCommonRep(Long rEP_SYS_ID);

	String getReportBuilder(Long rEP_SYS_ID) throws Exception;

}
