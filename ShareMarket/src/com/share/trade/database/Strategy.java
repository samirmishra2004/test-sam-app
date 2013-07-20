package com.share.trade.database;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Strategy {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;  
	@Persistent
	private String strategyName;
	@Persistent
	private String buyFactor;
	@Persistent
	private String sellFactor;
	@Persistent
	private String tradeOnOff;
	@Persistent
	private String globalSentiment;
	@Persistent
	private String tradeSegment;
	@Persistent
	private String tradeAmount;
	@Persistent
	private boolean forcesSquareOff;
	@Persistent
	private String positionOpenHour;
	@Persistent
	private String positionCloseHour;
	@Persistent
	private String positionOpenMinut;
	@Persistent
	private String positionCloseMinut;
	@Persistent
	private boolean autoTrade;
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public String getBuyFactor() {
		return buyFactor;
	}
	public void setBuyFactor(String buyFactor) {
		this.buyFactor = buyFactor;
	}
	public String getSellFactor() {
		return sellFactor;
	}
	public void setSellFactor(String sellFactor) {
		this.sellFactor = sellFactor;
	}
	public String getTradeOnOff() {
		return tradeOnOff;
	}
	public void setTradeOnOff(String tradeOnOff) {
		this.tradeOnOff = tradeOnOff;
	}
	public String getGlobalSentiment() {
		return globalSentiment;
	}
	public void setGlobalSentiment(String globalSentiment) {
		this.globalSentiment = globalSentiment;
	}
	public String getTradeSegment() {
		return tradeSegment;
	}
	public void setTradeSegment(String tradeSegment) {
		this.tradeSegment = tradeSegment;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public boolean isForcesSquareOff() {
		return forcesSquareOff;
	}
	public void setForcesSquareOff(boolean forcesSquareOff) {
		this.forcesSquareOff = forcesSquareOff;
	}
	public String getPositionOpenHour() {
		return positionOpenHour;
	}
	public void setPositionOpenHour(String positionOpenHour) {
		this.positionOpenHour = positionOpenHour;
	}
	public String getPositionCloseHour() {
		return positionCloseHour;
	}
	public void setPositionCloseHour(String positionCloseHour) {
		this.positionCloseHour = positionCloseHour;
	}
	public String getPositionOpenMinut() {
		return positionOpenMinut;
	}
	public void setPositionOpenMinut(String positionOpenMinut) {
		this.positionOpenMinut = positionOpenMinut;
	}
	public String getPositionCloseMinut() {
		return positionCloseMinut;
	}
	public void setPositionCloseMinut(String positionCloseMinut) {
		this.positionCloseMinut = positionCloseMinut;
	}
	public boolean isAutoTrade() {
		return autoTrade;
	}
	public void setAutoTrade(boolean autoTrade) {
		this.autoTrade = autoTrade;
	}
	
	
	
	
	
}
