package com.share.trade.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.share.trade.common.MethodUtil;
import com.share.trade.common.PMF;
import com.share.trade.common.ShareUtil;
import com.share.trade.database.FutureGapScript;
import com.share.trade.vo.FutureScriptQuote;

public class FutureGapTradeDAO extends ShareHomeDAO{

	
	public void updateScrip(FutureGapScript updatScripDTO) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try {
			FutureGapScript s = pm.getObjectById(FutureGapScript.class, updatScripDTO.getKey());
			
			System.out.println("updating for :"+s.getWatcherScriptUrl());
			s.setLotSize(updatScripDTO.getLotSize());
			s.setMaxGap(updatScripDTO.getMaxGap());
			s.setMinGap(updatScripDTO.getMinGap());
		  
			s.setExpDate1(updatScripDTO.getExpDate1());
			s.setExpDate2(updatScripDTO.getExpDate2());
			s.setWatcherScriptUrl(updatScripDTO.getWatcherScriptUrl()) ;
			s.setTradeScript1(updatScripDTO.getTradeScript1());
			s.setTradeScript2(updatScripDTO.getTradeScript2());
			s.setUpdateDate(new Date());
			
			
		} finally {
		    pm.close();
		}
		}
	
	public void addScrip(FutureGapScript newScripDTO) throws Exception{
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction(); 
		   try { 
		        tx.begin(); 
		        Query q=pm.newQuery(FutureGapScript.class);
		         pm.makePersistent(newScripDTO);
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
	
public boolean deleteScript(FutureGapScript shares)throws Exception{
	PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArrayList<Object> ol=new ArrayList<Object>();
		
		Object o=pm.getObjectById(FutureGapScript.class,shares.getKey());
		
		 
		   try { 
		       
			   pm.deletePersistent(o);
				
				
				
		    } catch (Exception ex) { 
		    	
		    	
		        throw ex;
		    } finally { 
		      pm.close(); 
		    } 
		//System.out.println("in dao:"+ share.getKey());
		
		return true;
	}
		
	public List<FutureGapScript> getMappedScripts(){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		List<FutureGapScript> mapperDTOs=new ArrayList<FutureGapScript>();
		Query q=pm.newQuery(FutureGapScript.class);
		//q.setFilter("isActive == activeForTrade");		
		//q.declareParameters("String activeForTrade");

	
		mapperDTOs=(List<FutureGapScript>)q.execute();
		
				
		return mapperDTOs;
	}
	public FutureGapScript getMappedScriptByName(String watcherName){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		List<FutureGapScript> mapperDTOs=new ArrayList<FutureGapScript>();
		Query q=pm.newQuery(FutureGapScript.class);
		q.setFilter("wtcher_script == wtchScrpt");		
		q.declareParameters("String wtchScrpt");

		Object result=q.execute(watcherName);
		
		if(result!=null){
		mapperDTOs=(List<FutureGapScript>)q.execute(watcherName);
		}
		
		if(mapperDTOs.size()>0)return mapperDTOs.get(0);else return null;
	}
	public FutureGapScript updateCalculatedGap(FutureGapScript gapScript) throws Exception{
		
		String url=gapScript.getWatcherScriptUrl()+"/"+gapScript.getExpDate1();
		FutureScriptQuote quoteNearMonth=getRealTimeFutureQuote(url);
		
		url=gapScript.getWatcherScriptUrl()+"/"+gapScript.getExpDate2();
		FutureScriptQuote quoteFarMonth=getRealTimeFutureQuote(url);
		
		double askPriceOfNearMonth=0;
		
		
		
		double bidPriceOfFarMonth=0;
		double gapPercent=0;
		double actualGap=0;
		
		if(!MethodUtil.isEmpty(quoteNearMonth.getOffferPrice())&&
				!MethodUtil.isEmpty(quoteFarMonth.getBidPrice())){
			askPriceOfNearMonth=Double.parseDouble(quoteNearMonth.getOffferPrice());
			bidPriceOfFarMonth=Double.parseDouble(quoteFarMonth.getBidPrice());
			actualGap=bidPriceOfFarMonth-askPriceOfNearMonth;
			
			gapPercent=(actualGap/askPriceOfNearMonth)*100;
			
			
			
		}
		
		
		
		System.out.println("Actual Gap :"+actualGap);
		System.out.println("Percentage Gap :"+gapPercent);
		if(gapScript.getMaxGap()==null||gapScript.getMinGap()==null){
			//% calulation
			System.out.println("Both are null first time:");
			
			
			gapScript.setMaxGap(MethodUtil.roundOff(gapPercent));
			gapScript.setMinGap(MethodUtil.roundOff(gapPercent));
		}
		System.out.println("Previous Max Gap :"+gapScript.getMaxGap());
		System.out.println("Previous Min Gap :"+gapScript.getMinGap());
		
		if(gapScript.getMaxGap()!=null&&MethodUtil.roundOff(gapPercent)>0&&MethodUtil.roundOff(gapPercent)>gapScript.getMaxGap()){
			
			gapScript.setMaxGap(MethodUtil.roundOff(gapPercent));
		}
		if(gapScript.getMinGap()!=null&&MethodUtil.roundOff(gapPercent)<gapScript.getMinGap()){
			gapScript.setMinGap(MethodUtil.roundOff(gapPercent));
		}
		
		if(quoteNearMonth.getLotSize()!=null)
		gapScript.setLotSize(Long.parseLong(quoteNearMonth.getLotSize()));
		
		
		updateScrip(gapScript);
		return gapScript;
	}

}
