package com.share.trade.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.PMF;
import com.share.trade.database.ScriptMapper;
import com.share.trade.database.TradeWatcher;

public class TradeWatcherDAO {
	
	public TradeWatcher getWatchDataForScript(String scriptName){
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		List<TradeWatcher> tradeWatcherList=new ArrayList<TradeWatcher>();
		Query q=pm.newQuery(TradeWatcher.class);
		q.setFilter("scriptName == script");		
		q.declareParameters("String script");
		Object result=q.execute(scriptName);
		if(result!=null)
		tradeWatcherList=(List<TradeWatcher>)q.execute(scriptName);
		
		tradeWatcherList.size();
		
		
		pm.close();
		
		if(tradeWatcherList!=null&&tradeWatcherList.size()>0)
		return (TradeWatcher)tradeWatcherList.get(0);
		else
			return null;
		
		
		
	}
	public List<TradeWatcher> getWatchDataForAllScript(){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		List<TradeWatcher> tradeWatcherList=new ArrayList<TradeWatcher>();
		Query q=pm.newQuery(TradeWatcher.class);
		

	
		tradeWatcherList=(List<TradeWatcher>)q.execute();
		
		
		return tradeWatcherList;
		
	}
	public void addScriptToWatch(TradeWatcher watchScript)throws Exception{
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(TradeWatcher.class);
		         pm.makePersistent(watchScript);
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
	
	public void clearAllWatchData() throws Exception{
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArrayList<Object> ol=new ArrayList<Object>();
		
		Query q=pm.newQuery(TradeWatcher.class);
		
		ol=(ArrayList<Object>)q.execute();
		try { 
		       
			   pm.deletePersistentAll(ol);
				
				
				
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		
	}
	public void updateWatchScript(TradeWatcher tw) throws Exception{

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try {
			
			TradeWatcher twm = pm.getObjectById(TradeWatcher.class, tw.getKey());
		   twm.setBuyAlerted(tw.isBuyAlerted());
		   twm.setBuyPrice(tw.getBuyPrice());
		   twm.setSellAlerted(tw.isSellAlerted());
		   twm.setSellPrice(tw.getSellPrice());
		   twm.setCurrentPrice1(tw.getCurrentPrice1());
		   twm.setCurrentPrice2(tw.getCurrentPrice2());
			
		} finally {
		    pm.close();
		}
		
	}

	}
