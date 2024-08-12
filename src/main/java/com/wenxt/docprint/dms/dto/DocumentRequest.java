package com.wenxt.docprint.dms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentRequest {
	@JsonProperty("TranId")
	private String TranId;
	private String filename;
	private String docType;

	// Getters and Setters

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTranId() {
		return TranId;
	}

	public void setTranId(String tranId) {
		TranId = tranId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
	
}
