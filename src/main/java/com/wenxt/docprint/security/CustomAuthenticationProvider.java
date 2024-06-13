package com.wenxt.docprint.security;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
    @Autowired
    private StringEncryptor encryptor;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException, BadCredentialsException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String pass = user.getPassword();

        if (password.equals(encryptor.decrypt(pass)) && username.equals(user.getUsername())) {
            // Authentication successful
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            // Authentication failed
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}