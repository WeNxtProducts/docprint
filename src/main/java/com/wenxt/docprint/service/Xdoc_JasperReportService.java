package com.wenxt.docprint.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import fr.opensagres.xdocreport.core.XDocReportException;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

public interface Xdoc_JasperReportService {

	String getTemplateData(String screenName);

	Optional<String> getFileLocationByTemplateName(String templateName);

	String generateJasperReport(HttpServletRequest request) throws JRException, IOException;

	String generateXdocReport(HttpServletRequest request) throws JRException, IOException;

	byte[] readPdfFile(String string) throws IOException;

	

	
}
