package com.share.trade.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.PMF;
import com.share.trade.database.TradeMaster;
import com.share.trade.database.TradeWatcher;

public class TradeMsterDAO {


	
	public List<TradeMaster> getTradeDetail(Date date,String script){
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		List<TradeMaster> tradeMasterList=new ArrayList<TradeMaster>();
		Query q=pm.newQuery(TradeWatcher.class);
		q.setFilter("tradeDate == date");		
		q.declareParameters("java.util.Date date");
		q.setFilter("scriptNames == script");		
		q.declareParameters("String script");
		Object result=q.execute(date,script);
		if(result!=null)
		tradeMasterList=(List<TradeMaster>)q.execute(date);
		
		if(tradeMasterList!=null&&tradeMasterList.size()>0)
		return (List<TradeMaster>)tradeMasterList;
		else
			return null;
		
	}
	public List<TradeMaster> getTradeDataForAllScript(){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		List<TradeMaster> tradeDataList=new ArrayList<TradeMaster>();
		Query q=pm.newQuery(TradeMaster.class);
		

	
		tradeDataList=(List<TradeMaster>)q.execute();
		
		
		return tradeDataList;
		
	}
	public void addTradeScript(TradeMaster tradeMaster)throws Exception{
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(TradeMaster.class);
		         pm.makePersistent(tradeMaster);
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
	
	public void clearAllTrdeData() throws Exception{
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArrayList<Object> ol=new ArrayList<Object>();
		
		Query q=pm.newQuery(TradeMaster.class);
		
		ol=(ArrayList<Object>)q.execute();
		try { 
		       
			   pm.deletePersistentAll(ol);
				
				
				
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		
	}
	public void updateTradedScript(TradeMaster tm) throws Exception{

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try {
			
			TradeMaster tmn = pm.getObjectById(TradeMaster.class, tm.getKey());
		   tmn.setTodayProfitLoss(tm.getTodayProfitLoss());
		   tmn.setTradedScriptNames(tm.getTradedScriptNames());
		   tmn.setTradeDate(tm.getTradeDate());
		  
			
		} finally {
		    pm.close();
		}
		
	}

	
}
