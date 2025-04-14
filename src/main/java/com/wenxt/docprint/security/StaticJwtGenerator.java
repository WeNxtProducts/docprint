package com.wenxt.docprint.security;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class StaticJwtGenerator {
	
	//@Value("${jwt.secret}")
    private String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private String staticToken;

    @PostConstruct
    public void init() {
        generateStaticToken();
    }

    //Regenerate every 30 minutes (1800000 milliseconds)
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void generateStaticToken() {
        staticToken = Jwts.builder()
                .setSubject("staticUser")
                .claim("role", "SYSTEM")
                .claim("type", "static")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000))) // 30 mins
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public String getStaticToken() {
        return staticToken;
    }
    
    public String generateCustomToken(String username, String email, String mobileNumber) {
        return Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .claim("mobileNumber", mobileNumber)
                .claim("role", "CUSTOM")
                .claim("type", "SECOND")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
