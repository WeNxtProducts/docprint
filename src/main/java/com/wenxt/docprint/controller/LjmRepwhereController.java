package com.wenxt.docprint.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.service.LjmRepwhereService;

@RestController
@RequestMapping("/rep_where")
public class LjmRepwhereController {
	
	@Autowired
	private LjmRepwhereService repwhereService;
	
	
	@PostMapping("/create")
	private String LjmRepwhereCreates(@RequestBody ReportBuilderRequest reportBuilderRequest) {
		try {
			return repwhereService.LjmRepwhereCreates(reportBuilderRequest);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@PostMapping("/update/{LJM_REP_SYS_ID}")
	private String updateLjmRepwhere(@RequestBody ReportBuilderRequest reportBuilderRequest,
			@PathVariable Long LJM_REP_SYS_ID) {
		return repwhereService.updatelLjmRepwhere(reportBuilderRequest, LJM_REP_SYS_ID);
	}

	@PostMapping("/delete/{LJM_REP_SYS_ID}")
	public String deleteLjmRepwhere(@PathVariable Long LJM_REP_SYS_ID) {
		return repwhereService.deleteLjmRepwhere(LJM_REP_SYS_ID);
	}

	@PostMapping("/getId")
	public String getLjmRepwhere(@RequestParam Long LJM_REP_SYS_ID) {
		try {
			return repwhereService.getLjmRepwhere(LJM_REP_SYS_ID);
		} catch (Exception e) {
			return e.getMessage();
		}
	}


}
