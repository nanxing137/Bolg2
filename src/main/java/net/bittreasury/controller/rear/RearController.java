package net.bittreasury.controller.rear;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class RearController {
	@RequestMapping("console")
	public String console() {
		return "console";
	}
}
