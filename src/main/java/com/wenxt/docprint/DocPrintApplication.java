package com.wenxt.docprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication

@Configuration

@PropertySource("classpath:Properties/common_msgs.properties")

public class DocPrintApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocPrintApplication.class, args);
	}

}
