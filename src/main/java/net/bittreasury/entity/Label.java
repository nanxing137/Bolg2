package net.bittreasury.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class Label implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private String name;

	@JSONField(serialize = false)
	@ManyToMany(cascade = CascadeType.ALL)
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
