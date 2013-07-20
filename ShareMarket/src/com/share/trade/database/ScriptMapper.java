package com.share.trade.database;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ScriptMapper {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	@Persistent
	private String wtcher_script;
	@Persistent
	private String broker_script;
	@Persistent
	private String tradeQuantity;
	@Persistent
	private String isActive;
	@Persistent
	private Date updatedDate;
	
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getWtcher_script() {
		return wtcher_script;
	}
	public void setWtcher_script(String wtcher_script) {
		this.wtcher_script = wtcher_script;
	}
	public String getBroker_script() {
		return broker_script;
	}
	public void setBroker_script(String broker_script) {
		this.broker_script = broker_script;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getTradeQuantity() {
		return tradeQuantity;
	}
	public void setTradeQuantity(String tradeQuantity) {
		this.tradeQuantity = tradeQuantity;
	}
	
	
	

}
