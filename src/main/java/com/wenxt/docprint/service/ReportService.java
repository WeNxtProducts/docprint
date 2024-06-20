package com.wenxt.docprint.service;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

public interface ReportService {

	String generatedocument(HttpServletRequest request) throws JRException;

}
