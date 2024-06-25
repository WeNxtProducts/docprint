package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.service.ReportService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/report")
public class ReportsController {

	@Autowired
	private ReportService reportservice;

	@PostMapping("/generatedocument")
	public ResponseEntity<String> generatedocument(HttpServletRequest request) {

		try {

			String response = reportservice.generatedocument(request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to generate report: " + e.getMessage());
		}
	}

}
