package com.wenxt.docprint.service;

import com.wenxt.docprint.dto.DocPrintSetupDto;
import com.wenxt.docprint.model.LjmDocprintSetup;

import jakarta.servlet.http.HttpServletRequest;

public interface LjmDocPrintSetupService {

	String createDocPrintSetup(DocPrintSetupDto ljmdocprint);

	String getDocPrintSetupByID(Long dpsSysid) throws IllegalArgumentException, IllegalAccessException;

	String deleteDocPrintSetupID(Long dpsSysid);

	String updateDocprint(DocPrintSetupDto ljmdocprint, Long dPS_SYSID);

}
