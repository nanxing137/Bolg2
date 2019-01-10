package net.bittreasury.controller.RESTful;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bittreasury.entity.Classification;
import net.bittreasury.service.ClassificationService;

/**
 * 分类
 * 
 * @author Thornhill
 *
 */
@RestController
public class ClassificationController {

	@Autowired
	private ClassificationService classificationService;

	@RequestMapping("/api/getAllClassification")
	public List<Classification> getAllClassification() {
		List<Classification> all = classificationService.getAll();
		return all;
		
	}

}
