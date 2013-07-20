package com.share.trade.vo;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ShareBucket {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	
	@Persistent
	private String sSymbol;
	@Persistent
	private String sQuantity;
	@Persistent
	private String sBuyPrice;
	@Persistent
	private String sIfactor;
	@Persistent
	private String sSfactor;
	
	@Persistent
	private Date sSoldDate;
	public Date getsSoldDate() {
		return sSoldDate;
	}

	public void setsSoldDate(Date sSoldDate) {
		this.sSoldDate = sSoldDate;
	}

	@Persistent
	private Date sBuyDate;
	
	public String getsSymbol() {
		return sSymbol;
	}

	public void setsSymbol(String sSymbol) {
		this.sSymbol = sSymbol;
	}

	public String getsQuantity() {
		return sQuantity;
	}

	public void setsQuantity(String sQuantity) {
		this.sQuantity = sQuantity;
	}

	public String getsBuyPrice() {
		return sBuyPrice;
	}

	public void setsBuyPrice(String sBuyPrice) {
		this.sBuyPrice = sBuyPrice;
	}

	public String getsIfactor() {
		return sIfactor;
	}

	public void setsIfactor(String sIfactor) {
		this.sIfactor = sIfactor;
	}

	public String getsSfactor() {
		return sSfactor;
	}

	public void setsSfactor(String sSfactor) {
		this.sSfactor = sSfactor;
	}

	public Date getsBuyDate() {
		return sBuyDate;
	}

	public void setsBuyDate(Date sBuyDate) {
		this.sBuyDate = sBuyDate;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

    
    
    

}
