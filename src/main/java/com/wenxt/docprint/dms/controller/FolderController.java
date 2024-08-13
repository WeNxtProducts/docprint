package com.wenxt.docprint.dms.controller;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenxt.docprint.dms.dto.DocumentRequest;
import com.wenxt.docprint.dms.dto.FileRequest;
import com.wenxt.docprint.dms.dto.FolderRequest;
import com.wenxt.docprint.dms.model.LjmFileAttributes;
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

//Search
	@GetMapping("/search")
	public ResponseEntity<List<LjmFileAttributes>> searchFiles(@RequestParam(required = false) String author,
			@RequestParam(required = false) String docType, @RequestParam(required = false) String fileName,
			@RequestParam(required = false) String tranId) {

		List<LjmFileAttributes> files = folderService.searchFiles(author, docType, fileName, tranId);
		return ResponseEntity.ok(files);
	}

	@PostMapping("/uploadMultiple")
	public ResponseEntity<String> uploadMultipleFiles(@RequestBody List<Map<String, Object>> fileRequests) {
		try {
			String response = folderService.uploadMultipleFilesArray(fileRequests);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file upload.");
		}
	}
}