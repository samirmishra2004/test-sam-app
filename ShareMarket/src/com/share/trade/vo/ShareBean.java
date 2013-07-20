package com.share.trade.vo;
/**
 * 
 * @author Samir
 *@Description This class holds the real time data
 *from stock market.
 */
public class ShareBean {

	double dayHigh;
	double dayLow;
	double currentTradePrice;
	double previousClose;
	double currentBid;
	double currentAsk;
	
	
	public ShareBean(double dayHigh, double dayLow, double currentTradePrice,
			double previousClose, double currentBid, double currentAsk) {
		super();
		this.dayHigh = dayHigh;
		this.dayLow = dayLow;
		this.currentTradePrice = currentTradePrice;
		this.previousClose = previousClose;
		this.currentBid = currentBid;
		this.currentAsk = currentAsk;
	}


	public double getDayHigh() {
		return dayHigh;
	}


	public void setDayHigh(double dayHigh) {
		this.dayHigh = dayHigh;
	}


	public double getDayLow() {
		return dayLow;
	}


	public void setDayLow(double dayLow) {
		this.dayLow = dayLow;
	}


	public double getCurrentTradePrice() {
		return currentTradePrice;
	}


	public void setCurrentTradePrice(double currentTradePrice) {
		this.currentTradePrice = currentTradePrice;
	}


	public double getPreviousClose() {
		return previousClose;
	}


	public void setPreviousClose(double previousClose) {
		this.previousClose = previousClose;
	}


	public double getCurrentBid() {
		return currentBid;
	}


	public void setCurrentBid(double currentBid) {
		this.currentBid = currentBid;
	}


	public double getCurrentAsk() {
		return currentAsk;
	}


	public void setCurrentAsk(double currentAsk) {
		this.currentAsk = currentAsk;
	}


	public ShareBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
