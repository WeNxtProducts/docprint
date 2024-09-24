package com.wenxt.docprint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

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

	

}
