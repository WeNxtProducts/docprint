package com.wenxt.docprint.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.service.Xdoc_JasperReportService;
import com.wenxt.docprint.util.FileUtility;

import fr.opensagres.xdocreport.core.XDocReportException;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/reports")
public class Xdoc_JasperReportController {

	@Autowired
	private Xdoc_JasperReportService reportService;

	@Autowired
	private FileUtility util;

	@PostMapping("/gettemplatedata")
	public String getTemplateData(@RequestBody Map<String, String> requestBody) {
		String screenName = requestBody.get("DPS_SCREEN_NAME");
		String jsonResponse = reportService.getTemplateData(screenName);
		return jsonResponse;
	}

//	@PostMapping("/generateJasper")
//	public String generateJasperReport(@RequestParam Long dpsSysid, HttpServletRequest request) throws JRException {
//		return reportService.generateJasperReport(dpsSysid, request);
//
//	}
//	
	
	  @PostMapping("/generateJasper")
	    public String generateJasperReport(@RequestBody Map<String, String> requestBody,
	                                       HttpServletRequest request) throws JRException {
	        String dpsSysidStr = requestBody.get("dpsSysid");

	        if (dpsSysidStr == null) {
	            throw new IllegalArgumentException("dpsSysid must be provided in the request body");
	        }

	        Long dpsSysid;
	        try {
	            dpsSysid = Long.parseLong(dpsSysidStr);
	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Invalid dpsSysid format. It must be a valid Long.");
	        }

	        return reportService.generateJasperReport(dpsSysid, request);
	    }
	

	@PostMapping("/generateXdoc")
	public ResponseEntity<?> generateXdocReport(@RequestBody Map<String, String> request)
			throws IOException, XDocReportException {
		String dpsSysidStr = request.get("dpsSysid");

		if (dpsSysidStr == null) {
			return ResponseEntity.badRequest().body("dpsSysid is required");
		}

		Long dpsSysid;
		try {
			dpsSysid = Long.parseLong(dpsSysidStr);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body("Invalid dpsSysid format");
		}

		String report = reportService.generateXdocReport(dpsSysid);
		return ResponseEntity.ok(report);
	}

	@PostMapping("/generateStaticDocs")
	public ResponseEntity<?> renderPdf(@RequestBody Map<String, String> request) {
		String templateName = request.get("templateName");

		if (templateName == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Template name is required");
		}

		Optional<String> fileLocation = reportService.getFileLocationByTemplateName(templateName);

		if (!fileLocation.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found for name: " + templateName);
		}

		try {
			byte[] pdfContent = util.readPdfFile(fileLocation.get());
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfContent);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error reading PDF file: " + e.getMessage());
		}
	}
}
