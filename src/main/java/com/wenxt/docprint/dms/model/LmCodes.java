package com.wenxt.docprint.dms.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "LM_CODES")
public class LmCodes {

    @Id
    @Column(name = "PC_CODE", nullable = false, length = 12)
    private String PC_CODE;

    @Column(name = "PC_TYPE", nullable = false, length = 12)
    private String PC_TYPE;

    @Column(name = "PC_CLASS_CODE", length = 12)
    private String PC_CLASS_CODE;

    @Column(name = "PC_DESC", nullable = false, length = 240)
    private String PC_DESC;

    @Column(name = "PC_SHORT_DESC", nullable = false, length = 100)
    private String PC_SHORT_DESC;

    @Column(name = "PC_LONG_DESC", length = 2000)
    private String PC_LONG_DESC;

    @Column(name = "PC_BL_DESC", length = 30)
    private String PC_BL_DESC;

    @Column(name = "PC_BL_SHORT_DESC", length = 15)
    private String PC_BL_SHORT_DESC;

    @Column(name = "PC_BL_LONG_DESC", length = 2000)
    private String PC_BL_LONG_DESC;

    @Column(name = "PC_FRZ_FLAG", nullable = false, length = 1)
    private String PC_FRZ_FLAG;

    @Column(name = "PC_INS_DT")
    @Temporal(TemporalType.DATE)
    private Date PC_INS_DT;

    @Column(name = "PC_INS_ID", length = 12)
    private String PC_INS_ID;

    @Column(name = "PC_MOD_DT")
    @Temporal(TemporalType.DATE)
    private Date PC_MOD_DT;

    @Column(name = "PC_MOD_ID", length = 12)
    private String PC_MOD_ID;

    @Column(name = "PC_VALUE", precision = 17, scale = 3)
    private Double PC_VALUE;

    @Column(name = "PC_EVENT_FM_DT")
    @Temporal(TemporalType.DATE)
    private Date PC_EVENT_FM_DT;

    @Column(name = "PC_EVENT_TO_DT")
    @Temporal(TemporalType.DATE)
    private Date PC_EVENT_TO_DT;

    @Column(name = "PC_REMARKS", length = 2000)
    private String PC_REMARKS;

    @Column(name = "PC_DECL_YN", length = 1)
    private String PC_DECL_YN;

    @Column(name = "PC_RI_APPL_YN", length = 1)
    private String PC_RI_APPL_YN;

    @Column(name = "PC_MODULE_ID", length = 2)
    private String PC_MODULE_ID;

	public String getPC_CODE() {
		return PC_CODE;
	}

	public void setPC_CODE(String pC_CODE) {
		PC_CODE = pC_CODE;
	}

	public String getPC_TYPE() {
		return PC_TYPE;
	}

	public void setPC_TYPE(String pC_TYPE) {
		PC_TYPE = pC_TYPE;
	}

	public String getPC_CLASS_CODE() {
		return PC_CLASS_CODE;
	}

	public void setPC_CLASS_CODE(String pC_CLASS_CODE) {
		PC_CLASS_CODE = pC_CLASS_CODE;
	}

	public String getPC_DESC() {
		return PC_DESC;
	}

	public void setPC_DESC(String pC_DESC) {
		PC_DESC = pC_DESC;
	}

	public String getPC_SHORT_DESC() {
		return PC_SHORT_DESC;
	}

	public void setPC_SHORT_DESC(String pC_SHORT_DESC) {
		PC_SHORT_DESC = pC_SHORT_DESC;
	}

	public String getPC_LONG_DESC() {
		return PC_LONG_DESC;
	}

	public void setPC_LONG_DESC(String pC_LONG_DESC) {
		PC_LONG_DESC = pC_LONG_DESC;
	}

	public String getPC_BL_DESC() {
		return PC_BL_DESC;
	}

	public void setPC_BL_DESC(String pC_BL_DESC) {
		PC_BL_DESC = pC_BL_DESC;
	}

	public String getPC_BL_SHORT_DESC() {
		return PC_BL_SHORT_DESC;
	}

	public void setPC_BL_SHORT_DESC(String pC_BL_SHORT_DESC) {
		PC_BL_SHORT_DESC = pC_BL_SHORT_DESC;
	}

	public String getPC_BL_LONG_DESC() {
		return PC_BL_LONG_DESC;
	}

	public void setPC_BL_LONG_DESC(String pC_BL_LONG_DESC) {
		PC_BL_LONG_DESC = pC_BL_LONG_DESC;
	}

	public String getPC_FRZ_FLAG() {
		return PC_FRZ_FLAG;
	}

	public void setPC_FRZ_FLAG(String pC_FRZ_FLAG) {
		PC_FRZ_FLAG = pC_FRZ_FLAG;
	}

	public Date getPC_INS_DT() {
		return PC_INS_DT;
	}

	public void setPC_INS_DT(Date pC_INS_DT) {
		PC_INS_DT = pC_INS_DT;
	}

	public String getPC_INS_ID() {
		return PC_INS_ID;
	}

	public void setPC_INS_ID(String pC_INS_ID) {
		PC_INS_ID = pC_INS_ID;
	}

	public Date getPC_MOD_DT() {
		return PC_MOD_DT;
	}

	public void setPC_MOD_DT(Date pC_MOD_DT) {
		PC_MOD_DT = pC_MOD_DT;
	}

	public String getPC_MOD_ID() {
		return PC_MOD_ID;
	}

	public void setPC_MOD_ID(String pC_MOD_ID) {
		PC_MOD_ID = pC_MOD_ID;
	}

	public Double getPC_VALUE() {
		return PC_VALUE;
	}

	public void setPC_VALUE(Double pC_VALUE) {
		PC_VALUE = pC_VALUE;
	}

	public Date getPC_EVENT_FM_DT() {
		return PC_EVENT_FM_DT;
	}

	public void setPC_EVENT_FM_DT(Date pC_EVENT_FM_DT) {
		PC_EVENT_FM_DT = pC_EVENT_FM_DT;
	}

	public Date getPC_EVENT_TO_DT() {
		return PC_EVENT_TO_DT;
	}

	public void setPC_EVENT_TO_DT(Date pC_EVENT_TO_DT) {
		PC_EVENT_TO_DT = pC_EVENT_TO_DT;
	}

	public String getPC_REMARKS() {
		return PC_REMARKS;
	}

	public void setPC_REMARKS(String pC_REMARKS) {
		PC_REMARKS = pC_REMARKS;
	}

	public String getPC_DECL_YN() {
		return PC_DECL_YN;
	}

	public void setPC_DECL_YN(String pC_DECL_YN) {
		PC_DECL_YN = pC_DECL_YN;
	}

	public String getPC_RI_APPL_YN() {
		return PC_RI_APPL_YN;
	}

	public void setPC_RI_APPL_YN(String pC_RI_APPL_YN) {
		PC_RI_APPL_YN = pC_RI_APPL_YN;
	}

	public String getPC_MODULE_ID() {
		return PC_MODULE_ID;
	}

	public void setPC_MODULE_ID(String pC_MODULE_ID) {
		PC_MODULE_ID = pC_MODULE_ID;
	}

    
}
