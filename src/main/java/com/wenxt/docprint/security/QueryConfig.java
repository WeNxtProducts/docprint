package com.wenxt.docprint.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryConfig {

    @Value("${getReportParametersQuery}")
    private String reportParametersQuery;

    public String getReportParametersQuery() {
        return reportParametersQuery;
    }
}