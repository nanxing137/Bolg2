package net.bittreasury.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.alibaba.fastjson.JSON;

/**
 * 用户登陆记录表</br>
 * 暂时用不到</br>
 * 可以改造成统计游客
 * 
 * @author Thornhill
 *
 */
@Entity
public class LandingHistory implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(targetEntity = User.class, cascade = { CascadeType.REFRESH,
			CascadeType.MERGE }, fetch = FetchType.EAGER, optional = true)
	private User user;

	@Column
	private Date landingDate;

	@Column
	private String landingIP;

	@Override
	public String toString() {
		String jsonString = JSON.toJSONString(this);
		return jsonString;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the landingDate
	 */
	public Date getLandingDate() {
		return landingDate;
	}

	/**
	 * @param landingDate
	 *            the landingDate to set
	 */
	public void setLandingDate(Date landingDate) {
		this.landingDate = landingDate;
	}

	/**
	 * @return the landingIP
	 */
	public String getLandingIP() {
		return landingIP;
	}

	/**
	 * @param landingIP
	 *            the landingIP to set
	 */
	public void setLandingIP(String landingIP) {
		this.landingIP = landingIP;
	}
}
