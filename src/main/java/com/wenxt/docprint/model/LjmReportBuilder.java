package com.wenxt.docprint.model;

import java.security.Timestamp;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LJM_REPORT_BUILDER")
public class LjmReportBuilder {

    @Id
  	@SequenceGenerator(name = "ReportBuilderSeq", sequenceName = "RB_SYS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReportBuilderSeq")
    @Column(name = "RB_SYS_ID")
    @JsonProperty("RB_SYS_ID")
    private Integer RB_SYS_ID;

    @Column(name = "RB_TEMP_NAME")
    @JsonProperty("RB_TEMP_NAME")
    private String RB_TEMP_NAME;

    @Column(name = "RB_TEMP_DESC")
    private String RB_TEMP_DESC;

    @Column(name = "RB_TEMP_QUERY")
    @JsonProperty("RB_TEMP_QUERY")
    private String RB_TEMP_QUERY;

    @Column(name = "RB_TEMP_PARAM_VAL")
    @JsonProperty("RB_TEMP_PARAM_VAL")
    private String RB_TEMP_PARAM_VAL;

    @Column(name = "RB_INS_DT")
    private Date RB_INS_DT;

    @Column(name = "RB_INS_ID")
    private String RB_INS_ID;

    @Column(name = "RB_UPD_DT")
    private Date RB_UPD_DT;

    @Column(name = "RB_UPD_ID")
    private String RB_UPD_ID;

    // Constructors, getters, and setters
    // Constructor with no arguments
    public LjmReportBuilder() {
    }

    // Getters and setters
    public Integer getRB_SYS_ID() {
        return RB_SYS_ID;
    }

    public void setRB_SYS_ID(Integer RB_SYS_ID) {
        this.RB_SYS_ID = RB_SYS_ID;
    }

    public String getRB_TEMP_NAME() {
        return RB_TEMP_NAME;
    }

    public void setRB_TEMP_NAME(String RB_TEMP_NAME) {
        this.RB_TEMP_NAME = RB_TEMP_NAME;
    }

    public String getRB_TEMP_DESC() {
        return RB_TEMP_DESC;
    }

    public void setRB_TEMP_DESC(String RB_TEMP_DESC) {
        this.RB_TEMP_DESC = RB_TEMP_DESC;
    }

    public String getRB_TEMP_QUERY() {
        return RB_TEMP_QUERY;
    }

    public void setRB_TEMP_QUERY(String RB_TEMP_QUERY) {
        this.RB_TEMP_QUERY = RB_TEMP_QUERY;
    }

    public String getRB_TEMP_PARAM_VAL() {
        return RB_TEMP_PARAM_VAL;
    }

    public void setRB_TEMP_PARAM_VAL(String RB_TEMP_PARAM_VAL) {
        this.RB_TEMP_PARAM_VAL = RB_TEMP_PARAM_VAL;
    }

    public Date getRB_INS_DT() {
        return RB_INS_DT;
    }

    public void setRB_INS_DT(Date RB_INS_DT) {
        this.RB_INS_DT = RB_INS_DT;
    }

    public String getRB_INS_ID() {
        return RB_INS_ID;
    }

    public void setRB_INS_ID(String RB_INS_ID) {
        this.RB_INS_ID = RB_INS_ID;
    }

    public Date getRB_UPD_DT() {
		return RB_UPD_DT;
	}

	public void setRB_UPD_DT(Date rB_UPD_DT) {
		RB_UPD_DT = rB_UPD_DT;
	}

	public String getRB_UPD_ID() {
        return RB_UPD_ID;
    }

    public void setRB_UPD_ID(String RB_UPD_ID) {
        this.RB_UPD_ID = RB_UPD_ID;
    }
}


