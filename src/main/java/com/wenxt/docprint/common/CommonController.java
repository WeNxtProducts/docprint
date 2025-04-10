package com.wenxt.docprint.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dms.dto.QueryParametersDTO;
import com.wenxt.docprint.dms.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/common")
public class CommonController {
	
	@Autowired
	private CommonService service;
	
	@PostMapping("/getMapQuery")
	public String getMapQuery(@RequestParam Integer queryId, @Nullable @RequestBody QueryParametersDTO queryParams) {
		return service.getMapQuery(queryId, queryParams);
	}

	@GetMapping("/getlov")
	public String getQueryLov(HttpServletRequest request) {
		return service.getQueryLOV(request);
	}
}
