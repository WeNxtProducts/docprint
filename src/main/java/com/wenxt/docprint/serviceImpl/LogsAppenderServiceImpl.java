package com.wenxt.docprint.serviceImpl;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.jdbc.JDBCAppender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wenxt.docprint.service.LogsAppender;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LogsAppenderServiceImpl implements LogsAppender {

	public void logToLJMLogs1(String message, HttpServletRequest request, String name) {
		Logger rootLogger = Logger.getRootLogger();
		Logger ljmLogger = Logger.getLogger("com.wenxt.docprint.serviceImpl.LogsAppenderServiceImpl");

		PropertyConfigurator.configure("src/main/resources/application.properties");
		JDBCAppender ljmLogsAppender = (JDBCAppender) Logger
				.getLogger("com.wenxt.docprint.serviceImpl.LogsAppenderServiceImpl").getAppender("DOC_PRINT_HISTORY");
		if (ljmLogsAppender == null) {
			ljmLogger.addAppender(rootLogger.getAppender("DOC_PRINT_HISTORY"));
		}

		String ipAddress = request.getRemoteAddr();
//    String username = request.getUserPrincipal().getName(); // Assuming the username can be retrieved this way
		String username = getUsernameFromSecurityContext();
		String hostname = request.getRemoteHost();
		MDC.put("DPH_USER", username);
		MDC.put("DESTINATION", name);

		MDC.put("DPH_HOST_NAME", hostname);
		MDC.put("DPH_IP_ADDR", ipAddress);
		ljmLogger.info(message);
		MDC.clear();
	}

	private String getUsernameFromSecurityContext() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}

	public void logToError(String message, HttpServletRequest request, Exception e) {
		Logger rootLogger = Logger.getRootLogger();
		Logger ljmLoggers = Logger.getLogger("com.wenxt.docprint.serviceImpl.LogsAppenderServiceImpl");

		PropertyConfigurator.configure("src/main/resources/application.properties");
		JDBCAppender ljmLogsAppender = (JDBCAppender) Logger
				.getLogger("com.wenxt.docprint.serviceImpl.LogsAppenderServiceImpl").getAppender("DOC_PRINT_HISTORY");
		if (ljmLogsAppender == null) {
			ljmLoggers.addAppender(rootLogger.getAppender("DOC_PRINT_HISTORY"));
		}

		String ipAddress = request.getRemoteAddr();
//    String username = request.getUserPrincipal().getName(); // Assuming the username can be retrieved this way
		String username = getUsernameFromSecurityContext();
		String hostname = request.getRemoteHost();
		MDC.put("DPH_USER", username);
		MDC.put("DPH_LOG_TYPE", e);
		MDC.put("DPH_HOST_NAME", hostname);
		MDC.put("DPH_IP_ADDR", ipAddress);
		ljmLoggers.info(message);
		MDC.clear();

	}

}
