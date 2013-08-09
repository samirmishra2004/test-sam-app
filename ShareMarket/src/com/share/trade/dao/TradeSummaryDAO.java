package com.share.trade.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.PMF;
import com.share.trade.database.Strategy;
import com.share.trade.database.TradeSummary;

public class TradeSummaryDAO {
	Logger log =Logger.getLogger(TradeSummaryDAO.class.getName());

	public boolean addOrUpdateTradeSummary(TradeSummary ts) throws Exception{
		
		List<TradeSummary> tsl=getTradeSummaryForScript(ts.getScript());
		log.info("tradesummary List size :"+""+(tsl==null?0:tsl.size()));
		Calendar today=Calendar.getInstance();
		today.setTime(new Date());
		Calendar tradeDay=Calendar.getInstance();
		TradeSummary tsToupdate=null;
		if(tsl!=null){
			for(TradeSummary t:tsl){
				Date trd=t.getTradeDate();
				tradeDay.setTime(trd);
				if(tradeDay.get(Calendar.YEAR)==today.get(Calendar.YEAR)
						&&tradeDay.get(Calendar.MONTH)==today.get(Calendar.MONTH)
						&&tradeDay.get(Calendar.DATE)==today.get(Calendar.DATE)){
					tsToupdate=t;
				}else if(tradeDay.get(Calendar.YEAR)==today.get(Calendar.YEAR)
						&&tradeDay.get(Calendar.MONTH)==today.get(Calendar.MONTH)
						&&(t.getBuyPrice()==0||t.getSellPrice()==0)){
					tsToupdate=t;
				}
			}
		}
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		if(tsToupdate!=null){
			//update
			tsToupdate = pm.getObjectById(TradeSummary.class, tsToupdate.getKey());
			tsToupdate.setBuyPrice(ts.getBuyPrice()+tsToupdate.getBuyPrice());
			tsToupdate.setSellPrice(ts.getSellPrice()+tsToupdate.getSellPrice());
			tsToupdate.setTradeDate(new Date());
			
			pm.close();
		}else{
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(TradeSummary.class);
		         pm.makePersistent(ts);
		        tx.commit(); 
		        

				
		    } catch (Exception ex) { 
		        if (tx.isActive()) { 
		            tx.rollback(); 
		        } 
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		}
		   return true;
	}
	
	public LinkedList<TradeSummary> getTradeSummary(){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		List<TradeSummary> ts=new ArrayList<TradeSummary>();
		try{
		
		Query q=pm.newQuery(TradeSummary.class);		
		q.setOrdering("tradeDate desc");
		Object result=q.execute();
		if(result!=null)
		ts=(List<TradeSummary>)result;
		ts.size();
		}catch(Exception ex){
			log.info("error "+ex.getMessage());
		}finally{
			pm.close();
		}
		
		if(ts!=null&&ts.size()>0)
		return new LinkedList(ts);
		else
			return null;
	}
	public List<TradeSummary> getTradeSummaryForScript(String script){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		List<TradeSummary> ts=new ArrayList<TradeSummary>();
		try{
		
		Query q=pm.newQuery(TradeSummary.class);		
		q.setFilter("script == sName");		
		q.declareParameters("String sName");
		Object result=q.execute(script);
		if(result!=null)
		ts=(List<TradeSummary>)result;
		ts.size();
		}catch(Exception ex){
			log.info("error "+ex.getMessage());
		}finally{
			pm.close();
		}
		
		if(ts!=null&&ts.size()>0)
		return ts;
		else
			return null;
	}
	
	public void deleteStrategy(){
		
	}

}
