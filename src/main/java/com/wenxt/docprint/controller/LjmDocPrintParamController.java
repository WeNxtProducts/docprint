package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dto.DocPrintParamDto;
import com.wenxt.docprint.dto.DocPrintSetupDto;
import com.wenxt.docprint.model.LjmDocprintParam;
import com.wenxt.docprint.service.LjmDocPrintParamService;

@RestController
@RequestMapping("/docparam")
public class LjmDocPrintParamController {

	@Autowired
	private LjmDocPrintParamService paramservice;

	@PostMapping("/create/{DPP_SYSID}")
	public String createDocparam(@RequestBody DocPrintParamDto param, @PathVariable Long DPP_SYSID) {
		return paramservice.createDocparam(param, DPP_SYSID);
	}

	@PostMapping("/updateDocparam/{DPP_SYSID}")
	public String updateDocparam(@RequestBody DocPrintParamDto param, @PathVariable Long DPP_SYSID) {
		return paramservice.updateDocparam(param, DPP_SYSID);
	}

	@PostMapping("getdocparambyid")
	public String getDocparamByID(@RequestParam Long dppSysid) {
		try {
			return paramservice.getDocparamByID(dppSysid);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@PostMapping("/deletedocparambyid/{dppSysid}")
	public String deleteDocparamByID(@PathVariable Long dppSysid) {
		return paramservice.deleteDocparamByID(dppSysid);
	}

}
