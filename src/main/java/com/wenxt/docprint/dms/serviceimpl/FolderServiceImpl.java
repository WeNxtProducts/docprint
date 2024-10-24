package com.wenxt.docprint.dms.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.wenxt.docprint.dms.dto.DocumentRequest;
import com.wenxt.docprint.dms.model.LjmFileAttributes;
import com.wenxt.docprint.dms.repo.LjmFileAttributesRepository;
import com.wenxt.docprint.dms.repo.lmcodesRepo;
import com.wenxt.docprint.dms.service.FolderService;

@Service
public class FolderServiceImpl implements FolderService {

	@Value("${spring.message.code}")
	private String messageCode;

	@Value("${spring.status.code}")
	private String statusCode;

	@Value("${spring.data.code}")
	private String dataCode;

	@Value("${spring.success.code}")
	private String successCode;

	@Value("${spring.error.code}")
	private String errorCode;

	@Value("${spring.warning.code}")
	private String warnCode;

	@Value("${dms.file.upload}")
	private String uploadDir;

	@Value("${spring.file.path}")
	private String filepaths;

	@Value("${spring.file.overall}")
	private String overallCode;

	@Value("${file.attributes.update.status.query}")
	private String updateStatusQuery;

	@Value("${file.attributes.insert.query}")
	private String insertQuery;

	@Value("${dms.file.deeplink}")
	private String outputFilePath;

	@Autowired
	private LjmFileAttributesRepository repo;

	@Value("${spring.dms.byteArray}")
	private String byteArrayProperty;

	@Value("${spring.dms.dms_status}")
	private String dmsStatusProperty;

	@Value("${spring.dms.tran_Id}")
	private String tranIdProperty;

	@Autowired
	private lmcodesRepo lmrepo;

	@Autowired
	private LjmFileAttributesRepository filerepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String createFolder(String folderPath) throws IOException {
		Path path = Paths.get(folderPath);
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			if (Files.exists(path)) {
				throw new IOException("File system entry denoted by path already exists: " + folderPath);
			}
			Files.createDirectories(path);

			response.put(statusCode, successCode);
			response.put(messageCode, "Folder created successfully");
			data.put(filepaths, path.toString());
			response.put(dataCode, data);

		} catch (IOException e) {
			response.put(statusCode, errorCode);
			response.put(messageCode, "An error occurred: " + e.getMessage());
			e.printStackTrace();

		} catch (Exception e) {
			response.put(statusCode, errorCode);
			response.put(messageCode, "An unexpected error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		return response.toString();
	}

	@Override
	public String deleteFile(String filePath) throws FileSystemException {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			File file = new File(filePath);

			if (!file.exists() || !file.isFile()) {
				throw new FileSystemException("File not found or path does not denote a file: " + filePath);
			}

			if (!file.delete()) {
				throw new FileSystemException("Failed to delete file: " + filePath);
			}

			response.put(statusCode, successCode);
			response.put(messageCode, "File deleted successfully");
			data.put(filepaths, filePath);
			response.put(dataCode, data);

		} catch (FileSystemException e) {
			response.put(statusCode, errorCode);
			response.put(messageCode, "An error occurred: " + e.getMessage());
			e.printStackTrace();

		} catch (Exception e) {
			response.put(statusCode, errorCode);
			response.put(messageCode, "An unexpected error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		return response.toString();
	}

	public String saveFileDetails(String filename, String module, String tranId, String docType, String filePath) {
		LjmFileAttributes fileDetails = new LjmFileAttributes();
		fileDetails.setFileName(filename);
		fileDetails.setDocModule(module);
		fileDetails.setTranId(tranId);
		fileDetails.setDocType(docType);
		fileDetails.setFilePath(filePath);
		fileDetails.setStatus("Y");

		LjmFileAttributes savedFileDetails = filerepo.save(fileDetails);

		return savedFileDetails.getId().toString();
	}

	public String downloadMultipleFiles(List<Map<String, String>> fileRequests) {
		JSONArray fileResponses = new JSONArray();

		for (Map<String, String> fileRequest : fileRequests) {
			String path = fileRequest.get("path");
			JSONObject response = new JSONObject();

			try {
				byte[] fileData = getFileAsByteArray(path);
				String base64FileContent = Base64.getEncoder().encodeToString(fileData);
				response.put(filepaths, path);
				response.put("fileContent", base64FileContent);
			} catch (IOException e) {
				response.put(filepaths, path);
				response.put(statusCode, errorCode);
				response.put(messageCode, e.getMessage());
			}

			fileResponses.put(response);
		}

		JSONObject overallResponse = new JSONObject();
		overallResponse.put("files", fileResponses);

		return overallResponse.toString();
	}

	private byte[] getFileAsByteArray(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IOException("File not found: " + filePath);
		}
		try (FileInputStream fis = new FileInputStream(file)) {
			return StreamUtils.copyToByteArray(fis);
		}
	}

	@Override
	public String processLinkDocument(DocumentRequest request) {
		String docType = request.getDocType();
		String tranId = request.getTranId();

		if ("TranDoc".equalsIgnoreCase(docType)) {

			Optional<LjmFileAttributes> fileAttributesOpt = repo.findByTranId(tranId);

			if (!fileAttributesOpt.isPresent()) {
				return new JSONObject().put("statusCode", "error")
						.put("messageCode", "Template not found for TranId: " + tranId).toString();
			}

			String filePath = fileAttributesOpt.get().getFilePath();

			String baseUrl = "http://localhost:8097/files/";
			String fileUrl = baseUrl + filePath.replace("D://", "").replace("\\", "/");

//			 String fileUrl=filePath;

			JSONObject response = new JSONObject();
			response.put("statusCode", "success");
			response.put("messageCode", "Static report generated and saved successfully");

			JSONObject data = new JSONObject();

			data.put("FileUrl", fileUrl);
			response.put("data", data);

			return response.toString();
		} else {
			return new JSONObject().put("statusCode", "error").put("messageCode", "Invalid docType").toString();
		}
	}

	public Optional<String> getFileLocation(String tranId) {
		// Query the repository to find the file attributes by TranId and DocType
		Optional<LjmFileAttributes> fileAttributesOpt = repo.findByTranId(tranId);

		// Return the file path if found, otherwise return an empty Optional
		return fileAttributesOpt.map(LjmFileAttributes::getFilePath);
	}

	@Override
	public List<LjmFileAttributes> searchFiles(String author, String docType, String fileName, String tranId) {

		if (author != null && !author.isEmpty()) {
			return repo.findByAuthorContaining(author);
		} else if (docType != null && !docType.isEmpty()) {
			return repo.findByDocTypeContaining(docType);
		} else if (fileName != null && !fileName.isEmpty()) {
			return repo.findByFileNameContaining(fileName);
		} else if (tranId != null && !tranId.isEmpty()) {
			return repo.findByTranIdContaining(tranId);
		} else {
			return repo.findAll();
		}

	}

	public String uploadMultipleFilesArray(List<Map<String, Object>> fileRequests) {
		JSONArray fileResponses = new JSONArray();
		JSONObject overallResponse = new JSONObject();

		for (Map<String, Object> fileRequest : fileRequests) {
			// Convert the list of integers to byte[]
			List<Integer> byteArrayList = (List<Integer>) fileRequest.get("byteArray");
			byte[] byteArray = new byte[byteArrayList.size()];
			for (int i = 0; i < byteArrayList.size(); i++) {
				byteArray[i] = byteArrayList.get(i).byteValue();
			}

			String docModule = (String) fileRequest.get("module");
			String tranId = (String) fileRequest.get("TranId");
			String docType = (String) fileRequest.get("DocType");
			String replaceFlag = (String) fileRequest.get("replaceFlag");
			String actfilename = (String) fileRequest.get("filename");
			String genType = (String) fileRequest.get("genType");
			String dms_status = (String) fileRequest.get("dms_status");
			String screenName = (String) fileRequest.get("screenName");
			String uploadscrn = (String) fileRequest.get("uploadscrn");

			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();
			
			 LocalDate currentDate = LocalDate.now();

			String filePath = ""; 
			String fileName = currentDate + "_" + actfilename + "." + genType; 
																			

			if ("DMS".equalsIgnoreCase(screenName)) {

				Optional<String> pcDescOptional = lmrepo.findPcDescByPcTypeAndPcCode(screenName, uploadscrn);

				if (pcDescOptional.isPresent()) {
					String pcDesc = pcDescOptional.get();
					filePath = pcDesc + fileName;
				}
			}

			if ("Y".equalsIgnoreCase(replaceFlag)) {
			
				filePath = updateFileVersionArray(filePath, docModule);
			}
			
			String genid = saveFileDetails(fileName, docModule, tranId, docType, filePath);

			
			uploadFileArray(byteArray, filePath);

			data.put("filePath", filePath);
			data.put(dmsStatusProperty, "Y");
			data.put(tranIdProperty, tranId);
			data.put("DocType", docType);
			data.put("doc_sys_id", genid);
			data.put("filename", actfilename);
			response.put(statusCode, successCode);
			response.put(messageCode, "File Uploaded successfully");
			response.put(dataCode, data);

			fileResponses.put(response);
		}

		overallResponse.put(overallCode, fileResponses);
		return overallResponse.toString();
	}

	private String uploadFileArray(byte[] byteArray, String filePath) {
		File file = new File(filePath);
		File parentDir = file.getParentFile();

		if (!parentDir.exists()) {
			boolean dirsCreated = parentDir.mkdirs();
			if (!dirsCreated) {
				throw new RuntimeException("Failed to create directory: " + parentDir.getAbsolutePath());
			}
		}

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(byteArray);
		} catch (IOException e) {
			e.printStackTrace(); // Log the exception
			throw new RuntimeException("Failed to upload file: " + filePath, e);
		}
		return filePath;
	}

	private String updateFileVersionArray(String filePath, String docModule) {
		File file = new File(filePath);
		String directory = file.getParent();
		String fileName = file.getName();
		String baseName = fileName;
		String extension = "";

		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0) {
			baseName = fileName.substring(0, dotIndex);
			extension = fileName.substring(dotIndex); 
		}

		String directoryPath = directory + File.separator + docModule;
		File moduleDirectory = new File(directoryPath);
		if (!moduleDirectory.exists()) {
			moduleDirectory.mkdirs(); 
		}

		int version = 1;
		File newFile;
		do {
			String newFileName = baseName + "_v" + version + extension;
			newFile = new File(directoryPath, newFileName);
			version++;
		} while (newFile.exists());

		return newFile.getAbsolutePath();
	}

	public List<List<Integer>> getFileByteArrays(List<String> filePaths) throws IOException {
		List<List<Integer>> byteArrayList = new ArrayList<>();

		for (String filePath : filePaths) {
			File file = new File(filePath);

			if (!file.exists()) {
				throw new IOException("File not found: " + filePath);
			}

			byte[] byteArray = Files.readAllBytes(Paths.get(filePath));
			byteArrayList.add(byteArrayToList(byteArray));
		}

		return byteArrayList;
	}

	private List<Integer> byteArrayToList(byte[] byteArray) {
		List<Integer> list = new ArrayList<>();
		for (byte b : byteArray) {
			list.add((int) b);
		}
		return list;
	}

	public Map<String, Object> deleteFiles(List<String> docsysIds) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> data = new HashMap<>();

		if (docsysIds.isEmpty()) {
			response.put("status", "ERROR");
			response.put("status_msg", "No IDs provided for update");
			return response;
		}

		try {

			List<Integer> intDocsysIds = docsysIds.stream().map(Integer::parseInt).collect(Collectors.toList());

			int updatedCount = updateFilesStatus(intDocsysIds);

			if (updatedCount == intDocsysIds.size()) {
				data.put("doc_sys_id", docsysIds);
				response.put(statusCode, successCode);
				response.put(messageCode, "Given ID(s) status have been updated successfully");
				response.put(dataCode, data);
			} else {

				List<String> notUpdatedIds = new ArrayList<>(docsysIds.subList(updatedCount, docsysIds.size()));

				data.put("doc_sys_id", notUpdatedIds);
				response.put(statusCode, warnCode);
				response.put(messageCode, "Some of the given ID(s) were not found or updated");
				response.put(dataCode, data);
			}

		} catch (Exception e) {
			response.put(statusCode, errorCode);
			response.put(messageCode, "An error occurred during update: " + e.getMessage());
		}

		return response;
	}

	private int updateFilesStatus(List<Integer> docsysIds) {
		if (docsysIds.isEmpty()) {
			return 0;
		}

		try {

			String placeholders = String.join(",", Collections.nCopies(docsysIds.size(), "?"));
			String queryWithPlaceholders = String.format(updateStatusQuery, placeholders);

			return jdbcTemplate.update(queryWithPlaceholders, docsysIds.toArray());

		} catch (Exception e) {

			System.err.println("An error occurred during update: " + e.getMessage());
			throw e;
		}
	}

}
