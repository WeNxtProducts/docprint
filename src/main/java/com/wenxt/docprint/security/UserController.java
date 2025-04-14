package com.wenxt.docprint.security;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	 @Autowired
	 private StaticJwtGenerator staticJwtGenerator;

//	@PostMapping("/addNewUser") 
//	public String addNewUser(@RequestBody UserInfo userInfo) { 
//		return service.addUser(userInfo); 
//	} 
//	
	@PostMapping("/generateToken")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
				authRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest);
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}


	    @GetMapping("/static-token")
	    public String getStaticToken() {
	    	JSONObject response = new JSONObject();
	    	
	    	response.put("status", "SUCCESS");
	    	response.put("Data", staticJwtGenerator.getStaticToken());
	        return response.toString();
	    }
	    
	    @PostMapping("/customToken")
	    public String getCustomToken(@RequestBody CustomTokenRequest customTokenRequest) {
//	    	System.out.println("CUSTOM AUTH");
	        return staticJwtGenerator.generateCustomToken(customTokenRequest.getUserName(), customTokenRequest.getEmail(), customTokenRequest.getMobileNumber());
	    }


}
