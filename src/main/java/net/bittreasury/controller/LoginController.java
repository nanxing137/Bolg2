package net.bittreasury.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.bittreasury.entity.User;

//@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password, String vcode, Boolean rememberMe) {
		System.out.println(username);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		SecurityUtils.getSubject().login(token);
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String home() {
		Subject subject = SecurityUtils.getSubject();
		User principal = (User) subject.getPrincipal();

		return "home";
	}
}
