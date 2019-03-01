package net.bittreasury.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import net.bittreasury.comparator.ArticleCompareable;
import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;

/**
 * 文章表
 *
 * @author Thornhill
 */
@Entity
public class Article implements Serializable, ArticleCompareable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private String title;
	@ManyToOne(targetEntity = User.class, cascade = {CascadeType.PERSIST,
			CascadeType.MERGE}, fetch = FetchType.EAGER, optional = true)
	private User author;
	// 标签
	@ManyToMany(mappedBy = "articles", fetch = FetchType.EAGER)
	private Set<Label> labels;
	// 分类
	@ManyToOne(targetEntity = Classification.class, cascade = {CascadeType.PERSIST,
			CascadeType.MERGE}, fetch = FetchType.EAGER, optional = true)
	private Classification classification;
	// 简介
	@Column
	private String introduction;

	@Column
	@Type(type = "text")
	private String content;

	// 点击量
	@Column
	private Long clickQuantity = 0L;

	// 创建日期
	@Column
	private Date creationDate = new Date();
	// 修改日期
	@Column
	private Date modifiedDate;
	@Version
	private Long version;
	// 附件列表
	@OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "article")
	private Set<Annex> annexs = new HashSet<>();

	@Override
	public String toString() {
		String jsonString = JSON.toJSONString(this);
		return jsonString;
	}

	public Article() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the annexs
	 */
	public Set<Annex> getAnnexs() {
		return annexs;
	}

	/**
	 * @param annexs the annexs to set
	 */
	public void setAnnexs(Set<Annex> annexs) {
		this.annexs = annexs;
	}

	/**
	 * @return the clickQuantity
	 */
	public Long getClickQuantity() {
		return clickQuantity;
	}

	/**
	 * @param clickQuantity the clickQuantity to set
	 */
	public void setClickQuantity(Long clickQuantity) {
		this.clickQuantity = clickQuantity;
	}

	/**
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return the classification
	 */
	public Classification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	/**
	 * @return the labels
	 */
	public Set<Label> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

}
