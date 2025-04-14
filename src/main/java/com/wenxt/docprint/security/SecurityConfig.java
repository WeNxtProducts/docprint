package com.wenxt.docprint.security;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
	@SuppressWarnings("removal")
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
				.requestMatchers("/auth/**").permitAll()
				.and()
				.authorizeHttpRequests() 
				.requestMatchers("/ltQuoteDetails/**").permitAll()
				.and()
				.authorizeHttpRequests().requestMatchers("/claimBfcry/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/claimchrgs/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/ltclaim/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/ccdtls/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/claimest/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/claimpymt/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/docToDoList/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/claimPaid/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/polBeneficiary/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/polBroker/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/polCharge/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/polDiscLoad/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/polEmpCover/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/polEmployee/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/policy/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/medexFeeDtl/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/policy/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/receiptHdr/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/receiptProcess/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/condition/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/task/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/deposit/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/pymntDetails/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/receiptDetail/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/endtCover/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/endtBenf/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/endtPol/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/loan/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/loanRepymnt/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/riPremAllocDtl/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/riPremAllocHead/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/surrMattValues/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/riEmpDtl/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/LtmedexFeeDtl/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/riEmployee/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/riEmpCvrDiscLoad/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/ProdMaster/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/applCharges/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/wdrasetup/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/prodFactor/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/intMaster/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/annuityMaster/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/prodTaxSetup/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/taxSlabSetup/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/taxSlabRate/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/vestingScale/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/pensionPurchase/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/summary/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/yearEndProHdr/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/yearEndProDet/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/wdraProHdr/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/matProHeader/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/wdraDetails/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/chargeDtls/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/pensPymtDtls/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/ltQuoteDetails/**").authenticated()
				.and()
				.authorizeHttpRequests().requestMatchers("/ltQquotAssuredDtls/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/ltQuoteBeneficiary/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/ltQquotDiscLoad/**").authenticated() 
				.and()
				.authorizeHttpRequests().requestMatchers("/docList/**").authenticated() 
				.and()
				.sessionManagement() 
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
				.and() 
				.authenticationProvider(authenticationProvider()) 
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) 
				.build(); 
	} 

	// Password Encoding 
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // Jasypt handles encryption and decryption
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider(userDetailsService());
        return authenticationProvider;
    }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
		return config.getAuthenticationManager(); 
	} 


} 

