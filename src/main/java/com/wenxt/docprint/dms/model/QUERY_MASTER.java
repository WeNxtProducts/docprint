package com.wenxt.docprint.dms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LJM_QUERY_MASTER")
public class QUERY_MASTER
{
	
	@Id
  	@SequenceGenerator(name = "QueryMasterSeq", sequenceName = "QM_SYS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QueryMasterSeq")
	@Column(name = "QM_SYS_ID")
	@JsonProperty("QM_SYS_ID")
	private int QM_SYS_ID;
	public int getQM_SYS_ID()
	{
		return this.QM_SYS_ID;
	}
	public void setQM_SYS_ID(int value)
	{
		this.QM_SYS_ID = value;
	}

	@Column(name = "QM_PROG_CODE")
	@JsonProperty("QM_PROG_CODE")
	private String QM_PROG_CODE;
	public String getQM_PROG_CODE()
	{
		return this.QM_PROG_CODE;
	}
	public void setQM_PROG_CODE(String value)
	{
		this.QM_PROG_CODE = value;
	}

	@Column(name = "QM_SCREEN_NAME")
	@JsonProperty("QM_SCREEN_NAME")
	private String QM_SCREEN_NAME;
	public String getQM_SCREEN_NAME()
	{
		return this.QM_SCREEN_NAME;
	}
	public void setQM_SCREEN_NAME(String value)
	{
		this.QM_SCREEN_NAME = value;
	}

	@Column(name = "QM_QUERY_TYPE")
	@JsonProperty("QM_QUERY_TYPE")
	private String QM_QUERY_TYPE;
	public String getQM_QUERY_TYPE()
	{
		return this.QM_QUERY_TYPE;
	}
	public void setQM_QUERY_TYPE(String value)
	{
		this.QM_QUERY_TYPE = value;
	}

	@Column(name = "QM_QUERY_NAME")
	@JsonProperty("QM_QUERY_NAME")
	private String QM_QUERY_NAME;
	public String getQM_QUERY_NAME()
	{
		return this.QM_QUERY_NAME;
	}
	public void setQM_QUERY_NAME(String value)
	{
		this.QM_QUERY_NAME = value;
	}

	@Column(name = "QM_QUERY_DESC")
	@JsonProperty("QM_QUERY_DESC")
	private String QM_QUERY_DESC;
	public String getQM_QUERY_DESC()
	{
		return this.QM_QUERY_DESC;
	}
	public void setQM_QUERY_DESC(String value)
	{
		this.QM_QUERY_DESC = value;
	}

	@Column(name = "QM_QUERY")
	@JsonProperty("QM_QUERY")
	private String QM_QUERY;
	public String getQM_QUERY()
	{
		return this.QM_QUERY;
	}
	public void setQM_QUERY(String value)
	{
		this.QM_QUERY = value;
	}

	@Column(name = "QM_INS_DT")
	private java.sql.Timestamp QM_INS_DT;
	public java.sql.Timestamp getQM_INS_DT()
	{
		return this.QM_INS_DT;
	}
	public void setQM_INS_DT(java.sql.Timestamp value)
	{
		this.QM_INS_DT = value;
	}

	@Column(name = "QM_INS_ID")
	private int QM_INS_ID;
	public int getQM_INS_ID()
	{
		return this.QM_INS_ID;
	}
	public void setQM_INS_ID(int value)
	{
		this.QM_INS_ID = value;
	}

	@Column(name = "QM_UPD_DT")
	private java.sql.Timestamp QM_UPD_DT;
	public java.sql.Timestamp getQM_UPD_DT()
	{
		return this.QM_UPD_DT;
	}
	public void setQM_UPD_DT(java.sql.Timestamp value)
	{
		this.QM_UPD_DT = value;
	}
}

