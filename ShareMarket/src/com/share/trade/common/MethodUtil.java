package com.share.trade.common;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.share.trade.bd.FutureGapTradeBD;
import com.share.trade.bd.ScriptMapperBD;
import com.share.trade.bd.StrategyBD;
import com.share.trade.dao.CommonDAO;
import com.share.trade.dao.PortfoliyoDAO;
import com.share.trade.database.ActivityLog;
import com.share.trade.database.Strategy;
import com.share.trade.vo.ShareBean;
import com.share.trade.vo.ShareBucket;

public class MethodUtil {
	static CommonDAO commonDAO=new CommonDAO();
	public static void removeFromShareUniqueList(ShareBucket s){
		System.out.println("PORTFOLIYO_SCRIPTS_COUNTER_MAP: "+ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP);
		System.out.println("s.getsSymbol(): in removeFromShareUniqueList "+s.getsSymbol());		
		if(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.containsKey(s.getsSymbol())){
						int count=ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.get(s.getsSymbol());
						if(count<=1){	
							
						ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.remove(s.getsSymbol());
						System.out.println("after removing : "+ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP);
						}
						else{
							
						ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s.getsSymbol(), --count);
						System.out.println("after decreasing count : "+ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP);
						}
					}else{
						System.out.println("List does not have value to remove");
					}
				ShareUtil.WATCHER_SCRIPT_SET.clear();
				ShareUtil.WATCHER_SCRIPT_SET.addAll(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.keySet());
				//return uniqueNameList;
				
			}
	
	public static Set<String> getUniqueShareList(){
		List<String> allShareNameList=new ArrayList<String>();
		PortfoliyoDAO dao=new PortfoliyoDAO();
		allShareNameList=dao.getDistinctShare();
		
		for(String s:allShareNameList){
			if(!ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.containsKey(s)){
				ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s, 1);
			}else{
				int count=ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.get(s);
				ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.put(s, ++count);
			}
		}
		ShareUtil.WATCHER_SCRIPT_SET.addAll(ShareUtil.PORTFOLIYO_SCRIPTS_COUNTER_MAP.keySet());
		return null;
		
	}
	

public static String trimDoubleQuote(String stock){
	if(stock.startsWith("\"")&&stock.endsWith("\""))
	return stock.substring(1, stock.length()-1);
	else
		return stock;
	
	
}
public static void createCronObjectMap(){
	ScriptMapperBD bd=new ScriptMapperBD();
	FutureGapTradeBD gapTradeBD=new FutureGapTradeBD();
	bd.getMappedScript();
		if(ShareUtil.WATCHER_SCRIPT_SET.size()>0){
			
		HashMap<String, Object> infoMap=null;	
		ShareUtil.stockWatcherData.clear();
		System.out.println("WATCHER_SCRIPT_SET "+ShareUtil.WATCHER_SCRIPT_SET);
		for(String s:ShareUtil.WATCHER_SCRIPT_SET){
			
			
			infoMap = new HashMap<String, Object>();
			infoMap.put(ShareUtil.BUY_ALTERD, new Boolean(false));
			infoMap.put(ShareUtil.SELL_ALTERD, new Boolean(false));
			ShareUtil.stockWatcherData.put(s, infoMap);
			
		}
		
		
		}
	
	gapTradeBD.getMappedScript();
}
public static void uiLog(String msg,String activity) throws Exception{
	ActivityLog activityLog=new ActivityLog();
	Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm:ss");
    sdf.setTimeZone (TimeZone.getTimeZone ("IST"));
    System.out.println ("Time in IST is " + sdf.format (now));
	ShareUtil.WATCHER_LOG.push(sdf.format (now)+" :"+ msg);
	activityLog.setLog(msg);
	activityLog.setProcessName(activity);
	activityLog.setTimeStamp(sdf.format (now));
	
	commonDAO.saveLog(activityLog);
	
}


public static List<ActivityLog> getUiLog(String activity) throws Exception{
	List<ActivityLog> activityLogs=new ArrayList<ActivityLog>();
	
	if(isEmpty(activity))
		activityLogs= commonDAO.getAllLog();
	else
		activityLogs= commonDAO.getAllLog(activity);
	
	if(activityLogs!=null){
		Collections.sort(activityLogs);
	}
	return activityLogs;
}
public static void clearLog(String activity) throws Exception{
	List<ActivityLog> activityLogs=new ArrayList<ActivityLog>();
	commonDAO.clearLog(activity);	
	
}
public static double roundOff(double a){
	double b=a*100;	
	double r=Math.round(b);
	return r/100;
}
public static synchronized boolean checkAccountBalance(String tradeAmount){
	StrategyBD strategyBD=new StrategyBD();
	double availabeBalance=0;
	double tradeAmt=0;
	if(tradeAmount!=null&&!"".equals(tradeAmount)){
		tradeAmt=Double.parseDouble(tradeAmount);
	}else{
		return false;
	}
	
	Strategy stg=strategyBD.getStrategy(ShareUtil.DEFAULTSTRATEGY);
	if(stg!=null && stg.getTradeAmount()!=null){
		availabeBalance=Double.parseDouble(stg.getTradeAmount());
	}
	
	return (tradeAmt<=availabeBalance);
	
}
public static synchronized boolean debitTradeAmt(String tradeAmount){
	StrategyBD strategyBD=new StrategyBD();
	double availabeBalance=0;
	double tradeAmt=0;
	if(tradeAmount!=null&&!"".equals(tradeAmount)){
		tradeAmt=Double.parseDouble(tradeAmount);
	}else{
		return false;
	}
	
	Strategy stg=strategyBD.getStrategy(ShareUtil.DEFAULTSTRATEGY);
	if(stg!=null && stg.getTradeAmount()!=null){
		availabeBalance=Double.parseDouble(stg.getTradeAmount());
	}
	
	double remainedBalance=availabeBalance-tradeAmt;
	stg.setTradeAmount(roundOff(remainedBalance)+"");
	strategyBD.addOrUpdateStrategy(stg);
	
	return (tradeAmt<=availabeBalance);
	
}
public static synchronized boolean creditTradeAmt(String tradeAmount){
	StrategyBD strategyBD=new StrategyBD();
	double availabeBalance=0;
	double tradeAmt=0;
	if(tradeAmount!=null&&!"".equals(tradeAmount)){
		tradeAmt=Double.parseDouble(tradeAmount);
	}else{
		return false;
	}
	
	Strategy stg=strategyBD.getStrategy(ShareUtil.DEFAULTSTRATEGY);
	if(stg!=null && stg.getTradeAmount()!=null){
		availabeBalance=Double.parseDouble(stg.getTradeAmount());
	}
	double remainedBalance=tradeAmt+availabeBalance;
	stg.setTradeAmount(roundOff(remainedBalance)+"");
	strategyBD.addOrUpdateStrategy(stg);
	return true;
	
}
public static boolean isEmpty(String s){
	
	return (s==null || "".equals(s.trim()));
	
}
public static String removeComma(String str){
	
	if(!isEmpty(str)&&str.contains(",")){
		
		str=str.replace(",","");
		
		
	}return str;
	
}
private static HTTPResponse getResponse(String url,String payload, String cookieStr,HTTPMethod httpMethod, HTTPResponse prevoiusResponse) throws IOException {
	  URLFetchService service=URLFetchServiceFactory.getURLFetchService();
	  URL uri=new URL(url);
	  HTTPRequest request=new HTTPRequest(uri,httpMethod,FetchOptions.Builder.doNotFollowRedirects().setDeadline(60.0));
	  
	  if(prevoiusResponse!=null){
	  for(HTTPHeader h:prevoiusResponse.getHeaders()){
		  
		  if(!h.getName().equalsIgnoreCase("Set-Cookie")){
			 // request.addHeader(h);
		  }
		  
	  }
	  }
	  //if(HTTPMethod.POST.equals(httpMethod)){
	  //HTTPHeader header=new HTTPHeader("Content-Type","application/x-www-form-urlencoded");
	  //request.setHeader(header);
	  //}
	 // header=new HTTPHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
	//  request.setHeader(header);
	  
	  if(cookieStr!=null && !"".equals(cookieStr)){
		  System.out.println("adding cookie to request : "+cookieStr);
	  request.addHeader(new HTTPHeader("Cookie",cookieStr));
	  }
	 // String payload="grant_type=assertion&assertion_type=";
	 // payload+="SignedJsonAssertionToken.GRANT_TYPE_VALUE";
	 // payload+="&assertion=";
	 // payload+=url;
	  if(payload!=null && !"".equals(payload)){
		  request.setPayload(payload.getBytes());
	  }
	  
	  HTTPResponse response=service.fetch(request);
	  //JsonParser parser=new JsonParser();
	  //String token=parser.parse(new String(response.getContent())));//.getAsJsonObject().get("access_token").getAsString();
	  String token=(new String(response.getContent()));
	  
	  System.out.println("=== *Response Payload Start*-1 ====");
	  System.out.println(token.substring(0, token.length()>100?100:token.length()));
	  System.out.println("=== Response Payload End====");
	  return response;
	}

public static void refreshRemoteServer(String url) throws Exception{
	System.out.println("Accessing remote srver call refreshRemoteServer");
	int retryCnt=3;
	try{
	while(retryCnt>0){
	System.out.println("Accessing remote srver: remaining try: "+retryCnt);
	getResponse(url, "", "", HTTPMethod.GET,null);
	break;
	}
	}catch(Exception e){
		System.err.println("Error while accessing remote "+e.getMessage());
		retryCnt--;
		if(retryCnt==0){
			System.err.println("Error while accessing remote failed\n Stoped further process "+e.getMessage());
			throw new Exception(e);
		}
	}
}

public static String decideLongFirstOrSort(ShareBean sb){
	
	double dh = 0;
	double dl = 0;
	double cp = 0;
	double bp = 0;
	double sp = 0;
	double pc = 0;
	double cbp = 0;
	double cap = 0;
	
	dh = MethodUtil.roundOff(sb.getDayHigh());
	cp = MethodUtil.roundOff(sb.getCurrentTradePrice());
	cbp = MethodUtil.roundOff(sb.getCurrentBid());
	cap = MethodUtil.roundOff(sb.getCurrentAsk());
	dl = MethodUtil.roundOff(sb.getDayLow());
	pc = MethodUtil.roundOff(sb.getPreviousClose());
	
	if((dh<pc)||(cp<pc)){
		return ShareUtil.LONG;
	}else{
		return ShareUtil.SORT;
	}
	
		
}

}
