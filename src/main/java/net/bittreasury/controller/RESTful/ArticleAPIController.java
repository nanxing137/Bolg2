package net.bittreasury.controller.RESTful;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import net.bittreasury.entity.Article;
import net.bittreasury.entity.Label;
import net.bittreasury.entity.User;
import net.bittreasury.service.ArticleService;

@RestController
public class ArticleAPIController {

	@Autowired
	private ArticleService articleService;

	/**
	 * 增加文章时自动添加一无标题文章
	 * 
	 * @param folderId
	 * @return
	 */
	@RequestMapping("API/addArticle/{folderId}")
	public Article addArticle(@PathVariable("folderId") Long folderId) {
		Article article = new Article();

		User user = (User) SecurityUtils.getSubject().getPrincipal();
		//
		// article.setAuthor(new User() {{setId(user.getId());}});
		article.setTitle("");
		article.setClickQuantity(0L);
		Article addArticle = articleService.addArticle(article);
		addArticle.setAuthor(user);
		article = articleService.addArticle(addArticle);
		return article;
	}

	@RequestMapping("API/updateArticle")
	public Article updateArticle(Article article) {
		Article updateArticle = articleService.updateArticle(article);
		return updateArticle;
	}

	/**
	 * 翻页查询
	 * 
	 * @param size
	 * @param page
	 * @return
	 */
	@RequestMapping("API/getAllArticles/{size}/{page}")
	public List<Article> getAllArticles(@PathVariable("size") Integer size, @PathVariable("page") Integer page) {
		List<Article> findAllArticles = articleService.getArticlesByPage(size, page - 1);
		return findAllArticles;
	}

	@RequestMapping("API/getCount")
	public Double count() {
		Long sum = articleService.getSum();
		double ceil = Math.ceil(sum / 3);
		return ceil;
	}

	@RequestMapping("API/getArticle/{id}")
	public Article getArticle(@PathVariable("id") Long id) {
		Article articleById = articleService.getArticleById(id);
		return articleById;
	}

	@RequestMapping("API/getHotArticle")
	public List<Article> getHotArticle() {
		List<Article> hotArticles = articleService.getHotArticles();
		return hotArticles;
	}

	@RequestMapping("API/getArticleBySelf")
	public List<Article> getArticleBySelf() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<Article> getArticleByUser = articleService.getArticleByUser(user);
		return getArticleByUser;
	}

	@RequestMapping("API/getArticlesByTime")
	public List<Article> getArticlesByTime() {
		List<Article> articlesByTime = articleService.getArticlesByTime();
		return articlesByTime;
	}
	
	@RequestMapping("API/getArticlesByLabel/{id}")
	public List<Article> getArticlesByLabel(@PathVariable("id") Long id){
		List<Article> articlesByLabel = articleService.getArticlesByLabelId(id);
		return articlesByLabel;
	}
}
