package com.wenxt.docprint.security;
import javax.sql.DataSource;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { 
	
	
	@Autowired
	private JwtAuthFilter authFilter; 
	
	@Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("yourEncryptionPassword"); // Set your encryption password here
        return encryptor;
    }

	// User Creation 
	@Bean
	public UserDetailsService userDetailsService() { 
		return new UserInfoService(); 
	} 

	// Configuring HttpSecurity 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		return http.csrf().disable() 
				.authorizeHttpRequests() 
				.requestMatchers("/auth/generateToken").permitAll()
				.and()
				.authorizeHttpRequests() 
				.requestMatchers("/auth/addNewUser").permitAll()
				.and()
				.authorizeHttpRequests() 
				.requestMatchers("/swagger-ui/**").permitAll()
				.and()
				.authorizeHttpRequests() 
				.requestMatchers("/v3/**").permitAll()
				.and()
				.authorizeHttpRequests() 
				.requestMatchers("/testLog/**").permitAll()
				.and()
				.authorizeHttpRequests() 
				.requestMatchers("/auth/**").permitAll()
				.and()
				.authorizeHttpRequests().requestMatchers("/docprintsetup/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/reportss/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/dms/**").permitAll() 
				.and()
				.authorizeHttpRequests().requestMatchers("/docparam/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/RepotBuilder/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/common_ReportDtl/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/common_Report/**").authenticated()
				.and()
				.authorizeHttpRequests().requestMatchers("/report/**").permitAll() 
				.and()
				.sessionManagement() 
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
				.and() 
				.authenticationProvider(authenticationProvider()) 
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) 
				.build(); 
	} 
    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider(userDetailsService());
        return authenticationProvider;
    }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
		return config.getAuthenticationManager(); 
	} 

	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests(authorizeRequests ->
	                authorizeRequests
	                    .requestMatchers("/files/**").permitAll() // Allow access to files
	                    .anyRequest().authenticated() // Other requests need authentication
	            )
	            .csrf(csrf -> csrf.disable()); // Disable CSRF protection
	    }

	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().requestMatchers("/files/**"); // Ignore files path from security
	    }
	    
}