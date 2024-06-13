package com.wenxt.docprint.service;

import java.io.IOException;
import java.util.Optional;

import fr.opensagres.xdocreport.core.XDocReportException;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

public interface Xdoc_JasperReportService {

	String getTemplateData(String screenName);

	String generateJasperReport(Long dpsSysid, HttpServletRequest request) throws JRException;

	String generateXdocReport(Long dpsSysid)throws IOException, XDocReportException;

	Optional<String> getFileLocationByTemplateName(String templateName);



}
