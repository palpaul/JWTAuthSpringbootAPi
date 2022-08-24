package com.jwtdemo1.demo.Mycontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Helloo {
	@GetMapping("/welcome")
	public String heyWelcome() {
		String txt ="Hey ";
		txt+="welcome";
		return txt;
	}
	
}
