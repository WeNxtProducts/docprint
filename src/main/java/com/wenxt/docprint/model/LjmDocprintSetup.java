package com.wenxt.docprint.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "LJM_DOCPRINT_SETUP", schema = "LIFE_DEV")
public class LjmDocprintSetup {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DPS_SYSID", nullable = false)
	@JsonProperty
	private Long DPS_SYSID;

	@Column(name = "DPS_TEMPLATE_NAME")
	private String DPS_TEMPLATE_NAME;

	@Column(name = "DPS_TYPE")
	private String DPS_TYPE;

	@Column(name = "DPS_TEMP_LOC")
	private String DPS_TEMP_LOC;

	@Column(name = "DPS_DESC")
	private String DPS_DESC;

	@Column(name = "DPS_SCREEN_NAME")
	private String DPS_SCREEN_NAME;

	@Column(name = "DPS_PROD_CODE")
	private String DPS_PROD_CODE;

	@Column(name = "DPS_CHANNEL")
	private String DPS_CHANNEL;

	@Column(name = "DPS_TRAN_STS")
	private String DPS_TRAN_STS;

	@Column(name = "DPS_PARAM1")
	private String DPS_PARAM1;

	@Column(name = "DPS_PARAM2")
	private String DPS_PARAM2;

	@Column(name = "DPS_PARAM3")
	private String DPS_PARAM3;

	@Column(name = "DPS_PARAM4")
	private String DPS_PARAM4;

	@Column(name = "DPS_PARAM5")
	private String DPS_PARAM5;

	@Column(name = "QPM_INS_DT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date QPM_INS_DT;

	@Column(name = "QPM_INS_ID")
	private String QPM_INS_ID;

	@Column(name = "QPM_MOD_DT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date QPM_MOD_DT;

	@Column(name = "QPM_MOD_ID")
	private String QPM_MOD_ID;

	

	public Long getDPS_SYSID() {
		return DPS_SYSID;
	}

	public void setDPS_SYSID(Long dPS_SYSID) {
		DPS_SYSID = dPS_SYSID;
	}

	public String getDPS_TEMPLATE_NAME() {
		return DPS_TEMPLATE_NAME;
	}

	public void setDPS_TEMPLATE_NAME(String dPS_TEMPLATE_NAME) {
		DPS_TEMPLATE_NAME = dPS_TEMPLATE_NAME;
	}

	public String getDPS_TYPE() {
		return DPS_TYPE;
	}

	public void setDPS_TYPE(String dPS_TYPE) {
		DPS_TYPE = dPS_TYPE;
	}

	public String getDPS_TEMP_LOC() {
		return DPS_TEMP_LOC;
	}

	public void setDPS_TEMP_LOC(String dPS_TEMP_LOC) {
		DPS_TEMP_LOC = dPS_TEMP_LOC;
	}

	public String getDPS_DESC() {
		return DPS_DESC;
	}

	public void setDPS_DESC(String dPS_DESC) {
		DPS_DESC = dPS_DESC;
	}

	public String getDPS_SCREEN_NAME() {
		return DPS_SCREEN_NAME;
	}

	public void setDPS_SCREEN_NAME(String dPS_SCREEN_NAME) {
		DPS_SCREEN_NAME = dPS_SCREEN_NAME;
	}

	public String getDPS_PROD_CODE() {
		return DPS_PROD_CODE;
	}

	public void setDPS_PROD_CODE(String dPS_PROD_CODE) {
		DPS_PROD_CODE = dPS_PROD_CODE;
	}

	public String getDPS_CHANNEL() {
		return DPS_CHANNEL;
	}

	public void setDPS_CHANNEL(String dPS_CHANNEL) {
		DPS_CHANNEL = dPS_CHANNEL;
	}

	public String getDPS_TRAN_STS() {
		return DPS_TRAN_STS;
	}

	public void setDPS_TRAN_STS(String dPS_TRAN_STS) {
		DPS_TRAN_STS = dPS_TRAN_STS;
	}

	public String getDPS_PARAM1() {
		return DPS_PARAM1;
	}

	public void setDPS_PARAM1(String dPS_PARAM1) {
		DPS_PARAM1 = dPS_PARAM1;
	}

	public String getDPS_PARAM2() {
		return DPS_PARAM2;
	}

	public void setDPS_PARAM2(String dPS_PARAM2) {
		DPS_PARAM2 = dPS_PARAM2;
	}

	public String getDPS_PARAM3() {
		return DPS_PARAM3;
	}

	public void setDPS_PARAM3(String dPS_PARAM3) {
		DPS_PARAM3 = dPS_PARAM3;
	}

	public String getDPS_PARAM4() {
		return DPS_PARAM4;
	}

	public void setDPS_PARAM4(String dPS_PARAM4) {
		DPS_PARAM4 = dPS_PARAM4;
	}

	public String getDPS_PARAM5() {
		return DPS_PARAM5;
	}

	public void setDPS_PARAM5(String dPS_PARAM5) {
		DPS_PARAM5 = dPS_PARAM5;
	}

	public Date getQPM_INS_DT() {
		return QPM_INS_DT;
	}

	public void setQPM_INS_DT(Date qPM_INS_DT) {
		QPM_INS_DT = qPM_INS_DT;
	}

	public String getQPM_INS_ID() {
		return QPM_INS_ID;
	}

	public void setQPM_INS_ID(String qPM_INS_ID) {
		QPM_INS_ID = qPM_INS_ID;
	}

	public Date getQPM_MOD_DT() {
		return QPM_MOD_DT;
	}

	public void setQPM_MOD_DT(Date qPM_MOD_DT) {
		QPM_MOD_DT = qPM_MOD_DT;
	}

	public String getQPM_MOD_ID() {
		return QPM_MOD_ID;
	}

	public void setQPM_MOD_ID(String qPM_MOD_ID) {
		QPM_MOD_ID = qPM_MOD_ID;
	}

	
	
}
