package com.wenxt.docprint.service;

import com.wenxt.docprint.dto.DocPrintParamDto;

public interface LjmDocPrintParamService {

	String createDocparam(DocPrintParamDto param, Long dPP_SYSID);

	String getDocparamByID(Long dppSysid) throws IllegalArgumentException, IllegalAccessException;

	String deleteDocparamByID(Long dppSysid);

	String updateDocparam(DocPrintParamDto param, Long dPP_SYSID);

}
