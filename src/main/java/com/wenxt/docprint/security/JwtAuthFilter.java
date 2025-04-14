package com.wenxt.docprint.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// This class helps us to validate the generated jwt token 
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserInfoService userDetailsService;

	@Autowired
	private StaticJwtGenerator staticJwtGenerator;

	@Autowired
	private InvalidatedTokenRepository invalidatedTokenRepository;
	
    private String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		String authHeader = request.getHeader("Authorization");
//
//		String token = null;
//		String username = null;
//		try {
//			if (authHeader != null && authHeader.startsWith("Bearer")) {
//				token = authHeader.substring(7);
//				username = jwtService.extractUsername(token);
//			}
//
//			if (token != null && invalidatedTokenRepository.existsByToken(token)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP 401 Unauthorized status
//                response.getWriter().write("JWT token has been invalidated.");
//                return;
//            }
//			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//				if (jwtService.validateToken(token, userDetails)) {
//					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
//							null, userDetails.getAuthorities());
//					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//					SecurityContextHolder.getContext().setAuthentication(authToken);
//				}
//			}
//		} catch (Exception e) {
//			// JWT token has expired
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP 401 Unauthorized status
//			response.getWriter().write("JWT token has expired");
//			return;
//		}
//
//		filterChain.doFilter(request, response);
//	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);

			try {
				if (isStaticToken(token)) {
					System.out.println("IN");
					authenticateStaticToken(token);
				} else if (isSecondTokenType(token)) {
					authenticateSecondToken(token);
				} else {
					String username = null;
					try {
						if (authHeader != null && authHeader.startsWith("Bearer")) {
							token = authHeader.substring(7);
							username = jwtService.extractUsername(token);
						}

						if (token != null && invalidatedTokenRepository.existsByToken(token)) {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP 401 Unauthorized
																						// status
							response.getWriter().write("JWT token has been invalidated.");
							return;
						}
						if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
							UserDetails userDetails = userDetailsService.loadUserByUsername(username);
							if (jwtService.validateToken(token, userDetails)) {
								UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
										userDetails, null, userDetails.getAuthorities());
								authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
								SecurityContextHolder.getContext().setAuthentication(authToken);
							}
						}
					} catch (Exception e) {
						// JWT token has expired
						e.printStackTrace();
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP 401 Unauthorized status
						response.getWriter().write("JWT token has expired");
						return;
					}

					filterChain.doFilter(request, response);
				}
			} catch (Exception ex) {
				// If all fail, return 401 or pass the request as unauthenticated
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean isStaticToken(String token) {
//		System.out.println(token);
//		System.out.println(staticJwtGenerator.getStaticToken());
//		return token.equals(staticJwtGenerator.getStaticToken());
		try {
	        Claims claims = Jwts.parserBuilder()
	        	    .setSigningKey(secretKey)
	        	    .build()
	        	    .parseClaimsJws(token)
	        	    .getBody();
	        return "static".equals(claims.get("type"));
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return false;
	    }
	}

	private void authenticateStaticToken(String token) {
		System.out.println("IN");
		// Set authentication manually for static token
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("staticUser", null,
				List.of(new SimpleGrantedAuthority("ROLE_SYSTEM")));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private boolean isSecondTokenType(String token) {
	    try {
	        Claims claims = Jwts.parserBuilder()
	        	    .setSigningKey(secretKey)
	        	    .build()
	        	    .parseClaimsJws(token)
	        	    .getBody();
	        return "SECOND".equals(claims.get("type"));
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return false;
	    }
	}
	
	private void authenticateSecondToken(String token) {
	    Claims claims = Jwts.parser()
	            .setSigningKey(secretKey) // replace with actual secret
	            .parseClaimsJws(token)
	            .getBody();

	    String username = claims.getSubject(); // e.g. "secondUser"
	    String role = claims.get("role", String.class); // optional

	    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	            username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
