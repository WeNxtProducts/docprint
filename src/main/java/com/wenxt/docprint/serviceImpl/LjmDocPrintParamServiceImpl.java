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

import com.wenxt.docprint.dto.DocPrintParamDto;
import com.wenxt.docprint.dto.DocPrintSetupDto;
import com.wenxt.docprint.model.LjmDocprintParam;
import com.wenxt.docprint.model.LjmDocprintSetup;
import com.wenxt.docprint.repo.LjmdocPrinsetup;
import com.wenxt.docprint.repo.ljmparam;
import com.wenxt.docprint.service.LjmDocPrintParamService;

import jakarta.persistence.Column;

@Service
public class LjmDocPrintParamServiceImpl implements LjmDocPrintParamService {
	
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
	private ljmparam paramRepository;

	@Autowired
	private LjmdocPrinsetup setuprepo;

	@Override
	public String createDocparam(DocPrintParamDto param) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();

		try {
			LjmDocprintParam docparam = new LjmDocprintParam();

			Map<String, Map<String, String>> fieldMaps = new HashMap<>();
			fieldMaps.put("frontForm", param.getDocPrintParam().getFormFields());
			for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
				setDocPrintParamFields(docparam, entry.getValue());
			}

			try {
				LjmDocprintParam docprintsetp = paramRepository.save(docparam);
				response.put("statusCode", successCode);
				response.put("message", "User created successfully");
				data.put("Id", docprintsetp.getDPP_SYSID());
				response.put("data", data);
			} catch (Exception e) {
				response.put("statusCode",errorCode);
				response.put("message", "An error occurred: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("statusCode", errorCode);
			response.put("message", "An error occurred: " + e.getMessage());
		}

		return response.toString();
	}

	@Override
	public String updateDocparam(DocPrintParamDto param, Long dPP_SYSID) {
		JSONObject response = new JSONObject();

		 try {
			 Long dppid = dPP_SYSID;
		        Optional<LjmDocprintParam> optionaldocprint = paramRepository.findById(dppid);
		        
		        if (optionaldocprint.isPresent()) {
		        	LjmDocprintParam docprintparam = optionaldocprint.get();

		            // Assuming `getFrontForm()` returns an object with a method `getFormFields()`
		            // which returns a Map<String, String>
		            Map<String, Map<String, String>> fieldMaps = new HashMap<>();
		            fieldMaps.put("frontForm", param.getDocPrintParam().getFormFields());

		            for (Map.Entry<String, Map<String, String>> entry : fieldMaps.entrySet()) {
		            	setDocPrintParamFields(docprintparam, entry.getValue());
		            }

		            paramRepository.save(docprintparam);
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
	
	private void setDocPrintParamFields(LjmDocprintParam docparam, Map<String, String> fields) throws Exception {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			setDocPrintParamField(docparam, entry.getKey(), entry.getValue());
		}
	}

	private void setDocPrintParamField(LjmDocprintParam user, String fieldName, String value) throws Exception {
		try {
			Field field = LjmDocprintParam.class.getDeclaredField(fieldName);
			Class<?> fieldType = field.getType();
			Object convertedValue = null;
			if (fieldType == LjmDocprintSetup.class) {
				convertedValue = getForeignObject(value);
			} else {
				convertedValue = convertStringToObject(value, fieldType);
			}
//			Object convertedValue = convertStringToObject(value, fieldType);
			String setterMethodName = "set" + fieldName;
			if (value != null && !value.isEmpty()) {
				System.out.println(setterMethodName);
				Method setter = LjmDocprintParam.class.getMethod(setterMethodName, fieldType);
				setter.invoke(user, convertedValue);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private LjmDocprintSetup getForeignObject(String value) {
		LjmDocprintSetup setup = setuprepo.getById(Long.parseLong(value));
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
	public String getDocparamByID(Long dppSysid) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> parametermap = new HashMap<String, Object>();
		JSONObject inputObject = new JSONObject();
		Optional<LjmDocprintParam> optionalUser = paramRepository.findById(dppSysid);
		LjmDocprintParam docprintparam = optionalUser.get();
		if (docprintparam != null) {
			for (int i = 0; i < docprintparam.getClass().getDeclaredFields().length; i++) {
				Field field = docprintparam.getClass().getDeclaredFields()[i];
				field.setAccessible(true);
				String columnName = null;
				if (field.isAnnotationPresent(Column.class)) {
					Annotation annotation = field.getAnnotation(Column.class);
					Column column = (Column) annotation;
					Object value = field.get(docprintparam);
					columnName = column.name();
					inputObject.put(columnName, value);
				}
			}
		}
		return inputObject.toString();}

	@Override
	public String deleteDocparamByID(Long dppSysid) {
		try {
			Optional<LjmDocprintParam> optionalEntity = paramRepository.findById(dppSysid);

			if (optionalEntity.isPresent()) {
				paramRepository.deleteById(dppSysid);

				JSONObject response = new JSONObject();
				response.put("Status", successCode);
				response.put("Message", "Record with ID " + dppSysid + " deleted successfully");
				return response.toString();

			} else {
				JSONObject response = new JSONObject();
				response.put("Status", errorCode);
				response.put("Message", "Record with ID " + dppSysid + " not found");
				return response.toString();
			}
		} catch (Exception e) {
			JSONObject response = new JSONObject();
			response.put("Status", errorCode);
			response.put("Message", "Error deleting record with ID " + dppSysid + ": " + e.getMessage());
			return response.toString();
		}
	}

	
}
