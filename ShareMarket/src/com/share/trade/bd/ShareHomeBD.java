package com.share.trade.bd;

import com.share.trade.dao.ShareHomeDAO;
import com.share.trade.vo.ShareBean;

public class ShareHomeBD {
	ShareHomeDAO homeDAO=new ShareHomeDAO();
	public String getPageFromMoneyControll(){
		String moneyControllPage=homeDAO.getInfoFromMoneyControll();
		
		return moneyControllPage;
	}
	
	public ShareBean getRealTimeFinanceData(String symbol,long time) throws Exception{
		System.out.println("callin dao in bd");
		ShareBean bean=homeDAO.getRealTimeFinanceData(symbol,time);
		
		return bean;
	}
	
}
