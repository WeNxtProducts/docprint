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

import com.wenxt.docprint.dto.ReportBuilderRequestDto;
import com.wenxt.docprint.model.LjmCommonRepConfig;
import com.wenxt.docprint.model.LjmCommonRepConfigDtl;
import com.wenxt.docprint.repo.LjmCommonRepConfigDtlsRepo;
import com.wenxt.docprint.repo.LjmCommonRepConfigRepo;
import com.wenxt.docprint.service.ljmCommonRepConfigDtlService;

import jakarta.persistence.Column;

@Service
public class ljmCommonRepConfigServiceDtlsImpl implements ljmCommonRepConfigDtlService {

	@Autowired
	private LjmCommonRepConfigDtlsRepo ljmcommonDtlsrepo;

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
	public String createDocparam(ReportBuilderRequestDto reportBuilderRequest, Long SRNO) {

		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			LjmCommonRepConfigDtl commonrepDtls = new LjmCommonRepConfigDtl();

			Map<String, Map<String, String>> fieldMaps = new HashMap<>();
			fieldMaps.put("frontForm", reportBuilderRequest.getLJM_COMMON_REP_CONFIG_DTL().getFormFields());

			fieldMaps.get("frontForm").put("LjmCommonRepConfig", SRNO.toString());

			for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
				setCommonRepDtlsFields(commonrepDtls, entry.getValue());
			}

			try {
				LjmCommonRepConfigDtl comrepdtls = ljmcommonDtlsrepo.save(commonrepDtls);
				response.put(statusCode, successCode);
				response.put(messageCode, "common details config added successfully");
				data.put("Id", comrepdtls.getREP_ID_DTL());
				response.put("data", data);
			} catch (Exception e) {
				response.put(statusCode, errorCode);
				response.put(messageCode, "An error occurred: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("statusCode", errorCode);
			response.put("message", "An error occurred: " + e.getMessage());
		}

		return response.toString();

	}

	@Override
	public String updateDocparam(ReportBuilderRequestDto reportBuilderRequest, Long SRNO) {
		JSONObject response = new JSONObject();

		try {
			Long srno = SRNO;
			Optional<LjmCommonRepConfigDtl> optionalcomconfig = ljmcommonDtlsrepo.findById(srno);

			if (optionalcomconfig.isPresent()) {
				LjmCommonRepConfigDtl docprintparam = optionalcomconfig.get();

				// Assuming `getFrontForm()` returns an object with a method `getFormFields()`
				// which returns a Map<String, String>
				Map<String, Map<String, String>> fieldMaps = new HashMap<>();
				fieldMaps.put("frontForm", reportBuilderRequest.getLJM_COMMON_REP_CONFIG_DTL().getFormFields());

				for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
					setCommonRepDtlsFields(docprintparam, entry.getValue());
				}

				ljmcommonDtlsrepo.save(docprintparam);
				response.put(statusCode, successCode);
				response.put(messageCode, "Common Report config details Updated Successfully");
			} else {
				response.put(statusCode, errorCode);
				response.put(messageCode, "Common Report with the provided ID not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put(statusCode, errorCode);
			response.put(messageCode, "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	private void setCommonRepDtlsFields(LjmCommonRepConfigDtl commonrepDtls, Map<String, String> fields)
			throws Exception {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			setCommonRepDtlsField(commonrepDtls, entry.getKey(), entry.getValue());
		}
	}

	private void setCommonRepDtlsField(LjmCommonRepConfigDtl user, String fieldName, String value) throws Exception {
		try {
			Field field = LjmCommonRepConfigDtl.class.getDeclaredField(fieldName);
			Class<?> fieldType = field.getType();
			Object convertedValue = null;
			if (fieldType == LjmCommonRepConfig.class) {
				convertedValue = getForeignObject(value);
			} else {
				convertedValue = convertStringToObject(value, fieldType);
			}
			String setterMethodName = "set" + fieldName;
			if (value != null && !value.isEmpty()) {
				Method setter = LjmCommonRepConfigDtl.class.getMethod(setterMethodName, fieldType);
				setter.invoke(user, convertedValue);
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
	public String deleteljmCommonRepDtls(Long sRNO) {

		try {
			Optional<LjmCommonRepConfigDtl> optionalEntity = ljmcommonDtlsrepo.findById(sRNO);

			if (optionalEntity.isPresent()) {
				ljmcommonDtlsrepo.deleteById(sRNO);

				JSONObject response = new JSONObject();
				response.put(statusCode, successCode);
				response.put(messageCode, "Record with ID " + sRNO + " deleted successfully");
				return response.toString();

			} else {
				JSONObject response = new JSONObject();
				response.put(statusCode, errorCode);
				response.put(messageCode, "Record with ID " + sRNO + " not found");
				return response.toString();
			}
		} catch (Exception e) {
			JSONObject response = new JSONObject();
			response.put(statusCode, errorCode);
			response.put(messageCode, "Error deleting record with ID " + sRNO + ": " + e.getMessage());
			return response.toString();
		}

	}

	@Override
	public String getReportBuilderDtls(Long sRNO) throws Exception {

		Map<String, Object> parametermap = new HashMap<String, Object>();
		JSONObject inputObject = new JSONObject();
		Optional<LjmCommonRepConfigDtl> optionalUser = ljmcommonDtlsrepo.findById(sRNO);
		LjmCommonRepConfigDtl claim = optionalUser.get();
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
