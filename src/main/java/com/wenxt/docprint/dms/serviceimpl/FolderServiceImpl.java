package com.wenxt.docprint.dms.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.wenxt.docprint.dms.dto.DocumentRequest;
import com.wenxt.docprint.dms.model.LjmFileAttributes;
import com.wenxt.docprint.dms.repo.LjmFileAttributesRepository;
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

	@Value("${dms.file.upload}")
	private String uploadDir;

	@Value("${spring.file.path}")
	private String filepaths;

	@Value("${spring.file.overall}")
	private String overallCode;

	@Value("${file.attributes.insert.query}")
	private String insertQuery;
	@Autowired
	private Environment env;

	@Value("${dms.file.deeplink}")
	private String outputFilePath;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LjmFileAttributesRepository repo;

//create folder
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

//	delete file
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

//Multiple File Upload
	public String uploadMultipleFiles(List<Map<String, String>> fileRequests) {
		JSONArray fileResponses = new JSONArray();
		JSONObject overallResponse = new JSONObject();

		for (Map<String, String> fileRequest : fileRequests) {
			String foldername = fileRequest.get("foldername");
			String filename = fileRequest.get("filename");
			String module = fileRequest.get("module");
			String tranId = fileRequest.get("TranId");
			String docType = fileRequest.get("DocType");

			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();

			try {
				String filePath = uploadFile(foldername, filename, module, tranId);
				saveFileDetails(filename, module, tranId, docType, filePath);

				response.put(statusCode, successCode);
				response.put(messageCode, "File uploaded successfully");
				data.put(filepaths, filePath);
				response.put(dataCode, data);
			} catch (IOException e) {
				response.put(statusCode, errorCode);
				response.put(messageCode, e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				response.put(statusCode, errorCode);
				response.put(messageCode, "Database error: " + e.getMessage());
				e.printStackTrace();
			}

			fileResponses.put(response);
		}

		overallResponse.put(overallCode, fileResponses);
		return overallResponse.toString();
	}

	public String uploadFile(String foldername, String filename, String module, String tranId) throws IOException {
		File directory = new File(foldername);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String fileExtension = "";
		int i = filename.lastIndexOf('.');
		if (i > 0) {
			fileExtension = filename.substring(i);
		}

		String filePath = foldername + "/" + tranId + fileExtension;
		File dest = new File(filePath);

		if (dest.exists()) {
			throw new IOException("File already exists: " + filePath);
		}

		Files.copy(Paths.get(foldername + "/" + filename), dest.toPath());
		return filePath;
	}

	private void saveFileDetails(String filename, String module, String tranId, String docType, String filePath)
			throws SQLException {

		String insertQuery = env.getProperty("file.attributes.insert.query");
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			preparedStatement.setString(1, filename);
			preparedStatement.setString(2, docType);
			preparedStatement.setString(3, tranId);
			preparedStatement.setString(4, module);
			preparedStatement.setString(5, filePath);
			preparedStatement.setString(6, "S");

			preparedStatement.executeUpdate();
		}
	}

//Multiple File Download
	public String downloadMultipleFiles(List<Map<String, String>> fileRequests) {
		JSONArray fileResponses = new JSONArray();

		for (Map<String, String> fileRequest : fileRequests) {
			String path = fileRequest.get("path");
			JSONObject response = new JSONObject();
//			JSONObject data = new JSONObject();

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

		// Wrap the responses in an overall JSON object
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
			// Query the repository to find the file attributes by TranId
			Optional<LjmFileAttributes> fileAttributesOpt = repo.findByTranId(tranId);

			if (!fileAttributesOpt.isPresent()) {
				return new JSONObject().put("statusCode", "error")
						.put("messageCode", "Template not found for TranId: " + tranId).toString();
			}

			// Get the file path from the file attributes
			String filePath = fileAttributesOpt.get().getFilePath();

			// Convert the file path to an HTTP URL
			String baseUrl = "http://localhost:8097/files/";
			String fileUrl = baseUrl + filePath.replace("D://", "").replace("\\", "/");

//			 String fileUrl=filePath;

			// Prepare the response
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

}
