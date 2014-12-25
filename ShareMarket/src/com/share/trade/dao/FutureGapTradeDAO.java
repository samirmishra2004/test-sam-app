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
import com.share.trade.database.Strategy;
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
	public FutureGapScript updateCalculatedGap(FutureGapScript gapScript,Strategy stg) throws Exception{
		//Case 1: buy far month sell near month
		//Case 2: buy near month sell far month
		String buyFactor=stg.getBuyFactor();
		String sellFactor=stg.getSellFactor();
		
		String url=gapScript.getWatcherScriptUrl()+"/"+gapScript.getExpDate1();
		FutureScriptQuote quoteNearMonth=getRealTimeFutureQuote(url);
		
		url=gapScript.getWatcherScriptUrl()+"/"+gapScript.getExpDate2();
		FutureScriptQuote quoteFarMonth=getRealTimeFutureQuote(url);
		
		double askPriceOfNearMonth=0;		
		double bidPriceOfFarMonth=0;
		
		
		double askPriceOfFarMonth=0;		
		double bidPriceOfNearMonth=0;
		
		double gapPercentCase1=0;
		double actualGapCase1=0;
		double gapPercentCase2=0;
		double actualGapCase2=0;
		
		//case1
		//buy far @ask price y
		
		if(!MethodUtil.isEmpty(quoteFarMonth.getOffferPrice())&&
				!MethodUtil.isEmpty(quoteNearMonth.getBidPrice())){
			askPriceOfFarMonth=Double.parseDouble(quoteFarMonth.getOffferPrice());
			bidPriceOfNearMonth=Double.parseDouble(quoteNearMonth.getBidPrice());
			actualGapCase1=askPriceOfFarMonth-bidPriceOfNearMonth;
			
			gapPercentCase1=(actualGapCase1/bidPriceOfNearMonth)*100;
			
			
			
		}
		//case2
		if(!MethodUtil.isEmpty(quoteNearMonth.getOffferPrice())&&
				!MethodUtil.isEmpty(quoteFarMonth.getBidPrice())){
			askPriceOfNearMonth=Double.parseDouble(quoteNearMonth.getOffferPrice());
			bidPriceOfFarMonth=Double.parseDouble(quoteFarMonth.getBidPrice());
			actualGapCase2=bidPriceOfFarMonth-askPriceOfNearMonth;
			
			gapPercentCase2=(actualGapCase2/askPriceOfNearMonth)*100;
			
			
			
		}
		System.out.println("Actual Gap cae1:"+actualGapCase1);
		System.out.println("Percentage Gap case1 :"+gapPercentCase1);
		System.out.println("Actual Gap cae2:"+actualGapCase2);
		System.out.println("Percentage Gap case2 :"+gapPercentCase2);
		//close position
		
		//case 1: buy far
		if(ShareUtil.OPEN_BUY_FAR.equals(gapScript.getTradeOn())){
			if(gapPercentCase2>=(Double.parseDouble(sellFactor)+Double.parseDouble("0.50"))){
				gapScript.setTradeOn(ShareUtil.SQUREOFF);
				MethodUtil.uiLog(
						"<font color=green>Auto Trade: </font>" + gapScript.getTradeScript1()
								+ " is signeled to Squared off OPEN-BUY-FAR @" + MethodUtil.roundOff(gapPercentCase2),
						ShareUtil.ORDER);
			}
		}else if(ShareUtil.OPEN_SELL_FAR.equals(gapScript.getTradeOn())){//case 2: sell far
			
			if(gapPercentCase1<=(Double.parseDouble(sellFactor)-Double.parseDouble("0.50"))){
				gapScript.setTradeOn(ShareUtil.SQUREOFF);

				MethodUtil.uiLog(
						"<font color=green>Auto Trade: </font>" + gapScript.getTradeScript1()
								+ " is signeled to Squared off OPEN-SELL-FAR @" + MethodUtil.roundOff(gapPercentCase2),
						ShareUtil.ORDER);
			}
		}
		//take position case1
				if(gapPercentCase1<=Double.parseDouble(buyFactor)){
					if(!ShareUtil.OPEN_BUY_FAR.equals(gapScript.getTradeOn())&&!ShareUtil.SQUREOFF.equals(gapScript.getTradeOn())){
						
							gapScript.setTradeOn(ShareUtil.OPEN_BUY_FAR);
							MethodUtil.uiLog(
									"<font color=green>Auto Trade: </font>" + gapScript.getTradeScript1()
											+ " is signeled to OPEN-BUY-FAR @" + MethodUtil.roundOff(gapPercentCase2),
									ShareUtil.ORDER);
						
						
					}
		//take position case2
		if(gapPercentCase2>=Double.parseDouble(sellFactor)&&!ShareUtil.SQUREOFF.equals(gapScript.getTradeOn())){
			if(stg.isAutoTrade()){
			
			}
			if(!ShareUtil.OPEN_SELL_FAR.equals(gapScript.getTradeOn())){
				
				gapScript.setTradeOn(ShareUtil.OPEN_SELL_FAR);
				MethodUtil.uiLog(
						"<font color=green>Auto Trade: </font>" + gapScript.getTradeScript1()
								+ " is signeled to OPEN-SELL-FAR @" + MethodUtil.roundOff(gapPercentCase2),
						ShareUtil.ORDER);
			
				//gapScript.setTradeOn("OPEN-SELL-FAR");
			}
			
		}
		
			
		}
		
		
		//update current gap
		if(gapScript.getMaxGap()==null||gapScript.getMinGap()==null){
			//% calulation
			System.out.println("Both are null first time:");
			
			
			gapScript.setMaxGap(MethodUtil.roundOff(gapPercentCase2));
			gapScript.setMinGap(MethodUtil.roundOff(gapPercentCase1));
		}
		System.out.println("Previous Max Gap :"+gapScript.getMaxGap());
		System.out.println("Previous Min Gap :"+gapScript.getMinGap());
		
		if(gapScript.getMaxGap()!=null&&MethodUtil.roundOff(gapPercentCase2)>0&&MethodUtil.roundOff(gapPercentCase2)>gapScript.getMaxGap()){
			
			gapScript.setMaxGap(MethodUtil.roundOff(gapPercentCase2));
		}
		if(gapScript.getMinGap()!=null&&MethodUtil.roundOff(gapPercentCase1)<gapScript.getMinGap()){
			gapScript.setMinGap(MethodUtil.roundOff(gapPercentCase1));
		}
		
		if(quoteNearMonth.getLotSize()!=null)
		gapScript.setLotSize(Long.parseLong(quoteNearMonth.getLotSize()));
		
		
		updateScrip(gapScript);
		return gapScript;
	}

}
