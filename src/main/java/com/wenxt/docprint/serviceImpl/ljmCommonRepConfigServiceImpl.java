package com.wenxt.docprint.serviceImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wenxt.docprint.model.LjmCommonRepConfig;
import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.repo.LjmCommonRepConfigRepo;
import com.wenxt.docprint.service.ljmCommonRepConfigService;

import jakarta.persistence.Column;

@Service
public class ljmCommonRepConfigServiceImpl implements ljmCommonRepConfigService {

	@Autowired
	private LjmCommonRepConfigRepo ljmcommonrepo;

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

	@Override
	public String ljmCommonRepConfigCreates(ReportBuilderRequest reportBuilderRequest) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			LjmCommonRepConfig comConfig = new LjmCommonRepConfig();

			Map<String, Map<String, String>> fieldMaps = new HashMap<>();
			fieldMaps.put("frontForm", reportBuilderRequest.getFrontForm().getFormFields());
			for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
				setCommonreportBuilderFields(comConfig, entry.getValue());
			}

			LjmCommonRepConfig savedCommonconfig = ljmcommonrepo.save(comConfig);
			response.put(statusCode, successCode);
			response.put(messageCode, "Report Builder Details Created Successfully");
			data.put("tranId", savedCommonconfig.getREP_ID());
			data.put("Id", savedCommonconfig.getREP_SYS_ID());
			response.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("statusCode", errorCode);
			response.put("message", "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	private void setCommonreportBuilderFields(LjmCommonRepConfig comConfig, Map<String, String> fields)
			throws Exception {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			setCommonreportBuilderField(comConfig, entry.getKey(), entry.getValue());
		}
	}

	private void setCommonreportBuilderField(LjmCommonRepConfig comConfig, String fieldName, String value)
			throws Exception {
		try {
			Field field = LjmCommonRepConfig.class.getDeclaredField(fieldName);
			Class<?> fieldType = field.getType();
			Object convertedValue = null;
			if (fieldType == LjmCommonRepConfig.class) {
				convertedValue = getForeignObject(value);
			} else {
				convertedValue = convertStringToObject(value, fieldType);
			}

			String setterMethodName = "set" + fieldName;
			if (value != null && !value.isEmpty()) {
				Method setter = LjmCommonRepConfig.class.getMethod(setterMethodName, fieldType);
				setter.invoke(comConfig, convertedValue);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private LjmCommonRepConfig getForeignObject(String value) {
		LjmCommonRepConfig setup = ljmcommonrepo.getById(Long.parseLong(value));
		return setup;

	}

	private Object convertStringToObject(String value, Class<?> fieldType) {
		if (fieldType.equals(Integer.class) && value.isEmpty() == false && value != null) {
			return Integer.parseInt(value);
		} else if (fieldType.equals(Long.class) && value != null && !value.isEmpty()) {
			return Long.parseLong(value);
		} else if (fieldType.equals(Double.class) && value.isEmpty() == false && value != null) {
			return Double.parseDouble(value);
		} else if (fieldType.equals(Short.class) && value.isEmpty() == false && value != null) {
			return Short.parseShort(value);
		} else if (fieldType.equals(LocalDateTime.class) && value.isEmpty() == false && value != null) {
			return dateTimeConverter(value);
		} else if (fieldType.equals(Date.class) && value.isEmpty() == false && value != null) {
			return dateConverter(value);
		} else {
			return value;
		}
	}

	private Object dateConverter(String value) {
		String dateStr = value;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
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
	public String updateljmCommonRep(ReportBuilderRequest reportCommonRequest, Long rEP_SYS_ID) {
		JSONObject response = new JSONObject();

		try {
			Long reportCommon = rEP_SYS_ID;
			Optional<LjmCommonRepConfig> optionalUser = ljmcommonrepo.findById(reportCommon);
			LjmCommonRepConfig comReport = optionalUser.get();
			if (comReport != null) {
				Map<String, Map<String, String>> fieldMaps = new HashMap<>();
				fieldMaps.put("frontForm", reportCommonRequest.getFrontForm().getFormFields());
				for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
					setCommonreportBuilderFields(comReport, entry.getValue());
				}

				try {
					LjmCommonRepConfig savedRepConfig = ljmcommonrepo.save(comReport);
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
	public String deleteljmCommonRep(Long rEP_SYS_ID) {

		try {
			Optional<LjmCommonRepConfig> optionalEntity = ljmcommonrepo.findById(rEP_SYS_ID);

			if (optionalEntity.isPresent()) {
				ljmcommonrepo.deleteById(rEP_SYS_ID);

				JSONObject response = new JSONObject();
				response.put(statusCode, successCode);
				response.put(messageCode, "Record with ID " + rEP_SYS_ID + " deleted successfully");
				return response.toString();

			} else {
				JSONObject response = new JSONObject();
				response.put(statusCode, errorCode);
				response.put(messageCode, "Record with ID " + rEP_SYS_ID + " not found");
				return response.toString();
			}
		} catch (Exception e) {
			JSONObject response = new JSONObject();
			response.put(statusCode, errorCode);
			response.put(messageCode, "Error deleting record with ID " + rEP_SYS_ID + ": " + e.getMessage());
			return response.toString();
		}

	}

	@Override
	public String getReportBuilder(Long rEP_SYS_ID) throws Exception {

		Map<String, Object> parametermap = new HashMap<String, Object>();
		JSONObject inputObject = new JSONObject();
		Optional<LjmCommonRepConfig> optionalUser = ljmcommonrepo.findById(rEP_SYS_ID);
		LjmCommonRepConfig claim = optionalUser.get();
		if (claim != null) {
			for (int i = 0; i < claim.getClass().getDeclaredFields().length; i++) {
				Field field = claim.getClass().getDeclaredFields()[i];
				field.setAccessible(true);
				String columnName = null;
				if (field.isAnnotationPresent(Column.class)) {
					Annotation annotation = field.getAnnotation(Column.class);
					Column column = (Column) annotation;
					Object value = field.get(claim);
					columnName = column.name();
					inputObject.put(columnName, value);
				}
			}
		}
		return inputObject.toString();

	}

}
