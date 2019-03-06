package net.bittreasury.controller.RESTful;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.bittreasury.bo.ArticleBO;
import net.bittreasury.comparator.ArticleCompareable;
import net.bittreasury.entity.Label;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.bittreasury.entity.Article;
import net.bittreasury.entity.User;
import net.bittreasury.service.ArticleService;

@RestController
public class ArticleAPIController {


	private final Comparator<Integer> desc = (t1, t2) -> {
		return (t1 > t2) ? -1 : ((t1 == t2) ? 0 : 1);
	};
	private final Comparator<String> strDesc = (t11, t21) -> {

		Integer t1 = Integer.parseInt(t11);
		Integer t2 = Integer.parseInt(t21);

		return (t1 > t2) ? -1 : ((t1 == t2) ? 0 : 1);
	};

	private final Comparator<ArticleCompareable> hot = (t1, t2) -> {
		int compare = Long.compare(t1.getClickQuantity(), t2.getClickQuantity());
		return Long.compare(t2.getClickQuantity(), t1.getClickQuantity());
	};

	private final Comparator<ArticleCompareable> time = (t1, t2) -> {
		return t2.getCreationDate().compareTo(t1.getCreationDate());
	};

	private final BiFunction<Predicate<Article>, Predicate<Article>, Predicate<Article>> andOP = (t1, t2) -> {
		return t1.and(t2);
	};
	@Autowired
	private ArticleService articleService;


	/**
	 * 使用统一的文章获取接口
	 *
	 * @param sort             定义排序方式
	 * @param classificationId 定义分类
	 * @param labelsSt         定义标签
	 * @return
	 */
	@RequestMapping("api/getArticles")
	public List<ArticleBO> getg(@RequestParam(value = "sort", defaultValue = "hot") String sort,
	                            @RequestParam(value = "classification", defaultValue = "") Long classificationId,
	                            @RequestParam(value = "label", defaultValue = "") String labelsSt, @RequestParam("size") Long size,
	                            @RequestParam("page") Long page) {
		long[] labels = ("".equals(labelsSt)) ? null : Arrays.asList(labelsSt.split(",")).stream().mapToLong(t -> Long.valueOf(t)).toArray();

		// List<Article> findAllArticles = articleService.findAllArticles();
		List<Article> findAllArticles = getAllArticles();

		/**
		 * 按热度或者按时间排序</br>
		 * 默认热度
		 */
		Comparator<ArticleCompareable> comparator;
		switch (sort) {
			case "Hot":
				comparator = hot;
				break;
			case "Time":
				comparator = time;
				break;
			default:
				comparator = hot;
				break;
		}
		// 先用并行流，出了问题检查这里
		Stream<Article> stream = findAllArticles.stream();
		Predicate<Article> finalPredicate = getFinalPredicate(classificationId, labels);
		/**
		 * 1. 通过条件过滤</br>
		 * 2. 根据条件排序</br>
		 * 3. 根据参数分页</br>
		 */
		stream = stream.filter(finalPredicate);
		stream = stream.sorted(comparator);
		// stream = stream.skip((page - 1) * size).limit(size);
		stream = getPaginationStream(stream, page, size);
		List<ArticleBO> collect = stream.map(ArticleBO::new).collect(Collectors.toList());

		return collect;
	}

	/**
	 * 使用统一的文章获取接口
	 *
	 * @param classificationId 定义分类
	 * @param labelsSt         定义标签
	 * @return
	 */
	@RequestMapping("api/getArticlesCount")
	public Integer getArticlesCount(
			@RequestParam(value = "classification", defaultValue = "") Long classificationId,
			@RequestParam(value = "label", defaultValue = "") String labelsSt,
			@RequestParam("size") Long size) {
		long[] labels = ("".equals(labelsSt)) ? null : Arrays.asList(labelsSt.split(",")).stream().mapToLong(t -> Long.valueOf(t)).toArray();

		// List<Article> findAllArticles = articleService.findAllArticles();
		List<Article> findAllArticles = getAllArticles();


		// 先用并行流，出了问题检查这里
		Stream<Article> stream = findAllArticles.stream();
		Predicate<Article> finalPredicate = getFinalPredicate(classificationId, labels);
		/**
		 * 1. 通过条件过滤</br>
		 * 2. 根据条件排序</br>
		 * 3. 根据参数分页</br>
		 */
		stream = stream.filter(finalPredicate);

		// stream = stream.skip((page - 1) * size).limit(size);
		long count = stream.count();
		double v = count / (double) size;

		return (int) Math.ceil(v);
	}

	/**
	 * 查询时间第page*size大的记录集</br>
	 * 注意：返回的List是同一年的</br>
	 *
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("api/timeline")
	public Map<String, Map<String, Set<ArticleBO>>> timeLine(@RequestParam("page") Long page,
	                                                         @RequestParam("size") Long size) {
		List<Article> allArticles = getAllArticles();
		Stream<ArticleBO> stream = allArticles.stream().map(ArticleBO::new);
		stream = stream.sorted(time);
		Map<String, Set<ArticleBO>> collect = stream.collect(groupingBy((ArticleBO t) -> String.valueOf(t.getCreationDate().getYear() + 1900), () -> new TreeMap<String, Set<ArticleBO>>(strDesc), toSet()));
		TreeSet<String> treeSet = new TreeSet<String>(strDesc);
		treeSet.addAll(collect.keySet());
		Stream<String> keySetStream = treeSet.stream();
		Stream<String> paginationStream = getPaginationStream(keySetStream, page, size);
		List<String> keyList = paginationStream.collect(toList());
		Map<String, Map<String, Set<ArticleBO>>> result = new TreeMap<String, Map<String, Set<ArticleBO>>>(strDesc);
		for (String s : keyList) {
			Set<ArticleBO> list = collect.get(s);
			TreeMap<String, Set<ArticleBO>> node = list.stream().collect(groupingBy((ArticleBO t) -> {
				return String.valueOf(t.getCreationDate().getMonth() + 1);
			}, () -> {
				return new TreeMap<String, Set<ArticleBO>>(strDesc);
			}, toSet()));
			Set<String> keySet = node.keySet();
			Set<ArticleBO> temp;
			for (String integer2 : keySet) {
				temp = new TreeSet<>(time);
				temp.addAll(node.get(integer2));
				node.put(integer2, temp);
			}
			// result.put(integer, map);
			result.put(s, node);
		}
		return result;
	}

	@RequestMapping("api/timelineCount")
	public Integer timelineCount(@RequestParam("size") Long size) {
		List<Article> allArticles = getAllArticles();
		Stream<Article> stream = allArticles.parallelStream();
		stream = stream.sorted(time);
		Map<Integer, Set<Article>> collect = stream.collect(groupingBy((Article t) -> {
			return t.getCreationDate().getYear();
		}, () -> {
			return new TreeMap<Integer, Set<Article>>(desc);
		}, toSet()));
		TreeSet<Integer> treeSet = new TreeSet<Integer>(desc);
		treeSet.addAll(collect.keySet());
		Stream<Integer> keySetStream = treeSet.stream();
		long count = keySetStream.count();
		double v = count / (double) size;

		return (int) Math.ceil(v);
	}

	/**
	 * 从流中获取分页</br>
	 * page从1开始的话，就需要这里-1
	 *
	 * @param stream
	 * @param page
	 * @param size
	 * @return
	 */
	private <T> Stream<T> getPaginationStream(Stream<T> stream, Long page, Long size) {
		stream = stream.skip(page * size);
		stream = stream.limit(size);
		return stream;
	}

	/**
	 * 返回两个过滤器的交集
	 *
	 * @param classificationId
	 * @param labels
	 * @return
	 */
	private Predicate<Article> getFinalPredicate(Long classificationId, long[] labels) {
		Predicate<Article> predicateByClassfication = getPredicateByClassfication(classificationId);
		Predicate<Article> predicateByLabels = getPredicateByLabels(labels);

		return predicateByClassfication.and(predicateByLabels);
	}

	/**
	 * 判断标签是否全部包含
	 *
	 * @param labels
	 * @return
	 */
	private Predicate<Article> getPredicateByLabels(long[] labels) {
		Predicate<Article> predicate = (t) -> {
			// 如果没有传入任何lables，直接满足条件
			if (labels == null || labels.length == 0) {
				return true;
			}
			Stream<Long> map = t.getLabels().stream().map(Label::getId);


			Set<Long> collect = map.collect(Collectors.toSet());
			Set<Long> labelsSet = new HashSet<>();
			for (long l : labels) {
				labelsSet.add(l);
			}
			if (collect.containsAll(labelsSet)) {
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
			if (classificationId == null || t.getClassification().getId().equals(classificationId)) {
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
	// /**
	// * 翻页查询
	// * 废弃了
	// *
	// * @param size
	// * @param page
	// * @return
	// */
	// @RequestMapping("api/getAllArticles/{size}/{page}")
	// public List<Article> getAllArticles(@PathVariable("size") Integer size,
	// @PathVariable("page") Integer page) {
	// List<Article> findAllArticles = articleService.getArticlesByPage(size, page -
	// 1);
	// return findAllArticles;
	// }

	/**
	 * @param classificationId
	 * @param labelsSt
	 * @return
	 */
	@RequestMapping("api/getCount")
	public Long count(@RequestParam(value = "classification", defaultValue = "") Long classificationId,
	                  @RequestParam(value = "label", defaultValue = "") String labelsSt) {
		long[] labels = new long[0];
		if (!"".equals(labelsSt)) {
			labels = Arrays.asList(labelsSt.split(",")).stream().mapToLong((t) -> Long.parseLong(t)).toArray();
		}
		List<Article> allArticles = getAllArticles();
		Stream<Article> stream = allArticles.stream();
		Predicate<Article> finalPredicate = getFinalPredicate(classificationId, labels);
		long sum = stream.filter(finalPredicate).count();
//		double ceil = Math.ceil(sum / 3);
		return sum;

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

	/**
	 * 相当于对于每个线程加缓存
	 *
	 * @return the allArticles
	 */
	private List<Article> getAllArticles() {
		List<Article> findAllArticles = articleService.findAllArticles();
		return findAllArticles;
	}
}
