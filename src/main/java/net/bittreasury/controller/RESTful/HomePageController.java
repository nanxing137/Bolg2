package net.bittreasury.controller.RESTful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bittreasury.entity.Article;
import net.bittreasury.service.ArticleService;

@RestController
public class HomePageController {

	@Autowired
	private ArticleService articleService;

	@RequestMapping("api/articles")
	public List<Article> articles() {
		List<Article> allArticles = articleService.findAllArticles();
		System.out.println(allArticles);
		return allArticles;
	}
}
