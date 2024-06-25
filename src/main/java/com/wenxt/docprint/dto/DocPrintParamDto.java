package com.wenxt.docprint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocPrintParamDto {

	@JsonProperty("Doc_Print_Param")
	private FormFieldsDTO docPrintParam;

	public FormFieldsDTO getDocPrintParam() {
		return docPrintParam;
	}

	public void setDocPrintParam(FormFieldsDTO docPrintParam) {
		this.docPrintParam = docPrintParam;
	}
}