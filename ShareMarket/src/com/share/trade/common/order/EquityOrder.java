package com.share.trade.common.order;

import com.share.trade.common.OrderUtil;
import com.share.trade.common.ShareUtil;

public class EquityOrder implements OrderInterface {

	String scriptName;
	String monthString;
	String nameAndMonth;
	long lotSize;
	private String buyOrSell;
	double price;
	String token;
	
	public void placeOrder() throws Exception {
		OrderUtil ou=OrderUtil.getInstance();
		ou.placeOrder(this);		
		
	}
	public void placeOrderManual() throws Exception {
		OrderUtil ou=OrderUtil.getInstance();
		ou.placeOrder(this);		
		
	}
	
	@Override
	public void buy() throws Exception {
		
	}

	@Override
	public void sell() throws Exception {
		OrderUtil ou=OrderUtil.getInstance();
		ou.placeOrder(this);
		setBuyOrSell(ShareUtil.SORT);
	}
	@Override
	public String checkOrderStatus() throws Exception {
		OrderUtil ou=OrderUtil.getInstance();
		return ou.getOrderStatus(this);		
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getMonthString() {
		return monthString;
	}
	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}
	public String getNameAndMonth() {
		return nameAndMonth;
	}
	public void setNameAndMonth(String nameAndMonth) {
		this.nameAndMonth = nameAndMonth;
	}
	public long getLotSize() {
		return lotSize;
	}
	public void setLotSize(long lotSize) {
		this.lotSize = lotSize;
	}
	public String getBuyOrSell() {
		return buyOrSell;
	}
	public void setBuyOrSell(String buyOrSell) {
		this.buyOrSell = buyOrSell;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
