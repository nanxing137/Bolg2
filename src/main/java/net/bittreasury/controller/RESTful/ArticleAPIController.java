package net.bittreasury.controller.RESTful;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import net.bittreasury.entity.Article;
import net.bittreasury.entity.Label;
import net.bittreasury.entity.User;
import net.bittreasury.service.ArticleService;
import net.bytebuddy.asm.Advice.Return;

@RestController
public class ArticleAPIController {

	private final Comparator<Article> hot = (t1, t2) -> {
		return (t1.getClickQuantity() > t2.getClickQuantity()) ? 1
				: (t1.getClickQuantity() == t2.getClickQuantity() ? 0 : -1);
	};
	private final Comparator<Article> time = (t1, t2) -> {
		return t1.getCreationDate().compareTo(t1.getCreationDate());
		// return (t1.getCreationDate().getTime() > t2.getCreationDate().getTime()) ? 1
		// : (t1.getCreationDate().equals(t2.getCreationDate()) ? 0 : -1);
	};

	@Autowired
	private ArticleService articleService;

	/**
	 * 使用统一的文章获取接口
	 * 
	 * @param sort
	 *            定义排序方式
	 * @param classification
	 *            定义分类
	 * @param labels
	 *            定义标签
	 * @return
	 */
	@RequestMapping("api/getArticles")
	public List<Article> getg(@RequestParam("sort") String sort, @RequestParam("classification") Long classificationId,
			@RequestParam("label") Long[] labels, @RequestParam("size") Long size, @RequestParam("page") Long page) {
		List<Article> findAllArticles = articleService.findAllArticles();
		/**
		 * 按热度或者按时间排序</br>
		 * 默认热度
		 */
		Comparator<Article> comparator = hot;
		switch (sort) {
		case "hot":
			comparator = hot;
			break;
		case "time":
			comparator = time;
			break;
		default:
			break;
		}
		findAllArticles.sort(comparator);
		Stream<Article> stream = findAllArticles.stream();
		Predicate<Article> finalPredicate = getFinalPredicate(classificationId, labels);
		stream.filter(finalPredicate);
		List<Article> collect = stream.collect(Collectors.toList());
		return collect;
	}

	/**
	 * 返回两个过滤器的交集
	 * 
	 * @param classificationId
	 * @param labels
	 * @return
	 */
	private Predicate<Article> getFinalPredicate(Long classificationId, Long[] labels) {
		Predicate<Article> predicateByClassfication = getPredicateByClassfication(classificationId);
		Predicate<Article> predicateByLabels = getPredicateByLabels(labels);
		Predicate<Article> and = predicateByClassfication.and(predicateByLabels);
		return and;
	}

	/**
	 * 判断标签是否全部包含
	 * 
	 * @param labels
	 * @return
	 */
	private Predicate<Article> getPredicateByLabels(Long[] labels) {
		Predicate<Article> predicate = (t) -> {
			// 如果没有传入任何lables，直接满足条件
			if (labels == null || labels.length == 0) {
				return true;
			}
			Stream<Long> map = t.getLabels().parallelStream().map((tt) -> {
				return tt.getId();
			});
			List<Long> collect = map.collect(Collectors.toList());
			if (collect.containsAll(Arrays.asList(labels))) {
				return true;
			}
			return false;
		};
		return predicate;
	}

	/**
	 * 判断分类是否正确
	 * 
	 * @param classificationId
	 * @return
	 */
	private Predicate<Article> getPredicateByClassfication(Long classificationId) {
		Predicate<Article> predicate = (t) -> {
			// 如果没有传入任何classificationId，直接满足条件
			if (classificationId == null && t.getClassification().getId().equals(classificationId)) {
				return true;
			}
			return false;

		};
		return predicate;
	}

	/**
	 * 增加文章时自动添加一无标题文章
	 * 
	 * @param folderId
	 * @return
	 */
	@RequestMapping("api/addArticle/{folderId}")
	public Article addArticle(@PathVariable("folderId") Long folderId) {
		Article article = new Article();

		User user = (User) SecurityUtils.getSubject().getPrincipal();
		// article.setAuthor(new User() {{setId(user.getId());}});
		article.setTitle("");
		article.setClickQuantity(0L);
		Article addArticle = articleService.addArticle(article);
		addArticle.setAuthor(user);
		article = articleService.addArticle(addArticle);
		return article;
	}

	@RequestMapping("api/updateArticle")
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
	@RequestMapping("api/getAllArticles/{size}/{page}")
	public List<Article> getAllArticles(@PathVariable("size") Integer size, @PathVariable("page") Integer page) {
		List<Article> findAllArticles = articleService.getArticlesByPage(size, page - 1);
		return findAllArticles;
	}

	@RequestMapping("api/getCount")
	public Double count() {
		Long sum = articleService.getSum();
		double ceil = Math.ceil(sum / 3);
		return ceil;
	}

	@RequestMapping("api/getArticle/{id}")
	public Article getArticle(@PathVariable("id") Long id) {
		Article articleById = articleService.getArticleById(id);
		return articleById;
	}

	@RequestMapping("api/getHotArticle")
	public List<Article> getHotArticle() {
		List<Article> hotArticles = articleService.getHotArticles();
		return hotArticles;
	}

	@RequestMapping("api/getArticleBySelf")
	public List<Article> getArticleBySelf() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<Article> getArticleByUser = articleService.getArticleByUser(user);
		return getArticleByUser;
	}

	@RequestMapping("api/getArticlesByTime")
	public List<Article> getArticlesByTime() {
		List<Article> articlesByTime = articleService.getArticlesByTime();
		return articlesByTime;
	}

	@RequestMapping("api/getArticlesByLabel/{id}")
	public List<Article> getArticlesByLabel(@PathVariable("id") Long id) {
		List<Article> articlesByLabel = articleService.getArticlesByLabelId(id);
		return articlesByLabel;
	}
}
