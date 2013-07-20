package com.share.trade.bd;

import java.util.List;

import com.share.trade.dao.TradeWatcherDAO;
import com.share.trade.database.TradeWatcher;

public class TradeWatcherBD {
	TradeWatcherDAO dao=new TradeWatcherDAO();
	
	public TradeWatcher getWatchDataForScript(String scriptName){
		System.out.println("getWatchDataForScript "+scriptName);
		return dao.getWatchDataForScript(scriptName);
		
		
	}
	public List<TradeWatcher> getWatchDataForAllScript(){
		
		return dao.getWatchDataForAllScript();
		
	}
	public TradeWatcher addScriptToWatch(String watchScriptName)throws Exception{
		System.out.println("addScriptToWatch "+watchScriptName);

		TradeWatcher watchScript=new TradeWatcher();
		watchScript.setScriptName(watchScriptName);		
		watchScript.setCurrentPrice1(0.0);
		watchScript.setCurrentPrice2(0.0);
		watchScript.setBuyAlerted(false);
		watchScript.setBuyPrice(0.0);
		watchScript.setSellAlerted(false);		
		watchScript.setSellPrice(0.0);
		
		dao.addScriptToWatch(watchScript);
		return watchScript;
		
	}
	
	public void clearAllWatchData() throws Exception{
		
		dao.clearAllWatchData();
	}
	public void updateWatchScript(TradeWatcher tw) throws Exception{
		System.out.println("updateWatchScript "+tw.getScriptName());

		dao.updateWatchScript(tw);
	}

	}
