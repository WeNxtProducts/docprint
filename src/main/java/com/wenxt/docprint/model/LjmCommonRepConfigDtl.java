package com.wenxt.docprint.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LJM_COMMON_REP_CONFIG_DTL")
public class LjmCommonRepConfigDtl  {

    @Id
    @Column(name = "SRNO")
    @SequenceGenerator(name = "LJM_COMMON_REG_CONFIG_DTLS_Seq", sequenceName = "LJM_COMMON_REP_CONFIG_DTLS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LJM_COMMON_REG_CONFIG_DTLS_Seq")
    private Long SRNO;

    @Column(name = "REP_ID_DTL")
    private String REP_ID_DTL;

    @Column(name = "PARAM_NAME")
    private String PARAM_NAME;

    @Column(name = "PARAM_TYPE")
    private String PARAM_TYPE;

    @Column(name = "PARAM_DATA_TYPE")
    private String PARAM_DATA_TYPE;

    @Column(name = "PARAM_ORDER")
    private Integer PARAM_ORDER;

    @Column(name = "PARAM_LOV_QUERY")
    private String PARAM_LOV_QUERY;

    @Column(name = "PARAM_LOV_YN")
    private Integer PARAM_LOV_YN;

    @Column(name = "PARAM_ID")
    private String PARAM_ID;

    @Column(name = "PARAM_IP_REP_COL")
    private String PARAM_IP_REP_COL;

    @Column(name = "UI_PARAM_IP_REP_COL")
    private String UI_PARAM_IP_REP_COL;

    @Column(name = "PM_REQUIRED")
    private String PM_REQUIRED;

    @Column(name = "PARM_FROMTO_REL")
    private Integer PARM_FROMTO_REL;

    @Column(name = "PARAM_DEPEN_ID")
    private String PARAM_DEPEN_ID;

    @Column(name = "PARAM_DEPEND_COL")
    private String PARAM_DEPEND_COL;

    @Column(name = "PM_ADD_EVEN")
    private Integer PM_ADD_EVEN;

    @Column(name = "PM_DISABLE")
    private String PM_DISABLE;

	public Long getSRNO() {
		return SRNO;
	}

	public void setSRNO(Long sRNO) {
		SRNO = sRNO;
	}

	public String getREP_ID_DTL() {
		return REP_ID_DTL;
	}

	public void setREP_ID_DTL(String rEP_ID_DTL) {
		REP_ID_DTL = rEP_ID_DTL;
	}

	public String getPARAM_NAME() {
		return PARAM_NAME;
	}

	public void setPARAM_NAME(String pARAM_NAME) {
		PARAM_NAME = pARAM_NAME;
	}

	public String getPARAM_TYPE() {
		return PARAM_TYPE;
	}

	public void setPARAM_TYPE(String pARAM_TYPE) {
		PARAM_TYPE = pARAM_TYPE;
	}

	public String getPARAM_DATA_TYPE() {
		return PARAM_DATA_TYPE;
	}

	public void setPARAM_DATA_TYPE(String pARAM_DATA_TYPE) {
		PARAM_DATA_TYPE = pARAM_DATA_TYPE;
	}

	public Integer getPARAM_ORDER() {
		return PARAM_ORDER;
	}

	public void setPARAM_ORDER(Integer pARAM_ORDER) {
		PARAM_ORDER = pARAM_ORDER;
	}

	public String getPARAM_LOV_QUERY() {
		return PARAM_LOV_QUERY;
	}

	public void setPARAM_LOV_QUERY(String pARAM_LOV_QUERY) {
		PARAM_LOV_QUERY = pARAM_LOV_QUERY;
	}

	public Integer getPARAM_LOV_YN() {
		return PARAM_LOV_YN;
	}

	public void setPARAM_LOV_YN(Integer pARAM_LOV_YN) {
		PARAM_LOV_YN = pARAM_LOV_YN;
	}

	public String getPARAM_ID() {
		return PARAM_ID;
	}

	public void setPARAM_ID(String pARAM_ID) {
		PARAM_ID = pARAM_ID;
	}

	public String getPARAM_IP_REP_COL() {
		return PARAM_IP_REP_COL;
	}

	public void setPARAM_IP_REP_COL(String pARAM_IP_REP_COL) {
		PARAM_IP_REP_COL = pARAM_IP_REP_COL;
	}

	public String getUI_PARAM_IP_REP_COL() {
		return UI_PARAM_IP_REP_COL;
	}

	public void setUI_PARAM_IP_REP_COL(String uI_PARAM_IP_REP_COL) {
		UI_PARAM_IP_REP_COL = uI_PARAM_IP_REP_COL;
	}

	public String getPM_REQUIRED() {
		return PM_REQUIRED;
	}

	public void setPM_REQUIRED(String pM_REQUIRED) {
		PM_REQUIRED = pM_REQUIRED;
	}

	public Integer getPARM_FROMTO_REL() {
		return PARM_FROMTO_REL;
	}

	public void setPARM_FROMTO_REL(Integer pARM_FROMTO_REL) {
		PARM_FROMTO_REL = pARM_FROMTO_REL;
	}

	public String getPARAM_DEPEN_ID() {
		return PARAM_DEPEN_ID;
	}

	public void setPARAM_DEPEN_ID(String pARAM_DEPEN_ID) {
		PARAM_DEPEN_ID = pARAM_DEPEN_ID;
	}

	public String getPARAM_DEPEND_COL() {
		return PARAM_DEPEND_COL;
	}

	public void setPARAM_DEPEND_COL(String pARAM_DEPEND_COL) {
		PARAM_DEPEND_COL = pARAM_DEPEND_COL;
	}

	public Integer getPM_ADD_EVEN() {
		return PM_ADD_EVEN;
	}

	public void setPM_ADD_EVEN(Integer pM_ADD_EVEN) {
		PM_ADD_EVEN = pM_ADD_EVEN;
	}

	public String getPM_DISABLE() {
		return PM_DISABLE;
	}

	public void setPM_DISABLE(String pM_DISABLE) {
		PM_DISABLE = pM_DISABLE;
	}
    
    
    
}
