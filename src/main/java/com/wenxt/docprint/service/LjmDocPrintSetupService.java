package com.wenxt.docprint.service;

import com.wenxt.docprint.dto.DocPrintSetupDto;

public interface LjmDocPrintSetupService {

	String createDocPrintSetup(DocPrintSetupDto ljmdocprint);

	String getDocPrintSetupByID(Long dpsSysid) throws IllegalArgumentException, IllegalAccessException;

	String deleteDocPrintSetupID(Long dpsSysid);

	String updateDocprint(DocPrintSetupDto ljmdocprint, Long dPS_SYSID);

}
