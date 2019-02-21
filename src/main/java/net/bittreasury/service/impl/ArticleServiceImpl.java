/**
 * 
 */
package net.bittreasury.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.bittreasury.entity.Article;
import net.bittreasury.entity.Label;
import net.bittreasury.entity.User;
import net.bittreasury.repository.ArticleRepository;
import net.bittreasury.repository.LabelRepository;
import net.bittreasury.service.ArticleService;

/**
 * @author Thornhill
 *
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Override
	// @Cacheable("findAllArticles")
	public List<Article> findAllArticles() {
		List<Article> articles = articleRepository.findAll();
		return articles;
	}

	@Override
	// @Cacheable(value = "addArticle", key = "#p0.getId()")
	@CacheEvict(value = "getArticleByFolder", key = "#p0.getFolder().getId()")
	public Article addArticle(Article article) {
		Article save = articleRepository.save(article);
		return save;
	}

	@Override
	// @Cacheable("getArticleById")
	public Article getArticleById(Long id) {
		Article one = articleRepository.getOne(id);
		one.setClickQuantity(one.getClickQuantity() + 1);
		return one;
	}

	@Override
	public List<Article> getArticleByUser(User user) {
		List<Article> findArticlesByAuthor = articleRepository.findArticlesByAuthor(user);
		return findArticlesByAuthor;
	}

	@Override
	// @Cacheable("getArticlesByPage")
	public List<Article> getArticlesByPage(Integer size, Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Article> findAll = articleRepository.findAll(pageable);
		List<Article> content = findAll.getContent();
		return content;
	}

	@Override
	/**
	 * 暂时更新title，context
	 */
	// @Cacheable("getArticleById")
	@CachePut(value = "getArticleById", key = "#p0.getId()")
	@CacheEvict(value = "getArticleByFolder", key = "#result.getFolder().getId()")
	public Article updateArticle(Article article) {
		Article articleById = articleRepository.getOne(article.getId());
		articleById.setTitle(article.getTitle());
		articleById.setContent(article.getContent());
		Article save = articleRepository.save(articleById);
		return save;
	}

	@Override
	public Long getSum(Long classificationId,Long[] labels) {
//		List<Long> longs = Arrays.asList(labels);
//		long count;
//		List<Article> all = articleRepository.findAll();
//		all.stream().filter()
//		if ("".equals(classificationId))
//			count = articleRepository.countByLabelsContains(longs);
//		else
//			count = articleRepository.countByClassificationEqualsAndLabelsContains(classificationId,longs);
		return 0l;
	}

	@Override
	public List<Article> getHotArticles() {
		/**
		 * 这里之后把id排序改成点击量排序
		 */
		Sort sort = new Sort(Sort.Direction.DESC, "clickQuantity");
		Pageable pageable = PageRequest.of(0, 3, sort);
		Page<Article> findAll = articleRepository.findAll(pageable);
		List<Article> content = findAll.getContent();
		return content;
	}

	@Override
	public List<Article> getArticlesByTime() {
		Sort sort = new Sort(Sort.Direction.DESC, "creationDate");
		List<Article> findAll = articleRepository.findAll(sort);
		return findAll;
	}

	@Override
	public List<Article> getArticlesByLabelId(Long labelId) {
		Label one = labelRepository.getOne(labelId);
		return new ArrayList<>(one.getArticles());
	}

}
