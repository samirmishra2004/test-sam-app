package com.share.trade.database;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FutureGapScript {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	@Persistent
	private String watcherScriptUrl;
	@Persistent
	private String expDate1;
	@Persistent
	private String expDate2;
	@Persistent
	private String tradeScript1;
	@Persistent
	private String tradeScript2;
	@Persistent
	private Double maxGap;
	@Persistent
	private Double minGap;
	@Persistent
	private long lotSize;
	@Persistent
	private String tradeOn;
	@Persistent
	private String sc1OrderStatus;
	@Persistent
	private String sc2OrderStatus;	
	
	@Persistent
	private Date updateDate;
	
	
	
	public long getLotSize() {
		return lotSize;
	}
	public void setLotSize(long lotSize) {
		this.lotSize = lotSize;
	}
	public String getTradeOn() {
		return tradeOn;
	}
	public void setTradeOn(String tradeOn) {
		this.tradeOn = tradeOn;
	}
	public String getSc1OrderStatus() {
		return sc1OrderStatus;
	}
	public void setSc1OrderStatus(String sc1OrderStatus) {
		this.sc1OrderStatus = sc1OrderStatus;
	}
	public String getSc2OrderStatus() {
		return sc2OrderStatus;
	}
	public void setSc2OrderStatus(String sc2OrderStatus) {
		this.sc2OrderStatus = sc2OrderStatus;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getWatcherScriptUrl() {
		return watcherScriptUrl;
	}
	public void setWatcherScriptUrl(String watcherScriptUrl) {
		this.watcherScriptUrl = watcherScriptUrl;
	}
	public String getExpDate1() {
		return expDate1;
	}
	public void setExpDate1(String expDate1) {
		this.expDate1 = expDate1;
	}
	public String getExpDate2() {
		return expDate2;
	}
	public void setExpDate2(String expDate2) {
		this.expDate2 = expDate2;
	}
	public String getTradeScript1() {
		return tradeScript1;
	}
	public void setTradeScript1(String tradeScript1) {
		this.tradeScript1 = tradeScript1;
	}
	public String getTradeScript2() {
		return tradeScript2;
	}
	public void setTradeScript2(String tradeScript2) {
		this.tradeScript2 = tradeScript2;
	}
	
	public Double getMaxGap() {
		return maxGap;
	}
	public void setMaxGap(Double maxGap) {
		this.maxGap = maxGap;
	}
	public Double getMinGap() {
		return minGap;
	}
	public void setMinGap(Double minGap) {
		this.minGap = minGap;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	

}
