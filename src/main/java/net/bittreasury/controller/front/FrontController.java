package net.bittreasury.controller.front;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 改造这个类</br>
 * 改成themyleaf
 * @author Thornhill
 *
 */
@Controller
public class FrontController {

	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("login")
	public String login() {
		return "login";
	}
	@RequestMapping("timeline")
	public String timeline() {
		return "timeline";
	}

	@PostMapping("login")
	public String login(String username, String password, String vcode,
			@RequestParam(defaultValue = "false") Boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		SecurityUtils.getSubject().login(token);
		return "redirect:index";
	}

	@RequestMapping("content/{id}")
	public String context() {
		return "content";
	}

	@RequestMapping("logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return index();
	}

	@RequestMapping("regest")
	public String regest() {
		return "regest";
	}
}
