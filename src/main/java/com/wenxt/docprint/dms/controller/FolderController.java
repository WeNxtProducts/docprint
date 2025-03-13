package com.wenxt.docprint.dms.controller;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.HashMap;
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

	@PostMapping("/createFolder")
	public String createFolder(@RequestBody FolderRequest folderRequest) throws IOException {
		return folderService.createFolder(folderRequest.getFolderPath());
	}

	@PostMapping("/deleteFile")
	public String deleteFile(@RequestBody FileRequest fileRequest) throws FileSystemException {

		return folderService.deleteFile(fileRequest.getFilePath());

	}

	@PostMapping("/downloadFiles")
	public String downloadFiles(@RequestBody List<Map<String, String>> fileRequests) {
		return folderService.downloadMultipleFiles(fileRequests);

	}

	@PostMapping("/process")
	public String processLinkDocument(@RequestBody DocumentRequest request) {
		return folderService.processLinkDocument(request);

	}

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

	@PostMapping("/view")
	public ResponseEntity<Map<String, Object>> viewFiles(@RequestBody List<Map<String, String>> filePaths)
			throws IOException {
		Map<String, Object> response = new HashMap<>();
		List<String> paths = filePaths.stream().map(filePathMap -> filePathMap.get("path")).toList();

		List<List<Integer>> byteArrayList = folderService.getFileByteArrays(paths);
		response.put("status", "SUCCESS");
		response.put("status_msg", "Files processed successfully");
		response.put("byteArray", byteArrayList);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/deleteFiles")
	public ResponseEntity<Map<String, Object>> deleteFiles(@RequestBody Map<String, List<String>> request) {
		List<String> docsysIds = request.get("doc_sys_id");

		Map<String, Object> result = folderService.deleteFiles(docsysIds);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/new/uploadMultiple")
	public ResponseEntity<String> uploadMultipleDocuments(@RequestBody List<Map<String, Object>> fileRequests) {
		try {
			String response = folderService.uploadMultipleDocuments(fileRequests);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file upload.");
		}
	}
	
	@PostMapping("/retrieve")
	public ResponseEntity<Map<String, Object>> retriveFiles(@RequestBody List<Map<String, String>> filePaths)
	        throws IOException {
	    Map<String, Object> response = new HashMap<>();
	    List<String> paths = filePaths.stream().map(filePathMap -> filePathMap.get("path")).toList();
	    List<String> base64List = folderService.getFileBase64Strings(paths);

	    
	    response.put("status", "SUCCESS");
	    response.put("status_msg", "Files processed successfully");
	    response.put("base64Strings", base64List);  
	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/editFiles")
	public ResponseEntity<String> editFiles(@RequestBody Map<String, Object> request) {

		Long docSysId = Long.valueOf(request.get("doc_sys_id").toString());
		String param_add1 = request.get("param_add1").toString();
		String result = folderService.editFiles(docSysId, param_add1);
		return ResponseEntity.ok(result);
	}
}