package com.wenxt.docprint.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wenxt.docprint.dms.model.QUERY_MASTER;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LJM_QUERY_PARAM_MASTER")
public class QUERY_PARAM_MASTER
{
	@Id
  	@SequenceGenerator(name = "QueryParamMasterSeq", sequenceName = "QPM_SYS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QueryParamMasterSeq")
	@Column(name = "QPM_SYS_ID")
	@JsonProperty("QPM_SYS_ID")
	private Integer QPM_SYS_ID;
	public Integer getQPM_SYS_ID()
	{
		return this.QPM_SYS_ID;
	}
	public void setQPM_SYS_ID(Integer value)
	{
		this.QPM_SYS_ID = value;
	}

	@ManyToOne
	@JoinColumn(name = "QPM_QM_SYS_ID")
	@JsonProperty("QPM_QM_SYS_ID")
	private QUERY_MASTER QPM_QM_SYS_ID;
	public QUERY_MASTER getQPM_QM_SYS_ID()
	{
		return this.QPM_QM_SYS_ID;
	}
	public void setQPM_QM_SYS_ID(QUERY_MASTER value)
	{
		this.QPM_QM_SYS_ID = value;
	}

	@Column(name = "QPM_PARAM_TYPE")
	@JsonProperty("QPM_PARAM_TYPE")
	private String QPM_PARAM_TYPE;
	public String getQPM_PARAM_TYPE()
	{
		return this.QPM_PARAM_TYPE;
	}
	public void setQPM_PARAM_TYPE(String value)
	{
		this.QPM_PARAM_TYPE = value;
	}

	@Column(name = "QPM_PARAM_NAME")
	@JsonProperty("QPM_PARAM_NAME")
	private String QPM_PARAM_NAME;
	public String getQPM_PARAM_NAME()
	{
		return this.QPM_PARAM_NAME;
	}
	public void setQPM_PARAM_NAME(String value)
	{
		this.QPM_PARAM_NAME = value;
	}

	@Column(name = "QPM_PARAM_VALUE")
	@JsonProperty("QPM_PARAM_VALUE")
	private String QPM_PARAM_VALUE;
	public String getQPM_PARAM_VALUE()
	{
		return this.QPM_PARAM_VALUE;
	}
	public void setQPM_PARAM_VALUE(String value)
	{
		this.QPM_PARAM_VALUE = value;
	}

	@Column(name = "QPM_INS_DT")
	private java.sql.Timestamp QPM_INS_DT;
	public java.sql.Timestamp getQPM_INS_DT()
	{
		return this.QPM_INS_DT;
	}
	public void setQPM_INS_DT(java.sql.Timestamp value)
	{
		this.QPM_INS_DT = value;
	}

	@Column(name = "QPM_INS_ID")
	private String QPM_INS_ID;
	public String getQPM_INS_ID()
	{
		return this.QPM_INS_ID;
	}
	public void setQPM_INS_ID(String value)
	{
		this.QPM_INS_ID = value;
	}

	@Column(name = "QPM_MOD_DT")
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

