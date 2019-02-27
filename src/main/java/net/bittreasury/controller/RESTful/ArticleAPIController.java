package net.bittreasury.controller.RESTful;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	// 线程级别的缓存
	// 注意这里有待改进
	private ThreadLocal<List<Article>> articlesCache = new ThreadLocal<>();

	private final Comparator<Integer> desc = (t1, t2) -> {
		return (t1 > t2) ? -1 : ((t1 == t2) ? 0 : 1);
	};

	private final Comparator<Article> hot = (t1, t2) -> {
		int compare = Long.compare(t1.getClickQuantity(), t2.getClickQuantity());
		return (t1.getClickQuantity() > t2.getClickQuantity()) ? 1
				: (t1.getClickQuantity() == t2.getClickQuantity() ? 0 : -1);
	};
	// private final Comparator<Article> time = (t1, t2) -> {
	// return t1.getCreationDate().compareTo(t2.getCreationDate());
	// };
	private final Comparator<Article> time = (t1, t2) -> {
		return t1.getCreationDate().compareTo(t2.getCreationDate()) * -1;
	};
	private final Comparator<Article> year = (t1, t2) -> {
		int year1 = t1.getCreationDate().getYear();
		int year2 = t2.getCreationDate().getYear();
		return year1 > year2 ? 1 : year1 == year2 ? 0 : -1;
	};
	private final BiFunction<Predicate<Article>, Predicate<Article>, Predicate<Article>> andOP = (t1, t2) -> {
		return t1.and(t2);
	};
	@Autowired
	private ArticleService articleService;

	/**
	 * 使用统一的文章获取接口
	 * 
	 * @param sort
	 *            定义排序方式
	 * @param classificationId
	 *            定义分类
	 * @param labelsSt
	 *            定义标签
	 * @return
	 */
	@RequestMapping("api/getArticles")
	public List<Article> getg(@RequestParam(value = "sort", defaultValue = "hot") String sort,
			@RequestParam(value = "classification", defaultValue = "") Long classificationId,
			@RequestParam(value = "label", defaultValue = "") String  labelsSt, @RequestParam("size") Long size,
			@RequestParam("page") Long page) {
		Long[] labels=("".equals(labelsSt))?null:(Long[]) Arrays.asList(labelsSt.split(",")).stream().map((t) -> (Long) Long.parseLong(t)).toArray();


		// List<Article> findAllArticles = articleService.findAllArticles();
		List<Article> findAllArticles = getAllArticles();

		/**
		 * 按热度或者按时间排序</br>
		 * 默认热度
		 */
		Comparator<Article> comparator;
		switch (sort) {
		case "hot":
			comparator = hot;
			break;
		case "time":
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
		List<Article> collect = stream.collect(Collectors.toList());
		return collect;
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
	public Map<Integer, Map<Integer, Set<Article>>> timeLine(@RequestParam("page") Long page,
			@RequestParam("size") Long size) {
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
		Stream<Integer> paginationStream = getPaginationStream(keySetStream, page, size);
		List<Integer> keyList = paginationStream.collect(toList());
		Map<Integer, Map<Integer, Set<Article>>> result = new TreeMap<>(desc);
		for (Integer integer : keyList) {
			Set<Article> list = collect.get(integer);
			TreeMap<Integer, Set<Article>> node = list.stream().collect(groupingBy((Article t) -> {
				return t.getCreationDate().getMonth();
			}, () -> {
				return new TreeMap<Integer, Set<Article>>(desc);
			}, toSet()));
			Set<Integer> keySet = node.keySet();
			Set<Article> temp;
			for (Integer integer2 : keySet) {
				temp = new TreeSet<>(time);
				temp.addAll(node.get(integer2));
				node.put(integer2, temp);
			}
			// result.put(integer, map);
			result.put(integer, node);
		}
		return result;
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
	private Predicate<Article> getFinalPredicate(Long classificationId, Long[] labels) {
		Predicate<Article> predicateByClassfication = getPredicateByClassfication(classificationId);
		Predicate<Article> predicateByLabels = getPredicateByLabels(labels);
		Predicate<Article> and = andOP.apply(predicateByClassfication, predicateByLabels);
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
	 *
	 * @param classificationId
	 * @param labelsSt
	 * @return
	 */
	@RequestMapping("api/getCount")
<<<<<<< HEAD
	public Double count() {
		Long sum = articleService.getSum();
		double ceil = Math.ceil(sum / 3);
		return ceil;
=======
	public Long count(@RequestParam(value = "classification", defaultValue = "") Long classificationId,
	                  @RequestParam(value = "label", defaultValue = "") String  labelsSt) {
		Long[] labels = new Long[0];
		if (!"".equals(labelsSt)) {
			labels = Arrays.asList(labelsSt.split(",")).stream().map((t) -> (Long) Long.parseLong(t)).toArray(Long[]::new);
		}
		List<Article> allArticles = getAllArticles();
		Stream<Article> stream = allArticles.stream();
		Predicate<Article> finalPredicate = getFinalPredicate(classificationId, labels);
		long sum = stream.filter(finalPredicate).count();
//		double ceil = Math.ceil(sum / 3);
		return sum;
>>>>>>> 7bc4d6019e065203325d211d8c0c6b7396596724
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
		if (articlesCache.get() == null || articlesCache.get().isEmpty()) {
			List<Article> findAllArticles = articleService.findAllArticles();
			articlesCache.set(findAllArticles);
		}
		return articlesCache.get();
	}
}
