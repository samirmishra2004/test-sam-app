package com.share.trade.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.ScriptMapperBD;
import com.share.trade.bd.ShareHomeBD;
import com.share.trade.bd.StrategyBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.common.TradeLong;
import com.share.trade.common.TradeSort;
import com.share.trade.database.ScriptMapper;
import com.share.trade.database.Strategy;
import com.share.trade.vo.ShareBean;


@Controller
public class CronAction{
	/**
	 * 
	 */
	private static final Logger log = Logger.getLogger(CronAction.class.getName());
	private static final long serialVersionUID = 1L;
	 
	 TradeSort watchSort=new TradeSort();
	 TradeLong watchLong=new TradeLong();
	 Calendar time=Calendar.getInstance();
	 int HOUR=0;
	 int MINUT=0;
	 int SECOND=0;
	 
	public static HashMap<String, HashMap<String, Object>> stockWatcherData=new HashMap<String,HashMap<String, Object>>();
	
	
	private ShareHomeBD homeBD=new ShareHomeBD();
	private StrategyBD  strategyBD=new StrategyBD();
	private ScriptMapperBD mapperBD=new ScriptMapperBD();
	@RequestMapping("/cronAction")
	public ModelAndView execute() throws Exception{
		time.setTimeZone(TimeZone.getTimeZone("IST"));
		time.setTime(new Date());
		HOUR=time.get(Calendar.HOUR_OF_DAY);
		MINUT=time.get(Calendar.MINUTE);
		SECOND=time.get(Calendar.SECOND);
		Strategy strategy=strategyBD.getStrategy(ShareUtil.DEFAULTSTRATEGY);
		ScriptMapper scriptMapper=null;
		
		if(HOUR == 9 && MINUT <=19){
		System.out.println("Clear the log");
		MethodUtil.clearLog("");
		}
		
		if(strategy!=null&&strategy.getTradeOnOff()!=null&&ShareUtil.TRADE_ON.equals(strategy.getTradeOnOff())){
			
		if(ShareUtil.WATCHER_LOG.size()>400){
			for(int index=ShareUtil.WATCHER_LOG.size();index>100;index--){
			ShareUtil.WATCHER_LOG.remove(index);
			}
		}
		
		if(ShareUtil.WATCHER_SCRIPT_SET.size()>0){
			
			for(String b:ShareUtil.WATCHER_SCRIPT_SET){
				boolean updateWatcherPrice=true;
				ShareBean shareBean=homeBD.getRealTimeFinanceData(b,0);
				ScriptMapper sm=mapperBD.getMappedScriptByScriptName(b);
				if(!ShareUtil.BULISH.equals(strategy.getGlobalSentiment())&&
						!ShareUtil.BEARISH.equals(strategy.getGlobalSentiment())){
					updateWatcherPrice=false;
				}
				if(!ShareUtil.BULISH.equals(strategy.getGlobalSentiment())){
					System.out.println("=========Sorting =======");
					watchSort.watch(shareBean, b,strategy,sm,updateWatcherPrice);
					updateWatcherPrice=true;//update at last
				}
				if(!ShareUtil.BEARISH.equals(strategy.getGlobalSentiment())){
					System.out.println("=========Long trade =======");
					watchLong.watch(shareBean, b,strategy,sm,updateWatcherPrice);
				}
				
				
			}
		}
		
		}
		return new ModelAndView("incorrectUrl");
		
	}
	
	
}
