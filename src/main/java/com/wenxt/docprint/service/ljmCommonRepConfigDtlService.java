package com.wenxt.docprint.service;

import com.wenxt.docprint.model.ReportBuilderRequest;

public interface ljmCommonRepConfigDtlService {

	String ljmCommonRepConfigDtlsCreates(ReportBuilderRequest reportBuilderRequest);

	String updateljmCommonRepDtls(ReportBuilderRequest reportBuilderRequest, Long sRNO);

	String deleteljmCommonRepDtls(Long sRNO);

	String getReportBuilderDtls(Long sRNO) throws Exception;

}
