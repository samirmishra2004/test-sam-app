package com.share.trade.bd;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.share.trade.dao.TradeSummaryDAO;
import com.share.trade.database.TradeSummary;

public class TradeSummaryBD {
	Logger log =Logger.getLogger(TradeSummaryBD.class.getName());
	TradeSummaryDAO tradeSummaryDAO= new TradeSummaryDAO();

	public boolean addOrUpdateTradeSummary(double bp,double sp,String s) throws Exception{
		TradeSummary ts=new TradeSummary();
		double bpWithbrokrage=0;
		double spWithbrokrage=0;
		if(bp>0){
		 bpWithbrokrage=(bp+(bp*0.0007));
		}
		if(sp>0){
		 spWithbrokrage=(sp-(bp*0.0007));
		}
		ts.setScript(s);
		ts.setBuyPrice(bpWithbrokrage);
		ts.setSellPrice(spWithbrokrage);
		ts.setTradeDate(new java.util.Date());
		tradeSummaryDAO.addOrUpdateTradeSummary(ts);
		   return true;
	}
	
	public LinkedList<TradeSummary> getTradeMonthlySummary(){
		LinkedList<TradeSummary> tsl=new LinkedList<TradeSummary>();
		LinkedList<TradeSummary> tsml=new LinkedList<TradeSummary>();
		Calendar trdCal=Calendar.getInstance();
		Calendar todayCal=Calendar.getInstance();
		todayCal.setTime(new Date());
		try{
			tsl= tradeSummaryDAO.getTradeSummary();
			int month=0;
			int index=0;
			for(TradeSummary ts:tsl){
				Date trdDate=ts.getTradeDate();
				trdCal.setTime(trdDate);
				month=trdCal.get(Calendar.MONTH);
				if(todayCal.get(Calendar.YEAR)==trdCal.get(Calendar.YEAR)&&
						trdCal.get(Calendar.MONTH)==todayCal.get(Calendar.MONTH)){
					tsml.add(ts);
				}
			}
		}catch(Exception ex){
			log.info("error "+ex.getMessage());
		}finally{
			//pm.close();
		}
		return tsml;
		
	}
	
	
	public void deleteTradeSummary(){
		
	}

}
