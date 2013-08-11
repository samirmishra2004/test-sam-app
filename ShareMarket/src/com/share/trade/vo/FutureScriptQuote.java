package com.share.trade.vo;

public class FutureScriptQuote {
	
	String openPrice;
	String previousClose;
	String totalTradedContract;
	String bidPrice;
	String offferPrice;
	String currentPrice;
	String dayHigh;
	String dayLow;
	String lotSize;
	
	public String getLotSize() {
		return lotSize;
	}
	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}
	public String getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}
	public String getPreviousClose() {
		return previousClose;
	}
	public void setPreviousClose(String previousClose) {
		this.previousClose = previousClose;
	}
	public String getTotalTradedContract() {
		return totalTradedContract;
	}
	public void setTotalTradedContract(String totalTradedContract) {
		this.totalTradedContract = totalTradedContract;
	}
	public String getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}
	public String getOffferPrice() {
		return offferPrice;
	}
	public void setOffferPrice(String offferPrice) {
		this.offferPrice = offferPrice;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getDayHigh() {
		return dayHigh;
	}
	public void setDayHigh(String dayHigh) {
		this.dayHigh = dayHigh;
	}
	public String getDayLow() {
		return dayLow;
	}
	public void setDayLow(String dayLow) {
		this.dayLow = dayLow;
	}
	
	

}
