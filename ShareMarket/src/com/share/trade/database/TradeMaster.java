package com.share.trade.database;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TradeMaster {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long Key;
	@Persistent
	private Double todayProfitLoss;
	@Persistent
	private String tradedScriptNames;
	@Persistent
	private Date tradeDate;
	public Long getKey() {
		return Key;
	}
	public void setKey(Long key) {
		Key = key;
	}
	public Double getTodayProfitLoss() {
		return todayProfitLoss;
	}
	public void setTodayProfitLoss(Double todayProfitLoss) {
		this.todayProfitLoss = todayProfitLoss;
	}
	public String getTradedScriptNames() {
		return tradedScriptNames;
	}
	public void setTradedScriptNames(String tradedScriptNames) {
		this.tradedScriptNames = tradedScriptNames;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	
	

}
