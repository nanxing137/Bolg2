package net.bittreasury.repository;

import io.swagger.models.auth.In;
import net.bittreasury.entity.Article;
import net.bittreasury.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	List<Article> findArticlesByAuthor(User author);

	Integer countByClassificationEqualsAndLabelsContains(Long classificationId, Collection labels);

	Integer countByLabelsContains(Collection labels);
}
