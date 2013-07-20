package com.share.trade.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.PMF;
import com.share.trade.database.Strategy;

public class StrategyDAO {
	Logger log =Logger.getLogger(StrategyDAO.class.getName());

	public boolean addOrUpdateStrategy(Strategy strategy) throws Exception{
		
		log.info("in strategy DAO addOrUpdate");
		String strategyName=strategy.getStrategyName();
		Strategy existingStrategy=null;
		if(strategyName!=null){
			existingStrategy=getStrategy(strategyName);
		}
		//log.info("after getting stg"+ existingStrategy.getKey());
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		if(existingStrategy!=null){
			try{
			Strategy stg = pm.getObjectById(Strategy.class, existingStrategy.getKey());
			stg.setBuyFactor(strategy.getBuyFactor());
			stg.setSellFactor(strategy.getSellFactor());
			stg.setTradeOnOff(strategy.getTradeOnOff());
			stg.setAutoTrade(strategy.isAutoTrade());
			stg.setGlobalSentiment(strategy.getGlobalSentiment());
			stg.setTradeSegment(strategy.getTradeSegment());
			stg.setTradeAmount(strategy.getTradeAmount());
			stg.setForcesSquareOff(strategy.isForcesSquareOff());
			stg.setPositionCloseHour(strategy.getPositionCloseHour());
			stg.setPositionCloseMinut(strategy.getPositionCloseMinut());
			stg.setPositionOpenHour(strategy.getPositionOpenHour());
			stg.setPositionOpenMinut(strategy.getPositionOpenMinut());
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}finally{
				pm.close();
			}
			
			return true;  
		}else{
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(Strategy.class);
		         pm.makePersistent(strategy);
		        tx.commit(); 
		        

				
		    } catch (Exception ex) { 
		    	ex.printStackTrace();
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
	
	public Strategy getStrategy(String strategyName){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		List<Strategy> tradeMasterList=new ArrayList<Strategy>();
		Strategy stg=null;
		try{
		
		Query q=pm.newQuery(Strategy.class);		
		q.setFilter("strategyName == stgName");		
		q.declareParameters("String stgName");
		Object result=q.execute(strategyName);
		if(result!=null)
		tradeMasterList=(List<Strategy>)result;
		tradeMasterList.size();
		if(tradeMasterList!=null&&tradeMasterList.size()>0)
			stg= (Strategy)tradeMasterList.get(0);
		
		}catch(Exception ex){
			log.info("error "+ex.getMessage());
		}finally{
			pm.close();
		}
		
		
			return stg;
	}
	
	
	public void deleteStrategy(){
		
	}

}
