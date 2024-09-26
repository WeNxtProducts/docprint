package com.wenxt.docprint.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "LJM_COMMON_REP_CONFIG")
public class LjmCommonRepConfig {

	@Id
	@SequenceGenerator(name = "LJM_COMMON_REG_CONFIG_Seq", sequenceName = "LJM_COMMON_REP_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LJM_COMMON_REG_CONFIG_Seq")
	@Column(name = "REP_SYS_ID")
	private Long REP_SYS_ID;

	@Column(name = "REP_ID")
	private String REP_ID;

	@Column(name = "REP_NAME")
	private String REP_NAME;

	@Column(name = "REP_MENU_FLAG")
	private String REP_MENU_FLAG;

	@Column(name = "REP_PARENT")
	private String REP_PARENT;

	@Column(name = "REP_ADDS1")
	private String REP_ADDS1;

	@Column(name = "REP_ADDS2")
	private String REP_ADDS2;

	@Column(name = "REP_ADDS3")
	private String REP_ADDS3;

	@Column(name = "REP_INS_DT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date REP_INS_DT;

	@Column(name = "REP_INS_ID")
	private String REP_INS_ID;

	@Column(name = "REP_MOD_DT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date REP_MOD_DT;

	@Column(name = "REP_MOD_ID")
	private String REP_MOD_ID;

	public Long getREP_SYS_ID() {
		return REP_SYS_ID;
	}

	public void setREP_SYS_ID(Long rEP_SYS_ID) {
		REP_SYS_ID = rEP_SYS_ID;
	}

	public String getREP_ID() {
		return REP_ID;
	}

	public void setREP_ID(String rEP_ID) {
		REP_ID = rEP_ID;
	}

	public String getREP_NAME() {
		return REP_NAME;
	}

	public void setREP_NAME(String rEP_NAME) {
		REP_NAME = rEP_NAME;
	}

	public String getREP_MENU_FLAG() {
		return REP_MENU_FLAG;
	}

	public void setREP_MENU_FLAG(String rEP_MENU_FLAG) {
		REP_MENU_FLAG = rEP_MENU_FLAG;
	}

	public String getREP_PARENT() {
		return REP_PARENT;
	}

	public void setREP_PARENT(String rEP_PARENT) {
		REP_PARENT = rEP_PARENT;
	}

	public String getREP_ADDS1() {
		return REP_ADDS1;
	}

	public void setREP_ADDS1(String rEP_ADDS1) {
		REP_ADDS1 = rEP_ADDS1;
	}

	public String getREP_ADDS2() {
		return REP_ADDS2;
	}

	public void setREP_ADDS2(String rEP_ADDS2) {
		REP_ADDS2 = rEP_ADDS2;
	}

	public String getREP_ADDS3() {
		return REP_ADDS3;
	}

	public void setREP_ADDS3(String rEP_ADDS3) {
		REP_ADDS3 = rEP_ADDS3;
	}

	public Date getREP_INS_DT() {
		return REP_INS_DT;
	}

	public void setREP_INS_DT(Date rEP_INS_DT) {
		REP_INS_DT = rEP_INS_DT;
	}

	public String getREP_INS_ID() {
		return REP_INS_ID;
	}

	public void setREP_INS_ID(String rEP_INS_ID) {
		REP_INS_ID = rEP_INS_ID;
	}

	public Date getREP_MOD_DT() {
		return REP_MOD_DT;
	}

	public void setREP_MOD_DT(Date rEP_MOD_DT) {
		REP_MOD_DT = rEP_MOD_DT;
	}

	public String getREP_MOD_ID() {
		return REP_MOD_ID;
	}

	public void setREP_MOD_ID(String rEP_MOD_ID) {
		REP_MOD_ID = rEP_MOD_ID;
	}

}
