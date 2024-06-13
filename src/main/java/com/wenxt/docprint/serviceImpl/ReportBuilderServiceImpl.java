package com.wenxt.docprint.serviceImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wenxt.docprint.model.LjmReportBuilder;
import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.repo.ReportBuilderRepo;
import com.wenxt.docprint.service.ReportBuilderService;

@Service
public class ReportBuilderServiceImpl implements ReportBuilderService {
	
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
	private ReportBuilderRepo reportBuilderRepo;

	@Override
	public String createReportBuilder(ReportBuilderRequest reportBuilderRequest) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			LjmReportBuilder template = new LjmReportBuilder();

			Map<String, Map<String, String>> fieldMaps = new HashMap<>();
			fieldMaps.put("frontForm", reportBuilderRequest.getFrontForm().getFormFields());
			for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
				setreportBuilderFields(template, entry.getValue());
			}

			try {
				LjmReportBuilder savedTemplate = reportBuilderRepo.save(template);
				response.put(statusCode, successCode);
				response.put(messageCode, "Report Builder Details Created Successfully");
				data.put("Id", savedTemplate.getRB_SYS_ID());
				response.put("data", data);
			} catch (Exception e) {
				response.put("statusCode", errorCode);
				response.put("message", "An error occurred: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("statusCode", errorCode);
			response.put("message", "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	private void setreportBuilderFields(LjmReportBuilder template, Map<String, String> value) throws Exception{
		for (Map.Entry<String, String> entry : value.entrySet()) {
			setReportBuilderField(template, entry.getKey(), entry.getValue());
		}
	}

	private void setReportBuilderField(LjmReportBuilder template, String key, String value) throws Exception {
		try {
			Field field = LjmReportBuilder.class.getDeclaredField(key);
			Class<?> fieldType = field.getType();
			Object convertedValue = convertStringToObject(value, fieldType);
			String setterMethodName = "set" + key;
			if (value != null && !value.isEmpty()) {
				Method setter = LjmReportBuilder.class.getMethod(setterMethodName, fieldType);
				setter.invoke(template, convertedValue);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private Object convertStringToObject(String value, Class<?> fieldType) {
		if (fieldType.equals(Integer.class) && value.isEmpty() == false && value != null) {
			return Integer.parseInt(value);
		} else if (fieldType.equals(Double.class) && value.isEmpty() == false && value != null) {
			return Double.parseDouble(value);
		} else if (fieldType.equals(Short.class) && value.isEmpty() == false && value != null) {
			return Short.parseShort(value);
		} else if (fieldType.equals(LocalDateTime.class) && value.isEmpty() == false && value != null) {
			return dateTimeConverter(value);
		} else {
			return value;
		}
	}

	private Object dateTimeConverter(String value) {
		String dateString = value;
		if (value.length() > 10) {
			dateString = value.substring(0, 10);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalTime defaultTime = LocalTime.of(0, 0, 0);
		LocalDate localDate = LocalDate.parse(dateString, formatter);
		LocalDateTime dateTime = LocalDateTime.of(localDate, defaultTime);
		String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateTime, formatters);
		return parsedDateTime;
	}

	@Override
	public String updateReportBuilder(ReportBuilderRequest reportBuilderRequest, Integer rbSysId) {
		JSONObject response = new JSONObject();

		try {
			Integer reportBuilderId = rbSysId;
			Optional<LjmReportBuilder> optionalUser = reportBuilderRepo.findById(reportBuilderId);
			LjmReportBuilder claim = optionalUser.get();
			if (claim != null) {
				Map<String, Map<String, String>> fieldMaps = new HashMap<>();
				fieldMaps.put("frontForm", reportBuilderRequest.getFrontForm().getFormFields());
				for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
					setreportBuilderFields(claim, entry.getValue());
				}

				try {
					LjmReportBuilder savedClaimDetails = reportBuilderRepo.save(claim);
					response.put(statusCode, successCode);
					response.put(messageCode, "Report Builder Details Updated Successfully");
				} catch (Exception e) {
					response.put("statusCode", errorCode);
					response.put("message", "An error occurred: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("statusCode", errorCode);
			response.put("message", "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	@Override
	public String deleteReportBuilder(Integer rbSysId) {
		try {
			Optional<LjmReportBuilder> optionalEntity = reportBuilderRepo.findById(rbSysId);

			if (optionalEntity.isPresent()) {
				reportBuilderRepo.deleteById(rbSysId);

				JSONObject response = new JSONObject();
				response.put(statusCode, successCode);
				response.put(messageCode, "Record with ID " + rbSysId + " deleted successfully");
				return response.toString();

			} else {
				JSONObject response = new JSONObject();
				response.put(statusCode, errorCode);
				response.put(messageCode, "Record with ID " + rbSysId + " not found");
				return response.toString();
			}
		} catch (Exception e) {
			JSONObject response = new JSONObject();
			response.put(statusCode, errorCode);
			response.put(messageCode, "Error deleting record with ID " + rbSysId + ": " + e.getMessage());
			return response.toString();
		}
	}

}
