package com.wenxt.docprint.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.wenxt.docprint.model.LjmDocprintParam;
import com.wenxt.docprint.model.LjmDocprintSetup;
import com.wenxt.docprint.repo.LjmdocPrinsetup;
import com.wenxt.docprint.repo.ljmparam;
import com.wenxt.docprint.service.ReportService;

import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import fr.opensagres.xdocreport.document.IXDocReport;
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
public class ReportServiceImpl implements ReportService {

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

	@Value("${spring.data.attachment}")
	private String attachmentCode;

	@Autowired
	private LjmdocPrinsetup lrmdocrprintRepository;

	@Autowired
	private ljmparam paramRepository;

	@Autowired
	private LogsAppenderServiceImpl logservice;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String generatedocument(HttpServletRequest request) throws JRException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> inputMap = mapper.readValue(request.getInputStream(), Map.class);

			String docTemplateName = (String) inputMap.get("docTemplateName");
			String genType = (String) inputMap.get("genType");

			Map<String, Object> docParms = (Map<String, Object>) inputMap.get("docParms");

			String sqlQuery = "SELECT dps_sysid FROM ljm_docprint_setup WHERE dps_template_name = ?";
			Long dpsSysid = jdbcTemplate.queryForObject(sqlQuery, Long.class, docTemplateName);

			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String formattedDate = dateFormat.format(currentDate);

			Optional<LjmDocprintSetup> setupOptional = lrmdocrprintRepository.findById(dpsSysid);
			if (!setupOptional.isPresent()) {
				throw new RuntimeException("Setup record not found for dps_template_name: " + docTemplateName);
			}

			String reportType = setupOptional.get().getDPS_TYPE();
			LjmDocprintSetup setup = setupOptional.get();

			List<LjmDocprintParam> paramList = paramRepository.findByDppDpsSysid(dpsSysid);
			if (paramList.isEmpty()) {
				throw new RuntimeException("No parameters found for the given setup");
			}

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

			if (docParms != null) {
				dataMap.putAll(docParms);
			}

//			String basePath = "C:/Wenxt_Base_Project/docprint/src/main/resources/";

			String basePath = "D:/WeNxt Product/docprint/src/main/resources/";

			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();

			if ("XDOC".equalsIgnoreCase(reportType)) {
				// Generate XDOC report
				IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(getTemplatePath(dpsSysid),
						TemplateEngineKind.Velocity);

				// Create the context for the report
				IContext context = xdocReport.createContext();
				context.put("Head", dataMap);

				String docxOutputFileName = basePath + "xdoc/XDOC_output_" + formattedDate + ".docx";
				String pdfOutputFileName = basePath + "xdoc/XDOC_output_" + formattedDate + ".pdf";

				// Generate and save the XDOC report as DOCX
				try (FileOutputStream outputStream = new FileOutputStream(docxOutputFileName)) {
					xdocReport.process(context, outputStream);
				} catch (IOException e) {
					throw new RuntimeException("Error writing DOCX report: " + docxOutputFileName, e);
				}

				// Convert DOCX to PDF using iTextPDF
				try (InputStream docxInputStream = new FileInputStream(docxOutputFileName);
						FileOutputStream pdfOutputStream = new FileOutputStream(pdfOutputFileName)) {
					convertToPdf(docxInputStream, pdfOutputStream);
				} catch (Exception e) {
					throw new RuntimeException("Error converting DOCX to PDF: " + pdfOutputFileName, e);
				}

				// Read PDF bytes
				byte[] pdfBytes;
				try (InputStream pdfInputStream = new FileInputStream(pdfOutputFileName)) {
					pdfBytes = IOUtils.toByteArray(pdfInputStream);
				} catch (IOException e) {
					throw new RuntimeException("Error reading PDF file: " + pdfOutputFileName, e);
				}

				response.put(statusCode, successCode);
				response.put(messageCode, "Xdoc report generated successfully");
				data.put(attachmentCode, pdfBytes);
				response.put(dataCode, data);

			} else if ("Static".equalsIgnoreCase(reportType)) {
				// Generate static report
				Optional<String> fileLocation = getFileLocationByTemplateName(docTemplateName);

				if (!fileLocation.isPresent()) {
					return new JSONObject().put(statusCode, errorCode)
							.put(messageCode, "Template not found for name: " + docTemplateName).toString();
				}

				byte[] pdfBytes = readPdfFile(fileLocation.get());

				response.put(statusCode, successCode);
				response.put(messageCode, "Static report generated successfully");

				data.put(attachmentCode, pdfBytes);
				response.put(dataCode, data);
			}
//			} else if ("JASPER".equalsIgnoreCase(reportType)) {
//				// Generate Jasper report
//				String location = setup.getDPS_TEMP_LOC();
//				String pdfOutputPath = basePath + "templates/output" + formattedDate + ".pdf";
//				String xlsxOutputPath = basePath + "templates/output" + formattedDate + ".xlsx";
//
//				File jrxmlFile = new File(location);
//				if (!jrxmlFile.exists()) {
//					throw new RuntimeException("JRXML file not found at location: " + location);
//				}
//
//				JasperReport jasperReport;
//				try {
//					jasperReport = JasperCompileManager.compileReport(location);
//				} catch (JRException e) {
//					throw new RuntimeException("Failed to compile JRXML file at location: " + location, e);
//				}
//
//				Map<String, Object> parameters = new HashMap<>(dataMap);
//				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
//						Collections.singletonList(parameters));
//				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//				// Export to PDF
//				try {
//					JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
//				} catch (JRException e) {
//					throw new RuntimeException("Failed to export report to PDF", e);
//				}
//
//				// Export to XLSX
//				try {
//					JRXlsxExporter xlsxExporter = new JRXlsxExporter();
//					xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//					xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxOutputPath));
//					SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
//					xlsxReportConfiguration.setOnePagePerSheet(false);
//					xlsxReportConfiguration.setDetectCellType(true);
//					xlsxExporter.setConfiguration(xlsxReportConfiguration);
//					xlsxExporter.exportReport();
//				} 

			else if ("JASPER".equalsIgnoreCase(reportType)) {
				// Generate Jasper report
				String location = setup.getDPS_TEMP_LOC();
				String pdfOutputPath = basePath + "templates/output" + formattedDate + ".pdf";
				String xlsxOutputPath = basePath + "templates/output" + formattedDate + ".xlsx";

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

				// Set parameters, including watermark image
				Map<String, Object> parameters = new HashMap<>(dataMap);
				parameters.put("WATERMARK_IMAGE", "path/to/watermark/image.png");

				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
						Collections.singletonList(parameters));
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

				// Export to PDF
				try {
					JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
				} catch (JRException e) {
					throw new RuntimeException("Failed to export report to PDF", e);
				}

				JRXlsxExporter xlsxExporter = new JRXlsxExporter();
				xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxOutputPath));
				SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
				xlsxReportConfiguration.setOnePagePerSheet(false);
				xlsxReportConfiguration.setDetectCellType(true);

				// Read PDF and XLSX files into byte arrays
				byte[] pdfBytes;
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
					// xlsxBytes = xlsxOutputStream.toByteArray();
				}

				String username = getUsernameFromSecurityContext();

				if (".xlsx".equalsIgnoreCase(genType)) {
					response.put(statusCode, successCode);
					response.put(messageCode, "Jasper report XLSX generated successfully");
					data.put(attachmentCode, pdfBytes);
					response.put(dataCode, data);

				} else {
					response.put(statusCode, errorCode);
					response.put(messageCode, "Jasper report PDF generated successfully");
					data.put(attachmentCode, pdfBytes);
					response.put(dataCode, data);
				}

			} else {
				throw new RuntimeException("Unsupported report type: " + reportType);
			}

			logservice.logToLJMLogs1("Generated Jasper report", request, setup.getDPS_TEMPLATE_NAME());

			return response.toString();

		} catch (Exception e) {
			logservice.logToError("Generated Jasper report", request, e);
			JSONObject errorResponse = new JSONObject();
			errorResponse.put(statusCode, errorCode);
			errorResponse.put(messageCode, "Failed to generate report");
			errorResponse.put("error", e.getMessage());
			return errorResponse.toString();
		}
	}

	private InputStream getTemplatePath(Long dpsSysid) throws IOException {
		String query = "SELECT DPS_TEMP_LOC FROM LJM_docprint_setup WHERE DPS_SYSID = ?";
		Map<String, Object> result = jdbcTemplate.queryForMap(query, dpsSysid);
		String filename = (String) result.get("DPS_TEMP_LOC");
		String templatePath = filename;
		return new FileInputStream(new File(templatePath));
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

		String fontDirectory = "D:/WeNxt Product/docprint/src/main/resources";
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

	private String getUsernameFromSecurityContext() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();

		}
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

	public Optional<String> getFileLocationByTemplateName(String docTemplateName) {
		return lrmdocrprintRepository.findFileLocationByTemplateName(docTemplateName);
	}
}
