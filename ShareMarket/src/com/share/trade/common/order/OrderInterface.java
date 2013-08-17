package com.share.trade.common.order;

public interface OrderInterface {
 
	public String OPTION_TYPE_CE="CE";
	public String OPTION_TYPE_PE="PE";
	
	public void buy() throws Exception;
	public void sell() throws Exception;
	public void checkOrderStatus() throws Exception;
}
