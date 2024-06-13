package com.wenxt.docprint.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
public class Xdoc_JasperReportServiceImpl implements Xdoc_JasperReportService {

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

//	public String generateJasperReport(Long dpsSysid, HttpServletRequest request) throws JRException {
//		// Retrieve the setup record from the database
//		Optional<LjmDocprintSetup> setupOptional = lrmdocrprintRepository.findById(dpsSysid);
//		if (!setupOptional.isPresent()) {
//			throw new RuntimeException("Setup record not found");
//		}
//
//		LjmDocprintSetup setup = setupOptional.get();
//
//		List<LjmDocprintParam> paramList = paramRepository.findByDppDpsSysid(dpsSysid);
//		if (paramList.isEmpty()) {
//			throw new RuntimeException("No parameters found for the given setup");
//		}
//
//		String location = setup.getDpsTempLoc();
//		String basePath = "D:/Kamali/DocPrint/DOC_PRINT/DOC_PRINT/src/main/resources/templates/";
//		String outputPath = basePath + "output2024.pdf";
//
//		JasperReport jasperReport = JasperCompileManager.compileReport(location);
//
//		// Set parameters
//		Map<String, Object> dataMap = new HashMap<>();
//		for (LjmDocprintParam param : paramList) {
//			String paramType = param.getDppType();
//			String paramName = param.getDppParamName();
//			String paramValue = param.getDppValue();
//
//			if ("S".equalsIgnoreCase(paramType.trim())) {
//				dataMap.put(paramName, paramValue);
//
//			} else if ("Q".equalsIgnoreCase(paramType.trim())) {
//				List<Map<String, Object>> queryResult = jdbcTemplate.query(paramValue, new Object[] { dpsSysid },
//						new ColumnMapRowMapper());
//				if (!queryResult.isEmpty()) {
//					// Assuming the query result has a single row
//					Map<String, Object> queryData = queryResult.get(0);
//					dataMap.putAll(queryData);
//				}
//			} else if ("P".equals(paramType)) {
//				dataMap.put(paramName, paramValue);
//			}
//		}
//
//		Map<String, Object> parameters = new HashMap<>(dataMap);
//		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(parameters));
//
//		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//		JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
//
//		// Log the report generation
//		String username = getUsernameFromSecurityContext();
//		logservice.logToLJMLogs1("Generated Jasper report", request, setup.getDpsTemplatename());
//
//		return outputPath;
//	}

	public String generateJasperReport(Long dpsSysid, HttpServletRequest request) throws JRException {
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

		String location = setup.getDPS_TEMP_LOC();
		String basePath = "D:/Kamali/DocPrint/DOC_PRINT/DOC_PRINT/src/main/resources/templates/";
		String pdfOutputPath = basePath + "output2024.pdf";
		String xlsxOutputPath = basePath + "output2024.xlsx";

		JasperReport jasperReport = JasperCompileManager.compileReport(location);

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
					// Assuming the query result has a single row
					Map<String, Object> queryData = queryResult.get(0);
					dataMap.putAll(queryData);
				}
			} else if ("P".equals(paramType)) {
				dataMap.put(paramName, paramValue);
			}
		}

		Map<String, Object> parameters = new HashMap<>(dataMap);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(parameters));

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		// Export to PDF
		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);

		// Export to XLSX
		JRXlsxExporter xlsxExporter = new JRXlsxExporter();
		xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsxOutputPath));
		SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
		xlsxReportConfiguration.setOnePagePerSheet(false);
		xlsxReportConfiguration.setDetectCellType(true);
		xlsxExporter.setConfiguration(xlsxReportConfiguration);
		xlsxExporter.exportReport();

		// Log the report generation
		String username = getUsernameFromSecurityContext();
		logservice.logToLJMLogs1("Generated Jasper report", request, setup.getDPS_TEMPLATE_NAME());

		return "PDF: " + pdfOutputPath + ", XLSX: " + xlsxOutputPath;
	}

//	public String generateXdocReport(Long dpsSysid) throws IOException, XDocReportException {
//		// Retrieve the setup record from the database
//		Optional<LjmDocprintSetup> setupOptional = lrmdocrprintRepository.findById(dpsSysid);
//		if (!setupOptional.isPresent()) {
//			throw new RuntimeException("Setup record not found");
//		}
//
//		LjmDocprintSetup setup = setupOptional.get();
//
//		// Retrieve the param records from the database
//		List<LjmDocprintParam> paramList = paramRepository.findByDppDpsSysid(dpsSysid);
//		if (paramList.isEmpty()) {
//			throw new RuntimeException("No parameters found for the given setup");
//		}
//
//		Map<String, Object> dataMap = new HashMap<>();
//		for (LjmDocprintParam param : paramList) {
//			String paramType = param.getDppType();
//			String paramName = param.getDppParamName();
//			String paramValue = param.getDppValue();
//
//			if ("S".equalsIgnoreCase(paramType.trim())) {
//				dataMap.put(paramName, paramValue);
//			} else if ("Q".equalsIgnoreCase(paramType.trim())) {
//				List<Map<String, Object>> queryResult = jdbcTemplate.query(paramValue, new Object[] { dpsSysid },
//						new ColumnMapRowMapper());
//				if (!queryResult.isEmpty()) {
//					// Assuming the query result has a single row
//					Map<String, Object> queryData = queryResult.get(0);
//					dataMap.putAll(queryData);
//				}
//			} else if ("P".equals(paramType.trim())) {
//				dataMap.put(paramName, paramValue);
//			}
//		}
//
//		// Load the XDOC template
//		IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(getTemplatePath(dpsSysid),
//				TemplateEngineKind.Velocity);
//
//		// Create the context for the report
//		IContext context = xdocReport.createContext();
//
//		// Add data to the context
//		context.put("Head", dataMap);
//
//		// Load the image from file
//		IImageProvider logo = new FileImageProvider(new File("C:/Users/Kamali/Downloads/logo.jpg"));
//		logo.setResize(true); // Optional: resize image to fit the placeholder
//		context.put("logo", logo);
//
//		// Define the output file path
//		String basePath = "D:/Kamali/DocPrint/DOC_PRINT/DOC_PRINT/src/main/resources/xdoc/";
//		String outputFileName = basePath + "XDOC_output2024.docx";
//
//		// Generate and save the XDOC report
//		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//			xdocReport.process(context, outputStream);
//
//			try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {
//				outputStream.writeTo(fileOutputStream);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace(); // Log or handle the exception appropriately
//				throw new FileNotFoundException("File not found: " + outputFileName);
//			} catch (IOException e) {
//				e.printStackTrace(); // Log or handle the exception appropriately
//				throw new IOException("Error writing to file: " + outputFileName, e);
//			}
//		}
//		return outputFileName;
//
//	}

	public String generateXdocReport(Long dpsSysid) throws IOException, XDocReportException {
	    // Retrieve the setup record from the database
	    Optional<LjmDocprintSetup> setupOptional = lrmdocrprintRepository.findById(dpsSysid);
	    if (!setupOptional.isPresent()) {
	        throw new RuntimeException("Setup record not found");
	    }

	    LjmDocprintSetup setup = setupOptional.get();

	    // Retrieve the param records from the database
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
	                // Assuming the query result has a single row
	                Map<String, Object> queryData = queryResult.get(0);
	                dataMap.putAll(queryData);
	            }
	        } else if ("P".equals(paramType.trim())) {
	            dataMap.put(paramName, paramValue);
	        }
	    }

	    // Load the XDOC template
	    IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(getTemplatePath(dpsSysid),
	            TemplateEngineKind.Velocity);

	    // Create the context for the report
	    IContext context = xdocReport.createContext();

	    // Add data to the context
	    context.put("Head", dataMap);

	    // Prepare and add the image to the context
	    FieldsMetadata metadata = xdocReport.createFieldsMetadata();
	    metadata.addFieldAsImage("logo");

	    FileImageProvider logo = new FileImageProvider(new File("C:/Users/Kamali/Downloads/logo.jpg"));
//	    logo.setSize(new ImageSize(200, 100)); // Set the desired size of the image
	    context.put("logo", logo);

	    // Define the output file path
	    String basePath = "D:/Kamali/DocPrint/DOC_PRINT/DOC_PRINT/src/main/resources/xdoc/";
	    String outputFileName = basePath + "XDOC_output2024.docx";

	    // Generate and save the XDOC report
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        xdocReport.process(context, outputStream);

	        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {
	            outputStream.writeTo(fileOutputStream);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace(); // Log or handle the exception appropriately
	            throw new FileNotFoundException("File not found: " + outputFileName);
	        } catch (IOException e) {
	            e.printStackTrace(); // Log or handle the exception appropriately
	            throw new IOException("Error writing to file: " + outputFileName, e);
	        }
	    }
	    return outputFileName;
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


}
