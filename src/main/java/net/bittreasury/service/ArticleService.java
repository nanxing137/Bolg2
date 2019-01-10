package net.bittreasury.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import net.bittreasury.entity.Article;
import net.bittreasury.entity.Label;
import net.bittreasury.entity.User;

public interface ArticleService {

	List<Article> findAllArticles();

	Article addArticle(Article article);

	Article getArticleById(Long id);
	
	List<Article> getArticleByUser(User user);
	
	List<Article> getArticlesByPage(Integer size,Integer page);
	List<Article> getHotArticles();
	
	Article updateArticle(Article article);
	
	Long getSum();
	
	List<Article> getArticlesByTime();
	
	List<Article> getArticlesByLabelId(Long labelId);
	
	
}
