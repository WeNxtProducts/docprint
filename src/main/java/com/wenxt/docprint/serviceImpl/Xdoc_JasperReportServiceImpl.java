package com.wenxt.docprint.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.crypto.Data;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.wenxt.docprint.dto.TemplateDataDTO;
import com.wenxt.docprint.model.LjmDocprintParam;
import com.wenxt.docprint.model.LjmDocprintSetup;
import com.wenxt.docprint.repo.LjmdocPrinsetup;
import com.wenxt.docprint.repo.ljmparam;
import com.wenxt.docprint.service.Xdoc_JasperReportService;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
public class Xdoc_JasperReportServiceImpl implements Xdoc_JasperReportService {

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
	@Autowired
	private LjmdocPrinsetup lrmdocrprintRepository;

	@Autowired
	private ljmparam paramRepository;

	@Autowired
	private LogsAppenderServiceImpl logservice;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String getTemplateData(String tempName) {
		List<TemplateDataDTO> templates = lrmdocrprintRepository.findByDspTemplatename(tempName);

		// Create a JSON response
		JSONObject response = new JSONObject();
		response.put("Status", "SUCCESS");
		response.put("Message", "Templates retrieved successfully");
		response.put("DocList", templates);

		return response.toString();
	}

	public String generateJasperReport(HttpServletRequest request) throws JRException, IOException {
		try {
			// Parse JSON input
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> inputMap = mapper.readValue(request.getInputStream(), Map.class);

			String docTemplateName = (String) inputMap.get("docTemplateName");
			String genType = (String) inputMap.get("genType");
			Map<String, Object> docParms = (Map<String, Object>) inputMap.get("docParms");

			// Execute SQL query to fetch dps_sysid based on dps_template_name
			String sqlQuery = "SELECT dps_sysid FROM ljm_docprint_setup WHERE dps_template_name = ?";
			Long dpsSysid = jdbcTemplate.queryForObject(sqlQuery, Long.class, docTemplateName);

			// Retrieve the setup record from the database
			Optional<LjmDocprintSetup> setupOptional = lrmdocrprintRepository.findById(dpsSysid);
			if (!setupOptional.isPresent()) {
				throw new RuntimeException("Setup record not found");
			}

			LjmDocprintSetup setup = setupOptional.get();

			List<LjmDocprintParam> paramList = paramRepository.findByDppDpsSysid(dpsSysid);
			if (paramList.isEmpty()) {
				throw new RuntimeException("No parameters found for the given setup");
			}

			Date currentdate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String formattedDate = dateFormat.format(currentdate);

			String location = setup.getDPS_TEMP_LOC();
//			String basePath = "D:/Kamali/DocPrint/DOC_PRINT/DOC_PRINT/src/main/resources/templates/";

			String basePath = "D:/WeNxt Product/docprint/src/main/resources/templates/";

			String pdfOutputPath = basePath + "ouput" + formattedDate + ".pdf"; // Adjust as per your setup
			String xlsxOutputPath = basePath + "ouput" + formattedDate + ".xlsx"; // Adjust as per your setup

			File jrxmlFile = new File(location);
			if (!jrxmlFile.exists()) {
				throw new RuntimeException("JRXML file not found at location: " + location);
			}

			JasperReport jasperReport;
			try {
				jasperReport = JasperCompileManager.compileReport(location);
			} catch (JRException e) {
				throw new RuntimeException("Failed to compile JRXML file at location: " + location, e);
			}

			// Set parameters
			Map<String, Object> dataMap = new HashMap<>();
			for (LjmDocprintParam param : paramList) {
				String paramType = param.getDPP_TYPE();
				String paramName = param.getDPP_PARAM_NAME();
				String paramValue = param.getDPP_VALUE();

				if ("S".equalsIgnoreCase(paramType.trim())) {
					dataMap.put(paramName, paramValue);
				} else if ("Q".equalsIgnoreCase(paramType.trim())) {
					List<Map<String, Object>> queryResult = jdbcTemplate.query(paramValue, new Object[] { dpsSysid },
							new ColumnMapRowMapper());
					if (!queryResult.isEmpty()) {
						Map<String, Object> queryData = queryResult.get(0);
						dataMap.putAll(queryData);
					}
				}
			}

			// Add docParms directly to dataMap
			if (docParms != null) {
				dataMap.putAll(docParms);
			}

			Map<String, Object> parameters = new HashMap<>(dataMap);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
					Collections.singletonList(parameters));

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			// Export to PDF
			try {
				JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
			} catch (JRException e) {
				throw new RuntimeException("Failed to export report to PDF", e);
			}

			// Export to XLSX
			try {
				JRXlsxExporter xlsxExporter = new JRXlsxExporter();
				xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxOutputPath));
				SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
				xlsxReportConfiguration.setOnePagePerSheet(false);
				xlsxReportConfiguration.setDetectCellType(true);
				xlsxExporter.setConfiguration(xlsxReportConfiguration);
				xlsxExporter.exportReport();
			} catch (JRException e) {
				throw new RuntimeException("Failed to export report to XLSX", e);
			}

			// Read PDF and XLSX files into byte arrays
			byte[] pdfBytes;
			byte[] xlsxBytes;
			try (FileInputStream pdfInputStream = new FileInputStream(pdfOutputPath);
					FileInputStream xlsxInputStream = new FileInputStream(xlsxOutputPath);
					ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
					ByteArrayOutputStream xlsxOutputStream = new ByteArrayOutputStream()) {

				// Read PDF bytes
				int pdfByte;
				while ((pdfByte = pdfInputStream.read()) != -1) {
					pdfOutputStream.write(pdfByte);
				}
				pdfBytes = pdfOutputStream.toByteArray();

				// Read XLSX bytes
				int xlsxByte;
				while ((xlsxByte = xlsxInputStream.read()) != -1) {
					xlsxOutputStream.write(xlsxByte);
				}
				xlsxBytes = xlsxOutputStream.toByteArray();
			}

			// Log the report generation
			String username = getUsernameFromSecurityContext();
			logservice.logToLJMLogs1("Generated Jasper report", request, setup.getDPS_TEMPLATE_NAME());

			// Build JSON response
			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();

			if (".pdf".equalsIgnoreCase(genType)) {
				response.put(statusCode, successCode);
				response.put(messageCode, "Jasper report pdf generated successfully");
				data.put("Id", setup.getDPS_SYSID());
				data.put("attachment", pdfBytes); // Include PDF byte array in response
				data.put("filePathLocation", pdfOutputPath); // Include PDF file path in response
			} else {
				response.put(statusCode, successCode);
				response.put(messageCode, "Jasper report xls generated successfully");
				data.put("attachment", xlsxBytes); // Include XLSX byte array in response
				data.put("filePathLocation1", xlsxOutputPath); // Include XLSX file path in response
			}

			response.put("data", data);
			return response.toString();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate Jasper report", e);
		}
	}

	private InputStream getTemplatePath(Long dpsSysid) throws IOException {
		String query = "SELECT DPS_TEMP_LOC FROM LJM_docprint_setup WHERE DPS_SYSID = ?";
		Map<String, Object> result = jdbcTemplate.queryForMap(query, dpsSysid);
		String filename = (String) result.get("DPS_TEMP_LOC");
		String templatePath = filename;
		return new FileInputStream(new File(templatePath));
	}

	private String getUsernameFromSecurityContext() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
			
		}
	}

	@Override
	public Optional<String> getFileLocationByTemplateName(String templateName) {
		return lrmdocrprintRepository.findFileLocationByTemplateName(templateName);
	}

	@Override
	public String generateXdocReport(HttpServletRequest request) throws JRException, IOException {
		try {
			// Parse JSON input
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> inputMap = mapper.readValue(request.getInputStream(), Map.class);

			String docTemplateName = (String) inputMap.get("docTemplateName");
			String genType = (String) inputMap.get("genType");
			Map<String, Object> docParms = (Map<String, Object>) inputMap.get("docParms");

			// Execute SQL query to fetch dps_sysid based on dps_template_name
			String sqlQuery = "SELECT dps_sysid FROM ljm_docprint_setup WHERE dps_template_name = ?";
			Long dpsSysid = jdbcTemplate.queryForObject(sqlQuery, Long.class, docTemplateName);

			Date currentdate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String formattedDate = dateFormat.format(currentdate);
			// Retrieve the setup record from the database
			Optional<LjmDocprintSetup> setupOptional = lrmdocrprintRepository.findById(dpsSysid);
			if (!setupOptional.isPresent()) {
				throw new RuntimeException("Setup record not found for dps_template_name: " + docTemplateName);
			}

			LjmDocprintSetup setup = setupOptional.get();

			// Retrieve the param records from the database
			List<LjmDocprintParam> paramList = paramRepository.findByDppDpsSysid(dpsSysid);
			if (paramList.isEmpty()) {
				throw new RuntimeException("No parameters found for the given setup");
			}

			// Prepare data map for XDOC report
			Map<String, Object> dataMap = new HashMap<>();
			for (LjmDocprintParam param : paramList) {
				String paramType = param.getDPP_TYPE();
				String paramName = param.getDPP_PARAM_NAME();
				String paramValue = param.getDPP_VALUE();

				if ("S".equalsIgnoreCase(paramType.trim())) {
					dataMap.put(paramName, paramValue);
				} else if ("Q".equalsIgnoreCase(paramType.trim())) {
					List<Map<String, Object>> queryResult = jdbcTemplate.query(paramValue, new Object[] { dpsSysid },
							new ColumnMapRowMapper());
					if (!queryResult.isEmpty()) {
						Map<String, Object> queryData = queryResult.get(0);
						dataMap.putAll(queryData);
					}
				}
			}

			System.out.println("KKKKKKKKKKK");

			// Add docParms directly to dataMap
			if (docParms != null) {
				System.out.println("RRRRRRR");
				dataMap.putAll(docParms);
			}

			// Load the XDOC template
			IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(getTemplatePath(dpsSysid),
					TemplateEngineKind.Velocity);

			// Create the context for the report
			IContext context = xdocReport.createContext();
			context.put("Head", dataMap);

			// Define the output file paths
//			String basePath = "D:/Kamali/DocPrint/DOC_PRINT/DOC_PRINT/src/main/resources/xdoc/";

			String basePath = "D:/WeNxt Product/docprint/src/main/resources/xdoc/";
			String docxOutputFileName = basePath + "XDOC_output_" + formattedDate + ".docx";
			String pdfOutputFileName = basePath + "XDOC_output_" + formattedDate + ".pdf";

			// Generate and save the XDOC report as DOCX
			try (FileOutputStream outputStream = new FileOutputStream(docxOutputFileName)) {
				xdocReport.process(context, outputStream);
			} catch (IOException e) {
				throw new IOException("Error writing DOCX report: " + docxOutputFileName, e);
			}

			// Convert DOCX to PDF using iTextPDF
			try (InputStream docxInputStream = new FileInputStream(docxOutputFileName);
					FileOutputStream pdfOutputStream = new FileOutputStream(pdfOutputFileName)) {
				convertToPdf(docxInputStream, pdfOutputStream);
			} catch (Exception e) {
				throw new IOException("Error converting DOCX to PDF: " + pdfOutputFileName, e);
			}

			// Read PDF bytes
			byte[] pdfBytes;
			try (InputStream pdfInputStream = new FileInputStream(pdfOutputFileName)) {
				pdfBytes = IOUtils.toByteArray(pdfInputStream);
			} catch (IOException e) {
				throw new IOException("Error reading PDF file: " + pdfOutputFileName, e);
			}

			// Build JSON response
			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();

			response.put("statusCode", "successCode");
			response.put("message", "XDOC report generated successfully");
			data.put("Id", setup.getDPS_SYSID());
			data.put("pdfPath", pdfOutputFileName);

			response.put("data", data);
			response.put("pdfBytes", pdfBytes);

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception properly
			throw new RuntimeException("Failed to generate XDOC report: " + e.getMessage(), e);
		}
	}

	public void convertToPdf(InputStream docxInputStream, OutputStream pdfOutputStream) throws Exception {
		// Load DOCX into XWPFDocument
		XWPFDocument document = new XWPFDocument(docxInputStream);

		// Create PDF Document
		Document pdfDocument = new Document();
		PdfWriter.getInstance(pdfDocument, pdfOutputStream);

		// Open the Document
		pdfDocument.open();

		// Register font directory dynamically
//		String fontDirectory = "C:/Wenxt_Base_Project/docprint/src/main/resources";

		String fontDirectory = "D:\\WeNxt Product\\docprint\\src\\main\\resources";
		BaseFont bf = BaseFont.createFont(fontDirectory + "/NotoSansEthiopic.ttf", BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf, 12);

		// Read content from DOCX and add to PDF
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (XWPFParagraph para : paragraphs) {
			String text = para.getText();
			pdfDocument.add(new Paragraph(text, font));
		}

		// Close the Document
		pdfDocument.close();
	}

	public byte[] readPdfFile(String fileLocation) throws IOException {
		File pdfFile = new File(fileLocation);
		if (!pdfFile.exists() || !pdfFile.isFile()) {
			throw new IOException("File not found at location: " + fileLocation);
		}
		try (FileInputStream fileInputStream = new FileInputStream(pdfFile)) {
			return IOUtils.toByteArray(fileInputStream);
		}
	}

	
}
