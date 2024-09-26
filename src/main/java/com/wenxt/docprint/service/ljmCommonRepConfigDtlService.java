package com.wenxt.docprint.service;

import com.wenxt.docprint.dto.ReportBuilderRequestDto;

public interface ljmCommonRepConfigDtlService {

	String deleteljmCommonRepDtls(Long sRNO);

	String getReportBuilderDtls(Long sRNO) throws Exception;

	String createDocparam(ReportBuilderRequestDto reportBuilderRequest, Long sRNO);

	String updateDocparam(ReportBuilderRequestDto reportBuilderRequest, Long sRNO);

}
