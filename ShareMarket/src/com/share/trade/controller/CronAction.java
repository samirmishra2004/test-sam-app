package com.share.trade.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.share.trade.bd.FutureGapTradeBD;
import com.share.trade.bd.ScriptMapperBD;
import com.share.trade.bd.ShareHomeBD;
import com.share.trade.bd.StrategyBD;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.ShareUtil;
import com.share.trade.common.TradeLong;
import com.share.trade.common.TradeSort;
import com.share.trade.database.FutureGapScript;
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
	private FutureGapTradeBD futureGapTradeBD=new FutureGapTradeBD();
	@RequestMapping("/cronAction")
	public ModelAndView execute() throws Exception{
		time.setTimeZone(TimeZone.getTimeZone("IST"));
		time.setTime(new Date());
		HOUR=time.get(Calendar.HOUR_OF_DAY);
		MINUT=time.get(Calendar.MINUTE);
		SECOND=time.get(Calendar.SECOND);
		Strategy strategy=strategyBD.getStrategy(ShareUtil.DEFAULTSTRATEGY);
		ScriptMapper scriptMapper=null;
		
		if(HOUR == 9 && MINUT <=17){
		System.out.println("Clear the log");
		MethodUtil.clearLog("");
		//reset static values
		ShareUtil.PRICE_CHANGE_DEC_MAP.clear();
		ShareUtil.PRICE_CHANGE_INC_MAP.clear();
		ShareUtil.hookPrice.clear();
		}
		
		if(strategy!=null&&strategy.getTradeOnOff()!=null&&ShareUtil.TRADE_ON.equals(strategy.getTradeOnOff())){
			
		if(ShareUtil.WATCHER_LOG.size()>400){
			for(int index=ShareUtil.WATCHER_LOG.size();index>100;index--){
			ShareUtil.WATCHER_LOG.remove(index);
			}
		}
		
		if(strategy.isFutGapTrd()){
			System.out.println("=========Gap update Start with"+ShareUtil.WATCHER_FNO_SCRIPT_SET.size()+" script =======");
			for(FutureGapScript gs:ShareUtil.WATCHER_FNO_SCRIPT_SET){
				if(HOUR == 9 && MINUT <=33){
					gs.setMaxGap(null);
					gs.setMinGap(null);
					gs.setTradeOn("");
				}
				futureGapTradeBD.updateCalculatedGap(gs,strategy);
			}
			futureGapTradeBD.getMappedScript();
			System.out.println("=========Gap update End =======");
		}else
		try{			
			ShareUtil.REMOTE_SERVER_CALL_CNT=0;
		//MethodUtil.refreshRemoteServer("http://dhanabriksh-samworld.rhcloud.com/");
		if(ShareUtil.WATCHER_SCRIPT_SET.size()>0){
			
			for(String b:ShareUtil.WATCHER_SCRIPT_SET){
				boolean updateWatcherPrice=true;
				ShareBean shareBean=homeBD.getRealTimeFinanceData(b,0);
				ScriptMapper sm=mapperBD.getMappedScriptByScriptName(b);
				System.out.println("broker script : "+sm.getBroker_script());
				String longFirstOrShort=MethodUtil.decideLongFirstOrSort(shareBean);
				
				if(ShareUtil.LONG.equals(longFirstOrShort)){
					System.out.println("*****LONG FIRST****");
					/*if(!ShareUtil.BULISH.equals(strategy.getGlobalSentiment())&&
							!ShareUtil.BEARISH.equals(strategy.getGlobalSentiment())){
						updateWatcherPrice=false;
					}*//*Not required, as it is going to run for either LONG or SORT not for both*/
					if(!ShareUtil.BEARISH.equals(strategy.getGlobalSentiment())){
						System.out.println("=========Long trade =======");
						watchLong.watch(shareBean, b,strategy,sm,updateWatcherPrice);
					}
					/*if(!ShareUtil.BULISH.equals(strategy.getGlobalSentiment())){
						System.out.println("=========Sorting =======");
						watchSort.watch(shareBean, b,strategy,sm,updateWatcherPrice);
						updateWatcherPrice=true;//update at last
					}*/ 
					
				}else{				
					
					if(!ShareUtil.BULISH.equals(strategy.getGlobalSentiment())){
						System.out.println("=========Sorting =======");
						watchSort.watch(shareBean, b,strategy,sm,updateWatcherPrice);
						updateWatcherPrice=true;//update at last
					}
					/*if(!ShareUtil.BEARISH.equals(strategy.getGlobalSentiment())){
						System.out.println("=========Long trade =======");
						watchLong.watch(shareBean, b,strategy,sm,updateWatcherPrice);
					}*/
				
				}
			}
		}
		}catch (Exception e) {
			System.err.println("Error: "+e.getMessage()+"\n"+e);
		}
		}
		return new ModelAndView("incorrectUrl");
		
	}
	
	
}
