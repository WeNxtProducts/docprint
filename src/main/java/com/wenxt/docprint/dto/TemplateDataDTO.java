package com.wenxt.docprint.dto;

public class TemplateDataDTO {
	private Long DPS_SYSID;
	private String DPS_TEMP_LOC;

	public TemplateDataDTO(Long dpsSysid, String dpsTempLoc) {
		this.DPS_SYSID = dpsSysid;
		this.DPS_TEMP_LOC = dpsTempLoc;
	}

	public Long getDPS_SYSID() {
		return DPS_SYSID;
	}

	public void setDPS_SYSID(Long dPS_SYSID) {
		DPS_SYSID = dPS_SYSID;
	}

	public String getDPS_TEMP_LOC() {
		return DPS_TEMP_LOC;
	}

	public void setDPS_TEMP_LOC(String dPS_TEMP_LOC) {
		DPS_TEMP_LOC = dPS_TEMP_LOC;
	}

}
