package net.bittreasury.controller.RESTful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bittreasury.entity.Label;
import net.bittreasury.service.LabelService;

@RestController
public class LabelAPIController {
	
	@Autowired
	private LabelService labelService;
	
	@RequestMapping("api/getAllLebels")
	List<Label> getAllLebels(){
		List<Label> all = labelService.getAll();
		return all;
	}
	
	
}
