package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.service.ljmCommonRepConfigService;

@RestController
@RequestMapping("/common_Report")
public class LjmCommonRepConfigController {
	
	@Autowired
	private ljmCommonRepConfigService repconfigService;
	
	
	@PostMapping("/create")
	private String ljmCommonRepConfigCreates(@RequestBody ReportBuilderRequest reportBuilderRequest) {
		try {
			return repconfigService.ljmCommonRepConfigCreates(reportBuilderRequest);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@PostMapping("/update/{REP_SYS_ID}")
	private String updateljmCommonRep(@RequestBody ReportBuilderRequest reportBuilderRequest,
			@PathVariable Long REP_SYS_ID) {
		return repconfigService.updateljmCommonRep(reportBuilderRequest, REP_SYS_ID);
	}

	@PostMapping("/delete/{REP_SYS_ID}")
	public String deleteljmCommonRep(@PathVariable Long REP_SYS_ID) {
		return repconfigService.deleteljmCommonRep(REP_SYS_ID);
	}

	@PostMapping("/getId")
	public String getReportBuilder(@RequestParam Long REP_SYS_ID) {
		try {
			return repconfigService.getReportBuilder(REP_SYS_ID);
		} catch (Exception e) {
			return e.getMessage();
		}
	}


}
