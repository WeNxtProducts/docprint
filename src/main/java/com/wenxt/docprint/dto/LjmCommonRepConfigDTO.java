package com.wenxt.docprint.dto;

import java.util.List;

public class LjmCommonRepConfigDTO {

	private Long repSysId;
	private String repId;
	private String repName;
	private String repMenuFlag;
	private String repParentId;

	private List<LjmCommonRepConfigDTO> childrens;

	// Getters and Setters
	public Long getRepSysId() {
		return repSysId;
	}

	public void setRepSysId(Long repSysId) {
		this.repSysId = repSysId;
	}

	public String getRepId() {
		return repId;
	}

	public void setRepId(String repId) {
		this.repId = repId;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getRepMenuFlag() {
		return repMenuFlag;
	}

	public void setRepMenuFlag(String repMenuFlag) {
		this.repMenuFlag = repMenuFlag;
	}

	public String getRepParentId() {
		return repParentId;
	}

	public void setRepParentId(String repParentId) {
		this.repParentId = repParentId;
	}

	public List<LjmCommonRepConfigDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<LjmCommonRepConfigDTO> childrens) {
		this.childrens = childrens;
	}
}
