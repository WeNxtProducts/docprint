package com.wenxt.docprint.dms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dms.service.ReportsBuilderService;

@RestController
@RequestMapping("/RepotBuilder")
public class ReportsBuilderController {

	@Autowired
	private ReportsBuilderService RepBuilderservice;

	@PostMapping("/generateGrid")
	public String generateReportGrid(@RequestBody Map<String, String> requestParams) {
		return RepBuilderservice.generateReportgrid(requestParams);
	}

	@GetMapping("/report/{screenName}")
	public String getReportBuildersParameter(@PathVariable String screenName) {
		return RepBuilderservice.getReportBuildersParameter(screenName);
	}
}
