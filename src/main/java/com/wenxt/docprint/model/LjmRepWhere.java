package com.wenxt.docprint.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ljm_rep_where")
public class LjmRepWhere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LJM_REP_SYS_ID", nullable = false)
    private Long ljmRepSysId;

    @Column(name = "LJM_REP_LEVEL", nullable = false)
    private Integer ljmRepLevel;

    @Column(name = "LJM_WHERE_STRING", nullable = false, length = 4000)
    private String ljmWhereString;

    @Column(name = "LJM_WHERE_STRING1", length = 4000)
    private String ljmWhereString1;

    @Column(name = "LJM_GROUP_BY", length = 4000)
    private String ljmGroupBy;

    @Column(name = "LJM_REP_TYPE_FLAG", nullable = false, length = 1)
    private String ljmRepTypeFlag;

    @Column(name = "LJM_REP_SQL_STRING", length = 4000)
    private String ljmRepSqlString;

    @Column(name = "LJM_REP_CR_UID", length = 12)
    private String ljmRepCrUid;

    @Column(name = "LJM_REP_CR_DT")
    private Date ljmRepCrDt;

    @Column(name = "LJM_REP_UPD_UID", length = 12)
    private String ljmRepUpdUid;

    @Column(name = "LJM_REP_UPD_DT")
    private Date ljmRepUpdDt;

    @Column(name = "LJM_REP_QUERY_STRING", length = 4000)
    private String ljmRepQueryString;

    @Column(name = "LJM_REP_CLIENT_ID", nullable = false, length = 12)
    private String ljmRepClientId;

    @Column(name = "LJM_REP_INST_CODE", length = 60)
    private String ljmRepInstCode;

    @Column(name = "LJM_REP_SECURITY_CODE", length = 12)
    private String ljmRepSecurityCode;

    @Column(name = "LJM_WHERE_CLIENT_ID", nullable = false, length = 12)
    private String ljmWhereClientId;

    @Column(name = "LJM_WHERE_INST_CODE", length = 60)
    private String ljmWhereInstCode;

    @Column(name = "LJM_WHERE_SECURITY_CODE", length = 12)
    private String ljmWhereSecurityCode;

    @Column(name = "LJM_GROUP_CLIENT_ID", nullable = false, length = 12)
    private String ljmGroupClientId;

    @Column(name = "LJM_GROUP_INST_CODE", length = 60)
    private String ljmGroupInstCode;

    @Column(name = "LJM_GROUP_SECURITY_CODE", length = 12)
    private String ljmGroupSecurityCode;

	public Long getLjmRepSysId() {
		return ljmRepSysId;
	}

	public void setLjmRepSysId(Long ljmRepSysId) {
		this.ljmRepSysId = ljmRepSysId;
	}

	public Integer getLjmRepLevel() {
		return ljmRepLevel;
	}

	public void setLjmRepLevel(Integer ljmRepLevel) {
		this.ljmRepLevel = ljmRepLevel;
	}

	public String getLjmWhereString() {
		return ljmWhereString;
	}

	public void setLjmWhereString(String ljmWhereString) {
		this.ljmWhereString = ljmWhereString;
	}

	public String getLjmWhereString1() {
		return ljmWhereString1;
	}

	public void setLjmWhereString1(String ljmWhereString1) {
		this.ljmWhereString1 = ljmWhereString1;
	}

	public String getLjmGroupBy() {
		return ljmGroupBy;
	}

	public void setLjmGroupBy(String ljmGroupBy) {
		this.ljmGroupBy = ljmGroupBy;
	}

	public String getLjmRepTypeFlag() {
		return ljmRepTypeFlag;
	}

	public void setLjmRepTypeFlag(String ljmRepTypeFlag) {
		this.ljmRepTypeFlag = ljmRepTypeFlag;
	}

	public String getLjmRepSqlString() {
		return ljmRepSqlString;
	}

	public void setLjmRepSqlString(String ljmRepSqlString) {
		this.ljmRepSqlString = ljmRepSqlString;
	}

	public String getLjmRepCrUid() {
		return ljmRepCrUid;
	}

	public void setLjmRepCrUid(String ljmRepCrUid) {
		this.ljmRepCrUid = ljmRepCrUid;
	}

	public Date getLjmRepCrDt() {
		return ljmRepCrDt;
	}

	public void setLjmRepCrDt(Date ljmRepCrDt) {
		this.ljmRepCrDt = ljmRepCrDt;
	}

	public String getLjmRepUpdUid() {
		return ljmRepUpdUid;
	}

	public void setLjmRepUpdUid(String ljmRepUpdUid) {
		this.ljmRepUpdUid = ljmRepUpdUid;
	}

	public Date getLjmRepUpdDt() {
		return ljmRepUpdDt;
	}

	public void setLjmRepUpdDt(Date ljmRepUpdDt) {
		this.ljmRepUpdDt = ljmRepUpdDt;
	}

	public String getLjmRepQueryString() {
		return ljmRepQueryString;
	}

	public void setLjmRepQueryString(String ljmRepQueryString) {
		this.ljmRepQueryString = ljmRepQueryString;
	}

	public String getLjmRepClientId() {
		return ljmRepClientId;
	}

	public void setLjmRepClientId(String ljmRepClientId) {
		this.ljmRepClientId = ljmRepClientId;
	}

	public String getLjmRepInstCode() {
		return ljmRepInstCode;
	}

	public void setLjmRepInstCode(String ljmRepInstCode) {
		this.ljmRepInstCode = ljmRepInstCode;
	}

	public String getLjmRepSecurityCode() {
		return ljmRepSecurityCode;
	}

	public void setLjmRepSecurityCode(String ljmRepSecurityCode) {
		this.ljmRepSecurityCode = ljmRepSecurityCode;
	}

	public String getLjmWhereClientId() {
		return ljmWhereClientId;
	}

	public void setLjmWhereClientId(String ljmWhereClientId) {
		this.ljmWhereClientId = ljmWhereClientId;
	}

	public String getLjmWhereInstCode() {
		return ljmWhereInstCode;
	}

	public void setLjmWhereInstCode(String ljmWhereInstCode) {
		this.ljmWhereInstCode = ljmWhereInstCode;
	}

	public String getLjmWhereSecurityCode() {
		return ljmWhereSecurityCode;
	}

	public void setLjmWhereSecurityCode(String ljmWhereSecurityCode) {
		this.ljmWhereSecurityCode = ljmWhereSecurityCode;
	}

	public String getLjmGroupClientId() {
		return ljmGroupClientId;
	}

	public void setLjmGroupClientId(String ljmGroupClientId) {
		this.ljmGroupClientId = ljmGroupClientId;
	}

	public String getLjmGroupInstCode() {
		return ljmGroupInstCode;
	}

	public void setLjmGroupInstCode(String ljmGroupInstCode) {
		this.ljmGroupInstCode = ljmGroupInstCode;
	}

	public String getLjmGroupSecurityCode() {
		return ljmGroupSecurityCode;
	}

	public void setLjmGroupSecurityCode(String ljmGroupSecurityCode) {
		this.ljmGroupSecurityCode = ljmGroupSecurityCode;
	}


    // Getters and Setters
    
    
}