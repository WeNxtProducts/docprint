package com.wenxt.docprint.dms.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ljm_File_Attributes")
public class LjmFileAttributes {

	@Id
  	@SequenceGenerator(name = "fileSysIdSeq", sequenceName = "FILE_SYS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fileSysIdSeq")
	@Column(name = "LJM_SYS_ID", nullable = false)
	private Long id;

	@Column(name = "LJM_FILE_NAME", nullable = false, length = 255)
	private String fileName;

	@Column(name = "LJM_AUTHOR", length = 255)
	private String author;

	@Column(name = "LJM_DOCTYPE", length = 50)
	private String docType;

	@Column(name = "LJM_TRANID", length = 50)
	private String tranId;

	@Column(name = "LJM_DOC_MODULE", length = 100)
	private String docModule;

	@Column(name = "LJM_CREATE_DT")
	private Timestamp createDt;

	@Column(name = "LJM_MOD_DT")
	private Timestamp modDt;

	@Column(name = "LJMA_ADDL1", length = 255)
	private String addl1;

	@Column(name = "LJM_ADDL2", length = 255)
	private String addl2;

	@Column(name = "LJM_ADDL3", length = 255)
	private String addl3;

	@Column(name = "LJM_ADDL4", length = 255)
	private String addl4;

	@Column(name = "LJM_ADDL5", length = 255)
	private String addl5;

	@Column(name = "LJM_STATUS", length = 50)
	private String status;

	@Lob
	@Column(name = "LJM_DEEPLINK_URL")
	private String deepLinkUrl;

	@Column(name = "LJM_FILE_PATH", length = 255)
	private String filePath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getDocModule() {
		return docModule;
	}

	public void setDocModule(String docModule) {
		this.docModule = docModule;
	}

	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public Timestamp getModDt() {
		return modDt;
	}

	public void setModDt(Timestamp modDt) {
		this.modDt = modDt;
	}

	public String getAddl1() {
		return addl1;
	}

	public void setAddl1(String addl1) {
		this.addl1 = addl1;
	}

	public String getAddl2() {
		return addl2;
	}

	public void setAddl2(String addl2) {
		this.addl2 = addl2;
	}

	public String getAddl3() {
		return addl3;
	}

	public void setAddl3(String addl3) {
		this.addl3 = addl3;
	}

	public String getAddl4() {
		return addl4;
	}

	public void setAddl4(String addl4) {
		this.addl4 = addl4;
	}

	public String getAddl5() {
		return addl5;
	}

	public void setAddl5(String addl5) {
		this.addl5 = addl5;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeepLinkUrl() {
		return deepLinkUrl;
	}

	public void setDeepLinkUrl(String deepLinkUrl) {
		this.deepLinkUrl = deepLinkUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
