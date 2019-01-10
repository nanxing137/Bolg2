package net.bittreasury.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.alibaba.fastjson.JSONObject;

/**
 * 附件</br>
 * 暂时用不到
 * 
 * @author Thornhill
 *
 */
@Entity
public class Annex implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(targetEntity = User.class, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, fetch = FetchType.EAGER, optional = true)
	private User author;
	@ManyToOne(targetEntity = Article.class, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, fetch = FetchType.EAGER, optional = true)
	private Article article;
	@Column
	private String address;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

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
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * @param article
	 *            the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}
