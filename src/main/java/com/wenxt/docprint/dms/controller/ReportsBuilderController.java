package com.wenxt.docprint.dms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dms.dto.ReportParameter;
import com.wenxt.docprint.dms.dto.ScreenNameRequest;
import com.wenxt.docprint.dms.service.ReportsBuilderService;

@RestController
@RequestMapping("/RepotBuilder")
public class ReportsBuilderController {

	@Autowired
	private ReportsBuilderService RepBuilderservice;

	@PostMapping("/genReports")
	public ResponseEntity<List<Map<String, Object>>> generateReportgrid(
			@RequestBody Map<String, String> requestParams) {
		try {

			List<Map<String, Object>> result = RepBuilderservice.generateReportgrid(requestParams);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/reportFields")
	public List<ReportParameter> getReportBuildersParameters(@RequestBody ScreenNameRequest screenNameRequest) {
		return RepBuilderservice.getReportBuildersParameters(screenNameRequest.getScreenName());
	}
}
