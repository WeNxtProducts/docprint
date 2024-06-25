package com.wenxt.docprint.service;

import jakarta.servlet.http.HttpServletRequest;

public interface LogsAppender {

	public void logToLJMLogs1(String message, HttpServletRequest request, String name);

}
