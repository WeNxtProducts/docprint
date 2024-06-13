package com.wenxt.docprint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wenxt.docprint.dto.FormFieldsDTO;

public class ReportBuilderRequest {
	
	@JsonProperty("frontForm")
	private FormFieldsDTO frontForm;

	public FormFieldsDTO getFrontForm() {
		return frontForm;
	}

	public void setFrontForm(FormFieldsDTO frontForm) {
		this.frontForm = frontForm;
	}

}
