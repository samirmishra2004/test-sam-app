package com.share.trade.common;

import java.util.Date;
import java.util.HashMap;

public class Mylog extends HashMap<Date, String>{
	Date key;
	@Override
	public String put(Date key, String value) {
		this.key=key;
		return super.put(key, value);
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	
}