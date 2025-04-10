package com.wenxt.docprint.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

public class QueryParamMasterDTO {

	private Integer QPM_SYS_ID;
	public Integer getQPM_SYS_ID()
	{
		return this.QPM_SYS_ID;
	}
	public void setQPM_SYS_ID(Integer value)
	{
		this.QPM_SYS_ID = value;
	}

	private Integer QPM_QM_SYS_ID;
	public Integer getQPM_QM_SYS_ID()
	{
		return this.QPM_QM_SYS_ID;
	}
	public void setQPM_QM_SYS_ID(Integer value)
	{
		this.QPM_QM_SYS_ID = value;
	}

	private String QPM_PARAM_TYPE;
	public String getQPM_PARAM_TYPE()
	{
		return this.QPM_PARAM_TYPE;
	}
	public void setQPM_PARAM_TYPE(String value)
	{
		this.QPM_PARAM_TYPE = value;
	}

	private String QPM_PARAM_NAME;
	public String getQPM_PARAM_NAME()
	{
		return this.QPM_PARAM_NAME;
	}
	public void setQPM_PARAM_NAME(String value)
	{
		this.QPM_PARAM_NAME = value;
	}

	private String QPM_PARAM_VALUE;
	public String getQPM_PARAM_VALUE()
	{
		return this.QPM_PARAM_VALUE;
	}
	public void setQPM_PARAM_VALUE(String value)
	{
		this.QPM_PARAM_VALUE = value;
	}

	private java.sql.Timestamp QPM_INS_DT;
	public java.sql.Timestamp getQPM_INS_DT()
	{
		return this.QPM_INS_DT;
	}
	public void setQPM_INS_DT(java.sql.Timestamp value)
	{
		this.QPM_INS_DT = value;
	}

	private String QPM_INS_ID;
	public String getQPM_INS_ID()
	{
		return this.QPM_INS_ID;
	}
	public void setQPM_INS_ID(String value)
	{
		this.QPM_INS_ID = value;
	}

	private java.sql.Timestamp QPM_MOD_DT;
	public java.sql.Timestamp getQPM_MOD_DT()
	{
		return this.QPM_MOD_DT;
	}
	public void setQPM_MOD_DT(java.sql.Timestamp value)
	{
		this.QPM_MOD_DT = value;
	}

}
