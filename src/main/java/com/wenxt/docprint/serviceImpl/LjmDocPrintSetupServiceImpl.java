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

import com.wenxt.docprint.dto.DocPrintSetupDto;
import com.wenxt.docprint.model.LjmDocprintSetup;
import com.wenxt.docprint.repo.LjmdocPrinsetup;
import com.wenxt.docprint.service.LjmDocPrintSetupService;

import jakarta.persistence.Column;

@Service
public class LjmDocPrintSetupServiceImpl implements LjmDocPrintSetupService {

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

	@Override
	public String createDocPrintSetup(DocPrintSetupDto ljmdocprint) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			LjmDocprintSetup doc = new LjmDocprintSetup();

			Map<String, Map<String, String>> fieldMaps = new HashMap<>();
			fieldMaps.put("frontForm", ljmdocprint.getFrontForm().getFormFields());
			for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
				setDocPrintFields(doc, entry.getValue());
			}

			try {
				LjmDocprintSetup docprintsetp = lrmdocrprintRepository.save(doc);
				response.put(statusCode, successCode);
				response.put(messageCode, "User created successfully");
				data.put("Id", docprintsetp.getDPS_SYSID());
				response.put("data", data);
			} catch (Exception e) {
				response.put(statusCode, errorCode);
				response.put(messageCode, "An error occurred: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put(statusCode, errorCode);
			response.put(messageCode, "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	@Override
	public String updateDocprint(DocPrintSetupDto ljmdocprint, Long DPS_SYSID) {

		JSONObject response = new JSONObject();

		try {
			Long dspid = DPS_SYSID;
			Optional<LjmDocprintSetup> optionaldocprint = lrmdocrprintRepository.findById(dspid);

			if (optionaldocprint.isPresent()) {
				LjmDocprintSetup docprint = optionaldocprint.get();

				// Assuming `getFrontForm()` returns an object with a method `getFormFields()`
				// which returns a Map<String, String>
				Map<String, Map<String, String>> fieldMaps = new HashMap<>();
				fieldMaps.put("frontForm", ljmdocprint.getFrontForm().getFormFields());

				for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
					setDocPrintFields(docprint, entry.getValue());
				}

				lrmdocrprintRepository.save(docprint);
				response.put(statusCode, successCode);
				response.put(messageCode, "Docprint Details Updated Successfully");
			} else {
				response.put(statusCode, errorCode);
				response.put(messageCode, "Docprint with the provided ID not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put(statusCode, errorCode);
			response.put(messageCode, "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	private void setDocPrintFields(LjmDocprintSetup doc, Map<String, String> fields) throws Exception {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			setDocPrintField(doc, entry.getKey(), entry.getValue());
		}
	}

	private void setDocPrintField(LjmDocprintSetup user, String fieldName, String value) throws Exception {
		try {
			Field field = LjmDocprintSetup.class.getDeclaredField(fieldName);
			Class<?> fieldType = field.getType();
			Object convertedValue = convertStringToObject(value, fieldType);
			String setterMethodName = "set" + fieldName;
			if (value != null && !value.isEmpty()) {
				Method setter = LjmDocprintSetup.class.getMethod(setterMethodName, fieldType);
				setter.invoke(user, convertedValue);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
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
	public String getDocPrintSetupByID(Long dpsSysid) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> parametermap = new HashMap<String, Object>();
		JSONObject inputObject = new JSONObject();
		Optional<LjmDocprintSetup> optionalUser = lrmdocrprintRepository.findById(dpsSysid);
		LjmDocprintSetup docprint = optionalUser.get();
		if (docprint != null) {
			for (int i = 0; i < docprint.getClass().getDeclaredFields().length; i++) {
				Field field = docprint.getClass().getDeclaredFields()[i];
				field.setAccessible(true);
				String columnName = null;
				if (field.isAnnotationPresent(Column.class)) {
					Annotation annotation = field.getAnnotation(Column.class);
					Column column = (Column) annotation;
					Object value = field.get(docprint);
					columnName = column.name();
					inputObject.put(columnName, value);
				}
			}
		}
		return inputObject.toString();
	}

	@Override
	public String deleteDocPrintSetupID(Long dpsSysid) {
		try {
			Optional<LjmDocprintSetup> optionalEntity = lrmdocrprintRepository.findById(dpsSysid);

			if (optionalEntity.isPresent()) {
				lrmdocrprintRepository.deleteById(dpsSysid);

				JSONObject response = new JSONObject();
				response.put(statusCode, successCode);
				response.put(messageCode, "Record with ID " + dpsSysid + " deleted successfully");
				return response.toString();

			} else {
				JSONObject response = new JSONObject();
				response.put(statusCode, errorCode);
				response.put(messageCode, "Record with ID " + dpsSysid + " not found");
				return response.toString();
			}
		} catch (Exception e) {
			JSONObject response = new JSONObject();
			response.put(statusCode, errorCode);
			response.put(messageCode, "Error deleting record with ID " + dpsSysid + ": " + e.getMessage());
			return response.toString();
		}
	}

}
