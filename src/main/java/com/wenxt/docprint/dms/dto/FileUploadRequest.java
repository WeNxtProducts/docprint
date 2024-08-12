package com.wenxt.docprint.dms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUploadRequest {

	@JsonProperty("byteArry")
	private byte[] byteArry;

	@JsonProperty("DocType")
	private String docType;

	@JsonProperty("genType")
	private String genType;

	@JsonProperty("module")
	private String module;

	@JsonProperty("TranId")
	private String tranId;

	@JsonProperty("replaceFlag")
	private String replaceFlag;

	// Getters and Setters
	public byte[] getByteArry() {
		return byteArry;
	}

	

	public void setByteArry(byte[] byteArry) {
		this.byteArry = byteArry;
	}



	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getGenType() {
		return genType;
	}

	public void setGenType(String genType) {
		this.genType = genType;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getReplaceFlag() {
		return replaceFlag;
	}

	public void setReplaceFlag(String replaceFlag) {
		this.replaceFlag = replaceFlag;
	}
}
