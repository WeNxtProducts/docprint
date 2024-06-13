package com.wenxt.docprint.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@Primary
public class UserInfoService implements UserDetailsService {

	@Autowired
	private UserMasterRepository userrrepo;

//	@Autowired(required = true)
//	private StringEncryptor encryptor;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<LM_MENU_USERS> userDetail = userrrepo.findByUserId(username);

		// Converting userDetail to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
	}
//
//	public String addUser(UserInfo userInfo) { 
//		userInfo.setPassword(encryptor.encrypt(userInfo.getPassword())); 
//		repository.save(userInfo); 
//		return "User Added Successfully"; 
//	} 

}
