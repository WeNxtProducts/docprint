package com.wenxt.docprint.dms.controller;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dms.dto.DocumentRequest;
import com.wenxt.docprint.dms.dto.FileRequest;
import com.wenxt.docprint.dms.dto.FolderRequest;
import com.wenxt.docprint.dms.service.FolderService;

@RestController
@RequestMapping("/dms")
public class FolderController {
	@Autowired
	private FolderService folderService;

//create Folder
	@PostMapping("/createFolder")
	public String createFolder(@RequestBody FolderRequest folderRequest) throws IOException {
		return folderService.createFolder(folderRequest.getFolderPath());
	}

//Delete Folder
	@PostMapping("/deleteFile")
	public String deleteFile(@RequestBody FileRequest fileRequest) throws FileSystemException {

		return folderService.deleteFile(fileRequest.getFilePath());

	}

//	Multiple upload
	@PostMapping("/uploadFiles")
	public String uploadFiles(@RequestBody List<Map<String, String>> fileRequests) throws SQLException {

		return folderService.uploadMultipleFiles(fileRequests);
	}

//	multiple Download
	@PostMapping("/downloadFiles")
	public String downloadFiles(@RequestBody List<Map<String, String>> fileRequests) {
		return folderService.downloadMultipleFiles(fileRequests);

	}

//	DeepLink

	@PostMapping("/process")
	public String processLinkDocument(@RequestBody DocumentRequest request) {
		return folderService.processLinkDocument(request);

	}

}
