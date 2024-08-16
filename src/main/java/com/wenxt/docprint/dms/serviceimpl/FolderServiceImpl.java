package com.wenxt.docprint.dms.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	@Value("${spring.dms.byteArray}")
	private String byteArrayProperty;

	@Value("${spring.dms.dms_status}")
	private String dmsStatusProperty;

	@Value("${spring.dms.tran_Id}")
	private String tranIdProperty;

	@Autowired
	private lmcodesRepo lmrepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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
			String dms_status = (String) fileRequest.get("dmsStatus");
			String screenName = (String) fileRequest.get("screenName");
			String uploadscrn = (String) fileRequest.get("uploadscrn");

			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();

			try {

				String filePath = ""; // Initialize filePath variable
				String fileName = docType + "_" + actfilename + "." + genType; // Construct file name based on docType
																				// and filename

				if ("DMS".equalsIgnoreCase(screenName)) {

					Optional<String> pcDescOptional = lmrepo.findPcDescByPcTypeAndPcCode(screenName, uploadscrn);

					if (pcDescOptional.isPresent()) {
						String pcDesc = pcDescOptional.get();
						System.out.println(pcDesc + "ppp");
						// Construct file path based on PC_DESC
						filePath = pcDesc + fileName;
					} else {
						System.out.println("No description found for the provided PC_TYPE and PC_CODE");
					}
				}

				if ("Y".equalsIgnoreCase(replaceFlag)) {
					// If replaceFlag is 'Y', update the file version
					filePath = updateFileVersionArray(filePath, docModule);
				}
				saveFileDetails(fileName, docModule, tranId, docType, filePath);

				// Upload the file
				uploadFileArray(byteArray, filePath);

				// Save file details to database

				data.put(byteArrayProperty, byteArrayList);
				data.put(dmsStatusProperty, "Y");
				data.put(tranIdProperty, tranId);

				response.put(statusCode, successCode);
				response.put(messageCode, "File Uploaded successfully");
				response.put(dataCode, data);
			} catch (SQLException e) {
				response.put(statusCode, errorCode);
				response.put(messageCode, "Database error: " + e.getMessage());
				e.printStackTrace();
			}

			fileResponses.put(response);
		}

		overallResponse.put("overallCode", fileResponses);
		return overallResponse.toString();
	}

	private void uploadFileArray(byte[] byteArray, String filePath) {
		File file = new File(filePath);
		File parentDir = file.getParentFile();

		// Check if the parent directory exists, if not, create it
		if (!parentDir.exists()) {
			boolean dirsCreated = parentDir.mkdirs(); // Create directories if they don't exist
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
	}

	private String updateFileVersionArray(String filePath, String docModule) {
		File file = new File(filePath);
		String directory = file.getParent();
		String fileName = file.getName();
		String baseName = fileName;
		String extension = "";

		// Check if the file has an extension
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0) {
			baseName = fileName.substring(0, dotIndex);
			extension = fileName.substring(dotIndex); // Include the dot in the extension
		}

		// Create directory for the module if it doesn't exist
		String directoryPath = directory + File.separator + docModule;
		File moduleDirectory = new File(directoryPath);
		if (!moduleDirectory.exists()) {
			moduleDirectory.mkdirs(); // Create the directory if it does not exist
		}

		// Find a new version number
		int version = 1;
		File newFile;
		do {
			String newFileName = baseName + "_v" + version + extension;
			newFile = new File(directoryPath, newFileName);
			version++;
		} while (newFile.exists());

		return newFile.getAbsolutePath();
	}

}
