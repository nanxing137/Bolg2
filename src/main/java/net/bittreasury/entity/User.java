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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户表
 * 
 * @author Thornhill
 *
 */
@Entity
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Long id;
	// 注解为标记为唯一键,且非空
	// 用户使用userName登陆
	@Column(unique = true, nullable = false)
	private String username;
	// 昵称
	@Column
	private String nickName;
	@Column(nullable = false)
	private String password;

	@Column
	private String address;
	@Column
	private String email;
	@Column
	private String phoneNumber;
	// 个性签名
	@Column
	private String signature;
	// 个人说明
	@Column
	private String personalStatement;
	// 注册日期
	@Column
	private Date registratioDate = new Date();
	// 最后活跃
	@Column
	private Date lastActive;

	// 用户登陆记录
	// 因为不可能删除用户，也不会删除登陆记录
	// 所以cascade可以为ALL
	@JSONField(serialize = false)
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "user")
	private Set<LandingHistory> landingHistories = new HashSet<>();
	// 文章
	@JSONField(serialize = false)
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "author")
	private Set<Article> articles = new HashSet<>();

	// 附件列表
	// @JsonIgnore
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "author")
	private Set<Annex> annexs = new HashSet<>();

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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the personalStatement
	 */
	public String getPersonalStatement() {
		return personalStatement;
	}

	/**
	 * @param personalStatement
	 *            the personalStatement to set
	 */
	public void setPersonalStatement(String personalStatement) {
		this.personalStatement = personalStatement;
	}

	/**
	 * @return the registratioDate
	 */
	public Date getRegistratioDate() {
		return registratioDate;
	}

	/**
	 * @param registratioDate
	 *            the registratioDate to set
	 */
	public void setRegistratioDate(Date registratioDate) {
		this.registratioDate = registratioDate;
	}

	/**
	 * @return the lastActive
	 */
	public Date getLastActive() {
		return lastActive;
	}

	/**
	 * @param lastActive
	 *            the lastActive to set
	 */
	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	/**
	 * @return the landingHistories
	 */
	public Set<LandingHistory> getLandingHistories() {
		return landingHistories;
	}

	/**
	 * @param landingHistories
	 *            the landingHistories to set
	 */
	public void setLandingHistories(Set<LandingHistory> landingHistories) {
		this.landingHistories = landingHistories;
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

	/**
	 * @return the annexs
	 */
	public Set<Annex> getAnnexs() {
		return annexs;
	}

	/**
	 * @param annexs
	 *            the annexs to set
	 */
	public void setAnnexs(Set<Annex> annexs) {
		this.annexs = annexs;
	}

}
