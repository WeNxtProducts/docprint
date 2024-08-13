package com.wenxt.docprint.dms.service;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Map;

import com.wenxt.docprint.dms.dto.DocumentRequest;
import com.wenxt.docprint.dms.model.LjmFileAttributes;

public interface FolderService {

	String createFolder(String fileRequest) throws IOException;

	String deleteFile(String filePath) throws FileSystemException;

	String downloadMultipleFiles(List<Map<String, String>> fileRequests);

	String processLinkDocument(DocumentRequest request);

	List<LjmFileAttributes> searchFiles(String author, String docType, String fileName, String tranId);

	String uploadMultipleFilesArray(List<Map<String, Object>> fileRequests);

}
