package com.jwtdemo1.demo.jwtController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtdemo1.demo.jwtUtil.JwtUtil;
import com.jwtdemo1.demo.model.JwtRequest;
import com.jwtdemo1.demo.model.JwtResponse;
import com.jwtdemo1.demo.services.MyuserDetailsService;

@RestController
public class JwtController {
		@Autowired
		private AuthenticationManager authenticationManager; 
		@Autowired
		private MyuserDetailsService myuserDetailsService;
		@Autowired
		private JwtUtil jwtUtil;
		
		@CrossOrigin
		@PostMapping("/genToken")
		public ResponseEntity<?>createAuthenticationToekn(@RequestBody JwtRequest jwtRequest)throws Exception {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
				
			} catch (BadCredentialsException e) {
				throw new Exception("Incorrect username or password", e);
			}
			final UserDetails userDetails = myuserDetailsService
					.loadUserByUsername(jwtRequest.getUsername());
			final String jwtToken =jwtUtil.generateToken(userDetails);
			System.out.println("Jwt Token is generated : "+jwtToken);
			return ResponseEntity.ok(new JwtResponse(jwtToken));
		}
		
		
		@GetMapping("/hello")
		public String hello() {
			return "Hell,Hey Welcome";
		}

}
