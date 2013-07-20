package com.share.trade.database;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BrokerDetail {

		
	@PrimaryKey
	@Persistent
	private String name;
	@Persistent
	private String userid;
	@Persistent
	private String pwd1;
	@Persistent
	private String pwd2;
	
	
	@Persistent
	private Date updatedDate;
	
	public BrokerDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public BrokerDetail(String name, String userid, String pwd1, String pwd2,
			Date updatedDate) {
		super();
		this.name = name;
		this.userid = userid;
		this.pwd1 = pwd1;
		this.pwd2 = pwd2;
		this.updatedDate = updatedDate;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPwd1() {
		return pwd1;
	}
	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}
	public String getPwd2() {
		return pwd2;
	}
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	
}
