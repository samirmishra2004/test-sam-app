package com.share.trade.bd;

import java.util.logging.Logger;

import com.share.trade.common.ShareUtil;
import com.share.trade.dao.StrategyDAO;
import com.share.trade.dao.TradeSummaryDAO;
import com.share.trade.database.Strategy;

public class StrategyBD {
	StrategyDAO dao=new StrategyDAO();
	Logger log =Logger.getLogger(TradeSummaryDAO.class.getName());
	
	public boolean addOrUpdateStrategy(Strategy stg){
		log.info("in strategy BD");
		stg.setStrategyName(ShareUtil.DEFAULTSTRATEGY);
		try {
			return dao.addOrUpdateStrategy(stg);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
	}
	public Strategy getStrategy(String stgname){
		Strategy stg=null;
		try {
			stg= dao.getStrategy(stgname);
		} catch (Exception e) {
			e.printStackTrace();
		
			
		}
		return stg;
	}
	
}
