package com.wenxt.docprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dto.DocPrintSetupDto;
import com.wenxt.docprint.service.LjmDocPrintSetupService;

@RestController
@RequestMapping("/docprintsetup")
public class LjmDocPrintSetupController {

	@Autowired
	private LjmDocPrintSetupService setupserv;

	@PostMapping("/create")
	public String createDocPrintSetup(@RequestBody DocPrintSetupDto ljmdocprint) {
		return setupserv.createDocPrintSetup(ljmdocprint);
	}

	@PostMapping("/updateDocprintsetup/{DPS_SYSID}")
	public String updateDocprint(@RequestBody DocPrintSetupDto ljmdocprint, @PathVariable Long DPS_SYSID) {
		return setupserv.updateDocprint(ljmdocprint, DPS_SYSID);
	}

	@PostMapping("getDocPrintSetupbyid")
	public String getDocPrintSetupByID(@RequestParam Long dpsSysid) {
		try {
			return setupserv.getDocPrintSetupByID(dpsSysid);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@PostMapping("/deletexdocbyid/{dpsSysid}")
	public String deleteDocPrintSetupID(@PathVariable Long dpsSysid) {
		return setupserv.deleteDocPrintSetupID(dpsSysid);
	}

}
