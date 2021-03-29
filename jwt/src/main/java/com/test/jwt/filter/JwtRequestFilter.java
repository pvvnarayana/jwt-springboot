package com.test.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.test.jwt.service.JwtUserDetailsService;
import com.test.jwt.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

			final String requestTokenHeader = request.getHeader("Authorization");
			String userName=null;
			String token=null;
			
			if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
				token = requestTokenHeader.substring(7);
				
				try {
					userName = jwtTokenUtil.getUsernameFromToken(token);
					
				}catch(IllegalArgumentException e) {
					System.out.println(e);
				}catch(ExpiredJwtException e) {
					System.out.println(e);
				}
			}
			else {
				System.err.println("invalid jwt token");
			}
			
			if(userName !=null && SecurityContextHolder.getContext().getAuthentication() ==null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
				
				if(jwtTokenUtil.validateToken(token, userDetails)) {
					
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);
	}

	
}
