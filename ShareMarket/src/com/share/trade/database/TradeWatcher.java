package com.share.trade.database;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TradeWatcher {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	@Persistent
	private String scriptName; 
	@Persistent
	private double currentPrice1;
	@Persistent
	private double currentPrice2;
	@Persistent
	private boolean buyAlerted;
	@Persistent
	private boolean sellAlerted;
	@Persistent
	private double buyPrice;
	@Persistent
	private double sellPrice;
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public double getCurrentPrice1() {
		return currentPrice1;
	}
	public void setCurrentPrice1(double currentPrice1) {
		this.currentPrice1 = currentPrice1;
	}
	public double getCurrentPrice2() {
		return currentPrice2;
	}
	public void setCurrentPrice2(double currentPrice2) {
		this.currentPrice2 = currentPrice2;
	}
	public boolean isBuyAlerted() {
		return buyAlerted;
	}
	public void setBuyAlerted(boolean buyAlerted) {
		this.buyAlerted = buyAlerted;
	}
	public boolean isSellAlerted() {
		return sellAlerted;
	}
	public void setSellAlerted(boolean sellAlerted) {
		this.sellAlerted = sellAlerted;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	
	

}
