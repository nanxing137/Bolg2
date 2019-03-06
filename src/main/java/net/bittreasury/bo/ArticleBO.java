package net.bittreasury.bo;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import net.bittreasury.comparator.ArticleCompareable;
import net.bittreasury.entity.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ArticleBO implements ArticleCompareable {

	private Long id;

	private String title;

	private String author;
	// 标签

	private Set<LabelsBO> labels;
	// 分类

	private ClassificationBO classification;
	// 简介

	private String introduction;


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

	private Set<Annex> annexs = new HashSet<>();

	public ArticleBO() {
	}

	public ArticleBO(Article article) {
		this.setId(article.getId());
		this.setTitle(article.getTitle());
		this.setAuthor(article.getAuthor().getNickName());
		this.setLabels(article.getLabels().stream().map(LabelsBO::new).collect(Collectors.toSet()));
		this.setClassification(new ClassificationBO(article.getClassification()));
		this.setIntroduction(article.getIntroduction());
		this.setContent(article.getContent());
		this.setClickQuantity(article.getClickQuantity());
		this.setCreationDate(article.getCreationDate());
		this.setModifiedDate(article.getModifiedDate());
		this.setVersion(article.getVersion());
	}


	@Override
	public String toString() {
		String jsonString = JSON.toJSONString(this);
		return jsonString;
	}
}
