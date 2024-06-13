package com.wenxt.docprint.dto;

import java.util.Map;

public class FormFieldsDTO {
	private Map<String, String> formFields;
	private String label;

	// Getters and setters
	public Map<String, String> getFormFields() {
		return formFields;
	}

	public void setFormFields(Map<String, String> formFields) {
		this.formFields = formFields;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
