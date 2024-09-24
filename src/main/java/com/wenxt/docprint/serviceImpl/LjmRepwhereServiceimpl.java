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
import com.wenxt.docprint.model.LjmRepWhere;
import com.wenxt.docprint.model.ReportBuilderRequest;
import com.wenxt.docprint.repo.LjmRepwhereRepo;
import com.wenxt.docprint.service.LjmRepwhereService;

import jakarta.persistence.Column;


@Service
public class LjmRepwhereServiceimpl implements LjmRepwhereService {

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
	private LjmRepwhereRepo comwhererepo;

	@Override
	public String LjmRepwhereCreates(ReportBuilderRequest reportBuilderRequest) {

		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {

			LjmRepWhere comrepWhere = new LjmRepWhere();

			Map<String, Map<String, String>> fieldMaps = new HashMap<>();
			fieldMaps.put("frontForm", reportBuilderRequest.getFrontForm().getFormFields());
			for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
				setCommonRepWhereFields(comrepWhere, entry.getValue());
			}

			LjmRepWhere savedrepWhere = comwhererepo.save(comrepWhere);
			response.put(statusCode, successCode);
			response.put(messageCode, "Report Builder Details Created Successfully");
			data.put("Id", savedrepWhere.getLjmRepSysId());
			response.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("statusCode", errorCode);
			response.put("message", "An error occurred: " + e.getMessage());
		}

		return response.toString();

	}

	private void setCommonRepWhereFields(LjmRepWhere comrepWhere, Map<String, String> fields) throws Exception {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			setCommonRepWhereField(comrepWhere, entry.getKey(), entry.getValue());
		}
	}

	private void setCommonRepWhereField(LjmRepWhere comrepWhere, String fieldName, String value) throws Exception {
		try {
			Field field = LjmRepWhere.class.getDeclaredField(fieldName);
			Class<?> fieldType = field.getType();
			Object convertedValue = null;
			if (fieldType == LjmRepWhere.class) {
				convertedValue = getForeignObject(value);
			} else {
				convertedValue = convertStringToObject(value, fieldType);
			}

			String setterMethodName = "set" + fieldName;
			if (value != null && !value.isEmpty()) {
				System.out.println(setterMethodName);
				Method setter = LjmCommonRepConfig.class.getMethod(setterMethodName, fieldType);
				setter.invoke(comrepWhere, convertedValue);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private LjmRepWhere getForeignObject(String value) {
		LjmRepWhere setup = comwhererepo.getById(Long.parseLong(value));
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
	public String updatelLjmRepwhere(ReportBuilderRequest reportWhereRequest, Long LJM_REP_SYS_ID) {
		
		

		JSONObject response = new JSONObject();

		try {
			Long reportwhere = LJM_REP_SYS_ID;
			Optional<LjmRepWhere> optionalUser = comwhererepo.findById(reportwhere);
			LjmRepWhere ReportWhere = optionalUser.get();
			if (ReportWhere != null) {
				Map<String, Map<String, String>> fieldMaps = new HashMap<>();
				fieldMaps.put("frontForm", reportWhereRequest.getFrontForm().getFormFields());
				for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
					setCommonRepWhereFields(ReportWhere, entry.getValue());
				}

				try {
					LjmRepWhere savedreports = comwhererepo.save(ReportWhere);
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
	public String deleteLjmRepwhere(Long LJM_REP_SYS_ID) {

		try {
			Optional<LjmRepWhere> optionalEntity = comwhererepo.findById(LJM_REP_SYS_ID);

			if (optionalEntity.isPresent()) {
				comwhererepo.deleteById(LJM_REP_SYS_ID);

				JSONObject response = new JSONObject();
				response.put(statusCode, successCode);
				response.put(messageCode, "Record with ID " + LJM_REP_SYS_ID + " deleted successfully");
				return response.toString();

			} else {
				JSONObject response = new JSONObject();
				response.put(statusCode, errorCode);
				response.put(messageCode, "Record with ID " + LJM_REP_SYS_ID + " not found");
				return response.toString();
			}
		} catch (Exception e) {
			JSONObject response = new JSONObject();
			response.put(statusCode, errorCode);
			response.put(messageCode, "Error deleting record with ID " + LJM_REP_SYS_ID + ": " + e.getMessage());
			return response.toString();
		}

	}

	@Override
	public String getLjmRepwhere(Long LJM_REP_SYS_ID) throws Exception {

		Map<String, Object> parametermap = new HashMap<String, Object>();
		JSONObject inputObject = new JSONObject();
		Optional<LjmRepWhere> optionalUser = comwhererepo.findById(LJM_REP_SYS_ID);
		LjmRepWhere claim = optionalUser.get();
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
		System.out.println(inputObject);
		return inputObject.toString();

	}

}
