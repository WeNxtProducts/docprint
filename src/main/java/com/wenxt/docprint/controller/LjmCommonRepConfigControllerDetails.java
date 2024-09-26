package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dto.ReportBuilderRequestDto;
import com.wenxt.docprint.service.ljmCommonRepConfigDtlService;

@RestController
@RequestMapping("/common_ReportDtl")
public class LjmCommonRepConfigControllerDetails {

	@Autowired
	private ljmCommonRepConfigDtlService repconfigDtlService;

	@PostMapping("/create/{SRNO}")
	private String ljmCommonRepConfigCreates(@RequestBody ReportBuilderRequestDto reportBuilderRequest,
			@PathVariable Long SRNO) {
		return repconfigDtlService.createDocparam(reportBuilderRequest, SRNO);
	}

	@PostMapping("/updateDocparam/{SRNO}")
	private String updateljmCommonRepDtls(@RequestBody ReportBuilderRequestDto reportBuilderRequest,
			@PathVariable Long SRNO) {
		return repconfigDtlService.updateDocparam(reportBuilderRequest, SRNO);
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
