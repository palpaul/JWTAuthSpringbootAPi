package com.jwtdemo1.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwtdemo1.demo.jwtUtil.JwtUtil;
import com.jwtdemo1.demo.services.MyuserDetailsService;

/*this class is responsible for like
after creating the jwt we need to intercept with everyrequest along with jwt filter  okk

validating the existing jwt token whether its valid or not
*/
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	@Autowired
	private MyuserDetailsService myuserDetailsService;
	@Autowired
	private JwtUtil jwtUtil;
	
	
	//examine the incoming request and check if the jet token is valid or not
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String token = null;
		//start with Bareer space 
		if(authorizationHeader!=null&& authorizationHeader.startsWith("Bearer ")) {
			token =authorizationHeader.substring(7);
			username= jwtUtil.getUsernameFromToken(token);
		}
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails = this.myuserDetailsService.loadUserByUsername(username);
			//calling validateToken from jwtUtil
			if(jwtUtil.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
