package com.wenxt.docprint.dms.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "LM_codes")
public class LMCodes {

    @Id
    @Column(name = "PC_CODE")
    private String pcCode;

    @Column(name = "PC_TYPE", nullable = false)
    private String pcType;

    @Column(name = "PC_CLASS_CODE")
    private String pcClassCode;

    @Column(name = "PC_DESC", nullable = false)
    private String pcDesc;

    @Column(name = "PC_SHORT_DESC", nullable = false)
    private String pcShortDesc;

    @Column(name = "PC_LONG_DESC")
    private String pcLongDesc;

    @Column(name = "PC_BL_DESC")
    private String pcBlDesc;

    @Column(name = "PC_BL_SHORT_DESC")
    private String pcBlShortDesc;

    @Column(name = "PC_BL_LONG_DESC")
    private String pcBlLongDesc;

    @Column(name = "PC_FRZ_FLAG", nullable = false)
    private String pcFrzFlag;

    @Column(name = "PC_INS_DT")
    @Temporal(TemporalType.DATE)
    private Date pcInsDt;

    @Column(name = "PC_INS_ID")
    private String pcInsId;

    @Column(name = "PC_MOD_DT")
    @Temporal(TemporalType.DATE)
    private Date pcModDt;

    @Column(name = "PC_MOD_ID")
    private String pcModId;

    @Column(name = "PC_VALUE")
    private Double pcValue;

    @Column(name = "PC_EVENT_FM_DT")
    @Temporal(TemporalType.DATE)
    private Date pcEventFmDt;

    @Column(name = "PC_EVENT_TO_DT")
    @Temporal(TemporalType.DATE)
    private Date pcEventToDt;

    @Column(name = "PC_REMARKS")
    private String pcRemarks;

    @Column(name = "PC_DECL_YN")
    private String pcDeclYn;

    @Column(name = "PC_RI_APPL_YN")
    private String pcRiApplYn;

    @Column(name = "PC_MODULE_ID")
    private String pcModuleId;

	public String getPcCode() {
		return pcCode;
	}

	public void setPcCode(String pcCode) {
		this.pcCode = pcCode;
	}

	public String getPcType() {
		return pcType;
	}

	public void setPcType(String pcType) {
		this.pcType = pcType;
	}

	public String getPcClassCode() {
		return pcClassCode;
	}

	public void setPcClassCode(String pcClassCode) {
		this.pcClassCode = pcClassCode;
	}

	public String getPcDesc() {
		return pcDesc;
	}

	public void setPcDesc(String pcDesc) {
		this.pcDesc = pcDesc;
	}

	public String getPcShortDesc() {
		return pcShortDesc;
	}

	public void setPcShortDesc(String pcShortDesc) {
		this.pcShortDesc = pcShortDesc;
	}

	public String getPcLongDesc() {
		return pcLongDesc;
	}

	public void setPcLongDesc(String pcLongDesc) {
		this.pcLongDesc = pcLongDesc;
	}

	public String getPcBlDesc() {
		return pcBlDesc;
	}

	public void setPcBlDesc(String pcBlDesc) {
		this.pcBlDesc = pcBlDesc;
	}

	public String getPcBlShortDesc() {
		return pcBlShortDesc;
	}

	public void setPcBlShortDesc(String pcBlShortDesc) {
		this.pcBlShortDesc = pcBlShortDesc;
	}

	public String getPcBlLongDesc() {
		return pcBlLongDesc;
	}

	public void setPcBlLongDesc(String pcBlLongDesc) {
		this.pcBlLongDesc = pcBlLongDesc;
	}

	public String getPcFrzFlag() {
		return pcFrzFlag;
	}

	public void setPcFrzFlag(String pcFrzFlag) {
		this.pcFrzFlag = pcFrzFlag;
	}

	public Date getPcInsDt() {
		return pcInsDt;
	}

	public void setPcInsDt(Date pcInsDt) {
		this.pcInsDt = pcInsDt;
	}

	public String getPcInsId() {
		return pcInsId;
	}

	public void setPcInsId(String pcInsId) {
		this.pcInsId = pcInsId;
	}

	public Date getPcModDt() {
		return pcModDt;
	}

	public void setPcModDt(Date pcModDt) {
		this.pcModDt = pcModDt;
	}

	public String getPcModId() {
		return pcModId;
	}

	public void setPcModId(String pcModId) {
		this.pcModId = pcModId;
	}

	public Double getPcValue() {
		return pcValue;
	}

	public void setPcValue(Double pcValue) {
		this.pcValue = pcValue;
	}

	public Date getPcEventFmDt() {
		return pcEventFmDt;
	}

	public void setPcEventFmDt(Date pcEventFmDt) {
		this.pcEventFmDt = pcEventFmDt;
	}

	public Date getPcEventToDt() {
		return pcEventToDt;
	}

	public void setPcEventToDt(Date pcEventToDt) {
		this.pcEventToDt = pcEventToDt;
	}

	public String getPcRemarks() {
		return pcRemarks;
	}

	public void setPcRemarks(String pcRemarks) {
		this.pcRemarks = pcRemarks;
	}

	public String getPcDeclYn() {
		return pcDeclYn;
	}

	public void setPcDeclYn(String pcDeclYn) {
		this.pcDeclYn = pcDeclYn;
	}

	public String getPcRiApplYn() {
		return pcRiApplYn;
	}

	public void setPcRiApplYn(String pcRiApplYn) {
		this.pcRiApplYn = pcRiApplYn;
	}

	public String getPcModuleId() {
		return pcModuleId;
	}

	public void setPcModuleId(String pcModuleId) {
		this.pcModuleId = pcModuleId;
	}

    // Getters and Setters
    
    
}