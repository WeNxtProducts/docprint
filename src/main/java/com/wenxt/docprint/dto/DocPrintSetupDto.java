
package com.wenxt.docprint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocPrintSetupDto {

	@JsonProperty("frontForm")
	private FormFieldsDTO frontForm;

	public FormFieldsDTO getFrontForm() {
		return frontForm;
	}

	public void setFrontForm(FormFieldsDTO frontForm) {
		this.frontForm = frontForm;
	}

}
