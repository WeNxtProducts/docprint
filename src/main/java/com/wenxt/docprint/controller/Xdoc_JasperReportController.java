package com.wenxt.docprint.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.service.Xdoc_JasperReportService;
import com.wenxt.docprint.util.FileUtility;

import jakarta.servlet.http.HttpServletRequest;

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

	@PostMapping("/generateJasper")
	public ResponseEntity<String> generateReport(HttpServletRequest request) {
		try {
			// Call the report generation method
			String response = reportService.generateJasperReport(request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to generate report: " + e.getMessage());
		}
	}

	@PostMapping("/generateXdoc")
	public ResponseEntity<String> generateXdocReport(HttpServletRequest request) {
		try {
			// Call the report generation method
			String response = reportService.generateXdocReport(request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to generate report: " + e.getMessage());
		}
	}

	@PostMapping("/generateStaticDocs")
    public ResponseEntity<?> renderPdf(@RequestBody Map<String, String> request) throws IOException {
        String templateName = request.get("templateName");

        if (templateName == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Template name is required");
        }

        Optional<String> fileLocation = reportService.getFileLocationByTemplateName(templateName);

        if (!fileLocation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template not found for name: " + templateName);
        }

        byte[] pdfBytes = reportService.readPdfFile(fileLocation.get());
		String pdfOutputFileName = fileLocation.get();

		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		
		response.put("statusCode", "successCode");
		response.put("message", "Report generated successfully");
		data.put("pdfPath", pdfOutputFileName);
		response.put("data", data);
		response.put("attachment", pdfBytes);

		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(response.toString());
    }
	
	
	
}
