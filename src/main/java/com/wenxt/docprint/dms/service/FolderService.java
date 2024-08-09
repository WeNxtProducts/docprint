package com.wenxt.docprint.dms.service;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.wenxt.docprint.dms.dto.DocumentRequest;

public interface FolderService {

	String createFolder(String fileRequest) throws IOException;

	String deleteFile(String filePath) throws FileSystemException;

	String uploadMultipleFiles(List<Map<String, String>> fileRequests) throws SQLException;

	String downloadMultipleFiles(List<Map<String, String>> fileRequests);

	String processLinkDocument(DocumentRequest request);

}
