package com.wenxt.docprint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportBuilderRequestDto {

	@JsonProperty("LJM_COMMON_REP_CONFIG_DTL")
	private FormFieldsDTO LJM_COMMON_REP_CONFIG_DTL;

	public FormFieldsDTO getLJM_COMMON_REP_CONFIG_DTL() {
		return LJM_COMMON_REP_CONFIG_DTL;
	}

	public void setLJM_COMMON_REP_CONFIG_DTL(FormFieldsDTO lJM_COMMON_REP_CONFIG_DTL) {
		LJM_COMMON_REP_CONFIG_DTL = lJM_COMMON_REP_CONFIG_DTL;
	}

}