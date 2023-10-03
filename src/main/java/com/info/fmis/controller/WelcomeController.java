package com.info.fmis.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

	private Logger LOGGER = Logger.getLogger(WelcomeController.class);

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}
}
