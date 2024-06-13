package com.wenxt.docprint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "LJM_DOCPRINT_PARAM", schema = "LIFE_DEV")
public class LjmDocprintParam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DPP_SYSID", nullable = false)
	private Long DPP_SYSID;

	@ManyToOne
	@JoinColumn(name = "DPP_DPS_SYSID", nullable = false)
	private LjmDocprintSetup LjmDocprintSetup;

	@Column(name = "DPP_TYPE")
	private String DPP_TYPE;

	@Column(name = "DPP_PARAM_NAME")
	private String DPP_PARAM_NAME;

	@Column(name = "DPP_VALUE")
	private String DPP_VALUE;

	// Getters and setters
	

	
	public Long getDPP_SYSID() {
		return DPP_SYSID;
	}

	

	


	public LjmDocprintSetup getLjmDocprintSetup() {
		return LjmDocprintSetup;
	}






	public void setLjmDocprintSetup(LjmDocprintSetup ljmDocprintSetup) {
		LjmDocprintSetup = ljmDocprintSetup;
	}






	public void setDPP_SYSID(Long dPP_SYSID) {
		DPP_SYSID = dPP_SYSID;
	}

	public String getDPP_TYPE() {
		return DPP_TYPE;
	}

	public void setDPP_TYPE(String dPP_TYPE) {
		DPP_TYPE = dPP_TYPE;
	}

	public String getDPP_PARAM_NAME() {
		return DPP_PARAM_NAME;
	}

	public void setDPP_PARAM_NAME(String dPP_PARAM_NAME) {
		DPP_PARAM_NAME = dPP_PARAM_NAME;
	}

	public String getDPP_VALUE() {
		return DPP_VALUE;
	}

	public void setDPP_VALUE(String dPP_VALUE) {
		DPP_VALUE = dPP_VALUE;
	}

	
}
