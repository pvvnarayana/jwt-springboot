package com.test.jwt.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username : "+username);
//		if("admin".equals(username)) {
//			return new User(username, "password", new ArrayList<>());
//		}
//		else
//			throw new UsernameNotFoundException("Invalid Credentials");
		return new User("admin","password",new ArrayList<>());
	}

}
