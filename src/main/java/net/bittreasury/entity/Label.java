package net.bittreasury.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 标签</br>
 * 和文章是多对多
 * @author Thornhill
 *
 */
@Entity
public class Label implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private String name;

	@JSONField(serialize = false)
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "label_article", joinColumns = {
			@JoinColumn(name = "LABEL_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID") })
	private Set<Article> articles;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the articles
	 */
	public Set<Article> getArticles() {
		return articles;
	}

	/**
	 * @param articles
	 *            the articles to set
	 */
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

}
