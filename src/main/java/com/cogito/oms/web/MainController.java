package com.cogito.oms.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class MainController {

	private Logger logger = LogManager.getLogger(MainController.class);

	@GetMapping
	String getDashboard() {
		return "admin/dashboard";
	}

	@GetMapping("/second")
	String getpage2() {
		return "vertical-navigation-primary-only";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	

	@RequestMapping("/lokee")
	public String test() {
		Subject currentUser = SecurityUtils.getSubject();
		return "second";
	}
}
