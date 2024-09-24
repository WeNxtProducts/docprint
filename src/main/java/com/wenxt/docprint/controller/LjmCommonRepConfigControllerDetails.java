package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.service.ljmCommonRepConfigDtlService;

@RestController
@RequestMapping("/common_ReportDtl")
public class LjmCommonRepConfigControllerDetails {

	@Autowired
	private ljmCommonRepConfigDtlService repconfigDtlService;

	@PostMapping("/create")
	private String ljmCommonRepConfigCreates(@RequestBody ReportBuilderRequest reportBuilderRequest) {
		try {
			return repconfigDtlService.ljmCommonRepConfigDtlsCreates(reportBuilderRequest);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@PostMapping("/update/{SRNO}")
	private String updateljmCommonRepDtls(@RequestBody ReportBuilderRequest reportBuilderRequest,
			@PathVariable Long SRNO) {
		return repconfigDtlService.updateljmCommonRepDtls(reportBuilderRequest, SRNO);
	}

	@PostMapping("/delete/{SRNO}")
	public String deleteljmCommonRepDtls(@PathVariable Long SRNO) {
		return repconfigDtlService.deleteljmCommonRepDtls(SRNO);
	}

	@PostMapping("/getId")
	public String getReportBuilderDtls(@RequestParam Long SRNO) {
		try {
			return repconfigDtlService.getReportBuilderDtls(SRNO);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
