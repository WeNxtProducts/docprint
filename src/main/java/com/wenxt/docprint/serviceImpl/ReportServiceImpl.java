package com.wenxt.docprint.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
	@Autowired
	private LjmdocPrinsetup lrmdocrprintRepository;

	@Autowired
	private ljmparam paramRepository;

	@Autowired
	private LogsAppenderServiceImpl logservice;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${Staticpath}")
	private String Staticpath;

	@Value("${XdocBasePath}")
	private String XdocBasePath;

	@Value("${Jasperpath}")
	private String Jasperpath;

	@Value("${ImgDocBasePath}")
	private String ImgDocBasePath;

	@Override
	public String generatedocument(HttpServletRequest request) throws JRException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> inputMap = mapper.readValue(request.getInputStream(), Map.class);

			String docTemplateName = (String) inputMap.get("docTemplateName");
			String genType = (String) inputMap.get("genType");

			Map<String, Object> docParms = (Map<String, Object>) inputMap.get("docParms");
			Map<String, Object> images = (Map<String, Object>) inputMap.get("images");

			String sqlQuery = "SELECT dps_sysid FROM ljm_docprint_setup WHERE dps_template_name = ?";
			Long dpsSysid = jdbcTemplate.queryForObject(sqlQuery, Long.class, docTemplateName);

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
						String qrServiceUrl = "http://localhost:8098/qr-code/generate";
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

				return response.toString();
			}

			else if ("Static".equalsIgnoreCase(reportType)) {
				// Generate static report
				Optional<String> fileLocation = getFileLocationByTemplateName(docTemplateName);

				if (!fileLocation.isPresent()) {
					return new JSONObject().put(statusCode, errorCode)
							.put(messageCode, "Template not found for name: " + docTemplateName).toString();
				}

				byte[] pdfBytes = readPdfFile(fileLocation.get());

				try {
					String outputFilePath = Staticpath + docTemplateName + ".pdf";
					Files.write(Paths.get(outputFilePath), pdfBytes);
					response.put(statusCode, successCode);
					response.put(messageCode, "Static report generated and saved successfully");

					data.put(attachmentCode, pdfBytes);
					response.put(dataCode, data);
				} catch (IOException e) {
					return new JSONObject().put(statusCode, errorCode)
							.put(messageCode, "Failed to save the report: " + e.getMessage()).toString();
				}
			}

			else if ("JASPER".equalsIgnoreCase(reportType)) {
				// Generate Jasper report
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

				String username = getUsernameFromSecurityContext();

				if (".pdf".equalsIgnoreCase(genType)) {
					response.put(statusCode, successCode);
					response.put(messageCode, "Jasper report PDF generated successfully");
					data.put(attachmentCode, pdfBytes);
					response.put(dataCode, data);
				} else {
					response.put(statusCode, successCode); // Changed errorCode to successCode
					response.put(messageCode, "Jasper report XLSX generated successfully");
					data.put(attachmentCode, xlsxBytes); // Changed pdfBytes to xlsxBytes
					response.put(dataCode, data);
				}
			}

			else {
				throw new RuntimeException("Unsupported report type: " + reportType);
			}

			logservice.logToLJMLogs1("Generated report", request, setup.getDPS_TEMPLATE_NAME());

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
			img.setAbsolutePosition(200, 400);
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
