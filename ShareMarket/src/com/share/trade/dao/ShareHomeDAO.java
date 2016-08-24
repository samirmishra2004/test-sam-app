package com.share.trade.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mortbay.log.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.share.trade.common.MethodUtil;
import com.share.trade.common.PMF;
import com.share.trade.common.ShareUtil;
import com.share.trade.vo.FutureScriptQuote;
import com.share.trade.vo.ShareBean;
import com.share.trade.vo.StaticHtmlStore;

public class ShareHomeDAO {
	Logger logger=Logger.getLogger(ShareHomeDAO.class.getName());
	
	 private static HashMap<String, ShareBean> stocks = new HashMap<String, ShareBean>();
	public String getInfoFromMoneyControll(){
		String datahtml=getData(ShareUtil.MONEY_CONTROLL_URL);
		
		return datahtml;
	}

	
	@SuppressWarnings("unused")
	public String getData(String url){ 
		String html="";
		Elements elements		=null;
		Query q 				=null;
		Date lastUpdatedDate	=null;
		PersistenceManager pm 	=PMF.getInstance().getPersistenceManager();	
		q = pm.newQuery(StaticHtmlStore.class);
		
		List<StaticHtmlStore> results = (List)q.execute();
		if(results!=null)
			for(StaticHtmlStore htm:results){
				html=htm.getDescription().getValue();
				lastUpdatedDate=htm.getLastUpdatedDate();
			}
			
		System.out.println("result size: "+results.size());
	boolean update=false;
		Calendar lastUpdatedDateCal= Calendar.getInstance();
		if(lastUpdatedDate!=null)
		lastUpdatedDateCal.setTime(lastUpdatedDate);
		else
			lastUpdatedDateCal.setTime(new Date());
		Calendar currentDateCal= Calendar.getInstance();
		int lastUpdatedYr=lastUpdatedDateCal.get(Calendar.YEAR);
		int lastUpdatedMonth=lastUpdatedDateCal.get(Calendar.MONTH);
		int lastUpdatedDt=lastUpdatedDateCal.get(Calendar.DATE);
		int currYr=currentDateCal.get(Calendar.YEAR);
		int currMonth=currentDateCal.get(Calendar.MONTH);
		int currDate=currentDateCal.get(Calendar.DATE);
		if(lastUpdatedYr<currYr){
			update=true;
		}else if(lastUpdatedMonth<currMonth){
			update=true;
		}else if(lastUpdatedDt<currDate){
			update=true;
		}else if(currentDateCal.get(Calendar.HOUR)>=4){
			update=true;
		}
			//logic to fetch the data once in a day at 4:00 PM IST
		//if(lastUpdatedDate!=null&lastUpdatedDateCal.getTime().after(lastUpdatedDate)){
		if(update){	
			try {
				Document doc = Jsoup.connect(url).get();
				elements= doc.select("div[class^=w00]");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("elements  :"+elements.html());
			//System.out.println("elements  :"+elements.size());
			if(elements!=null){
				Iterator<Element> iterator=elements.listIterator();
				
				
				while(iterator.hasNext()){
					Element element=iterator.next();
					html=element.html();
				}
		
			
				StaticHtmlStore htmlStore = new StaticHtmlStore(new Text(html), new Date(), new Date()) ;
				try {
		            pm.makePersistent(htmlStore);
		        } finally {
		            pm.close();
		        }
			}
		}
		
//		
		return html;
	}
	
	public ShareBean getRealTimeFinanceData(String symbol,long time) throws Exception{
		ShareBean bean=null;
		 System.out.println("in dao in getRealTimeFinanceData");
		//bean=getRealTimeQuoteFromMoneyControll(symbol);
		bean=getRealTimeQuoteFromNSE(symbol);
		return bean;
	}
	/**This is synched so we only do one request at a time
     *If yahoo doesn't return stock info we will try to return it from the map in memory
     *s=symbol,l1 = last trade price, b2=ask ,b3=bid, g= day low, h=day high, p= previous close
	 * @throws Exception 
	 */
    private synchronized ShareBean refreshStockInfo(String symbol, long time) throws Exception {
        
    	System.out.println("in dao in refreshStockInfo");
    	try {
            URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1b2b3ghp&e=.csv");
            URLConnection yc = yahoofin.openConnection();
            yc.setConnectTimeout(1000*60);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            ShareBean stockInfo;
            System.out.println("in dao in refreshStockInfo");
            while ((inputLine = in.readLine()) != null) {
                String[] yahooStockInfo = inputLine.split(",");
                stockInfo = new ShareBean();
                System.out.println(""+inputLine);
                stockInfo.setCurrentTradePrice(Float.valueOf(yahooStockInfo[1]));
                stockInfo.setCurrentAsk(Float.valueOf(yahooStockInfo[2]));
                stockInfo.setCurrentBid(Float.valueOf(yahooStockInfo[3]));
                stockInfo.setDayLow(Float.valueOf(yahooStockInfo[4]));
                stockInfo.setDayHigh(Float.valueOf(yahooStockInfo[5]));
                stockInfo.setPreviousClose(Float.valueOf(yahooStockInfo[6]));                
               
                stocks.put(symbol, stockInfo);
                System.out.println("symbol 1"+symbol+" object"+stocks);
                break;
            }
            in.close();
        } catch (Exception ex) {
            logger.info("Unable to get stockinfo for: " + symbol + ex);
            throw new Exception("");
        }
    	System.out.println("symbol"+symbol+" object"+stocks);
        return stocks.get(symbol);
     }
    
    public synchronized ShareBean getRealTimeQuoteFromMoneyControll(String scriptId) throws Exception{
    	ShareBean stockInfo = new ShareBean();
    	
    	String url="http://www.moneycontrol.com/mccode/common/get_pricechart_div.php?bse_id=Y&nse_id=Y&sc_id=534149&BNsetick=51.84|01.84&ins=&sc_mapindex=21";
    	url="http://www.moneycontrol.com/mccode/common/get_pricechart_div.php?bse_id=Y&nse_id=Y&sc_id="+scriptId+"&BNsetick=&ins=&sc_mapindex=21";
    	Document doc;
    	try {
    		doc = Jsoup.connect(url).timeout(60*1000).get();
    		
    	//Elements elements= doc.select("div[class^=w00]");
    	Elements elementsNse= doc.select("div[id^=content_nse]");
    	Elements elementsBse= doc.select("div[id^=content_bse]");
    	Float currPrice;
    	Float dayHigh;
    	Float previosClose;
    	Float bidPrice;
    	Float askPrice;
    	Float dayLow;
    	
    	
    	if(elementsNse!=null){
    		Iterator<Element> iterator=elementsNse.listIterator();
    		System.out.println(elementsNse.size());
    		
    		while(iterator.hasNext()){
    			Element element=iterator.next();
    			
    			Elements elementsNseContent= element.select("span[id^=Nse_Prc_tick]");
    			
    			String curPriceStr=elementsNseContent.get(0).text();
    			System.out.println("Current Price NSE : "+curPriceStr);
    			currPrice=Float.valueOf(curPriceStr);
    			
    			//
    			Elements elemtBOPC= element.select("div[class^=gD_12 PB3]");
    			System.out.println("elemtBOPC size "+elemtBOPC.size());
    			//previous close
    			Elements previousClose=elemtBOPC.get(0).getAllElements();
    			
    			System.out.println("previousClose: "+previousClose.get(1).text());
    			previosClose=Float.valueOf(previousClose.get(1).text());
    			
    			//openPrice 
    			Elements openPrice=elemtBOPC.get(1).getAllElements();
    			
    			System.out.println("openPrice: "+openPrice.get(1).text());
    			//openPrice=Float.valueOf(previousClose.get(1).text());
    			
    			//bid  
    			Elements bidPriceAndQnty=elemtBOPC.get(2).getAllElements();
    			
    			System.out.println("bidPrice: "+bidPriceAndQnty.get(1).text().substring(0,bidPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			bidPrice=Float.valueOf(bidPriceAndQnty.get(1).text().substring(0,bidPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			//ask  
    			Elements askPriceAndQnty=elemtBOPC.get(3).getAllElements();
    			
    			System.out.println("askPrice: "+askPriceAndQnty.get(1).text().substring(0,askPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			askPrice=Float.valueOf(askPriceAndQnty.get(1).text().substring(0,askPriceAndQnty.get(1).text().indexOf("(")-1).trim());
    			//
    			Elements elemtDL= element.select("span[class^=PR5]");
    			System.out.println("elemtDHDL size "+elemtDL.size());
    			System.out.println("day low: "+elemtDL.get(0).getAllElements().get(0).text());
    			
    			dayLow = Float.valueOf(elemtDL.get(0).getAllElements().get(0).text());
    			//day low
    			Elements elemtDH= element.select("span[class^=PL5]");
    			System.out.println("elemtDHDL size "+elemtDH.size());
    			
    			System.out.println("day high: "+elemtDH.get(0).text());
    			dayHigh = Float.valueOf(elemtDH.get(0).text());
    			
    			
    			stockInfo.setCurrentTradePrice(currPrice);
                stockInfo.setCurrentAsk(askPrice);
                stockInfo.setCurrentBid(bidPrice);
                stockInfo.setDayLow(dayLow);
                stockInfo.setDayHigh(dayHigh);
                stockInfo.setPreviousClose(previosClose);
    	
    		}
    	}
    	}catch(Exception ex){
    		System.err.println("Error in quote service "+ex.getMessage()+ex);
    		throw new Exception("can not fetch quote"+ex);
    	}
    	return stockInfo;
    }
    public synchronized ShareBean getRealTimeQuoteFromNSE(String scriptId) throws Exception{
    	ShareBean stockInfo = new ShareBean();
    	
    	String url="https://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/GetQuote.jsp?symbol="+scriptId+"&illiquid=0&smeFlag=0&itpFlag=0";
    	//url="https://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/GetQuote.jsp?symbol="+"ZEEL"+"&illiquid=0&smeFlag=0&itpFlag=0";
    	//url="https://www.nseindia.com/";
    	Document doc; 
    	try {
    	/*Response	respJsp = Jsoup.connect(url).
    				referrer("https://www.nseindia.com").
    				userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36").timeout(60*1000)
    				.execute();*/
    	
    	doc=Jsoup.connect(url)
        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
        .header("Accept-Language", "en-US,en")
        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
        .header("Host", "www.nseindia.com")
        .header("Connection", "keep-alive")
        .header("Cache-Control", "max-age=0")
        .header("Upgrade-Insecure-Requests", "1")
        .header("Accept-Encoding", "gzip,deflate,sdch,br")
        .timeout(60*1000)
        .get();
    		
    		//HTTPResponse res=getResponse(url, "", "", HTTPMethod.GET,null);
    	//doc= Jsoup.parse(new String(res.getContent()));
    	
    	//doc= respJsp.parse();
    	//doc=Jsoup.parse(new URL(url), 6000);
    	
    	Float currPrice;
    	Float dayHigh;
    	Float previosClose;
    	Float bidPrice;
    	Float askPrice;
    	Float dayLow;
    	
    	
    	if(doc!=null){
    		
    		
    		//while(iterator.hasNext()){}

    		//System.out.println(doc.html());
    		
    		Elements jsonstr=doc.select("div[id^=responseDiv]");
    		
    		JsonParser jp=new JsonParser();
    		JsonObject je= (JsonObject)jp.parse(jsonstr.text());
    		JsonArray dataStringJsnArry =(JsonArray)je.get("data");
    		System.out.println("dataString "+dataStringJsnArry);
    		
    		JsonObject scripPriceDataJson= (JsonObject)dataStringJsnArry.get(0);
    		
    		System.out.println("Current PRice "+scripPriceDataJson.get("lastPrice"));
    		System.out.println("previousClose "+scripPriceDataJson.get("previousClose"));
    		System.out.println("OpenPrice "+scripPriceDataJson.get("previousClose"));
    		System.out.println("DayHigh "+scripPriceDataJson.get("dayHigh"));
    		System.out.println("DayLow "+scripPriceDataJson.get("dayLow"));
    		System.out.println("BidPrice "+scripPriceDataJson.get("buyPrice1"));
    		System.out.println("AskPrice "+scripPriceDataJson.get("sellPrice1"));
    			
    		try{
    			String p=scripPriceDataJson.get("lastPrice").getAsString().replaceAll(",", "");
        		currPrice=Float.parseFloat(p);
            	}catch(NumberFormatException ne){
            		System.err.println(ne);
            		currPrice=0.0f;
            }
    		try{
    			String p=scripPriceDataJson.get("sellPrice1").getAsString().replaceAll(",", "");
    			askPrice=Float.parseFloat(p);
            	}catch(NumberFormatException ne){
            		System.err.println(ne);
            		askPrice=0.0f;
            }
    		try{
    			String p=scripPriceDataJson.get("buyPrice1").getAsString().replaceAll(",", "");
    			bidPrice=Float.parseFloat(p);
            	}catch(NumberFormatException ne){
            		System.err.println(ne);
            		bidPrice=0.0f;
            }
    		try{
    			String p=scripPriceDataJson.get("dayLow").getAsString().replaceAll(",", "");
    			dayLow=Float.parseFloat(p);
            	}catch(NumberFormatException ne){
            		System.err.println(ne);
            		dayLow=0.0f;
            }
    		try{
    			String p=scripPriceDataJson.get("dayHigh").getAsString().replaceAll(",", "");
    			dayHigh=Float.parseFloat(p);
            	}catch(NumberFormatException ne){
            		System.err.println(ne);
            		dayHigh=0.0f;
            }
    		try{
    			String p=scripPriceDataJson.get("previousClose").getAsString().replaceAll(",", "");
    			previosClose=Float.parseFloat(p);
            	}catch(NumberFormatException ne){
            		System.err.println(ne);
            		previosClose=0.0f;
            }
    			
    			stockInfo.setCurrentTradePrice(currPrice);
                stockInfo.setCurrentAsk(askPrice);
                stockInfo.setCurrentBid(bidPrice);
                stockInfo.setDayLow(dayLow);
                stockInfo.setDayHigh(dayHigh);
                stockInfo.setPreviousClose(previosClose);
    	
    		
    	}
    	}catch(Exception ex){
    		System.err.println("Error in quote service "+ex.getMessage()+ex);
    		throw new Exception("can not fetch quote"+ex);
    	}
    	return stockInfo;
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
    public synchronized FutureScriptQuote getRealTimeFutureQuote(String  scUrl) throws Exception{
		
		String url=ShareUtil.MONEY_CONTROL_FNO_URL+scUrl;
		
		
		FutureScriptQuote quote=new FutureScriptQuote();
		Document doc;
    	try {
    		doc = Jsoup.connect(url).timeout(10*1000).get();
    		
    		
    		Elements futureCurPriceElements= doc.select("p[class^=r_28 FL]");
    		if(futureCurPriceElements!=null&&futureCurPriceElements.size()>0){
    		System.out.println("Current Price : "+futureCurPriceElements.get(0).text());
    		//set quote
    		quote.setCurrentPrice(MethodUtil.removeComma(futureCurPriceElements.get(0).text()));
    		}else{
    			futureCurPriceElements= doc.select("p[class^=gr_28 FL]");
    			quote.setCurrentPrice(MethodUtil.removeComma(futureCurPriceElements.get(0).text()));
    		}
    		Elements futureQuoteHtmlElements1= doc.select("div[class^=FL PA10 brdr]");
    		
    		Elements futureQuoteHtmlElements2= doc.select("div[class^=FR PA10]");

        	if(futureQuoteHtmlElements1!=null){
        		//Iterator<Element> iterator=futureQuoteHtmlElements1.listIterator();
        		//System.out.println(futureQuoteHtmlElements1.get(0).text());
        		Elements otherParameter=futureQuoteHtmlElements1.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        		
        		
        		Iterator<Element> ohereElementIterator=otherParameter.listIterator();
        		if(ohereElementIterator.hasNext()){
        			Element element=ohereElementIterator.next();
        			
        			System.out.println("Oherer Param: "+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
        			//set quote
        			quote.setOpenPrice(MethodUtil.removeComma(element.getElementsByTag("td").text()));
        			
        			if(ohereElementIterator.hasNext()){
        		    element=ohereElementIterator.next();
        		    logger.info("day high :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
        		    quote.setDayHigh(MethodUtil.removeComma(element.getElementsByTag("td").text()));
        			}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("day low :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setDayLow(MethodUtil.removeComma(element.getElementsByTag("td").text()));
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("previous close :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setPreviousClose(MethodUtil.removeComma(element.getElementsByTag("td").text()));
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();     //escAPE       		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();         //escAPE        		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("bid price :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setBidPrice(MethodUtil.removeComma(element.getElementsByTag("td").text()));
            		}
        			//System.out.println("Oherer Param: "+element.html());

        		}
        		
        	}
        	if(futureQuoteHtmlElements2!=null){
        		//Iterator<Element> iterator=futureQuoteHtmlElements2.listIterator();
        		Elements otherParameter=futureQuoteHtmlElements2.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        		Iterator<Element> ohereElementIterator=otherParameter.listIterator();
        		if(ohereElementIterator.hasNext()){
        			Element element=ohereElementIterator.next();
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();
            		    logger.info("lot size :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setLotSize(MethodUtil.removeComma(element.getElementsByTag("td").text()));
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();        //escAPE         		    
            		}
        			if(ohereElementIterator.hasNext()){
            		    element=ohereElementIterator.next();   
            		    logger.info("Offer Price :"+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
            		    quote.setOffferPrice(MethodUtil.removeComma(element.getElementsByTag("td").text()));
            		}
        			//System.out.println("Oherer Param1: "+element.getElementsByTag("th").text()+" = "+element.getElementsByTag("td").text());
        			//Elements elementsNseContent= element.select("span[id^=gr_28 FL]");
        		}
        	}
    		//System.out.println("previousClose: "+previousClose.get(1).text());
    	}
    	catch (Exception e) {
    		System.err.println("Jsoup can not get the document :"+e.getMessage());
			throw new Exception("Jsoup can not get the document :"+e.getMessage());
		}
		
		return quote;
		
	}
    public static void main(String[] args) {/*
	//	ShareHomeDAO homeDAO=new ShareHomeDAO();
	//	homeDAO.getRealTimeFutureQuote("sail/SAI/2013-08-29");
    	String url="https://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/GetQuote.jsp?symbol="+"HDFCBANK"+"&illiquid=0&smeFlag=0&itpFlag=0";
    	//String jstr="{\"futLink\":\"\\/live_market\\/dynaContent\\/live_watch\\/get_quote\\/GetQuoteFO.jsp?underlying=ZEEL&instrument=FUTSTK&expiry=25AUG2016&type=-&strike=-\",\"otherSeries\":[\"EQ\",\"P2\"],\"lastUpdateTime\":\"11-AUG-2016 16:00:04\",\"tradedDate\":\"11AUG2016\",\"data\":[{\"extremeLossMargin\":\"5.00\",\"cm_ffm\":\"27,454.91\",\"bcStartDate\":\"-\",\"change\":\"-13.55\",\"buyQuantity3\":\"-\",\"sellPrice1\":\"-\",\"buyQuantity4\":\"-\",\"sellPrice2\":\"-\",\"priceBand\":\"No Band\",\"buyQuantity1\":\"32\",\"deliveryQuantity\":\"9,34,741\",\"buyQuantity2\":\"-\",\"sellPrice5\":\"-\",\"quantityTraded\":\"15,54,397\",\"buyQuantity5\":\"-\",\"sellPrice3\":\"-\",\"sellPrice4\":\"-\",\"open\":\"515.00\",\"low52\":\"346.70\",\"securityVar\":\"6.00\",\"marketType\":\"N\",\"pricebandupper\":\"566.50\",\"totalTradedValue\":\"7,808.82\",\"faceValue\":\"1.00\",\"ndStartDate\":\"-\",\"previousClose\":\"515.00\",\"symbol\":\"ZEEL\",\"varMargin\":\"7.50\",\"lastPrice\":\"501.45\",\"pChange\":\"-2.63\",\"adhocMargin\":\"-\",\"companyName\":\"Zee Entertainment Enterprises Limited\",\"averagePrice\":\"502.37\",\"secDate\":\"11AUG2016\",\"series\":\"EQ\",\"isinCode\":\"INE256A01028\",\"indexVar\":\"-\",\"pricebandlower\":\"463.50\",\"totalBuyQuantity\":\"32\",\"high52\":\"517.80\",\"purpose\":\"ANNUAL GENERAL MEETING\\/ DIVIDEND -RS 2.25\\/- PER SHARE\",\"cm_adj_low_dt\":\"24-AUG-15\",\"closePrice\":\"501.50\",\"isExDateFlag\":false,\"recordDate\":\"22-JUL-16\",\"cm_adj_high_dt\":\"10-AUG-16\",\"totalSellQuantity\":\"-\",\"dayHigh\":\"515.65\",\"exDate\":\"21-JUL-16\",\"sellQuantity5\":\"-\",\"bcEndDate\":\"-\",\"css_status_desc\":\"Listed\",\"ndEndDate\":\"-\",\"sellQuantity2\":\"-\",\"sellQuantity1\":\"-\",\"buyPrice1\":\"501.50\",\"sellQuantity4\":\"-\",\"buyPrice2\":\"-\",\"sellQuantity3\":\"-\",\"applicableMargin\":\"12.50\",\"buyPrice4\":\"-\",\"buyPrice3\":\"-\",\"buyPrice5\":\"-\",\"dayLow\":\"497.15\",\"deliveryToTradedQuantity\":\"60.14\",\"totalTradedVolume\":\"15,54,397\"}],\"optLink\":\"\\/marketinfo\\/sym_map\\/symbolMapping.jsp?symbol=ZEEL&instrument=-&date=-&segmentLink=17&symbolCount=2\"}";
    	
    	try{
    		//Document doc=Jsoup.parse(new File("C:\\Users\\HP pc\\Desktop\\NSEZeelQuote.html"), "UTF-8");
    		
    		Document doc=Jsoup.parse(new URL(url),60000);
    		System.out.println(doc.html());
    		
    		Elements jsonstr=doc.select("div[id^=responseDiv]");
    		
    		JsonParser jp=new JsonParser();
    		JsonObject je= (JsonObject)jp.parse(jsonstr.text());
    		JsonArray dataStringJsnArry =(JsonArray)je.get("data");
    		//System.out.println("dataString "+dataString);
    		
    		JsonObject scripPriceDataJson= (JsonObject)dataStringJsnArry.get(0);
    		
    		System.out.println("Current PRice "+scripPriceDataJson.get("lastPrice"));
    		System.out.println("previousClose "+scripPriceDataJson.get("previousClose"));
    		System.out.println("OpenPrice "+scripPriceDataJson.get("open"));
    		System.out.println("DayHigh "+scripPriceDataJson.get("dayHigh"));
    		System.out.println("DayLow "+scripPriceDataJson.get("dayLow"));
    		System.out.println("BidPrice "+scripPriceDataJson.get("buyPrice1"));
    		System.out.println("AskPrice "+scripPriceDataJson.get("sellPrice1"));
    		
    		Float currPrice;
        	Float dayHigh;
        	Float previosClose;
        	Float bidPrice;
        	Float askPrice;
        	Float dayLow;
    		
        	try{
    		currPrice=scripPriceDataJson.get("lastPrice").getAsFloat();
        	}catch(NumberFormatException ne){
        		System.err.println(ne);
        		currPrice=0.0f;
        	}
    		
    		askPrice=Float.parseFloat(scripPriceDataJson.get("sellPrice1").getAsString().replaceAll(",", ""));
    		//bidPrice=scripPriceDataJson.get("buyPrice1").getAsFloat();
    		//dayLow=scripPriceDataJson.get("dayLow").getAsFloat();
    		//dayHigh=scripPriceDataJson.get("dayHigh").getAsFloat();
    		
    		
    		System.out.println("Current PRice "+currPrice);
    		//System.out.println("previousClose "+scripPriceDataJson.get("previousClose").getAsFloat());
    		//System.out.println("OpenPrice "+scripPriceDataJson.get("previousClose"));
    		//System.out.println("DayHigh "+dayHigh);
    		//System.out.println("DayLow "+dayLow);
    		//System.out.println("BidPrice "+bidPrice);
    		System.out.println("AskPrice "+askPrice);
    		
    		
    		Elements elements=doc.select("table[class^=rpm mkt]");
    		String quote="sail".toUpperCase();
    		String orderType="Sell";
    		for(Element e:elements){
    			//System.out.println(e.html());
    			//System.out.println(e.children().get(0).child(1).html());
    			Elements allOrderRows=e.children().get(0).children();
    			
    			for(int i=1;i<allOrderRows.size();i++){
    				Elements tds=allOrderRows.get(i).children();
    				boolean orderFound=false;
    				for(Element td:tds){
    					//System.out.println("found order");
    					if(td.text().contains(quote)){
    						System.out.println("Found Order");
    						orderFound=true;
    						//System.out.println(td.html());
    					}
    					if(td.text().contains(quote)&&td.nextElementSibling().text().contains(orderType)){
    						System.out.println("Its "+orderType+" Order");
    						//System.out.println(""+td.child(1).text());
    						System.out.println(td.nextElementSibling().children().get(1).text());
    					}else if(td.text().contains(quote)&&td.nextElementSibling().text().contains(orderType)){
    						
    					}
    					
    				}
    				
    				
    				
    			}
    		}
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
	*/}
}
