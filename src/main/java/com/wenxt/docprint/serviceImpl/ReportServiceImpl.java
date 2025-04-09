package com.wenxt.docprint.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.wenxt.docprint.model.LjmDocprintParam;
import com.wenxt.docprint.model.LjmDocprintSetup;
import com.wenxt.docprint.repo.LjmdocPrinsetup;
import com.wenxt.docprint.repo.ljmparam;
import com.wenxt.docprint.service.ReportService;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
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

	@Value("${XdocTempBasePath}")
	private String XdocTempBasePath;

	@Value("${spring.aicbase.url}")
	private String aicbase;

	@Value("${query.selectDocPrintSetup}")
	private String selectDocPrintSetupQuery;

	@Autowired
	private LjmdocPrinsetup lrmdocrprintRepository;

	@Autowired
	private ljmparam paramRepository;

	@Autowired
	private LogsAppenderServiceImpl logservice;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Value("${Staticpath}")
	private String Staticpath;

	@Value("${XdocBasePath}")
	private String XdocBasePath;

	@Value("${Jasperpath}")
	private String Jasperpath;

	@Value("${ImgDocBasePath}")
	private String ImgDocBasePath;

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Override
	public String generatedocument(HttpServletRequest request) throws JRException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> inputMap = mapper.readValue(request.getInputStream(), Map.class);

			String docTemplateName = (String) inputMap.get("docTemplateName");
			String genType = (String) inputMap.get("genType");

			Map<String, Object> docParms = (Map<String, Object>) inputMap.get("docParms");
			Map<String, Object> images = (Map<String, Object>) inputMap.get("images");
			String tranId = (String) inputMap.get("tranId");

			String sysID = (String) inputMap.get("sysId");

			Long dpsSysid = jdbcTemplate.queryForObject(selectDocPrintSetupQuery, Long.class, docTemplateName);

			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
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
//				String paramValue = param.getDPP_VALUE();

				if ("S".equalsIgnoreCase(paramType.trim())) {
					dataMap.put(paramName, param.getDPP_VALUE());
				}

				if ("Q".equalsIgnoreCase(paramType.trim())) {
					MapSqlParameterSource parameters = new MapSqlParameterSource();
					parameters.addValue("tranId", tranId);
					parameters.addValue("sysId", sysID);
					List<Map<String, Object>> queryResult = namedParameterJdbcTemplate.query(param.getDPP_VALUE(),
							parameters, new ColumnMapRowMapper());

					// Check if the result is not empty and put the data into the dataMap
					if (!queryResult.isEmpty()) {
						Map<String, Object> queryData = queryResult.get(0);
						dataMap.putAll(queryData);
					}
				}

				if ("P".equalsIgnoreCase(paramType.trim())) {
					if (inputMap.get(paramName) != null) {
						dataMap.put(paramName, inputMap.get(paramName));
					}
				}

			}

			if (docParms != null) {
				dataMap.putAll(docParms);
			}

			JSONObject response = new JSONObject();
			JSONObject data = new JSONObject();

			if ("XDOC".equalsIgnoreCase(reportType)) {
				IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(getTemplatePath(dpsSysid),
						TemplateEngineKind.Velocity);

				IContext context = xdocReport.createContext();
				FieldsMetadata metadata = xdocReport.createFieldsMetadata();
				JSONObject inJson = new JSONObject(inputMap);

				if (inJson.has("images")) {
					Iterator<String> keys = inJson.getJSONObject("images").keys();
					while (keys.hasNext()) {
						String key = keys.next();
						File file = new File(ImgDocBasePath + inJson.getJSONObject("images").getString(key));
						if (!file.exists()) {
							System.out.println("Image file not found: " + file.getAbsolutePath());
						}
						metadata.addFieldAsImage(key);
					}
				}

				if (inJson.has("qrcode")) {
					String authorizationHeader = request.getHeader("Authorization");
					String token = authorizationHeader.substring(7).trim();
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.set("Authorization", "Bearer " + token);
					RestTemplate restTemplate = new RestTemplate();

					try {
						JSONObject qrData = inJson.getJSONObject("qrcode").getJSONObject("data");
						String topText = inJson.getJSONObject("qrcode").getString("topText");
						String bottomText = inJson.getJSONObject("qrcode").getString("bottomText");

						JSONObject qrRequest = new JSONObject();
						qrRequest.put("data", qrData);
						qrRequest.put("topText", topText);
						qrRequest.put("bottomText", bottomText);

						HttpEntity<String> requestEntity = new HttpEntity<>(qrRequest.toString(), headers);
						String qrServiceUrl = aicbase;

						ResponseEntity<String> responseEntity = restTemplate.postForEntity(qrServiceUrl, requestEntity,
								String.class);

						JSONObject responseJson = new JSONObject(responseEntity.getBody());
						String qrCodeLocation = responseJson.getJSONObject("Data").getString("Location");

						metadata.addFieldAsImage("qrcode");
						context.put("qrcode", new FileImageProvider(new File(qrCodeLocation)));

					} catch (Exception e) {
						return e.getMessage();
					}
				}

				context = setimages(inJson, context);

				for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
					if (entry.getValue() == null) {
						entry.setValue("");
					}
				}

				context.put("Head", dataMap);

				String docxOutputFileName = XdocBasePath + docTemplateName + formattedDate + ".docx";
				String pdfOutputFileName = XdocBasePath + docTemplateName + formattedDate + ".pdf";
				String tempOutputFileName = XdocTempBasePath + docTemplateName + formattedDate + ".pdf";

				try (FileOutputStream outputStream = new FileOutputStream(docxOutputFileName)) {
					xdocReport.process(context, outputStream);
				} catch (IOException e) {
					throw new RuntimeException("Error writing DOCX report: " + docxOutputFileName, e);
				}

				try (InputStream docxInputStream = new FileInputStream(docxOutputFileName);
						FileOutputStream pdfOutputStream = new FileOutputStream(tempOutputFileName)) {
					convertToPdf(docxInputStream, pdfOutputStream);
					String waterMarkLocation = ImgDocBasePath;
					if (inJson.has("watermark")) {
						createwatermarkonpdf(tempOutputFileName, pdfOutputFileName,
								waterMarkLocation + inJson.getString("watermark"));
					} else {
						pdfOutputFileName = tempOutputFileName;
					}
				} catch (Exception e) {
					throw new RuntimeException("Error converting DOCX to PDF: " + pdfOutputFileName, e);
				}
				String pdfBase64 = null;
				try (InputStream pdfInputStream = new FileInputStream(pdfOutputFileName);
						ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {
					IOUtils.copy(pdfInputStream, pdfOutputStream);
					byte[] pdfBytes = pdfOutputStream.toByteArray();
					pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes); // Convert byte array to Base64
				} catch (IOException e) {
					throw new RuntimeException("Error reading PDF file: " + pdfOutputFileName, e);
				}
				response.put(statusCode, successCode);
				response.put(messageCode, "Xdoc report generated successfully");
				data.put(attachmentCode, pdfBase64); 
				response.put(dataCode, data);

				return response.toString();
			}

			else if ("Static".equalsIgnoreCase(reportType)) {
				Optional<String> fileLocation = getFileLocationByTemplateName(docTemplateName);

				if (!fileLocation.isPresent()) {
					return new JSONObject().put(statusCode, errorCode)
							.put(messageCode, "Template not found for name: " + docTemplateName).toString();
				}
				byte[] pdfBytes = readPdfFile(fileLocation.get());
				try {
					String outputFilePath = Staticpath + docTemplateName + ".pdf";
					Files.write(Paths.get(outputFilePath), pdfBytes);
					String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);
					response.put(statusCode, successCode);
					response.put(messageCode, "Static report generated and saved successfully");
					data.put(attachmentCode, pdfBase64);
					response.put(dataCode, data);

				} catch (IOException e) {
					return new JSONObject().put(statusCode, errorCode)
							.put(messageCode, "Failed to save the report: " + e.getMessage()).toString();
				}
			}

			else if ("JASPER".equalsIgnoreCase(reportType)) {
				String location = setup.getDPS_TEMP_LOC();
				String pdfOutputPath = Jasperpath + docTemplateName + formattedDate + ".pdf";
				String xlsxOutputPath = Jasperpath + docTemplateName + formattedDate + ".xlsx";

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
				Map<String, Object> parameters = new HashMap<>(dataMap);
				parameters.put("WATERMARK_IMAGE", "path/to/watermark/image.png");

				Map<String, Object> parammMap = new HashMap<>();

				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
						Collections.singletonList(parameters));
				Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				try {
					JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
				} catch (JRException e) {
					throw new RuntimeException("Failed to export report to PDF", e);
				}

				// Export to XLSX
				JRXlsxExporter xlsxExporter = new JRXlsxExporter();
				xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxOutputPath));
				SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
				xlsxReportConfiguration.setOnePagePerSheet(false);
				xlsxReportConfiguration.setDetectCellType(true);
				xlsxExporter.setConfiguration(xlsxReportConfiguration);
				try {
					xlsxExporter.exportReport();
				} catch (JRException e) {
					throw new RuntimeException("Failed to export report to XLSX", e);
				}

				// Convert PDF to Base64
				String pdfBase64 = null;
				try (FileInputStream pdfInputStream = new FileInputStream(pdfOutputPath);
						ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {

					int pdfByte;
					while ((pdfByte = pdfInputStream.read()) != -1) {
						pdfOutputStream.write(pdfByte);
					}
					byte[] pdfBytes = pdfOutputStream.toByteArray();
					pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);
				} catch (IOException e) {
					throw new RuntimeException("Failed to convert PDF file to Base64", e);
				}

				// Convert XLSX to Base64
				String xlsxBase64 = null;
				try (FileInputStream xlsxInputStream = new FileInputStream(xlsxOutputPath);
						ByteArrayOutputStream xlsxOutputStream = new ByteArrayOutputStream()) {

					int xlsxByte;
					while ((xlsxByte = xlsxInputStream.read()) != -1) {
						xlsxOutputStream.write(xlsxByte);
					}
					byte[] xlsxBytes = xlsxOutputStream.toByteArray();
					xlsxBase64 = Base64.getEncoder().encodeToString(xlsxBytes);
				} catch (IOException e) {
					throw new RuntimeException("Failed to convert XLSX file to Base64", e);
				}

				String username = getUsernameFromSecurityContext();

				if (".pdf".equalsIgnoreCase(genType)) {
					response.put(statusCode, successCode);
					response.put(messageCode, "Jasper report PDF generated successfully");
					data.put(attachmentCode, pdfBase64); // Use Base64-encoded PDF string
					response.put(dataCode, data);
				} else {
					response.put(statusCode, successCode); // Changed errorCode to successCode
					response.put(messageCode, "Jasper report XLSX generated successfully");
					data.put(attachmentCode, xlsxBase64); // Use Base64-encoded XLSX string
					response.put(dataCode, data);
				}
			}

			else {
				throw new RuntimeException("Unsupported report type: " + reportType);
			}

			logservice.logToLJMLogs1("Generated report", request, setup.getDPS_TEMPLATE_NAME());

			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
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
		final XWPFDocument document = new XWPFDocument(docxInputStream);
		final PdfOptions options = PdfOptions.create();
		PdfConverter.getInstance().convert(document, pdfOutputStream, options);
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

	public IContext setimages(JSONObject ijson, IContext context) {
		// Process regular images
		if (ijson.has("images")) {
			JSONObject imagejson = ijson.getJSONObject("images");
			Iterator<String> keys = imagejson.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				String imagePath = ImgDocBasePath + imagejson.getString(key);
				File file = new File(imagePath);
				if (file.exists()) {
					IImageProvider imageProvider = new FileImageProvider(file);
					context.put(key, imageProvider);
				} else {
					System.out.println("Image file not found: " + imagePath);
				}
			}
		}

		// Process QR code images
		if (ijson.has("qrcode")) {
			String qrImagePath = context.get("qrcode").toString();
			File qrFile = new File(qrImagePath);
			if (qrFile.exists()) {
				IImageProvider qrProvider = new FileImageProvider(qrFile);
				context.put("qrcode", qrProvider);
			} else {
				System.out.println("QR code image file not found: " + qrImagePath);
			}
		}

		return context;
	}

	public static void createwatermarkonpdf(final String tempoutfile, final String outfile,
			final String watermarklocation) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(tempoutfile);
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outfile));

		if (null != watermarklocation && !watermarklocation.equals("")) {
			Image img = Image.getInstance(watermarklocation);
			img.setAbsolutePosition(25, 50);
			int n = reader.getNumberOfPages();
			int i = 0;
			PdfContentByte under;
			while (i < n) {
				i++;
				under = stamp.getUnderContent(i);
				under.addImage(img);
			}
		}
		stamp.close();
		File file = new File(tempoutfile);
		file.delete();
	}
}
