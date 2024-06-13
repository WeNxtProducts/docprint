package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.service.ReportBuilderService;

@RestController
@RequestMapping("/reportBuilder")
public class ReportBuilderController {
	
	@Autowired
	private ReportBuilderService reportBuilderService;
	
	@PostMapping("/createRB")
	private String createReportBuilder(@RequestBody ReportBuilderRequest reportBuilderRequest) {
		try {
			return reportBuilderService.createReportBuilder(reportBuilderRequest);
		}catch(Exception e) {
			return e.getMessage();
		}
	}
	
	@PostMapping("/updateRB/{rbSysId}")
	private String updateReportBuilder(@RequestBody ReportBuilderRequest reportBuilderRequest, @PathVariable Integer rbSysId) {
		return reportBuilderService.updateReportBuilder(reportBuilderRequest, rbSysId);
	}
	
	@PostMapping("/deleteRB/{rbSysId}")
	public String deleteReportBuilder(@PathVariable Integer rbSysId) {
		return reportBuilderService.deleteReportBuilder(rbSysId);
	}
	
	@PostMapping("/getRB")
	public String getReportBuilder(@RequestParam Integer rbSysId) {
		try {
			return reportBuilderService.getReportBuilder(rbSysId);
		}catch(Exception e) {
			return e.getMessage();
		}
	}

}

